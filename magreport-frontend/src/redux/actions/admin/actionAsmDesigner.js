import {
    ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_CHANGE_ROOT_DATA,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_DATA_TYPES_LOADED,
    ASM_DESIGNER_DATA_TYPES_LOAD_FAILED
} from "redux/reduxTypes";

export function actionAsmDesignerChangeRootData(key, value) {
    return {
        type: ASM_DESIGNER_CHANGE_ROOT_DATA,
        key,
        value
    };
}

export function actionAsmDesignerChangeSecuritySourceData(sourceIndex, key, value) {
    return {
        type: ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
        sourceIndex,
        key,
        value
    };
}

export function actionAsmDesignerSetSecuritySourceDataSet(sourceIndex, dataSet) {
    return {
        type: ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
        sourceIndex,
        dataSet
    };
}

export function actionAsmDesignerAddSecuritySourceField(sourceIndex, field) {
    return {
        type: ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
        sourceIndex,
        field: {
            dataSetFieldId: 0,
            filterInstanceFields: [],
            ...field
        }
    };
}

export function actionAsmDesignerSetSecuritySourceField(sourceIndex, fieldIndex, oldField, newField) {
    return {
        type: ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
        sourceIndex,
        fieldIndex,
        oldField: {
            dataSetFieldId: 0,
            filterInstanceFields: [],
            ...oldField
        },
        newField: {
            dataSetFieldId: 0,
            filterInstanceFields: [],
            ...newField
        }
    };
}

export function actionAsmDesignerAddSecuritySourceSecurityFilter(sourceIndex, securityFilter) {
    return {
        type: ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
        sourceIndex,
        securityFilter
    };
}

export function actionAsmDesignerDeleteSecuritySourceSecurityFilter(sourceIndex, securityFilterIndex, deletedFieldMappings) {
    return {
        type: ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
        sourceIndex,
        securityFilterIndex,
        deletedFieldMappings
    };
}

export function actionAsmDesignerAddSecurityFilterFieldMapping(sourceIndex, securityFilterIndex, fieldMapping) {
    return {
        type: ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
        sourceIndex,
        securityFilterIndex,
        fieldMapping: {
            dataSetFieldId: 0,
            filterInstanceFieldId: 0,
            ...fieldMapping
        }
    };
}

export function actionAsmDesignerUpdateSecurityFilterFieldMapping(sourceIndex,
                                                                  securityFilterIndex,
                                                                  fieldMappingIndex,
                                                                  oldFieldMapping,
                                                                  newFieldMapping) {
    return {
        type: ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING,
        sourceIndex,
        securityFilterIndex,
        fieldMappingIndex,
        oldFieldMapping,
        newFieldMapping: {
            ...oldFieldMapping,
            ...newFieldMapping
        }
    };
}

export function actionAsmDesignerDataTypesLoaded(data) {
    return {
        type: ASM_DESIGNER_DATA_TYPES_LOADED,
        data
    };
}

export function actionAsmDesignerDataTypesLoadFailed(error) {
    return {
        type: ASM_DESIGNER_DATA_TYPES_LOAD_FAILED,
        error
    }
}

