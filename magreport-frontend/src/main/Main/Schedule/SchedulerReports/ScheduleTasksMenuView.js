import React from 'react';
import { connect } from 'react-redux';
import {useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

import {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionEditItemClick,
    actionDeleteItemClick,
    actionAddItemClick,
    actionSearchClick,
    actionScheduleTaskRunClick,
    actionSortClick
} from 'redux/actions/menuViews/folderActions';

import {actionScheduleTaskSwitch} from 'redux/actions/admin/actionSchedules';

// const
import {FLOW_STATE_BROWSE_FOLDER , scheduleTasksMenuViewFlowStates} from  'redux/reducers/menuViews/flowStates';
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

// local components

import DataLoader from  '../../../DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';
import ScheduleTasksDesigner from './ScheduleTasksDesigner';
import ScheduleTasksViewer from './ScheduleTasksViewer';


function ScheduleTasksMenuView(props){

    const navigateBack = useNavigateBack();

    const state = props.state;

    let designerMode = typeof (state.editScheduleTaskId) === "number" ? "edit" : "create";

    let loadFunc = dataHub.scheduleController.taskGetAll;

    let reload = {needReload: state.needReload};
    let folderItemsType = FolderItemTypes.scheduleTasks;
    let isSortingAvailable = true;

    function handleExit() {
        navigateBack();
    }

    let component;
    if (state.flowState === FLOW_STATE_BROWSE_FOLDER) {
        component = (
            <DataLoader
                loadFunc={loadFunc}
                loadParams={[]}
                reload={reload}
                onDataLoaded={(data) => {
                    props.actionFolderLoaded(folderItemsType, {scheduleTasks: data, childFolders: []}, isSortingAvailable)
                }}
                onDataLoadFailed={(message) => {
                    props.actionFolderLoadFailed(folderItemsType, message)
                }}
            >
                <FolderContent
                    itemsType={folderItemsType}
                    showAddFolder={false}
                    showAddItem={true}
                    data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                    searchParams={state.searchParams || {}} 
                    searchWithoutRecursive
                    onItemClick={(scheduleTaskId) => {
                        props.actionItemClick(folderItemsType, scheduleTaskId)
                    }}
                    onAddItemClick={() => {
                        props.actionAddItemClick(folderItemsType)
                    }}
                    onEditItemClick={(scheduleTaskId) => {
                        props.actionEditItemClick(folderItemsType, scheduleTaskId)
                    }}
                    onDeleteItemClick={(scheduleTaskId) => {
                        props.actionDeleteItemClick(folderItemsType, null, scheduleTaskId)
                    }}
                    onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, [], searchParams)}}
                    onScheduleTaskRunClick = {(scheduleTaskId) => {props.actionScheduleTaskRunClick(folderItemsType, scheduleTaskId) }} 
                    onScheduleTaskSwitchClick = {(index, scheduleTaskId) => {props.actionScheduleTaskSwitch(folderItemsType, index, scheduleTaskId)}}
                    contextAllowed
                    sortParams = {state.sortParams || {}}
                    onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                />  
            </DataLoader>
        );
    }
    
    else if (state.flowState === scheduleTasksMenuViewFlowStates.scheduleTasksDesigner) {
        component = <ScheduleTasksDesigner
            status={state.editScheduleTaskId ? state.currentFolderData.scheduleTasks?.find((item)=> item.id === state.editScheduleTaskId ).status : 'CHANGED'}
            mode={designerMode}
            scheduleId={state.editScheduleTaskId}
            onExit={handleExit}
        />;
    } else if (state.flowState === scheduleTasksMenuViewFlowStates.scheduleTasksViewer) {
        component = <ScheduleTasksViewer
            scheduleId={state.viewScheduleTaskId}
            onOkClick={handleExit}
        />;
    } else {
        component = <div>Неизвестное состояние</div>;
    }

    return (
        <div style={{display: 'flex', flex: 1}}>
            {component}
        </div>
    );
}

const mapStateToProps = state => {
    return {
        state : state.scheduleTasksMenuView
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionEditItemClick,
    actionDeleteItemClick,
    actionAddItemClick,
    actionSearchClick,
    actionScheduleTaskRunClick,
    actionSortClick,
    actionScheduleTaskSwitch
};

export default connect(mapStateToProps, mapDispatchToProps)(ScheduleTasksMenuView);