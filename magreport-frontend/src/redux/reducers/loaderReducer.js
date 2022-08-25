import { SHOWLOADER, HIDELOADER } from '../reduxTypes'

const initialState = {
    loader: false
}

export const loaderReducer = (state = initialState, action) => {
    switch (action.type){
        case SHOWLOADER:
            return {loader: true}
        case HIDELOADER:
                return {loader: false}
        default:
            return state
    }
}