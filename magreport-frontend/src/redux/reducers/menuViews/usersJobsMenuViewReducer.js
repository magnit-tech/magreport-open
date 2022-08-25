import dataHub from 'ajax/DataHub';

import {FLOW_STATE_BROWSE_FOLDER, usersJobsMenuViewFlowStates} from './flowStates';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';

import {REPORT_START, FOLDER_CONTENT_ITEM_CLICK, JOBS_FILTER, JOB_CANCEL, JOB_CANCELED, JOB_CANCEL_FAILED} from 'redux/reduxTypes';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function usersJobsMenuViewReducer(state = initialState, action){
    switch(action.type){
        case REPORT_START:
            if(action.sidebarItemKey === SidebarItems.admin.subItems.userJobs.key){
                return{
                    ...state,
                    flowState : usersJobsMenuViewFlowStates.startReport,
                    reportId : action.reportId,
                    jobId : action.jobId
                }
            }
            else{
                return state;
            }

        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.userJobs){
                const reportInfo = dataHub.localCache.getJobInfo(action.itemId)
                const reportName = reportInfo.report.name
                const reportId = reportInfo.report.id
                const excelTemplates = reportInfo.excelTemplates

                return{
                    ...state,
                    flowState : usersJobsMenuViewFlowStates.reportJob,
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
            if(action.itemsType === FolderItemTypes.userJobs){
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
                        let flagUsername = true
                        if (action.filters.periodStart && new Date(item.created) < action.filters.periodStart){
                            flagStart = false
                        }
                        if (action.filters.periodEnd && new Date(item.created) > action.filters.periodEnd){
                            flagEnd = false
                        }
                        if (action.filters.selectedStatuses && !action.filters.selectedStatuses.includes(item.status)){
                            flagStatus = false
                        }
                        if (action.filters.user && item.user.name.trim().toLowerCase() !== action.filters.user.trim().toLowerCase()){
                            flagUsername = false
                        }
                        return flagStart && flagEnd && flagStatus && flagUsername
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
            //debugger
            if(action.itemsType === FolderItemTypes.userJobs){
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
            if(action.itemsType === FolderItemTypes.userJobs){
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
            if(action.itemsType === FolderItemTypes.userJobs){
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
            return folderStateReducer(state, action, SidebarItems.admin.subItems.userJobs, FolderItemTypes.userJobs);
    }
}
