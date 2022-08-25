import { SHOWALERTDIALOG, HIDEALERTDIALOG } from '../reduxTypes'

export const showAlertDialog = (title, entityType, entity, callback) =>{
    return {
        type: SHOWALERTDIALOG, 
        title,
        entityType,
        entity,
        callback,
    }
}

export const hideAlertDialog = () =>{
    return {type: HIDEALERTDIALOG}
}