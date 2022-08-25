import {
    SETTINGS_LOADED, 
    SETTINGS_GET_JOURNAL_START,
    SETTINGS_GET_JOURNAL_SUCCESS,
    SETTINGS_GET_JOURNAL_FAIL,
    SETTINGS_GET_FULL_JOURNAL_START,
    SETTINGS_GET_FULL_JOURNAL_SUCCESS,
    SETTINGS_GET_FULL_JOURNAL_FAIL,
    SETTINGS_SET_START,
    SETTINGS_SET_SUCCESS,
    SETTINGS_SET_FAIL,
    SETTINGS_VALUE_CHANGED,
    SETTINGS_DISABLE_CHANGE,
    SETTINGS_HISTORY_CLOSE,
} from 'redux/reduxTypes'

function getParam(folders, folderIndex, folderCode, paramIndex, paramId){
    
    // находим раздел в настройках
    // debugger
    let folder = folders[folderIndex]
    if (folder.code !== folderCode){
        folderIndex = folders.findIndex(f => f.id === folderCode)
        folder = folders[folderIndex]
    }

    // находим саму настройку
    let param = folder.parameters[paramIndex]
    if (param.id !== paramId){
        paramIndex = folder.parameters.findIndex(p => p.id === paramId)
        param = folder.parameters[paramIndex]
    }

    return [
        folderIndex,
        folder,
        paramIndex,
        param,
    ]
}

function editParam(folders, folderIndex, folderCode, paramIndex, paramId, key, value){

    let fi
    let pi
    let folder
    let param

    [fi, folder, pi, param] = getParam(folders, folderIndex, folderCode, paramIndex, paramId)
    
    param[key] = value
    folder.parameters.splice(pi, 1, {...param})
    folders.splice(fi, 1, {...folder})

    return folders
}


const initialState = {
    folders: null,
    history: null,
    getFullJournal:false,
}

export const serverSettingsReducer = (state = initialState, action) => {

    let folders

    switch (action.type){
        case SETTINGS_LOADED:
            return {...state, folders: action.folders}
        
        case SETTINGS_GET_JOURNAL_START:
            return {...state, folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "getJournal", true)}
        
        case SETTINGS_GET_JOURNAL_SUCCESS:
            return {
                ...state, 
                folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "getJournal", false),
                history: action.data
            }
        
        case SETTINGS_GET_JOURNAL_FAIL:
            return {...state, folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "getJournal", false)}
        
        case SETTINGS_GET_FULL_JOURNAL_START:
            return {...state, getFullJournal: true}
        
        case SETTINGS_GET_FULL_JOURNAL_SUCCESS:
            return {...state, history: action.data, getFullJournal: false}
        
        case SETTINGS_GET_FULL_JOURNAL_FAIL:
            return {...state, getFullJournal: false}

        case SETTINGS_SET_START:
            return {...state, folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "saving", true)}

        case SETTINGS_SET_SUCCESS:
            folders = [...state.folders]
            let [folderIndex, folder, paramIndex, param] = getParam(folders, action.foldIndex, action.folderCode, action.paramIndex, action.settingId)
            param.saving = false
            param.value = param.tempValue
            param.disabled = false
            delete param.tempValue
            folder.parameters.splice(paramIndex, 1, {...param})
            folders.splice(folderIndex, 1, {...folder})
            return {...state, folders}
        
        case SETTINGS_SET_FAIL:
            return {...state, folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "saving", false)}
        
        case SETTINGS_VALUE_CHANGED:
            return {...state, folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "tempValue", action.value)}

        case SETTINGS_DISABLE_CHANGE:
            if (action.value)
                return {...state, folders: editParam([...state.folders], action.foldIndex, action.folderCode, action.paramIndex, action.settingId, "disabled", action.value)}
            else {
                folders = [...state.folders]
                let [folderIndex, folder, paramIndex, param] = getParam(folders, action.foldIndex, action.folderCode, action.paramIndex, action.settingId)
                param.disabled = false
                delete param.tempValue
                folder.parameters.splice(paramIndex, 1, {...param})
                folders.splice(folderIndex, 1, {...folder})
                return {...state, folders}
            }
        
        case SETTINGS_HISTORY_CLOSE:
            return {...state, history: null}
        
        default:
            return state
    }
}