import {
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_CHANGE_ROOT_DATA,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
    ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING, ASM_DATA_LOADED
} from "redux/reduxTypes";
import {NAME, DESCRIPTION, ROLE_TYPE_ID, SECURITY_SOURCES} from "utils/asmConstants";
import {asmDesignerDataSecuritySourcesReducer} from "./asmDesignerDataSecuritySourcesReducer";

export const asmDesignerDataReducer = (state, action) => {
    switch (action.type) {
        case ASM_DATA_LOADED:
            return {
                ...state,
                id: action.data.id,
                [NAME]: action.data[NAME],
                [DESCRIPTION]: action.data[DESCRIPTION],
                [ROLE_TYPE_ID]: action.data.roleType ? action.data.roleType.id : state[ROLE_TYPE_ID],
                [SECURITY_SOURCES]: asmDesignerDataSecuritySourcesReducer(state[SECURITY_SOURCES],
                    {...action, data: action.data.sources || []})
            };

        case ASM_DESIGNER_CHANGE_ROOT_DATA:
            return {
                ...state,
                [action.key]: action.value
            };

        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER:
        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
        case ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING:
        case ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING:
            return {
                ...state,
                [SECURITY_SOURCES]: asmDesignerDataSecuritySourcesReducer(state[SECURITY_SOURCES], action)
            };

        default:
            return state;
    }
};

