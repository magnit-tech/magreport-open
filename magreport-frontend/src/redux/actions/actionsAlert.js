import { SHOWALERT, HIDEALERT } from '../reduxTypes'

export const showAlert = (title, text, buttons, callback) =>{
    return {
        type: SHOWALERT, 
        title,
        text,
        buttons,
        callback,
    }
}

export const hideAlert = () =>{
    return {type: HIDEALERT}
}