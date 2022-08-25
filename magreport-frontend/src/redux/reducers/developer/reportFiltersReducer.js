import { 
    REPORT_FILTERS_LOADED,
    REPORT_FILTER_CHANGED,
    REPORT_FILTER_ADDED,
    REPORT_FILTER_DELETED,
    REPORT_FILTER_MOVED_BEFORE,
    REPORT_FILTER_MOVED_INTO,
    REPORT_GROUP_FILTER_CHANGED,
    REPORT_GROUP_FILTER_ADDED,
    REPORT_GROUP_FILTER_DELETED,
    REPORT_FILTERS_ORDINAL_CHANGED,
} from '../../reduxTypes'

import { randomWordCode } from 'utils/randomWordCode';

const initialState = {
    childGroups: new Map(),
    linkFiltersAndFilterGroup: new Map(),
    codeFiltersMap: new Map(),
    maxKey: null,
}

/**
 * Считает расстояние Левенштейна между двумя строками
 * (минимальное число односимвольных вставок, удалений или замен, необходимых для изменения одного слова в другое).
 * https://en.wikipedia.org/wiki/Levenshtein_distance
 * @param {String} first
 * @param {String} second
 * @return {number} - расстояние Левенштейна между first и second
 */
function levenshteinDistance(first, second) {
    let i, j, substitutionCost;
    let d = new Array(first.length+1).fill(null).map(_ => new Array(second.length+1).fill(0));

    for(i = 1; i <= first.length; i++) {
        d[i][0] = i;
    }

    for(j = 1; j <= second.length; j++) {
        d[0][j] = j;
    }

    for(j = 1; j <= second.length; j++) {
        for(i = 1; i <= first.length; i++) {
            if(first[i] === second[j]) {
                substitutionCost = 0;
            }
            else {
                substitutionCost = 1;
            }

            d[i][j] = Math.min(d[i-1][j] + 1,
                d[i][j-1] + 1,
                d[i-1][j-1] + substitutionCost);
        }
    }

    return d[first.length][second.length];
}

function getMatchingReportFieldId(filterInstanceField, reportFields) {
    if(reportFields.length === 0) {
        return null;
    }
    const name = filterInstanceField.name;
    const closestReportFields = reportFields
        .map(f => [levenshteinDistance(name.toLowerCase(), f.name.toLowerCase()), f])
        .sort((a, b) => (a[0] - b[0]))
        .map(([_, f]) => f);
    return closestReportFields[0].id;
}

