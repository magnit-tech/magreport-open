import {
    CHANGE_TYPE_FIELD,
    DATASET_FIELD_ID,
    DATASET_ID,
    DESCRIPTION,
    FIELD_TYPE,
    FIELDS,
    FILTER_INSTANCE_FIELDS,
    GROUP_SOURCE,
    NAME,
    PERMISSION_SOURCE,
    POST_SQL,
    PRE_SQL,
    ROLE_NAME_FIELD, ROLE_TYPE_ID,
    SECURITY_FILTERS,
    SECURITY_SOURCES,
    SOURCE_TYPE,
    USER_MAP_SOURCE,
    USER_NAME_FIELD
} from "utils/asmConstants";
import {
    ASM_ADDED,
    ASM_EDITED,
    ASM_DATA_LOADED,
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_CHANGE_ROOT_DATA,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA, ASM_DESIGNER_DATA_TYPES_LOAD_FAILED, ASM_DESIGNER_DATA_TYPES_LOADED,
    ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD, ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING, ASM_ADD_ITEM_CLICK
} from "redux/reduxTypes";
import {asmDesignerDataReducer} from "./asmDesignerDataReducer";
import {asmDesignerErrorsReducer} from "./asmDesignerErrorsReducer";
import {asmDesignerDataSetsReducer} from "./asmDesignerDataSetsReducer";
import {asmDesignerSecurityFiltersReducer} from "./asmDesignerSecurityFiltersReducer";
import {asmDesignerFieldMappingsReducer} from "./asmDesignerFieldMappingsReducer";
import {asmDesignerDataTypesReducer} from "./asmDesignerDataTypesReducer";

const initialState = {
    data: {
        [NAME]: "",
        [DESCRIPTION]: "",
        [ROLE_TYPE_ID]: null,
        [SECURITY_SOURCES]: [
            {
                [SOURCE_TYPE]: GROUP_SOURCE,
                [NAME]: "",
                [DESCRIPTION]: "",
                [PRE_SQL]: "",
                [POST_SQL]: "",
                [DATASET_ID]: 0,
                [FIELDS]: [
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    },
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: ROLE_NAME_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    }
                ],
                [SECURITY_FILTERS]: []
            },
            {
                [SOURCE_TYPE]: USER_MAP_SOURCE,
                [NAME]: "",
                [DESCRIPTION]: "",
                [PRE_SQL]: "",
                [POST_SQL]: "",
                [DATASET_ID]: 0,
                [FIELDS]: [
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    },
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: ROLE_NAME_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    },
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: USER_NAME_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    }
                ],
                [SECURITY_FILTERS]: []
            },
            {
                [SOURCE_TYPE]: PERMISSION_SOURCE,
                [NAME]: "",
                [DESCRIPTION]: "",
                [PRE_SQL]: "",
                [POST_SQL]: "",
                [DATASET_ID]: 0,
                [FIELDS]: [
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    },
                    {
                        [DATASET_FIELD_ID]: 0,
                        [FIELD_TYPE]: ROLE_NAME_FIELD,
                        [FILTER_INSTANCE_FIELDS]: []
                    }
                ],
                [SECURITY_FILTERS]: []
            }
        ]
    },
    errors: {
        [NAME]: true,
        [DESCRIPTION]: true,
        [ROLE_TYPE_ID]: true,
        [SECURITY_SOURCES]: [
            {
                [NAME]: true,
                [DESCRIPTION]: true,
                [DATASET_ID]: true,
                [FIELDS]: [
                    {
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        [DATASET_FIELD_ID]: true
                    },
                    {
                        [FIELD_TYPE]: ROLE_NAME_FIELD,
                        [DATASET_FIELD_ID]: true
                    }],
            },
            {
                [NAME]: true,
                [DESCRIPTION]: true,
                [DATASET_ID]: true,
                [FIELDS]: [
                    {
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        [DATASET_FIELD_ID]: true
                    },
                    {
                        [FIELD_TYPE]: ROLE_NAME_FIELD,
                        [DATASET_FIELD_ID]: true
                    },
                    {
                        [FIELD_TYPE]: USER_NAME_FIELD,
                        [DATASET_FIELD_ID]: true
                    }],
            },
            {
                [NAME]: true,
                [DESCRIPTION]: true,
                [DATASET_ID]: true,
                [FIELDS]: [
                    {
                        [FIELD_TYPE]: CHANGE_TYPE_FIELD,
                        [DATASET_FIELD_ID]: true
                    },
                    {
                        [FIELD_TYPE]: ROLE_NAME_FIELD,
                        [DATASET_FIELD_ID]: true
                    }],
            }
        ]
    },
    dataSets: [null, null, null],
    securityFilters: {},
    fieldMappings: {},
    dataTypes: {}
}

const asmDesignerResponseDataReducer = (state, action) => {
    switch (action.type) {
        case ASM_DATA_LOADED:
            return action.data;
        default:
            return state;
    }
}

export const asmDesignerRootReducer = (state = initialState, action) => {
    switch (action.type) {
        case ASM_ADD_ITEM_CLICK:
        case ASM_ADDED:
        case ASM_EDITED:
            return initialState;

        case ASM_DATA_LOADED:
        case ASM_DESIGNER_CHANGE_ROOT_DATA:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER:
        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
        case ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING:
        case ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING:
        case ASM_DESIGNER_DATA_TYPES_LOADED:
        case ASM_DESIGNER_DATA_TYPES_LOAD_FAILED:
            return {
                ...state,
                data: asmDesignerDataReducer(state.data, action),
                responseData: asmDesignerResponseDataReducer(state.responseData, action),
                errors: asmDesignerErrorsReducer(state.errors, action),
                dataSets: asmDesignerDataSetsReducer(state.dataSets, action),
                securityFilters: asmDesignerSecurityFiltersReducer(state.securityFilters, action),
                fieldMappings: asmDesignerFieldMappingsReducer(state.fieldMappings, action),
                dataTypes: asmDesignerDataTypesReducer(state.dataTypes, action)
            };
        default:
            return state;
    }
};