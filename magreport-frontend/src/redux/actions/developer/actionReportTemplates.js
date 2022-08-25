import store from 'redux/store';
import dataHub from 'ajax/DataHub';

import {
    REPORT_TEMPLATES_LOADED,
    REPORT_TEMPLATES_LOAD_FAILED,
    REPORT_TEMPLATES_ADD_STARTED,
    REPORT_TEMPLATES_ADDED,
    REPORT_TEMPLATES_ADD_FAILED,
    REPORT_TEMPLATES_DELETE_STARTED,
    REPORT_TEMPLATES_DELETED,
    REPORT_TEMPLATES_DELETE_FAILED,
    REPORT_TEMPLATES_SET_DEFAULT_STARTED,
    REPORT_TEMPLATES_SET_DEFAULT_SUCCESS,
    REPORT_TEMPLATES_SET_DEFAULT_FAILED,
} from '../../reduxTypes'

export const actionLoaded = (reportId, data) => {
    
    return {
        type: REPORT_TEMPLATES_LOADED,
        reportId,
        data
    }
}

export const actionLoadedFailed = (reportId, data) => {

    return {
        type: REPORT_TEMPLATES_LOAD_FAILED,
        reportId,
        data
    }
}

export const actionAdd = (name, description, file, reportId) => {

    const data = new FormData() 
    data.append('params', JSON.stringify({
        folderId: 1,
        name,
        description,
    }))
    data.append('file', file)

    dataHub.excelTemplateController.add(data, resp => handleAdd(resp, reportId))

    return {
        type: REPORT_TEMPLATES_ADD_STARTED,
    }
}

function handleAdd(resp, reportId) {
    if (resp.ok) {
        dataHub.excelTemplateController.addToReport(resp.data.excelTemplateId, reportId, m => handleAddToReport(m, resp.data))
    }
    else {
        store.dispatch({
            type: REPORT_TEMPLATES_ADD_FAILED,
            data: resp.data,
        })
    }
}

function handleAddToReport(resp, data) {
    store.dispatch({
        type: resp.ok ? REPORT_TEMPLATES_ADDED : REPORT_TEMPLATES_ADD_FAILED,
        data: resp.ok ? data : resp.data,
    })
}

export const actionDelete = id => {
    dataHub.excelTemplateController.delete(id, resp => handleDelete(resp, id))

    return {
        type: REPORT_TEMPLATES_DELETE_STARTED,
    }
}

function handleDelete(resp, id) {
    store.dispatch({
        type: resp.ok ? REPORT_TEMPLATES_DELETED : REPORT_TEMPLATES_DELETE_FAILED,
        data: resp.data,
        id,
    })
}

export const actionSetDefault = (excelTemplateId, reportId) => {
    dataHub.excelTemplateController.setDefault(excelTemplateId, reportId, resp => handleSetDefault(resp, excelTemplateId))

    return {
        type: REPORT_TEMPLATES_SET_DEFAULT_STARTED,
    }
}

function handleSetDefault(resp, id) {
    store.dispatch({
        type: resp.ok ? REPORT_TEMPLATES_SET_DEFAULT_SUCCESS : REPORT_TEMPLATES_SET_DEFAULT_FAILED,
        data: resp.data,
        id,
    })
}