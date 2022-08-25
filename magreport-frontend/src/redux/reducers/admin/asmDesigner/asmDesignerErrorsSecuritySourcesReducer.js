import {asmDesignerErrorsSecuritySourceReducer} from "./asmDesignerErrorsSecuritySourceReducer";
import {
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET, ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD, ASM_DATA_LOADED
} from "redux/reduxTypes";


export const asmDesignerErrorsSecuritySourcesReducer = (state, action) => {
    switch (action.type) {
        case ASM_DATA_LOADED:
            return action.data.sources.map(source => {
                return asmDesignerErrorsSecuritySourceReducer({}, {...action, data: source})
            });

        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
            if(action.sourceIndex >= state.length) {
                return state;
            }

            return state.map((securitySourceErrors, index) => {
                if (action.sourceIndex === index) {
                    return asmDesignerErrorsSecuritySourceReducer(securitySourceErrors, action);
                } else {
                    return securitySourceErrors;
                }
            });

        default:
            return state;
    }
};