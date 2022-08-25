import {
    ASM_DATA_LOADED, ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER,
    ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER
} from "redux/reduxTypes";

export const asmDesignerSecurityFiltersReducer = (state, action) => {
    let securityFilters;
    switch (action.type) {
        case ASM_DATA_LOADED:
            const newState = {};
            action.data.sources.forEach((source, sourceIndex) => {
                securityFilters = [];
                source.securityFilters.forEach(asmSecurityFilter => {
                   securityFilters.push(asmSecurityFilter.securityFilter);
                });

                newState[sourceIndex] = securityFilters;
            });

            return newState;
        case ASM_DESIGNER_ADD_SECURITY_SOURCE_SECURITY_FILTER:
            if(!(action.sourceIndex in state)) {
                securityFilters = [action.securityFilter];
            } else {
                securityFilters = [...state[action.sourceIndex], action.securityFilter];
            }

            return {
                ...state,
                [action.sourceIndex]: securityFilters
            };

        case ASM_DESIGNER_DELETE_SECURITY_SOURCE_SECURITY_FILTER:
            if(!(action.sourceIndex in state)) {
                return state;
            }

            securityFilters = state[action.sourceIndex].slice();
            if(action.securityFilterIndex >= securityFilters.length) {
                return state;
            }

            securityFilters.splice(action.securityFilterIndex, 1);

            return {
                ...state,
                [action.sourceIndex]: securityFilters
            };

        default:
            return state;
    }
};