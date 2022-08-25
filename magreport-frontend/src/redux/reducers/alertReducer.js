import { SHOWALERT, HIDEALERT } from '../reduxTypes'

const initialState = {
    data: {
        open: false,
        title: '',
        text: "",
        buttons: [],
        callback: null,
    }
}

export const alertReducer = (state = initialState, action) => {
    switch (action.type){
        case SHOWALERT:
            return { 
                data: {
                    open: true,
                    title: action.title,
                    text: action.text,
                    buttons: action.buttons,
                    callback: action.callback,
                }
            }
        case HIDEALERT:
                return {
                    data: {...state.data, open:false, callback:null}
                }
        default:
            return state
    }
}