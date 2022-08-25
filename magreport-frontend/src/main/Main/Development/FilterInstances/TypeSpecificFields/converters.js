// Date Range
export function convertDateRangeFilterToLocalData(filterInstanceData){

    let idField = {
        name : '',
        description : ''
    }
    let fromField = {
        name : '',
        description : ''
    };
    let toField = {
        name : '',
        description : ''
    };

    for(let f of filterInstanceData.fields){
        if(f.type === 'ID_FIELD' && f.level === 1){
            idField.name = f.name;
            idField.description = f.description;
        }
        else if(f.type === 'CODE_FIELD' && f.level === 1){
            fromField.name = f.name;
            fromField.description = f.description;
        }
        else if(f.type === 'CODE_FIELD' && f.level === 2){
            toField.name = f.name;
            toField.description = f.description;
        }
    }

    return {
        idField : idField,
        fromField : fromField,
        toField : toField
    }
}

export function convertDateRangeLocalToFilterData(filterInstanceData, localData){

    let newFilterInstanceData = {
        ...filterInstanceData,
        fields : [{
            level : 1,
            type : "ID_FIELD",
            name : localData.idField.name,
            description : localData.idField.description
        },
            {
                level : 1,
                type : "CODE_FIELD",
                name : localData.fromField.name,
                description : localData.fromField.description
            },
            {
                level : 2,
                type : "ID_FIELD",
                name : localData.idField.name,
                description : localData.idField.description
            },
            {
                level : 2,
                type : "CODE_FIELD",
                name : localData.toField.name,
                description : localData.toField.description
            }]
    }

    let errors = {
        idFieldName : localData.idField.name.trim() === '' ? 'Не задано имя поля "Дата в отчёте"' : undefined,
        idFieldDescription : localData.idField.description.trim() === '' ? 'Не задано описание поля "Дата в отчёте"' : undefined,
        fromFieldName : localData.fromField.name.trim() === '' ? 'Не задано имя поля "Дата с"' : undefined,
        fromFieldDescription : localData.fromField.description.trim() === '' ? 'Не задано описание поля "Дата с"' : undefined,
        toFieldName : localData.toField.name.trim() === '' ? 'Не задано имя поля "Дата по"' : undefined,
        toFieldDescription : localData.toField.description.trim() === '' ? 'Не задано описание поля "Дата по"' : undefined,
    }

    return{
        filterInstanceData: newFilterInstanceData,
        errors : errors
    }
}

// Date Value
export function convertDateValueFilterToLocalData(filterInstanceData){

    /*let idField = {
        name : '',
        description : ''
    }*/
    let dateField = {
        name : '',
        description : ''
    };

    for(let f of filterInstanceData.fields){
        /*if(f.type === 'ID_FIELD' && f.level === 1){
            idField.name = f.name;
            idField.description = f.description;
        }*/
        if(f.type === 'CODE_FIELD' && f.level === 1){
            dateField.name = f.name;
            dateField.description = f.description;
        }
    }

    return {
        //idField : idField,
        dateField : dateField,
    }
}

export function convertDateValueLocalToFilterData(filterInstanceData, localData){

    let newFilterInstanceData = {
        ...filterInstanceData,
        fields : [{
            level : 1,
            type : "ID_FIELD",
            //name : localData.idField.name,
            //description : localData.idField.description
            name : localData.dateField.name,
            description : localData.dateField.description
        },
            {
                level : 1,
                type : "CODE_FIELD",
                name : localData.dateField.name,
                description : localData.dateField.description
            }]
    }

    let errors = {
        //idFieldName : localData.idField.name.trim() === '' ? 'Не задано имя поля "Дата в отчёте"' : undefined,
        //idFieldDescription : localData.idField.description.trim() === '' ? 'Не задано описание поля "Дата в отчёте"' : undefined,
        dateFielddName : localData.dateField.name.trim() === '' ? 'Не задано имя поля "Дата"' : undefined,
        dateFieldDescription : localData.dateField.description.trim() === '' ? 'Не задано описание поля "Дата"' : undefined,
    }

    return{
        filterInstanceData: newFilterInstanceData,
        errors : errors
    }
}

