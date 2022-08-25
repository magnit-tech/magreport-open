import { SHOWALERTDIALOG, HIDEALERTDIALOG } from '../reduxTypes'

const initialState = {
    data: {
        open: false,
        title: '',
        entityType: '',
        entity: {},
        callback: null,
    }
}

export const alertDialogReducer = (state = initialState, action) => {
    switch (action.type){
        case SHOWALERTDIALOG:
            return {
                data: {
                    open: true,
                    title: action.title,
                    entity: action.entity,                    
                    entityType: action.entityType,
                    callback: action.callback,
                }
            }
        case HIDEALERTDIALOG:
                return {
                    data:{...state.data, open:false}
                }
        default:
            return state
    }
}