export const reportFiltersReducer = (state = initialState, action) => {
    let newChildGroupsMap
    let newFiltersMap
    let codeFilters
    switch (action.type){
        case REPORT_FILTERS_LOADED:
            let keyId = {value: 0}
            const rootGroup = {
                ...action.data.filterGroup,
                id: keyId.value,
                description: action.data.filterGroup.description || 'Фильтры к отчету',
                filters: action.data.filterGroup.filters || [],
                name: action.data.filterGroup.name || 'Фильтры к отчету',
                operationType: action.data.filterGroup.operationType || 'AND',
                childGroups: action.data.filterGroup.childGroups || [],
                linkedFilters: action.data.filterGroup.linkedFilters || false,
                ordinal: action.data.filterGroup.ordinal || 0,
                reportId: action.data.filterGroup.reportId || action.reportId,
                code: action.data.filterGroup.reportId || randomWordCode(6),
                parentId: null, 
                mandatory: action.data.filterGroup.mandatory || false,
                errors: {}, 
                expandedCollapse: true,
            }
            newChildGroupsMap = new Map()
            newFiltersMap = new Map()
            codeFilters = new Map()
            newChildGroupsMap.set(keyId.value, rootGroup)
            recursiveSetChildGroups(rootGroup, newChildGroupsMap, newFiltersMap, codeFilters, keyId)

            return {
                ...state, 
                childGroups: newChildGroupsMap, 
                linkFiltersAndFilterGroup: newFiltersMap,
                codeFiltersMap: codeFilters,
                maxKey: keyId.value
            }
        
        case REPORT_FILTER_CHANGED:
            {
                newChildGroupsMap = new Map(state.childGroups)
                codeFilters = new Map(state.codeFiltersMap)
                const childGroup = newChildGroupsMap.get(action.childGroupId)
                let arr = [...childGroup.filters]
                const old = arr[action.index]
                const new_ = action.filterItem
                if (old.code !== new_.code && codeFilters.has(old.code) && codeFilters.get(old.code) === new_.id){
                    codeFilters.delete(old.code)
                }
                arr.splice(action.index, 1, validateFilter(action.filterItem, codeFilters))
                newChildGroupsMap.set(action.childGroupId, validate({...childGroup, filters: arr}))
                return {...state, childGroups: newChildGroupsMap, codeFiltersMap: codeFilters}
            }
        
        case REPORT_FILTER_ADDED:
            {
                let fieldsArr=[]
                let ordinal=0
                const item = action.item
                const reportFields = action.reportFields || [];

                newChildGroupsMap = new Map(state.childGroups)
                newFiltersMap = new Map(state.linkFiltersAndFilterGroup)
                codeFilters = new Map(state.codeFiltersMap)
                const nextId = state.maxKey + 1 
                const childGroup = newChildGroupsMap.get(action.childGroupId)

                for(let i of item.fields){
                    if (i.type === 'ID_FIELD'){
                        const field = {
                            description: '',
                            filterInstanceFieldId: i.id,
                            id: null,
                            name: i.name,
                            level: i.level,
                            reportFieldId: getMatchingReportFieldId(i, reportFields),
                            ordinal: ordinal,
                            type: i.type,
                            expand: i.expand
                        }
                        fieldsArr.push(field)
                        ordinal++
                    }
                }

                let arr = [...childGroup.filters]

                const code = item.code ? item.code : randomWordCode(6)
                const newFilter = {
                    description: item.description,
                    fields: fieldsArr,
                    filterInstanceId: item.id,
                    hidden: false,
                    id: nextId,
                    mandatory: true,
                    name: item.name,
                    code: code,
                    type: item.type.name,
                    expand: item.expand,
                    ordinal: getNextOrdinal(state.childGroups, childGroup),
                    rootSelectable: true
                }
                arr.push(validateFilter(newFilter, codeFilters))

                newChildGroupsMap.set(action.childGroupId, validate({...childGroup, filters: arr}))
                newFiltersMap.set(nextId, action.childGroupId)
                return {
                    ...state, 
                    childGroups: newChildGroupsMap,
                    linkFiltersAndFilterGroup: newFiltersMap,
                    codeFiltersMap: codeFilters,
                    maxKey: nextId,
                }
            }

        case REPORT_FILTER_DELETED:
            {
                newChildGroupsMap = new Map(state.childGroups)
                newFiltersMap = new Map(state.linkFiltersAndFilterGroup)
                codeFilters = new Map(state.codeFiltersMap)
                const childGroup = newChildGroupsMap.get(action.childGroupId)
                let arr = [...childGroup.filters]
                const deletedFilter = arr.splice(action.index, 1)
                newChildGroupsMap.set(action.childGroupId, validate({...childGroup, filters: arr}))
                newFiltersMap.delete(deletedFilter[0].id)
                codeFilters.delete(deletedFilter[0].code)
                return {
                    ...state, 
                    childGroups: newChildGroupsMap,
                    codeFiltersMap: codeFilters,
                    linkFiltersAndFilterGroup: newFiltersMap,
                }
            }
        
        case REPORT_FILTER_MOVED_BEFORE:
            {
                newChildGroupsMap = new Map(state.childGroups)
                newFiltersMap = new Map(state.linkFiltersAndFilterGroup)
                
                // узнаем тип перетягиваемого элемента
                let elementType = getElementType(action.entityId, newChildGroupsMap)
                
                // находим старого родителя
                let oldParent = getOldParent(elementType, action.entityId, newChildGroupsMap, newFiltersMap)
                
                
                // получаем переносимый объект и его индекс в старой группе фильтров
                let entity = getAndRemoveEntity(elementType, action.entityId, oldParent, newChildGroupsMap)
                
                // меняем ordinal у всех элементов старой группы, т.к. удалили элемент
                oldParent = validate(oldParent)
                changeOrdinalInOldParent(entity.ordinal, oldParent, newChildGroupsMap)
                
                // находим нового родителя и элемент, перед которым хотим вставить перетаскиваемый элемент
                let beforeId = parseInt(action.beforeId.replace('_filters', '').replace('_childGroups', ''))
                let newParent = getNewParent(beforeId, 'before', newChildGroupsMap, newFiltersMap)
                
                let beforeElement = newChildGroupsMap.get(beforeId)
                if (!beforeElement){
                    beforeElement = newParent.filters.find(f => f.id === beforeId)
                }

                // находим индекс в массиве, куда будем вставлять элемент
                let newElementIndex = 0
                let ordinal = beforeElement.ordinal
                if (elementType === 'filters'){
                    for (let i=0; i < newParent.filters.length; i++){
                        if (newParent.filters[i].ordinal < ordinal){
                            newElementIndex = i+1
                        }
                    }
                }
                else {
                    for (let i=0; i < newParent.childGroups.length; i++){
                        const cg = newChildGroupsMap.get(newParent.childGroups[i])
                        if (cg.ordinal < ordinal){
                            newElementIndex = i+1
                        }
                    }
                }

                // вставляем элемент в целевой массив и меняем ordinal у всех элементов
                entity.parentId = newParent.id
                entity.ordinal = ordinal
                if (elementType === 'filters'){
                    if (newParent.filters[newElementIndex]){
                        newParent.filters.splice(newElementIndex, 1, {...entity, ordinal}, newParent.filters[newElementIndex])
                    }
                    else {
                        newParent.filters.splice(newElementIndex, 1, {...entity, ordinal})
                    }
                    
                    for (let cgId of newParent.childGroups){
                        let cg = newChildGroupsMap.get(cgId)
                        if (cg.ordinal>=ordinal){
                            newChildGroupsMap.set(cgId, {...cg, ordinal: cg.ordinal+1})
                        }
                    }
                    for (let i = newElementIndex +1; i < newParent.filters.length; i++){
                        newParent.filters[i].ordinal += 1
                    }

                    newFiltersMap.set(entity.id, newParent.id)
                }
                else {
                    if (newParent.childGroups[newElementIndex]){
                        newParent.childGroups.splice(newElementIndex, 1, entity.id, newParent.childGroups[newElementIndex])
                    }
                    else {
                        newParent.childGroups.splice(newElementIndex, 1, entity.id)
                    }
                    
                    for (let i = newElementIndex +1; i < newParent.childGroups.length; i++){
                        const cgId = newParent.childGroups[i]
                        let cg = newChildGroupsMap.get(cgId)
                        newChildGroupsMap.set(cgId, {...cg, ordinal: cg.ordinal+1})
                    }
                    for (let f of newParent.filters){
                        if (f.ordinal>=ordinal){
                            f.ordinal += 1
                        }
                    }
                }

                newParent = validate(newParent)

                return {
                    ...state, 
                    childGroups: newChildGroupsMap,
                    linkFiltersAndFilterGroup: newFiltersMap,
                }
            }
        
        case REPORT_FILTER_MOVED_INTO:
            {   
                newChildGroupsMap = new Map(state.childGroups)
                newFiltersMap = new Map(state.linkFiltersAndFilterGroup)
                // узнаем тип родителя (перетягивать можно только в группы фильтров)
                let targetId = parseInt(action.targetId.replace('_filters', '').replace('_childGroups', ''))
                let parentType = getElementType(targetId, newChildGroupsMap)

                if (parentType !== 'filters' && action.targetId !== action.entityId){
                        

                    // узнаем тип перетягиваемого элемента
                    let elementType = getElementType(action.entityId, newChildGroupsMap)
                    
                    // находим старого родителя
                    let oldParent = getOldParent(elementType, action.entityId, newChildGroupsMap, newFiltersMap)
                    
                    
                    // получаем переносимый объект и его индекс в старой группе фильтров
                    let entity = getAndRemoveEntity(elementType, action.entityId, oldParent, newChildGroupsMap)
                    
                    // меняем ordinal у всех элементов старой группы, т.к. удалили элемент
                    oldParent = validate(oldParent)
                    changeOrdinalInOldParent(entity.ordinal, oldParent, newChildGroupsMap)
                    
                    // находим нового родителя
                    let newParent = getNewParent(targetId, 'target', newChildGroupsMap, newFiltersMap)
                    
                    let ordinal = getMaxOrdinal(newParent, newChildGroupsMap) + 1

                    if (elementType === 'filters'){
                        newParent.filters.push({...entity, ordinal})
                        newFiltersMap.set(entity.id, newParent.id)
                    }
                    else {
                        newParent.childGroups.push(entity.id)
                        newChildGroupsMap.set(entity.id, {...newChildGroupsMap.get(entity.id), ordinal, parentId: newParent.id})
                    }

                    newParent = validate(newParent)

                    return {
                        ...state, 
                        childGroups: newChildGroupsMap,
                        linkFiltersAndFilterGroup: newFiltersMap,
                    }
                }
                else {
                    return state
                }
            }
        
        case REPORT_GROUP_FILTER_CHANGED:
            {
                newChildGroupsMap = new Map(state.childGroups)
                const childGroup = newChildGroupsMap.get(action.childGroupId)
                newChildGroupsMap.set(action.childGroupId, validate({...childGroup, [action.field]: action.value}))
                return {...state, childGroups: newChildGroupsMap}
            }
        
        case REPORT_GROUP_FILTER_ADDED:
            {
                newChildGroupsMap = new Map(state.childGroups)
                const childGroup = newChildGroupsMap.get(action.childGroupId)
                let arr = [...childGroup.childGroups]
                const key = state.maxKey + 1

                let newChildGroups = validate({
                    id: key,
                    childGroups: [],
                    filters:[],
                    linkedFilters: false,
                    name: '',
                    code: randomWordCode(6),
                    description: '',
                    operationType: 'AND',
                    reportId: action.reportId,
                    ordinal: getNextOrdinal(state.childGroups, childGroup),
                    mandatory: false,
                    expandedCollapse: true,
                    errors: {}
                })
                
                arr.push(key)
                newChildGroupsMap.set(key, newChildGroups)
                newChildGroupsMap.set(action.childGroupId, validate({...childGroup, childGroups: arr}))
                return {...state, childGroups: newChildGroupsMap, maxKey: key}
            }
        
        case REPORT_GROUP_FILTER_DELETED:
            {
                newChildGroupsMap = new Map(state.childGroups)
                const childGroup = newChildGroupsMap.get(action.childGroupId)
                let arr = [...childGroup.childGroups]
                newChildGroupsMap.delete(arr[action.index])
                arr.splice(action.index, 1)
                newChildGroupsMap.set(action.childGroupId, validate({...childGroup, childGroups: arr}))
                return {...state, childGroups: newChildGroupsMap}
            }
        
        case REPORT_FILTERS_ORDINAL_CHANGED:
            {
                // debugger
                newChildGroupsMap = new Map(state.childGroups)
                let filters = []
                let childGroups = []
                const objects = action.listObjects
                const childGroup = newChildGroupsMap.get(action.childGroupId)

                for(let i = 0; i < objects.length; i++){
                    if (objects[i].__elmtype === 'childGroups'){
                        childGroups.push(objects[i].id)
                        newChildGroupsMap.set(
                            objects[i].id, 
                            {...newChildGroupsMap.get(objects[i].id), ordinal:i+1}
                        )
                    }
                    else {
                        filters.push({...objects[i], ordinal: i+1})
                    }
                }

                newChildGroupsMap.set(action.childGroupId, {...childGroup, childGroups, filters})

                return {...state, childGroups: newChildGroupsMap}
            }
        
        default:
            return state
    }
}

