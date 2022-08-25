import { SHOWSNACKBAR, HIDESNACKBAR } from '../reduxTypes'

export const showSnackbar = (text, color) =>{
    return {
        type: SHOWSNACKBAR, 
        text,
        color,
    }
}

export const hideSnackbar = (text, color) =>{
    return {
        type: HIDESNACKBAR,
        text,
        color,
    }
}