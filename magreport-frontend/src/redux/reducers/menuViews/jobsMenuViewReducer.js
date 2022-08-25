import dataHub from 'ajax/DataHub';

import {FLOW_STATE_BROWSE_FOLDER, jobsMenuViewFlowStates} from './flowStates';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';

import {REPORT_START, FOLDER_CONTENT_ITEM_CLICK, JOBS_FILTER, JOB_CANCEL, JOB_CANCELED, JOB_CANCEL_FAILED} from 'redux/reduxTypes';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function jobsMenuViewReducer(state = initialState, action){
    switch(action.type){
        case REPORT_START:
            if(action.sidebarItemKey === SidebarItems.jobs.key){
                return{
                    ...state,
                    flowState : jobsMenuViewFlowStates.startReport,
                    reportId : action.reportId,
                    jobId : action.jobId
                }
            }
            else{
                return state;
            }

        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.job){
                const reportInfo = dataHub.localCache.getJobInfo(action.itemId)
                const reportName = reportInfo.report.name
                const reportId = reportInfo.report.id
                const excelTemplates = reportInfo.excelTemplates

                return{
                    ...state,
                    flowState : jobsMenuViewFlowStates.reportJob,
                    jobId : action.itemId,
                    reportName,
                    reportId,
                    excelTemplates,
                }
            }
            else{
                return state;
            }
        
        case JOBS_FILTER:
            if(action.itemsType === FolderItemTypes.job){
                let newState = {}
                if (action.filters.isCleared){
                    newState = {...state}
                    delete newState.filteredJobs
                    delete newState.filters
                }
                else {
                    const tmp = [...state.currentFolderData.jobs]
                    let arr = tmp.filter(item => {
                        let flagStart = true
                        let flagEnd = true
                        let flagStatus = true
                        if (action.filters.periodStart && new Date(item.created) < action.filters.periodStart){
                            flagStart = false
                        }
                        if (action.filters.periodEnd && new Date(item.created) < action.filters.periodStart){
                            flagEnd = false
                        }
                        if (action.filters.selectedStatuses && !action.filters.selectedStatuses.includes(item.status)){
                            flagStatus = false
                        }
                        return flagStart && flagEnd && flagStatus
                    })

                    newState = {
                        ...state, filteredJobs: {jobs: arr}, filters: action.filters
                    }
                }
                

                return newState
            }
            else{
                return state;
            }
        
        case JOB_CANCEL:
            if(action.itemsType === FolderItemTypes.job){
                let newState = {...state}
                const fullArr = [...state.currentFolderData.jobs]
                let newJob = {...fullArr[action.jobIndex], waitCancelResponse: true}
                if (state.filters){
                    const filteredArr = [...state.filteredJobs.jobs]
                    newJob = {...filteredArr[action.jobIndex], waitCancelResponse: true}                    
                    let indexFullArr = fullArr.findIndex(item => item.id === action.jobId)
                    filteredArr.splice(action.jobIndex, 1, newJob)
                    fullArr.splice(indexFullArr, 1, newJob)
                    newState.filteredJobs.jobs = filteredArr
                }
                else {
                    fullArr.splice(action.jobIndex, 1, newJob)
                }
                newState.currentFolderData.jobs = fullArr
                return newState
            }
            else {
                return state;
            }
        
        case JOB_CANCELED:
            if(action.itemsType === FolderItemTypes.job){
                let newState = {...state}
                const fullArr = [...state.currentFolderData.jobs]
                let newJob = {...fullArr[action.jobIndex], status: 'CANCELED'}
                delete newJob.waitCancelResponse
                if (state.filters){
                    const filteredArr = [...state.filteredJobs.jobs]
                    newJob = {...filteredArr[action.jobIndex], status: 'CANCELED'}
                    delete newJob.waitCancelResponse
                    let indexFullArr = fullArr.findIndex(item => item.id === action.jobId)
                    filteredArr.splice(action.jobIndex, 1, newJob)
                    fullArr.splice(indexFullArr, 1, newJob)
                    newState.filteredJobs.jobs = filteredArr
                }
                else {
                    fullArr.splice(action.jobIndex, 1, newJob)
                }
                newState.currentFolderData.jobs = fullArr
                return newState
            }
            else {
                return state;
            }

        case JOB_CANCEL_FAILED:
            if(action.itemsType === FolderItemTypes.job){
                let newState = {...state}
                const fullArr = [...state.currentFolderData.jobs]
                let newJob = {...fullArr[action.jobIndex], status: 'RUNNING'}
                delete newJob.waitCancelResponse
                if (state.filters){
                    const filteredArr = [...state.filteredJobs.jobs]
                    newJob = {...filteredArr[action.jobIndex], status: 'RUNNING'}  
                    delete newJob.waitCancelResponse                 
                    let indexFullArr = fullArr.findIndex(item => item.id === action.jobId)
                    filteredArr.splice(action.jobIndex, 1, newJob)
                    fullArr.splice(indexFullArr, 1, newJob)
                    newState.filteredJobs.jobs = filteredArr
                }
                else {
                    fullArr.splice(action.jobIndex, 1, newJob)
                }
                newState.currentFolderData.jobs = fullArr
                return newState
            }
            else {
                return state;
            }

        default:
            return folderStateReducer(state, action, SidebarItems.jobs, FolderItemTypes.job);
    }
}