// Hier Tree
export function convertHierTreeFilterToLocalData(filterInstanceData){

    let inputFields = filterInstanceData.fields
    inputFields.sort( (f1,f2) => (f1.level - f2.level));

    let levels = [];
    let curLevel = 0;

    for(let f of inputFields){
        if(f.level > curLevel){
            curLevel = f.level;
            levels.push({});
        }

        let field = {
            dataSetFieldId : f.dataSetFieldId,
            dataSetFieldIdError : f.dataSetFieldId === undefined || f.dataSetFieldId === null,
            name : f.name,
            nameError : (f.name.trim() === ''),
            description : f.description,
            descriptionError : (f.description.trim() === ''),
            expand : Boolean(f.expand),
        }

        if(f.type === 'ID_FIELD'){
            levels[curLevel - 1].idField = field
        }
        else if(f.type === 'NAME_FIELD'){
            levels[curLevel - 1].nameField = field
        }
    }

    let data = {
        dataSetId : filterInstanceData.dataSetId,
        dataSetIdError : (filterInstanceData.dataSetId === undefined || filterInstanceData.dataSetId === null),
        levels: levels
    };
    return data;
}

export function convertHierTreeLocalToFilterData(filterInstanceData, localData){

    let fields = [];
    let errors = [];
    let levelsLength = localData.levels.length;

    for(let lev in localData.levels){
        let levelObj = localData.levels[lev];
        let level = parseInt(lev) + 1;

        fields.push({
            level: level,
            type: "ID_FIELD",
            dataSetFieldId: levelObj.idField.dataSetFieldId,
            name: levelObj.idField.name,
            description: levelObj.idField.description,
            expand: level === levelsLength ? false: levelObj.idField.expand
        });
        if(levelObj.idField.name.trim() === ''){
            errors.push("Не задано имя поля типа ID уровня " + level);
        }
        if(levelObj.idField.description.trim() === ''){
            errors.push("Не задано описание поля типа ID уровня " + level);
        }
        fields.push({
            level: level,
            type: "CODE_FIELD",
            dataSetFieldId: levelObj.idField.dataSetFieldId,
            name: levelObj.idField.name,
            description: levelObj.idField.description
        });
        fields.push({
            level: level,
            type: "NAME_FIELD",
            dataSetFieldId: levelObj.nameField.dataSetFieldId,
            name: levelObj.nameField.name,
            description: levelObj.nameField.description
        });
        if(levelObj.nameField.name.trim() === ''){
            errors.push("Не задано имя поля типа NAME уровня " + level);
        }
        if(levelObj.nameField.description.trim() === ''){
            errors.push("Не задано описание поля типа NAME уровня " + level);
        }
    }

    let newFilterInstanceData = {
        ...filterInstanceData,
        dataSetId : localData.dataSetId,
        fields : fields
    }

    return {
        filterInstanceData: newFilterInstanceData,
        errors: errors
    };
}

// Token Input

export function convertTokenInputFilterToLocalData(filterInstanceData){

    let idField = {
        id : null,
        name : '',
        description : ''
    };
    let nameField = {
        id : null,
        name : '',
        description : ''
    };

    for(let f of filterInstanceData.fields){
        if(f.type === 'ID_FIELD'){
            idField.id = f.dataSetFieldId;
            idField.name = f.name;
            idField.description = f.description;
        }
        else if(f.type === 'NAME_FIELD'){
            nameField.id = f.dataSetFieldId;
            nameField.name = f.name;
            nameField.description = f.description;
        }
    }

    let data = {
        dataSetId : filterInstanceData.dataSetId,
        datasetFields : {
            idField : idField,
            nameField : nameField
        }
    };

    let errorFields = {
        dataSetId : data.dataSetId === undefined,
        idField:{
            id : (data.datasetFields.idField.id === null),
            name : (data.datasetFields.idField.name.trim() === ''),
            description : (data.datasetFields.idField.description.trim() === '')
        },
        nameField:{
            id : (data.datasetFields.nameField.id === null),
            name : (data.datasetFields.nameField.name.trim() === ''),
            description : (data.datasetFields.nameField.description.trim() === '')
        }
    };

    return {
        localData : data,
        errorFields : errorFields
    };
}

