import React from 'react';
import { connect } from 'react-redux';
import {useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {actionFolderLoaded, actionFolderLoadFailed, actionFolderClick, 
        actionItemClick, actionEditItemClick, actionDeleteItemClick, actionAddFolder, actionAddItemClick,
        actionEditFolder, actionDeleteFolderClick, actionSearchClick, actionChangeParentFolder, actionCopyFolder, actionSortClick} from 'redux/actions/menuViews/folderActions';

// const

import {FLOW_STATE_BROWSE_FOLDER, securityFiltersMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';

// components
import DataLoader from '../../../DataLoader/DataLoader';
import FolderContent from '../../../FolderContent/FolderContent';
import SidebarItems from '../../Sidebar/SidebarItems'
import SecurityFilterDesigner from './SecurityFilterDesigner'
import SecurityFilterViewer from "./SecurityFilterViewer";

function SecurityFiltersMenuView(props){

    const navigateBack = useNavigateBack();

    const state = props.state;
    let designerMode = props.state.editSecurityFilterId === null ? 'create' : 'edit';

    let loadFunc = dataHub.securityFilterController.getFolder;

    let reload = {needReload : state.needReload};
    let folderItemsType = SidebarItems.admin.subItems.securityFilters.folderItemType;
    let isSortingAvailable = true;
    
    function handleDesignerExit(){
        navigateBack();
    }

    return(
        <div  style={{display: 'flex', flex: 1}}>
        {
        state.flowState === FLOW_STATE_BROWSE_FOLDER ?
            (
            <DataLoader
                loadFunc = {loadFunc}
                loadParams = {[state.currentFolderId]}
                reload = {reload}
                onDataLoaded = {(data) => {props.actionFolderLoaded(folderItemsType, data, isSortingAvailable)}}
                onDataLoadFailed = {(message) => {props.actionFolderLoadFailed(folderItemsType, message)}}
            >
                <FolderContent
                    itemsType = {folderItemsType}
                    showAddFolder = {true}
                    showAddItem = {true}
                    data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                    searchParams = {state.searchParams || {}}
                    sortParams = {state.sortParams || {}}
                    onFolderClick = {(folderId) => {props.actionFolderClick(folderItemsType, folderId)}}
                    onItemClick = {(securityFilterId) => {props.actionItemClick(folderItemsType, securityFilterId)}}
                    onAddFolder = {(name, description) => {props.actionAddFolder(folderItemsType, state.currentFolderData.id, name, description)}}
                    onAddItemClick = {() => {props.actionAddItemClick(folderItemsType)}}
                    onEditFolderClick = {(folderId, name, description) => {props.actionEditFolder(folderItemsType, state.currentFolderData.id, folderId, name, description)}}
                    onEditItemClick = {(securityFilterId) => {props.actionEditItemClick(folderItemsType, securityFilterId)}}
                    onDeleteFolderClick = {(folderId) => {props.actionDeleteFolderClick(folderItemsType, state.currentFolderData.id, folderId)}}
                    onDeleteItemClick = {(securityFilterId) => {props.actionDeleteItemClick(folderItemsType, state.currentFolderId, securityFilterId)}}
                    onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, state.currentFolderId, searchParams)}}
                    onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                    contextAllowed
                    copyAndMoveAllowed
                    notAllowedForItems
                    onChangeParentFolder={(itemsType, folderId, parentFolderId) => props.actionChangeParentFolder(itemsType, folderId, parentFolderId)}
                    onCopyFolder = {(itemsType, destFolderId, folderIds) => props.actionCopyFolder(itemsType, destFolderId, folderIds)}
                />
            </DataLoader>
            )

        : state.flowState === securityFiltersMenuViewFlowStates.securityFiltersDesigner ?
        <SecurityFilterDesigner
            mode = {designerMode}
            securityFilterId = {state.editSecurityFilterId}
            folderId = {state.currentFolderId}
            onExit = {handleDesignerExit}
        />
        : state.flowState === securityFiltersMenuViewFlowStates.securityFiltersViewer ?
        <SecurityFilterViewer
            securityFilterId = {state.viewSecurityFilterId}
            onOkClick = {handleDesignerExit}
        />
        : <div>Неизвестное состояние</div>
        }
        </div>
    )
}

const mapStateToProps = state => {
    return {
        state : state.securityFiltersMenuView
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
    actionChangeParentFolder,
    actionCopyFolder,
    actionSortClick
}

export default connect(mapStateToProps, mapDispatchToProps)(SecurityFiltersMenuView);
