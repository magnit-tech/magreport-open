import {
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD, ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD, ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING, ASM_DATA_LOADED
} from "redux/reduxTypes";
import {
    DATASET_FIELD_ID,
    FIELD_TYPE,
    FILTER_INSTANCE_FIELD_ID,
    FILTER_INSTANCE_FIELDS,
    FILTER_VALUE_FIELD
} from "utils/asmConstants";


function getStateWithAddedFieldMapping(state, dataSetFieldId, filterInstanceFieldId) {
    const fieldIndex = state.findIndex(f => f[FIELD_TYPE] === FILTER_VALUE_FIELD
        && f[DATASET_FIELD_ID] === dataSetFieldId);
    // FILTER_VALUE_FIELD for dataset field not found, return same state
    if (fieldIndex === -1) {
        return state;
    }

    const field = state[fieldIndex];
    const filterInstanceFields = field[FILTER_INSTANCE_FIELDS].slice();
    const newFilterInstanceField = {[FILTER_INSTANCE_FIELD_ID]: filterInstanceFieldId};

    filterInstanceFields.push(newFilterInstanceField);

    const newState = state.slice();
    newState[fieldIndex] = {
        ...field,
        [FILTER_INSTANCE_FIELDS]: filterInstanceFields
    };

    return newState;
}

function getStateWithRemovedFieldMapping(state, dataSetFieldId, filterInstanceFieldId) {
    const fieldIndex = state.findIndex(f => f[FIELD_TYPE] === FILTER_VALUE_FIELD
        && f[DATASET_FIELD_ID] === dataSetFieldId);
    // FILTER_VALUE_FIELD for dataset field not found, return same state
    if (fieldIndex === -1) {
        return state;
    }

    const field = state[fieldIndex];
    const filterInstanceFields = field[FILTER_INSTANCE_FIELDS];
    const filterInstanceFieldIds = filterInstanceFields.map(f => f[FILTER_INSTANCE_FIELD_ID]);

    const filterInstanceIndex = filterInstanceFieldIds.indexOf(filterInstanceFieldId);

    if(filterInstanceIndex === -1) {
        return state;
    } else {
        filterInstanceFields.splice(filterInstanceIndex, 1);
    }

    const newState = state.slice();
    newState[fieldIndex] = {
        ...field,
        [FILTER_INSTANCE_FIELDS]: filterInstanceFields
    };

    return newState;
}

function getStateWithChangedFilterInstanceField(state, dataSetFieldId, oldFilterInstanceFieldId, newFilterInstanceFieldId) {
    const fieldIndex = state.findIndex(f => f[FIELD_TYPE] === FILTER_VALUE_FIELD
        && f[DATASET_FIELD_ID] === dataSetFieldId);
    // FILTER_VALUE_FIELD for dataset field not found, return same state
    if (fieldIndex === -1) {
        return state;
    }

    const field = state[fieldIndex];
    const filterInstanceFields = field[FILTER_INSTANCE_FIELDS].slice();
    const filterInstanceFieldIds = filterInstanceFields.map(f => f[FILTER_INSTANCE_FIELD_ID]);
    const newFilterInstanceField = {[FILTER_INSTANCE_FIELD_ID]: newFilterInstanceFieldId};

    const filterInstanceIndex = filterInstanceFieldIds.indexOf(oldFilterInstanceFieldId);

    if(filterInstanceIndex === -1) {
        // should not reach here
        return state;
    } else {
        filterInstanceFields[filterInstanceIndex] = newFilterInstanceField;
    }

    const newState = state.slice();
    newState[fieldIndex] = {
        ...field,
        [FILTER_INSTANCE_FIELDS]: filterInstanceFields
    };

    return newState;
}

