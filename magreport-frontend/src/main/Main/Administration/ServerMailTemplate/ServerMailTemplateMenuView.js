import React from 'react';
import { connect } from 'react-redux';
import { useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {actionFolderLoaded, actionFolderLoadFailed, actionFolderClick, actionItemClick, actionAddFolder,
    actionAddItemClick, actionEditFolder, actionDeleteFolderClick, actionEditItemClick, actionDeleteItemClick, actionSearchClick, actionSortClick
} from 'redux/actions/menuViews/folderActions';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';

// components
import DataLoader from '../../../DataLoader/DataLoader';
import FolderContent from '../../../FolderContent/FolderContent';
import SidebarItems from '../../Sidebar/SidebarItems';
//import RoleDesigner from './RoleDesigner';


// states
import {FLOW_STATE_BROWSE_FOLDER, mailTemplateMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import ServerMailTemplateView from "./ServerMailTemplateView";
import ServerMailTemplateDesigner from "./ServerMailTemplateDesigner";

function MailTemplatesMenuView(props){

    const navigateBack = useNavigateBack();

    let state = props.state;

    let loadFunc = dataHub.serverMailTemplateController.getMailTemplateType;

    let reload = {needReload : state.needReload};
    let folderItemsType = SidebarItems.admin.subItems.mailTexts.folderItemType;
    let sidebarItemType = SidebarItems.admin.subItems.mailTexts.key;
    let isSortingAvailable = true;

    function handleExit() {
            navigateBack();
    }

    return(
        <div  style={{display: 'flex', flex: 1}}>
            {
                state.flowState === FLOW_STATE_BROWSE_FOLDER ?
                    <DataLoader
                        loadFunc = {loadFunc}
                        loadParams = {[state.currentFolderId]}
                        reload = {reload}
                        onDataLoaded = {(data) => {props.actionFolderLoaded(folderItemsType, data, isSortingAvailable)}}
                        onDataLoadFailed = {(message) => {props.actionFolderLoadFailed(folderItemsType, message)}}
                    >
                        <FolderContent
                            itemsType = {folderItemsType}
                            showAddFolder = {false}
                            showAddItem = {false}
                            searchParams = {state.searchParams || {}}
                            sortParams = {state.sortParams || {}}
                            data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                            onFolderClick = {(folderId) => {props.actionFolderClick(folderItemsType, folderId)}}
                            onItemClick = {(templateId) => {props.actionItemClick(folderItemsType, templateId)}}
                            onAddFolder = {(name, description) => {props.actionAddFolder(folderItemsType, state.currentFolderData.id, name, description)}}
                            onAddItemClick = {() => {props.actionAddItemClick(folderItemsType)}}
                            onEditItemClick = {(templateId) => {props.actionEditItemClick(folderItemsType, templateId)}}
                            onDeleteFolderClick = {(folderId) => {props.actionDeleteFolderClick(folderItemsType, state.currentFolderData.id, folderId)}}
                            onDeleteItemClick = {(roleId) => {props.actionDeleteItemClick(folderItemsType, state.currentFolderId, roleId)}}
                            onEditFolder = {(folderId, name, description) => {props.actionEditFolder(sidebarItemType, folderItemsType, state.currentFolderData.id, folderId, name, description)}}
                            contextAllowed
                            onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, state.currentFolderId, searchParams)}}
                            onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                        />
                    </DataLoader>
                    : state.flowState === mailTemplateMenuViewFlowStates.mailTemplateViewer ?
                    <ServerMailTemplateView
                        serverMailTemplateId = {state.viewMailTemplateId}
                        onOkClick = {handleExit}
                        onEditClick={actionItemClick}
                    />
                    : state.flowState === mailTemplateMenuViewFlowStates.mailTemplateDesigner ?
                        <ServerMailTemplateDesigner
                            serverMailTemplateId = {state.viewMailTemplateId}
                            onOkClick = {handleExit}
                            onExitClick={handleExit}
                            onEditClick={actionItemClick}
                        />
                        : <div>Неизвестное состояние</div>
            }
        </div>
    )
}

const mapStateToProps = state => {
    return {
        state : state.mailTemplateMenuView
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionEditItemClick,
    actionDeleteItemClick,
    actionAddFolder,
    actionAddItemClick,
    actionEditFolder,
    actionDeleteFolderClick,
    actionSearchClick,
    actionSortClick,
    actionSetSidebarItem
}

export default connect(mapStateToProps, mapDispatchToProps)(MailTemplatesMenuView);