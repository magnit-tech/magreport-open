import store from 'redux/store';
import dataHub from 'ajax/DataHub';

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

export const actionLoadedSettings = data => {
    return {
        type: SETTINGS_LOADED,
        folders: data.folders,
    }
}

export const actionGetJournal = (foldIndex, folderCode, paramIndex, settingId, callback) => {

    dataHub.serverSettings.getJournal(settingId, resp => handleGetJournal(foldIndex, folderCode, paramIndex, settingId, callback, resp))

    return {
        type: SETTINGS_GET_JOURNAL_START,
        foldIndex, 
        folderCode, 
        paramIndex,
        settingId, 
    }
}

function handleGetJournal(foldIndex, folderCode, paramIndex, settingId, callback, resp){
    if (!resp.ok){
        callback(`Ошибка получения журнала: ${resp.data}`, {variant: "error"})
    }
    
    store.dispatch({
        type: resp.ok ? SETTINGS_GET_JOURNAL_SUCCESS : SETTINGS_GET_JOURNAL_FAIL,
        foldIndex, 
        folderCode, 
        paramIndex,
        settingId, 
        data: resp.data,
    })
}

export const actionGetFullJournal = callback => {

    dataHub.serverSettings.getFullJournal(resp => handleGeFulltJournal(callback, resp))

    return {
        type: SETTINGS_GET_FULL_JOURNAL_START,
    }
}

function handleGeFulltJournal(callback, resp){
    if (!resp.ok){
        callback(`Ошибка получения журнала: ${resp.data}`, {variant: "error"})
    }

    store.dispatch({
        type: resp.ok ? SETTINGS_GET_FULL_JOURNAL_SUCCESS : SETTINGS_GET_FULL_JOURNAL_FAIL,
        data: resp.data
    })
}

export const actionSetSetting = (foldIndex, folderCode, paramIndex, settingId, value, callback) => {

    dataHub.serverSettings.set(settingId, value, resp => handleSetSetting(foldIndex, folderCode, paramIndex, settingId, callback, resp))

    return {
        type: SETTINGS_SET_START,
        foldIndex, 
        folderCode, 
        paramIndex,
        settingId,
    }
}

function handleSetSetting(foldIndex, folderCode, paramIndex, settingId, callback, resp){
    if (!resp.ok){
        callback(`При сохранении возникла ошибка: ${resp.data}`, {variant: "error"})
    }

    store.dispatch({
        type: resp.ok ? SETTINGS_SET_SUCCESS : SETTINGS_SET_FAIL,
        foldIndex, 
        folderCode, 
        paramIndex,
        settingId, 
    })
}

export const actionSettingValueChanged = (foldIndex, folderCode, paramIndex, settingId, value) => {
    return {
        type: SETTINGS_VALUE_CHANGED,
        foldIndex,
        folderCode,
        paramIndex,
        settingId, 
        value,
    }
}

export const actionSettingDisableChanged = (foldIndex, folderCode, paramIndex, settingId, value) => {
    return {
        type: SETTINGS_DISABLE_CHANGE,
        foldIndex,
        folderCode,
        paramIndex,
        settingId, 
        value,
    }
}

export const actionCloseHistory = () => {
    return {
        type: SETTINGS_HISTORY_CLOSE,
    }
}