export const asmDesignerDataSecuritySourceFieldsReducer = (state, action) => {
    let newState;

    switch (action.type) {
        case ASM_DATA_LOADED:
            const data = action.data || [];

            return data.map(field => ({
                id: field.id,
                [FIELD_TYPE]: field[FIELD_TYPE],
                [DATASET_FIELD_ID]: field.dataSetField ? field.dataSetField.id : 0,
                [FILTER_INSTANCE_FIELDS] : (field[FILTER_INSTANCE_FIELDS] || [])
                                                .map(fiWrapper => ({
                                                    id: fiWrapper.id,
                                                    [FILTER_INSTANCE_FIELD_ID]: fiWrapper.filterInstanceField.id
                                                }))
            }));

        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
            let fieldTypeExists = {};
            newState = state
                .filter(field => {
                    if (field[FIELD_TYPE] in fieldTypeExists) {
                        return false;
                    } else {
                        fieldTypeExists[field[FIELD_TYPE]] = true;
                        return true;
                    }
                })
                .map(field => ({
                    [DATASET_FIELD_ID]: 0,
                    [FIELD_TYPE]: field[FIELD_TYPE],
                    [FILTER_INSTANCE_FIELDS]: []
                }));
            return newState;

        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
            newState = state.map((field, fieldIndex) => {
                if (action.fieldIndex === fieldIndex) {
                    return {
                        ...field,
                        ...action.newField,
                        [FILTER_INSTANCE_FIELDS]: []
                    };
                } else {
                    return field;
                }
            });

            if (newState[action.fieldIndex][DATASET_FIELD_ID] === 0 &&
                newState[action.fieldIndex][FIELD_TYPE] === FILTER_VALUE_FIELD) {
                newState.splice(action.fieldIndex, 1);
            }

            return newState;

        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
            if (action.field.dataSetFieldId === 0) {
                return state;
            }

            return [
                ...state,
                {
                    ...action.field
                }
            ];

        case ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING:
            const oldDataSetFieldId = action.oldFieldMapping[DATASET_FIELD_ID];
            const oldFilterInstanceFieldId = action.oldFieldMapping[FILTER_INSTANCE_FIELD_ID];
            const newDataSetFieldId = action.newFieldMapping[DATASET_FIELD_ID];
            const newFilterInstanceFieldId = action.newFieldMapping[FILTER_INSTANCE_FIELD_ID];

            if (oldDataSetFieldId === newDataSetFieldId
                && oldFilterInstanceFieldId === newFilterInstanceFieldId) {
                return state;
            }

            //
            // same FILTER_VALUE_FIELD
            if(oldDataSetFieldId === newDataSetFieldId) {
                // ADD filter instance field
                if(oldFilterInstanceFieldId === 0) {
                    return getStateWithAddedFieldMapping(state, oldDataSetFieldId, newFilterInstanceFieldId);
                }

                // REMOVE filter instance field
                if(newFilterInstanceFieldId === 0) {
                    return getStateWithRemovedFieldMapping(state, oldDataSetFieldId, oldFilterInstanceFieldId);
                }

                // CHANGE filter instance field
                return getStateWithChangedFilterInstanceField(state, oldDataSetFieldId, oldFilterInstanceFieldId, newFilterInstanceFieldId);
            }

            //
            // DataSet field (FILTER_VALUE_FIELD) changed
            if (oldFilterInstanceFieldId === newFilterInstanceFieldId) {
                // ADD filter instance field to FILTER_VALUE_FIELD
                if (oldDataSetFieldId === 0) {
                    return getStateWithAddedFieldMapping(state, newDataSetFieldId, newFilterInstanceFieldId);
                }

                // REMOVE field instance field from FILTER_VALUE_FIELD
                if (newDataSetFieldId === 0) {
                    return getStateWithRemovedFieldMapping(state, oldDataSetFieldId, newFilterInstanceFieldId);
                }

                // MOVE field instance field from old to new FILTER_VALUE_FIELD
                newState = getStateWithRemovedFieldMapping(state, oldDataSetFieldId, newFilterInstanceFieldId);
                newState = getStateWithAddedFieldMapping(newState, newDataSetFieldId, newFilterInstanceFieldId);

                return newState;
            }

            //
            // BOTH dataset field and filter instance field have changed
            // this is impossible in normal flow

            return state;

        case ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING:
            const dataSetFieldId = action.fieldMapping[DATASET_FIELD_ID];
            const filterInstanceFieldId = action.fieldMapping[FILTER_INSTANCE_FIELD_ID];

            if(dataSetFieldId === 0 || filterInstanceFieldId === 0) {
                return state;
            }

            return getStateWithAddedFieldMapping(state, dataSetFieldId, filterInstanceFieldId);

        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
            const deletedFieldMappings = action.deletedFieldMappings;

            return deletedFieldMappings.reduce((prevState, fieldMapping) =>
                    getStateWithRemovedFieldMapping(prevState,
                        fieldMapping[DATASET_FIELD_ID],
                        fieldMapping[FILTER_INSTANCE_FIELD_ID]),
                state);

        default:
            return state;
    }
};