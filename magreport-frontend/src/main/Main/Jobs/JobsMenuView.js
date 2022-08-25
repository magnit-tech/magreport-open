import React, {useState} from 'react';
import { connect } from 'react-redux';

// dataHub
import dataHub from 'ajax/DataHub';

// redux
import {FLOW_STATE_BROWSE_FOLDER, jobsMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import {actionFolderLoaded, actionFolderLoadFailed, actionItemClick} from 'redux/actions/menuViews/folderActions';
import {actionFilterJobs, actionJobCancel, showSqlDialog} from 'redux/actions/jobs/actionJobs';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';
import {startReport} from 'redux/actions/menuViews/reportActions';

// components
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';

import SidebarItems from '../Sidebar/SidebarItems';
import ReportJob from 'main/Report/ReportJob';
import ReportStarter from 'main/Report/ReportStarter';

function JobsMenuView(props){
    let state = props.state;
    const [reload, setReload] = useState({needReload : false})

    let folderItemsType = SidebarItems.jobs.folderItemType;

    function handleReportCancel(){
        props.actionSetSidebarItem(SidebarItems.jobs);
    }

    function handleRestartReportClick(reportId, jobId){
        props.startReport(reportId, jobId, SidebarItems.jobs.key);
    }

    function handleRefreshFolder(){
        setReload({needReload : true})
    }

    return(
        <div  style={{display: 'flex', flex: 1}}>
        {
            state.flowState === FLOW_STATE_BROWSE_FOLDER ?
            <DataLoader
                loadFunc = {dataHub.reportJobController.getMyJobs}
                loadParams = {[]}
                reload = {reload}
                onDataLoaded = {(data) => {props.actionFolderLoaded(folderItemsType, data)}}
                onDataLoadFailed = {(message) => {props.actionFolderLoadFailed(folderItemsType, message)}}
            >
                <FolderContent
                    itemsType = {folderItemsType}
                    data = {props.filteredJobs ? props.filteredJobs : props.currentFolderData}
                    filters = {props.filters}
                    showAddFolder = {false}
                    showAddItem = {false}
                    showItemControls = {false}
                    pagination = {true}
                    onItemClick = {jobId => {props.actionItemClick(folderItemsType, jobId)}}
                    onReportRunClick = {(reportId, jobId) => {props.startReport(reportId, jobId, SidebarItems.jobs.key, SidebarItems.jobs.folderItemType)}}
                    onFilterClick = {filters => {props.actionFilterJobs(folderItemsType, filters)}}
                    onJobCancelClick = {(jobIndex, jobId) => {props.actionJobCancel(folderItemsType, jobIndex, jobId)}}
                    onRefreshClick = {handleRefreshFolder}
                    showDialog = {props.showSqlDialog}
                />

            </DataLoader>

            : state.flowState === jobsMenuViewFlowStates.reportJob ?
            <div style={{display: 'flex', flex: 1, flexDirection: 'column', overflow: 'auto'}}>
                <ReportJob
                    jobId = {state.jobId}
                    excelTemplates={state.excelTemplates}
                    onRestartReportClick = {handleRestartReportClick}
                />
            </div>
            : state.flowState === jobsMenuViewFlowStates.startReport ?
                <ReportStarter
                    reportId = {state.reportId}
                    jobId = {state.jobId}
                    onCancel = {handleReportCancel}
                    onDataLoadFunction={dataHub.reportController.get}
                />

            : <div>Неизвестное состояние</div>
        }
        </div>
    )
}

const mapStateToProps = state => {
    return {
        state : state.jobsMenuView,
        currentFolderData : state.jobsMenuView.currentFolderData,
        filteredJobs : state.jobsMenuView.filteredJobs,
        filters : state.jobsMenuView.filters
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionItemClick,
    actionFilterJobs,
    actionSetSidebarItem,
    actionJobCancel,
    startReport,
    showSqlDialog
}

export default connect(mapStateToProps, mapDispatchToProps)(JobsMenuView);
