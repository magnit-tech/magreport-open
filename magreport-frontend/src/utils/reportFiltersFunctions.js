/**
 * Возвращает идентификатор и имя поля фильтра
 * @param {{fields: Array}} filterData - данные по фильтру
 * @return {{fieldName: string, fieldId: number}}
 */
export function getValueField(filterData) {
    let fieldId = 0;
    let fieldName = "";

    for (let f of filterData.fields || []) {
        if (f.type === "CODE_FIELD" && f.level === 1) {
            fieldId = f.id;
            fieldName = f.name;
        }
    }

    return {
        fieldId,
        fieldName
    };
}

/**
 * Возвращает идентификатор поля фильтра
 * @param {{fields: Array}} filterData - данные по фильтру
 * @return {number|null}
 */
export function getCodeFieldId(filterData) {
    let codeFieldId = null;
    for (let f of filterData.fields || []) {
        if (f.type === "CODE_FIELD") {
            codeFieldId = f.id;
        }
    }

    return codeFieldId;
}

/**
 * Возвращает значение фильтра поля с указанным идентификатором
 * @param {{parameters: Array}} lastFilterValue - значения фильтра с последнего запуска отчета
 * @param {number} fieldId - идентификатор поля
 * @return {number|null}
 */
export function getValueFieldValue(lastFilterValue, fieldId) {
    let value = null;

    if(lastFilterValue){
        for(let f of lastFilterValue.parameters || []){
            if(f.values[0].fieldId === fieldId){
                value = f.values[0].value;
            }
        }
    }

    return value;
}

/**
 * Возвращает список значений поля в виде строки
 * @param {{parameters: Array}} lastFilterValue - значения фильтра с последнего запуска отчета
 * @param {number} codeFieldId - идентификатор поля
 * @param {String} [sep=';'] - разделитель между значениями
 * @return {String}
 */
 export function buildTextFromLastValues(type, lastFilterValue, codeFieldId, sep=';', fields) {
    if(!lastFilterValue) {
        return "";
    } 
    else if (typeof (type) === 'object' ? type.name === "TOKEN_INPUT" : type === "TOKEN_INPUT") {
        let nameFieldId = null,
            nameValue = [];

        if(fields){
            for(let f of fields){
                if(f.type === "NAME_FIELD"){
                    nameFieldId = f.id
                }
            }
        } 
        for(let p of lastFilterValue.parameters){
            for(let v of p.values){
                if (v.fieldId === nameFieldId){
                    nameValue.push(v.value)
                }
            }
        }
        return nameValue.join(sep);
    }

    return (lastFilterValue.parameters || [])
        .flatMap(p => p.values || [])
        .filter(v => v.fieldId === codeFieldId)
        .map(v => v.value)
        .join(sep);

}

/**
 * Возвращает идентификаторы и имена поля фильтра типа RANGE
 * @param {{fields: Array}} filterData - данные по фильтру
 * @return {{startFieldName: string, startFieldId: number, endFieldName: string, endFieldId: number}}
 */
export function getRangeFields(filterData) {
    let startFieldId = 0;
    let startFieldName = "";
    let endFieldId = 0;
    let endFieldName = "";

    for (let f of filterData.fields || []) {
        if (f.type === "CODE_FIELD" && f.level === 1) {
            startFieldId = f.id;
            startFieldName = f.name;
        }
        if (f.type === "CODE_FIELD" && f.level === 2) {
            endFieldId = f.id;
            endFieldName = f.name;
        }
    }

    return {
        startFieldId,
        startFieldName,
        endFieldId,
        endFieldName
    };
}

/**
 * Возвращает значения фильтра типа RANGE
 * @param {{parameters: Array}} lastFilterValue - значения фильтра с последнего запуска отчета
 * @param {number} startFieldId - идентификатор поля с начальным значением диапазона
 * @param {number} endFieldId - идентификатор поля с конечным значением диапазона
 * @return {{startValue: number|null, endValue: number|null}}
 */
export function getRangeFieldsValues(lastFilterValue, startFieldId, endFieldId) {
    let startValue = null;
    let endValue = null;

    if(lastFilterValue){
        for(let f of lastFilterValue.parameters || []){
            if(f.values[0].fieldId === startFieldId){
                startValue = f.values[0].value
            }
            if(f.values[1].fieldId === endFieldId){
                endValue = f.values[1].value
            }
        }
    }

    return {
        startValue,
        endValue
    };
}

/**
 * Возвращает список всех дочерних элементов группы
 * @param {Object} groupData - данные по группе фильтров
 * @return {(*&{_elementType: string})[]}
 */
export function getAllGroupChildren(groupData) {
    const groups = groupData.childGroups.map((v) => ({...v, _elementType: "group"}));
    const filters = groupData.filters.map((v) => ({...v, _elementType: "filter"}));

    return groups
        .concat(filters)
        .sort((a, b) => (a.ordinal - b.ordinal));
}
