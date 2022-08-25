import React, {useState } from 'react';
import { connect } from 'react-redux';

// dataHub
import dataHub from 'ajax/DataHub';

// redux
import {FLOW_STATE_BROWSE_FOLDER, usersJobsMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import {actionFolderLoaded, actionFolderLoadFailed, actionItemClick} from 'redux/actions/menuViews/folderActions';
import {actionFilterJobs, actionJobCancel, showSqlDialog} from 'redux/actions/jobs/actionJobs';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';
import {startReport} from 'redux/actions/menuViews/reportActions';

// components
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';

import SidebarItems from '../../Sidebar/SidebarItems';
import ReportJob from 'main/Report/ReportJob';
import ReportStarter from 'main/Report/ReportStarter';

function UsersJobsMenuView(props){

    let state = props.state;
    const [reload, setReload] = useState({needReload : false})

    let folderItemsType = SidebarItems.admin.subItems.userJobs.folderItemType;

    function handleRefreshFolder(){
        setReload({needReload : true})
    }

    function handleReportCancel(){
        props.actionSetSidebarItem(SidebarItems.admin.subItems.userJobs);
    }

    function handleRestartReportClick(reportId, jobId){
        props.startReport(reportId, jobId, SidebarItems.admin.subItems.userJobs.key);
    }

    return (
        <div  style={{display: 'flex', flex: 1}}>
        {
            state.flowState === FLOW_STATE_BROWSE_FOLDER ?
            <DataLoader
                loadFunc = {dataHub.reportJobController.getAllUsersJobs}
                loadParams = {[]}
                reload = {reload}
                onDataLoaded = {data => {props.actionFolderLoaded(folderItemsType, data)}}
                onDataLoadFailed = {message => {props.actionFolderLoadFailed(folderItemsType, message)}}
            >
                <FolderContent
                    itemsType = {folderItemsType}
                    data = {props.filteredJobs ? props.filteredJobs : props.currentFolderData}
                    filters = {props.filters}
                    showAddFolder = {false}
                    showAddItem = {false}
                    showItemControls = {false}
                    pagination = {true}
                    dialog = {props.dialog}
                    onItemClick = {jobId => {props.actionItemClick(folderItemsType, jobId)}}
                    onReportRunClick = { (reportId, jobId) => {props.startReport(reportId, jobId, SidebarItems.admin.subItems.userJobs.key, SidebarItems.admin.subItems.userJobs.folderItemType)}}
                    onFilterClick = {filters => {props.actionFilterJobs(folderItemsType, filters)}}
                    onRefreshClick = {handleRefreshFolder}
                    onJobCancelClick = {(jobIndex, jobId) => {props.actionJobCancel(folderItemsType, jobIndex, jobId)}}
                    showDialog = {props.showSqlDialog}
                />

            </DataLoader>

            : state.flowState === usersJobsMenuViewFlowStates.reportJob ?
            <div style={{display: 'flex', flex: 1, flexDirection: 'column', overflow: 'auto'}}>
                <ReportJob
                    jobId = {state.jobId}
                    excelTemplates={state.excelTemplates}
                    onRestartReportClick = {handleRestartReportClick}
                />
            </div>
            : state.flowState === usersJobsMenuViewFlowStates.startReport ?
            <ReportStarter
                reportId = {state.reportId}
                jobId = {state.jobId}
                scheduleTaskId={undefined}
                onCancel = {handleReportCancel}
                onDataLoadFunction={dataHub.reportController.get}
            />

            : <p>Неизвестное состояние</p>
        }
        </div>
    )
}

const mapStateToProps = state => {
    return {
        state : state.usersJobsMenuView,
        currentFolderData : state.usersJobsMenuView.currentFolderData,
        filteredJobs : state.usersJobsMenuView.filteredJobs,
        filters : state.usersJobsMenuView.filters
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionItemClick,
    startReport,
    actionFilterJobs,
    actionJobCancel,
    actionSetSidebarItem,
    showSqlDialog
}

export default connect(mapStateToProps, mapDispatchToProps)(UsersJobsMenuView);
