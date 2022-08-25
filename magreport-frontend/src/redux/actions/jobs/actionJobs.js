import store from 'redux/store';
import dataHub from 'ajax/DataHub';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

import { JOBS_FILTER, JOB_CANCEL, JOB_CANCELED, JOB_CANCEL_FAILED, JOB_SQL_CLICK, JOB_SQL_CLOSE , JOB_SQL_LOADED, JOB_SQL_LOAD_FAILED } from '../../reduxTypes'

export function actionFilterJobs(itemsType, filters){
    return {
        type: JOBS_FILTER,
        itemsType,
        filters
    }
}

export function actionJobCancel(itemsType, jobIndex, jobId){

    dataHub.reportJobController.jobCancel(jobId, m => handleCancelResponse(itemsType, jobIndex, jobId, m))
    return {
        type: JOB_CANCEL,
        itemsType,
        jobIndex,
        jobId
    }
}

function handleCancelResponse(itemsType, jobIndex, jobId, m){
    let type = m.ok ? JOB_CANCELED : JOB_CANCEL_FAILED
    
    store.dispatch({
        type,
        itemsType,
        jobIndex,
        jobId
    })
}

function handleClickSql(magrepResponse){
    let type = magrepResponse.ok ? JOB_SQL_LOADED : JOB_SQL_LOAD_FAILED;
    let data = magrepResponse.data;

    store.dispatch({
        open: true,
        type: type,
        data : data,
    });
}

export const showSqlDialog = (itemsType, titleName, id) =>{
    if (itemsType === FolderItemTypes.job || itemsType === FolderItemTypes.userJobs) {
        dataHub.reportJobController.getSqlQuery(id, handleClickSql)
    }
    return {
        type: JOB_SQL_CLICK,
        itemsType,
        titleName,
        id
    }
}

export const hideSqlDialog = (itemsType) =>{
    return {type: JOB_SQL_CLOSE, open: false, itemsType, data: {}}
}

export const actionJobSqlLoadFailed = error =>{
    return {
        type: JOB_SQL_LOAD_FAILED,
        error
    }
}