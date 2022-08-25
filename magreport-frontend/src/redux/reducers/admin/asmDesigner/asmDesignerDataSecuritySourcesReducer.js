import {
    ASM_DATA_LOADED,
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD, ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA, ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD, ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
} from "redux/reduxTypes";
import {asmDesignerDataSecuritySourceReducer} from "./asmDesignerDataSecuritySourceReducer";

export const asmDesignerDataSecuritySourcesReducer = (state, action) => {
    switch (action.type) {
        case ASM_DATA_LOADED:
            const data = action.data || [];
            return data.map((source, index) => {
                return asmDesignerDataSecuritySourceReducer(index in state ? state[index] : {},
                    {...action, data: source});
            });

        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER:
        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
        case ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING:
        case ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING:
            if(action.sourceIndex >= state.length) {
                return state;
            }

            return state.map((securitySource, index) => {
                if (action.sourceIndex === index) {
                    return asmDesignerDataSecuritySourceReducer(securitySource, action);
                } else {
                    return securitySource;
                }
            });
        default:
            return state;
    }
};