import {
    ASM_DATA_LOADED,
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
} from "redux/reduxTypes";
import {DATASET_FIELD_ID, FIELD_TYPE, FILTER_INSTANCE_FIELD_ID, FILTER_VALUE_FIELD} from "utils/asmConstants";

export function createFieldMappingsFromResponse(data) {
    const newState = {};
    data.sources.forEach((source, sourceIndex) => {
        const fiFieldToSecurityFilterIndex = {};

        // get FILTER_VALUE_FIELD
        const fvFields = source.fields.filter(field => field[FIELD_TYPE] === FILTER_VALUE_FIELD);
        if (fvFields.length === 0) {
            return;
        }

        //create FilterInstanceField ID -> SecurityFilter Index mappings
        source.securityFilters.forEach((sfWrapper, sfIndex) => {
            sfWrapper.securityFilter.filterInstance.fields.forEach(fiField => {
                fiFieldToSecurityFilterIndex[fiField.id] = sfIndex;
            });
        });

        //for each FILTER_VALUE_FIELD get sourceIndex->securityFilterIndex path
        //and create Field Mapping
        fvFields.forEach(fvField => {
            const dataSetFieldId = fvField.dataSetField.id;

            fvField.filterInstanceFields.forEach(fiFieldWrapper => {
                const fiFieldId = fiFieldWrapper.filterInstanceField.id;
                const securityFilterIndex = fiFieldToSecurityFilterIndex[fiFieldId];
                if (securityFilterIndex === undefined) {
                    return;
                }

                if (!(sourceIndex in newState)) {
                    newState[sourceIndex] = {};
                }

                if (!(securityFilterIndex in newState[sourceIndex])) {
                    newState[sourceIndex][securityFilterIndex] = [];
                }

                newState[sourceIndex][securityFilterIndex].push({
                    [DATASET_FIELD_ID]: dataSetFieldId,
                    [FILTER_INSTANCE_FIELD_ID]: fiFieldId
                });
            });
        });
    });

    return newState;
}

export const asmDesignerFieldMappingsReducer = (state, action) => {
    let sourceIndex;
    let securityFilterIndex;
    let sourceSecurityFilters;
    let securityFilterFieldMappings;

    switch (action.type) {
        case ASM_DATA_LOADED:
            return createFieldMappingsFromResponse(action.data);

        case ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING:
            sourceIndex = action.sourceIndex;
            securityFilterIndex = action.securityFilterIndex;
            const fieldMapping = action.fieldMapping;
            let {dataSetFieldId, filterInstanceFieldId} = fieldMapping;

            if (!(dataSetFieldId || filterInstanceFieldId)) {
                return state;
            }

            if (sourceIndex in state) {
                sourceSecurityFilters = {...state[sourceIndex]};


                if (securityFilterIndex in sourceSecurityFilters) {
                    securityFilterFieldMappings = [...sourceSecurityFilters[securityFilterIndex], fieldMapping];
                } else {
                    securityFilterFieldMappings = [fieldMapping]
                }

                sourceSecurityFilters[securityFilterIndex] = securityFilterFieldMappings;
            } else {
                sourceSecurityFilters = {
                    [securityFilterIndex]: [fieldMapping]
                }
            }

            return {
                ...state,
                [sourceIndex]: sourceSecurityFilters
            };

        case ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING:
            sourceIndex = action.sourceIndex;
            securityFilterIndex = action.securityFilterIndex;
            const fieldMappingIndex = action.fieldMappingIndex;
            const newFieldMapping = action.newFieldMapping;

            if (!(sourceIndex in state)) {
                return state;
            }

            sourceSecurityFilters = state[sourceIndex];
            if (!(securityFilterIndex in sourceSecurityFilters)) {
                return state;
            }

            securityFilterFieldMappings = [...sourceSecurityFilters[securityFilterIndex]];
            if (fieldMappingIndex >= securityFilterFieldMappings.length) {
                return state;
            }

            // delete mapping if both fields are empty
            if (newFieldMapping.dataSetFieldId === 0 && newFieldMapping.filterInstanceFieldId === 0) {
                securityFilterFieldMappings.splice(fieldMappingIndex, 1);
            } else {
                securityFilterFieldMappings.splice(fieldMappingIndex, 1, newFieldMapping);
            }

            return {
                ...state,
                [sourceIndex]: {
                    ...state[sourceIndex],
                    [securityFilterIndex]: securityFilterFieldMappings
                }
            };

        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
            sourceIndex = action.sourceIndex;
            securityFilterIndex = action.securityFilterIndex;

            if (!(sourceIndex in state)) {
                return state;
            }

            sourceSecurityFilters = state[sourceIndex];
            if (!(securityFilterIndex in sourceSecurityFilters)) {
                return state;
            }

            sourceSecurityFilters = {...sourceSecurityFilters};
            delete sourceSecurityFilters[securityFilterIndex];

            return {
                ...state,
                [sourceIndex]: sourceSecurityFilters
            };

        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
            let stateUpdated = false;
            const oldField = action.oldField;
            //const newField = action.newField;
            sourceIndex = action.sourceIndex;


            if(oldField[FIELD_TYPE] !== FILTER_VALUE_FIELD) {
                return state;
            }

            if(!(sourceIndex in state)) {
                return state;
            }

            sourceSecurityFilters = {...state[sourceIndex]};

            for(let [index, fieldMappings] of Object.entries(sourceSecurityFilters)) {
                let mappingsUpdated = false;
                const newFieldMappings = fieldMappings.map(m => {
                    if(m[DATASET_FIELD_ID] === oldField[DATASET_FIELD_ID]) {
                        mappingsUpdated = true;
                        return {
                            ...m,
                            [DATASET_FIELD_ID]: 0
                        };
                    }

                    return m;
                });

                if(mappingsUpdated) {
                    stateUpdated = true;
                    sourceSecurityFilters[index] = newFieldMappings;
                }
            }

            if(stateUpdated) {
                return {
                    ...state,
                    [sourceIndex]: sourceSecurityFilters
                }
            } else {
                return state;
            }




        default:
            return state;
    }
};