function recursiveSetChildGroups(parent, targetMap, filterMap, codeFilters, keyId){
    const parentId = keyId.value
    let groupsIds = []
    
    for (let f of parent.filters){
        keyId.value++
        f.id = keyId.value
        filterMap.set(keyId.value, parent.id)
        codeFilters.set(f.code, f.id)
    }
    
    for(let cg of parent.childGroups){
        keyId.value++
        const newChildGroup = {...cg, id: keyId.value, parentId: parent.id}
        targetMap.set(keyId.value, newChildGroup)
        groupsIds.push(keyId.value)
        recursiveSetChildGroups(newChildGroup, targetMap, filterMap, codeFilters, keyId)
    }
    
    const parentObj = targetMap.get(parentId)
    targetMap.set(parentId, {...parentObj, childGroups:groupsIds})
}

function getNextOrdinal(childGroupsMap, childGroup){
    let ordinals = []
    for (let cgId of childGroup.childGroups){
        const cgObj = childGroupsMap.get(cgId)
        ordinals.push(cgObj.ordinal)
    }
    for (let f of childGroup.filters){
        ordinals.push(f.ordinal)
    }
    return Math.max( 0, ...ordinals)+1
}

function validate(obj){
    let errors = {}
    let haveErrors=false

    if (!obj.name) { 
        errors.groupName = 'Не заполнено название группы фильтров'
    }

    if (!obj.code) { 
        errors.groupCode = 'Не заполнен кодовый идентификатор группы фильтров'
    }
    
    if (!obj.description) { 
        errors.groupDescription = 'Не заполнено описание группы фильтров'
    }

    if (!obj.childGroups.length && !obj.filters.length && obj.id !== 0){
        errors.emptyFilters = 'Не задан ни один фильтр или группа фильтров'
    }

    if (obj.mandatory && !obj.filters.length && obj.id !== 0){
        errors.noFilters = "В обязательной группе фильтров должен быть хотя бы один фильтр"
    }

    for (let f of obj.filters){
        for (let field of f.fields){
            if (field.type === 'ID_FIELD' && !field.reportFieldId){
                field.error = true
                errors.filters = 'Свяжите все поля фильтра с полями отчета'
                haveErrors = true
            }
        }
    }

    if (Object.keys(errors).length){
        haveErrors = true
        obj.errors = errors
    }

    obj.haveErrors = haveErrors
    return {...obj}
}