export function convertTokenInputLocalToFilterData(filterInstanceData, localData){

    let newFilterInstanceData = {
        ...filterInstanceData,
        dataSetId : localData.dataSetId,
        fields : [{
            level : 1,
            type : "ID_FIELD",
            dataSetFieldId : localData.datasetFields.idField.id,
            name : localData.datasetFields.idField.name,
            description : localData.datasetFields.idField.description
        },
            {
                level : 1,
                type : "CODE_FIELD",
                dataSetFieldId : localData.datasetFields.idField.id,
                name : localData.datasetFields.idField.name,
                description : localData.datasetFields.idField.description
            },
            {
                level : 1,
                type : "NAME_FIELD",
                dataSetFieldId : localData.datasetFields.nameField.id,
                name : localData.datasetFields.nameField.name,
                description : localData.datasetFields.nameField.description
            }]
    }

    let errors = {
        dataSetId : localData.dataSetId === undefined ? "Не определён набор данных справочника" : undefined,
        fieldIdName : localData.datasetFields.idField.name.trim() === '' ? "Не задано имя поля типа ID" : undefined,
        fieldIdDescription : localData.datasetFields.idField.description.trim() === '' ? "Не задано описание поля типа ID" : undefined,
        fieldIdId : localData.datasetFields.idField.id === null ? "Не выбрано поле типа ID" : undefined,
        fieldNameName : localData.datasetFields.nameField.name.trim() === '' ? "Не задано имя поля типа CODE" : undefined,
        fieldNameDescription : localData.datasetFields.nameField.description.trim() === '' ? "Не задано описание поля типа CODE" : undefined,
        fieldNameId : localData.datasetFields.nameField.id === null ? "Не выбрано поле типа CODE" : undefined
    };

    return {
        filterInstanceData: newFilterInstanceData,
        errors: errors
    };
}

// Value List
export function convertValueListFilterToLocalData(filterInstanceData){

    let idField = {
        id : null,
        name : '',
        description : ''
    };
    let codeField = {
        id : null,
        name : '',
        description : ''
    };

    for(let f of filterInstanceData.fields){
        if(f.type === 'ID_FIELD'){
            idField.id = f.dataSetFieldId;
            idField.name = f.name;
            idField.description = f.description;
        }
        else if(f.type === 'CODE_FIELD'){
            codeField.id = f.dataSetFieldId;
            codeField.name = f.name;
            codeField.description = f.description;
        }
    }

    let data = {
        dataSetId : filterInstanceData.dataSetId,
        datasetFields : {
            idField : idField,
            codeField : codeField
        }
    };

    let errorFields = {
        dataSetId : data.dataSetId === undefined,
        idField:{
            id : (data.datasetFields.idField.id === null),
            name : (data.datasetFields.idField.name.trim() === ''),
            description : (data.datasetFields.idField.description.trim() === '')
        },
        codeField:{
            id : (data.datasetFields.codeField.id === null),
            name : (data.datasetFields.codeField.name.trim() === ''),
            description : (data.datasetFields.codeField.description.trim() === '')
        }
    };

    return {
        localData : data,
        errorFields : errorFields
    };
}

export function convertValueListLocalToFilterData(filterInstanceData, localData){

    let newFilterInstanceData = {
        ...filterInstanceData,
        dataSetId : localData.dataSetId,
        fields : [{
            level : 1,
            type : "ID_FIELD",
            dataSetFieldId : localData.datasetFields.idField.id,
            name : localData.datasetFields.idField.name,
            description : localData.datasetFields.idField.description
        },
            {
                level : 1,
                type : "CODE_FIELD",
                dataSetFieldId : localData.datasetFields.codeField.id,
                name : localData.datasetFields.codeField.name,
                description : localData.datasetFields.codeField.description
            }]
    }

    let errors = {
        dataSetId : localData.dataSetId === undefined ? "Не определён набор данных справочника" : undefined,
        fieldIdName : localData.datasetFields.idField.name.trim() === '' ? "Не задано имя поля типа ID" : undefined,
        fieldIdDescription : localData.datasetFields.idField.description.trim() === '' ? "Не задано описание поля типа ID" : undefined,
        fieldIdId : localData.datasetFields.idField.id === null ? "Не выбрано поле типа ID" : undefined,
        fieldCodeName : localData.datasetFields.codeField.name.trim() === '' ? "Не задано имя поля типа CODE" : undefined,
        fieldCodeDescription : localData.datasetFields.codeField.description.trim() === '' ? "Не задано описание поля типа CODE" : undefined,
        fieldCodeId : localData.datasetFields.codeField.id === null ? "Не выбрано поле типа CODE" : undefined
    };

    return {
        filterInstanceData: newFilterInstanceData,
        errors: errors
    };
}
