import { REPORTTREEFOLDERSLOADED, REPORTTREEFOLDERSLOADFAIL, REPORTTREEFOLDERSLOADSTART } from '../../reduxTypes'

const initialState = {
    status: "init",
    folders: null
}

export const reportTreeFoldersReducer = (state = initialState, action) => {
    switch (action.type){
        case REPORTTREEFOLDERSLOADSTART:
            return { 
                status: "startLoading",
                folders: null
            }        
        case REPORTTREEFOLDERSLOADED:
            return { 
                status: "loaded",
                folders: action.folders
            }        
        case REPORTTREEFOLDERSLOADFAIL:
            return { 
                status: "loadFail",
                folders: null
            }
        default:
            return state
    }
}