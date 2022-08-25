import {
    NAME,
    DATASET_FIELD_ID,
    DESCRIPTION,
    FIELD_TYPE,
    FIELDS,
    FILTER_VALUE_FIELD,
    SECURITY_SOURCES,
    FILTER_INSTANCE_FIELD_ID,
    CODE_FIELD
} from "utils/asmConstants";
import {checkFieldMappingErrors} from "./checkFieldMappingErrors";

//Data
export function selectData(state) {
    return state.data;
}
export function selectDataValue(state, key) {
    return selectData(state)[key];
}

export function selectSecuritySource(state, sourceIndex) {
    return selectDataValue(state, SECURITY_SOURCES)[sourceIndex]
}

export function selectSecuritySourceDataValue(state, sourceIndex, key) {
    return selectSecuritySource(state, sourceIndex)[key];
}

//Errors
export function selectErrors(state) {
    return state.errors;
}

export function selectErrorValue(state, key) {
    return selectErrors(state)[key];
}

export function selectSecuritySourceErrors(state, sourceIndex) {
    return selectErrorValue(state, SECURITY_SOURCES)[sourceIndex];
}

export function selectSecuritySourceErrorValue(state, sourceIndex, key) {
    return selectSecuritySourceErrors(state, sourceIndex)[key];
}

export function selectSecuritySourceFieldErrors(state, sourceIndex, fieldIndex) {
    return selectSecuritySourceErrorValue(state, sourceIndex, FIELDS)[fieldIndex];
}

export function selectSecuritySourceFieldErrorValue(state, sourceIndex, fieldIndex, key) {
    return selectSecuritySourceFieldErrors(state, sourceIndex, fieldIndex)[key];
}

export function selectFieldMappingsErrors(state, sourceIndex, securityFilterIndex) {
    const fieldMappings = sourceIndex in state.fieldMappings
        ? state.fieldMappings[sourceIndex][securityFilterIndex] || []
        : [];
    return fieldMappings.map(fm => checkFieldMappingErrors(fm));
}

//DataSets
export function selectDataSet(state, sourceIndex) {
    return state.dataSets[sourceIndex] || {};
}

//SecurityFilters
export function selectSourceSecurityFilters(state, sourceIndex) {
    return state.securityFilters[sourceIndex] || [];
}

export function selectSecurityFilter(state, sourceIndex, securityFilterIndex) {
    return selectSourceSecurityFilters(state, sourceIndex)[securityFilterIndex] || {};
}

export function selectFilterInstance(state, sourceIndex, securityFilterIndex) {
    return sourceIndex in state.securityFilters
        ? state.securityFilters[sourceIndex][securityFilterIndex].filterInstance || {}
        : {};
}

export function selectFieldMappings(state, sourceIndex, securityFilterIndex) {
    return sourceIndex in state.fieldMappings
        ? state.fieldMappings[sourceIndex][securityFilterIndex] || []
        : [];
}

export function selectDataSetFieldsForSecurityFilter(state, sourceIndex, securityFilterIndex) {
    let dataSetFields = [];
    const dataSet = fns.selectDataSet(state, sourceIndex);
    const securitySource = fns.selectSecuritySource(state, sourceIndex);

    if (dataSet && "id" in dataSet) {

        dataSetFields = dataSet.fields;

        const filterValueFields = securitySource[FIELDS].filter(field => field[FIELD_TYPE] === FILTER_VALUE_FIELD);

        const dataSetIdsInFilterValueFields = filterValueFields.map(field => field[DATASET_FIELD_ID]);

        dataSetFields = dataSetFields.filter(field => {
            return dataSetIdsInFilterValueFields.indexOf(field.id) > -1;
        });
    }

    return dataSetFields;
}

export function selectFilterInstanceFields(state, sourceIndex, securityFilterIndex) {
    let filterInstanceFields = [];
    const filterInstance = fns.selectFilterInstance(state, sourceIndex, securityFilterIndex);

    if(filterInstance && "fields" in filterInstance) {
        filterInstanceFields = filterInstance.fields.filter(field => field.type === CODE_FIELD);
    }

    return filterInstanceFields;
}

export function selectDataTypeName(state, dataTypeId) {
    const dataTypes = state.dataTypes;
    if(!(dataTypeId in dataTypes)) {
        return "<Unknown type>";
    }

    const dataType = dataTypes[dataTypeId];

    return "name" in dataType ? dataType.name : "<Unknown type>";
}

export function selectHasErrors(state) {
    const errors = state.errors;
    let hasErrors = false;
    let hasNoMappings = true;

    hasErrors =  hasErrors || errors[NAME] || errors[DESCRIPTION];

    for(let securitySource of errors[SECURITY_SOURCES]) {
        for(let [prop, value] of Object.entries(securitySource)) {
            if(prop === FIELDS) {
                for(let field of value) {
                    hasErrors = hasErrors || field[DATASET_FIELD_ID];
                }
            } else {
                hasErrors = hasErrors || value;
            }
        }
    }

    for(let sourceSecurityFilters of Object.values(state.fieldMappings)) {
        for(let fieldMappings of Object.values(sourceSecurityFilters)) {
            for(let fieldMapping of fieldMappings) {
                const fieldMappingErrors = checkFieldMappingErrors(fieldMapping);
                hasErrors = hasErrors
                    || fieldMappingErrors[DATASET_FIELD_ID]
                    || fieldMappingErrors[FILTER_INSTANCE_FIELD_ID];
                hasNoMappings = false;
            }
        }
    }

    return hasErrors || hasNoMappings;
}

export const fns = {
    selectDataSet,
    selectSecurityFilter,
    selectSecuritySource,
    selectFilterInstance
}