function getElementType(entityId, childGroupsMap){
    // узнаем тип перетягиваемого элемента
    let elementType = childGroupsMap.get(entityId)
    if (elementType !== undefined){
        elementType ='childGroups'
    }
    else {
        elementType = 'filters'
    }
    return elementType
}

function getOldParent(elementType, entityId, childGroupsMap, filtersMap){
    let oldParent
    let entityParentId
    if (elementType === "filters"){
        entityParentId = filtersMap.get(entityId)
        oldParent = childGroupsMap.get(entityParentId)
    }
    else {
        entityParentId = childGroupsMap.get(entityId).parentId || 0
        oldParent = childGroupsMap.get(entityParentId)
    }
    return oldParent
}
    
function getAndRemoveEntity(elementType, entityId, oldParent, childGroupsMap){
    let entity
    let index
    if (elementType === 'filters'){
        index=oldParent.filters.findIndex(f => f.id === entityId)
        entity = oldParent.filters.splice(index, 1)[0]
    }
    else{
        index=oldParent.childGroups.findIndex(cgId => cgId === entityId)
        oldParent.childGroups.splice(index, 1)
        entity = childGroupsMap.get(entityId)
    }
    return entity
}

function changeOrdinalInOldParent(ordinal, oldParent, childGroupsMap){
    for (let cgId of oldParent.childGroups){
        const cg = childGroupsMap.get(cgId)
        if (cg.ordinal > ordinal){
            childGroupsMap.set(cgId, {...cg, ordinal: cg.ordinal-1})
        }
    }
    for (let f of oldParent.filters){
        if (f.ordinal > ordinal){
            f.ordinal = f.ordinal-1
        }
    }
}

