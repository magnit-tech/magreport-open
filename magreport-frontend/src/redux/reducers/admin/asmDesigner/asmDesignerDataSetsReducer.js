import {ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET, ASM_DATA_LOADED} from "redux/reduxTypes";

export const asmDesignerDataSetsReducer = (state, action) => {
    switch (action.type) {
        case ASM_DATA_LOADED:
            return action.data.sources.map(s => s.dataSet || {});

        case ASM_DESIGNER_SET_SECURITY_SOURCE_DATASET:
            let newState = state.slice();
            newState[action.sourceIndex] = action.dataSet;
            return newState;
        default:
            return state;
    }
};