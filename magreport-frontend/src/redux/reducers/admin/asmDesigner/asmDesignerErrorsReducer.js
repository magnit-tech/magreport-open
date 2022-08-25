import {
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_CHANGE_ROOT_DATA,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD,
    ASM_DATA_LOADED
} from "redux/reduxTypes";
import {asmDesignerErrorsSecuritySourcesReducer} from "./asmDesignerErrorsSecuritySourcesReducer";
import {NAME, DESCRIPTION, ROLE_TYPE_ID, SECURITY_SOURCES} from "utils/asmConstants";

function checkValue(value) {
    return typeof(value) === "string"
        ? value.trim() === ""
        : !Boolean(value)
}

function checkRoleTypeId(roleTypeId) {
    return !(roleTypeId > -1); //because role types ID sequence starts from 0
}
export const asmDesignerErrorsReducer = (state, action) => {
    switch (action.type) {
        case ASM_DATA_LOADED:
            const data = action.data;
            return {
                [NAME]: checkValue(data[NAME]),
                [DESCRIPTION]: checkValue(data[DESCRIPTION]),
                [ROLE_TYPE_ID]: checkRoleTypeId(data.roleType ? data.roleType.id : -1),
                [SECURITY_SOURCES]: asmDesignerErrorsSecuritySourcesReducer([], action)
            };

        case ASM_DESIGNER_CHANGE_ROOT_DATA:
            let isError;
            if(action.key === ROLE_TYPE_ID) {
                isError = checkRoleTypeId(action.value);
            } else {
                isError = checkValue(action.value)
            }

            return {
                ...state,
                [action.key]: isError
            }

        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
            return {
                ...state,
                [SECURITY_SOURCES]: asmDesignerErrorsSecuritySourcesReducer(state[SECURITY_SOURCES], action)
            };

        default:
            return state;
    }
};