function getNewParent(id, operation, childGroupsMap, filtersMap){
    let newParent
    if (operation === 'before'){
        let beforeElement = childGroupsMap.get(id)
        if (!beforeElement){
            newParent = childGroupsMap.get(filtersMap.get(id))
            beforeElement = newParent.filters.find(f => f.id === id)
        }
        else {
            newParent = childGroupsMap.get(beforeElement.parentId || 0)
        }
    }
    else {
        newParent = childGroupsMap.get(id)
    }
    return newParent
}

function getMaxOrdinal(group, childGroupsMap){
    let ordinal = 0
    for (let cgId of group.childGroups){
        const cg = childGroupsMap.get(cgId)
        if (cg.ordinal > ordinal){
            ordinal = cg.ordinal
        }
    }
    for (let f of group.filters){
        if (f.ordinal > ordinal){
            ordinal = f.ordinal
        }
    }
    return ordinal
}

function validateFilter(obj, codeFilters){
    let errors = {}

    if (!obj.name) { 
        errors.name = 'Не указано название фильтра'
    }

    if (!obj.code) { 
        errors.code = 'Не заполнен код фильтра'
    }
    else {
        if (codeFilters.has(obj.code) && obj.id !== codeFilters.get(obj.code)){ 
            errors.code = 'Код фильтра должен быть уникальным'
        }
        else {
            codeFilters.set(obj.code, obj.id)
        }
    }

    for (let field of obj.fields){
        if (field.type === 'ID_FIELD' && !field.reportFieldId){
            errors.filters = 'Свяжите все поля фильтра с полями отчета'
            break
        }
    }

    if (Object.keys(errors).length === 0){
        delete obj.errors
        return {...obj}
    }
    else {
        return {...obj, errors}
    }
}
