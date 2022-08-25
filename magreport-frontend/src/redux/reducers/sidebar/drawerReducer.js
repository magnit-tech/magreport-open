import { DRAWERTOOGLE } from '../../reduxTypes'

const initialState = {
    open: true
}

export const drawerReducer = (state = initialState, action) => {
    switch (action.type){
        case DRAWERTOOGLE:
            return { 
                open: !state.open
            }
        default:
            return state
    }
}