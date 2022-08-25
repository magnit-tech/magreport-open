import { MAINFLOWSTATE } from '../reduxTypes'

const initialState = {
    mainState: MAINFLOWSTATE.init
}

export const mainReducer = (state = initialState, action) => {
    if (Object.values(MAINFLOWSTATE).includes(action.type)){
        return {mainState: action.type};
    }
    else {
        return state;
    }
}