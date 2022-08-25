import React from 'react';
import { connect } from 'react-redux';
import { useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {actionFolderLoaded, actionFolderLoadFailed, actionFolderClick, actionItemClick, actionAddFolder, 
    actionAddItemClick, actionEditFolder, actionDeleteFolderClick, actionEditItemClick, actionDeleteItemClick, 
    actionSearchClick, actionSortClick, actionBackEditRoleFromUserClick
} from 'redux/actions/menuViews/folderActions';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';

// components
import DataLoader from '../../../DataLoader/DataLoader';
import FolderContent from '../../../FolderContent/FolderContent';
import SidebarItems from '../../Sidebar/SidebarItems'
import RoleDesigner from './RoleDesigner'
import RoleViewer from "./RoleViewer";

// states 
import {FLOW_STATE_BROWSE_FOLDER, rolesMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';

function RolesMenuView(props){

    const navigateBack = useNavigateBack();

    let state = props.state;
    let designerMode = props.state.editRoleId === null ? 'create' : 'edit';
    
    let loadFunc = dataHub.roleController.getType;

    let reload = {needReload : state.needReload};
    let folderItemsType = SidebarItems.admin.subItems.roles.folderItemType;
    let sidebarItemType = SidebarItems.admin.subItems.roles.key;
    let isSortingAvailable = true;

    function handleExit() {
        if (state.fromUsers) {
            //props.actionSetSidebarItem(SidebarItems.admin.subItems.users);
            props.actionBackEditRoleFromUserClick(folderItemsType);
        //    navigateBack();
            
        }
        else {
            navigateBack();
        }
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
                            showAddItem = {true}
                            searchParams = {state.searchParams || {}}
                            sortParams = {state.sortParams || {}}
                            data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                            onFolderClick = {(folderId) => {props.actionFolderClick(folderItemsType, folderId)}}
                            onItemClick = {(roleId) => {props.actionItemClick(folderItemsType, roleId)}}
                            onAddFolder = {(name, description) => {props.actionAddFolder(folderItemsType, state.currentFolderData.id, name, description)}}
                            onAddItemClick = {() => {props.actionAddItemClick(folderItemsType)}}
                            onEditItemClick = {(roleId) => {props.actionEditItemClick(folderItemsType, roleId)}}
                            onDeleteFolderClick = {(folderId) => {props.actionDeleteFolderClick(folderItemsType, state.currentFolderData.id, folderId)}}
                            onDeleteItemClick = {(roleId) => {props.actionDeleteItemClick(folderItemsType, state.currentFolderId, roleId)}}
                            onEditFolder = {(folderId, name, description) => {props.actionEditFolder(sidebarItemType, folderItemsType, state.currentFolderData.id, folderId, name, description)}}
                            onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, state.currentFolderId, searchParams)}}
                            onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                            contextAllowed
                        />
                    </DataLoader>
                : state.flowState === rolesMenuViewFlowStates.roleViewer ?
                    <RoleViewer
                        roleId = {state.viewRoleId}
                        roleTypeId = {state.currentFolderId}
                        onOkClick = {handleExit}
                    />
                : state.flowState === rolesMenuViewFlowStates.roleDesigner ?
                    <RoleDesigner
                        mode = {designerMode}
                        roleId = {state.editRoleId}
                        roleTypeId = {state.currentFolderId}
                        onExit = {handleExit}
                        sortParams = {state.sortParams || {}}
                        folderName = {state.currentFolderData.name}
                    />

                : <div>Неизвестное состояние</div>
            }
        </div>
    )
}

const mapStateToProps = state => { 
    return {
        state : state.rolesMenuView
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
    actionSetSidebarItem,
    actionBackEditRoleFromUserClick
}

export default connect(mapStateToProps, mapDispatchToProps)(RolesMenuView);