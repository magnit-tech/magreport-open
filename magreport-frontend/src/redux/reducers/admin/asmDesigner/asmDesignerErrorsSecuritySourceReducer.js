import {
    ASM_DATA_LOADED,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD
} from "redux/reduxTypes";
import {
    NAME,
    DESCRIPTION,
    DATASET_FIELD_ID,
    DATASET_ID,
    FIELD_TYPE,
    FILTER_VALUE_FIELD,
    FIELDS,
    POST_SQL,
    PRE_SQL
} from "utils/asmConstants";

function checkValue(value) {
    return typeof(value) === "string"
        ? value.trim() === ""
        : !Boolean(value);
}

export const asmDesignerErrorsSecuritySourceReducer = (state, action) => {
    let fieldType;
    let dataSetFieldId;
    let isError;
    switch (action.type) {
        case ASM_DATA_LOADED:
            const data = action.data;

            return {
                [NAME]: checkValue(data[NAME]),
                [DESCRIPTION]: checkValue(data[DESCRIPTION]),
                [DATASET_ID]: checkValue(data.dataSet ? data.dataSet.id : 0),
                [FIELDS]: (data[FIELDS] ? data[FIELDS] : []).map(fieldWrapper => {
                    const field = fieldWrapper.dataSetField || {};
                    return {
                        [FIELD_TYPE]: fieldWrapper[FIELD_TYPE],
                        [DATASET_FIELD_ID]: checkValue(field.id)
                    }
                })
            };

        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
            if([PRE_SQL, POST_SQL].indexOf(action.key) > -1) {
                return state;
            }

            return {
                ...state,
                [action.key]: checkValue(action.value)
            };

        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
            return {
                ...state,
                [DATASET_ID]: !Boolean(action.dataSet && action.dataSet.id)
            };

        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
            fieldType = action.field[FIELD_TYPE];
            dataSetFieldId = action.field[DATASET_FIELD_ID];
            isError = dataSetFieldId === 0;
            if (fieldType === FILTER_VALUE_FIELD && isError) {
                return state;
            }
            return {
                ...state,
                [FIELDS]: [...state[FIELDS], {
                    [FIELD_TYPE]: fieldType,
                    [DATASET_FIELD_ID]: isError
                }]
            };

        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
            const fieldIndex = action.fieldIndex;
            dataSetFieldId = action.newField[DATASET_FIELD_ID];
            isError = dataSetFieldId === 0;

            if (fieldIndex >= state[FIELDS].length) {
                return state;
            }

            const fields = state[FIELDS].slice();
            const field = fields[fieldIndex];
            if (field[FIELD_TYPE] === FILTER_VALUE_FIELD && isError) {
                fields.splice(fieldIndex, 1);
            } else {
                fields.splice(fieldIndex, 1, {...field, [DATASET_FIELD_ID]: isError});
            }

            return {
                ...state,
                [FIELDS]: fields
            };

        default:
            return state;
    }
}