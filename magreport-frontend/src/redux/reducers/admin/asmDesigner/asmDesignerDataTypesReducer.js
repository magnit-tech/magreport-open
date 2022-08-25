import {ASM_DESIGNER_DATA_TYPES_LOADED} from "../../../reduxTypes";

export const asmDesignerDataTypesReducer = (state, action) => {
    switch (action.type) {
        case ASM_DESIGNER_DATA_TYPES_LOADED:
            const newState = {...state};
            action.data.forEach(item => {
                newState[item.id] = item;
            });
            return newState;

        default:
            return state;
    }
}