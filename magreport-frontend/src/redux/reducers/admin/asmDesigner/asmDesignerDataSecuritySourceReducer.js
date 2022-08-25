import {
    ASM_DATA_LOADED,
    ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING,
    ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD, ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA, ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET,
    ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD, ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING
} from "redux/reduxTypes";
import {
    NAME,
    DATASET_ID,
    DESCRIPTION,
    FIELDS,
    POST_SQL,
    PRE_SQL,
    SECURITY_FILTERS,
    SECURITY_FILTER_ID,
    SOURCE_TYPE
} from "utils/asmConstants";
import {asmDesignerDataSecuritySourceFieldsReducer} from "./asmDesignerDataSecuritySourceFieldsReducer";

export const asmDesignerDataSecuritySourceReducer = (state, action) => {
    let securityFilters;

    switch (action.type) {
        case ASM_DATA_LOADED:
            const data = action.data;
            securityFilters = data.securityFilters || [];
            return {
                ...state,
                id: data.id,
                [SOURCE_TYPE]: data[SOURCE_TYPE],
                [NAME]: data[NAME],
                [DESCRIPTION]: data[DESCRIPTION],
                [PRE_SQL]: data[PRE_SQL],
                [POST_SQL]: data[POST_SQL],
                [DATASET_ID]: "dataSet" in data ? data.dataSet.id : state[DATASET_ID],
                [FIELDS]: asmDesignerDataSecuritySourceFieldsReducer(state[FIELDS] || [],
            {...action, data: data[FIELDS]}),
                [SECURITY_FILTERS]: securityFilters.map(sf => ({id: sf.id, [SECURITY_FILTER_ID]: sf.securityFilter.id}))
            };

        case ASM_DESIGNER_CHANGE_SECURITY_SOURCE_DATA:
            return {
                ...state,
                [action.key]: action.value
            };

        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
            return {
                ...state,
                [FIELDS]: asmDesignerDataSecuritySourceFieldsReducer(state[FIELDS], action),
                [DATASET_ID]: action.dataSet ? action.dataSet.id : 0
            };

        case ASM_DESIGNER_ADD_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_SET_SECURITY_SOURCE_FIELD:
        case ASM_DESIGNER_ADD_SECURITY_FILTER_FIELD_MAPPING:
        case ASM_DESIGNER_UPDATE_SECURITY_FILTER_FIELD_MAPPING:
            return {
                ...state,
                [FIELDS]: asmDesignerDataSecuritySourceFieldsReducer(state[FIELDS], action)
            };
            
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER:
            if(!action.securityFilter || !("id" in action.securityFilter)) {
                return state;
            }
            const newSecurityFilter = {[SECURITY_FILTER_ID]: action.securityFilter.id};

            securityFilters = state[SECURITY_FILTERS].slice();
            securityFilters.push(newSecurityFilter);
            return {
                ...state,
                [SECURITY_FILTERS]: securityFilters
            };

        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
            if(action.securityFilterIndex >= state[SECURITY_FILTERS].length) {
                return state;
            }

            securityFilters = state[SECURITY_FILTERS].slice();
            securityFilters.splice(action.securityFilterIndex, 1);

            return {
                ...state,
                [FIELDS]: asmDesignerDataSecuritySourceFieldsReducer(state[FIELDS], action),
                [SECURITY_FILTERS]: securityFilters
            };

        default:
            return state;
    }
};