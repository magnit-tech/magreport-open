import React from 'react';
import { connect } from 'react-redux';
import {useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {actionFolderLoaded, actionFolderLoadFailed, actionFolderClick, 
    actionItemClick, actionEditItemClick, actionDeleteItemClick, actionAddFolder, actionAddItemClick,
    actionEditFolder, actionDeleteFolderClick, actionGetDependencies, actionSearchClick, actionChangeParentFolder, actionCopyFolder, actionMoveFolderItem, actionCopyFolderItem, actionSortClick} from 'redux/actions/menuViews/folderActions';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';

// flowstates
import {FLOW_STATE_BROWSE_FOLDER, filterInstancesMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import { folderItemTypeSidebarItem } from 'main/FolderContent/folderItemTypeSidebarItem';
// components
import DataLoader from '../../..//DataLoader/DataLoader';
import FolderContent from '../../../FolderContent/FolderContent';
import SidebarItems from '../../Sidebar/SidebarItems'
import FilterInstancesDesigner from './FilterInstanceDesigner';
import FilterInstanceViewer from './FilterInstanceViewer';
import DependencyViewer from '../DependencyViewer';

function FilterInstancesMenuView(props){

    const navigateBack = useNavigateBack();

    let state = props.state;
    let designerMode = props.state.editFilterInstanceId === null ? 'create' : 'edit';

    let loadFunc = dataHub.filterInstanceController.getFolder;

    let reload = {needReload : state.needReload};
    let folderItemsType = SidebarItems.development.subItems.filterInstances.folderItemType;
    let isSortingAvailable = true;

    function handleDesignerExit(){
        navigateBack();
    }

    function handleDependencyPathClick(argFolderItemsType, folderId){
        props.actionSetSidebarItem(folderItemTypeSidebarItem(argFolderItemsType));
        props.actionFolderClick(argFolderItemsType, folderId)
    }

    return(
        <div style={{display: 'flex', flex: 1}}>
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
                    showAddFolder = {true}
                    showAddItem = {true}
                    data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                    searchParams = {state.searchParams || {}}
                    sortParams = {state.sortParams || {}}
                    onFolderClick = {(folderId) => {props.actionFolderClick(folderItemsType, folderId)}}
                    onItemClick = {(filterInstanceId) => {props.actionItemClick(folderItemsType, filterInstanceId)}}
                    onAddFolder = {(name, description) => {props.actionAddFolder(folderItemsType, state.currentFolderData.id, name, description)}}
                    onAddItemClick = {() => {props.actionAddItemClick(folderItemsType)}}
                    onEditFolderClick = {(folderId, name, description) => {props.actionEditFolder(folderItemsType, state.currentFolderData.id, folderId, name, description)}}
                    onEditItemClick = {(filterInstanceId) => {props.actionEditItemClick(folderItemsType, filterInstanceId)}}
                    onDeleteFolderClick = {(folderId) => {props.actionDeleteFolderClick(folderItemsType, state.currentFolderData.id, folderId)}}
                    onDeleteItemClick = {(filterInstanceId) => {props.actionDeleteItemClick(folderItemsType, state.currentFolderId, filterInstanceId)}}
                    onDependenciesClick = {filterInstanceId => props.actionGetDependencies(folderItemsType, filterInstanceId)}
                    onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, state.currentFolderId, searchParams)}}
                    onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                    contextAllowed
                    copyAndMoveAllowed
                    onChangeParentFolder={(itemsType, folderId, parentFolderId) => props.actionChangeParentFolder(itemsType, folderId, parentFolderId)}
                    onCopyFolder = {(itemsType, destFolderId, folderIds) => props.actionCopyFolder(itemsType, destFolderId, folderIds)}
                    onMoveFolderItem = {(itemsType, destFolderId, objIds, textForSnackbar) => props.actionMoveFolderItem(itemsType, destFolderId, objIds, textForSnackbar)}
                    onCopyFolderItem = {(itemsType, destFolderId, objIds, textForSnackbar) => props.actionCopyFolderItem(itemsType, destFolderId, objIds, textForSnackbar)}
                />
            </DataLoader>

            : state.flowState === filterInstancesMenuViewFlowStates.filterInstancesDesigner ?
            <FilterInstancesDesigner
                mode = {designerMode}
                filterInstanceId = {state.editFilterInstanceId}
                folderId = {state.currentFolderId}
                onExit = {handleDesignerExit}
            />
            : state.flowState === filterInstancesMenuViewFlowStates.filterInstancesViewer ?
            <FilterInstanceViewer
                filterInstanceId = {state.viewFilterInstanceId}
                onOkClick = {handleDesignerExit}
            />
            : state.flowState === filterInstancesMenuViewFlowStates.filterInstanceDependenciesView ?
                <DependencyViewer
                    itemsType = {folderItemsType}
                    itemId={state.reportId}
                    data={state.dependenciesData}
                    onLinkPathClick={handleDependencyPathClick}
                    onExit = {() => props.actionFolderClick(folderItemsType, state.currentFolderId)}
                />
            : <div>Неизвестное состояние</div>
            }
            </div>
    )
}

const mapStateToProps = state => {
    return {
        state : state.filterInstancesMenuView
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionAddFolder,
    actionAddItemClick,
    actionEditFolder,
    actionDeleteFolderClick,
    actionEditItemClick,
    actionDeleteItemClick,
    actionGetDependencies,
    actionSearchClick,
    actionSetSidebarItem,
    actionChangeParentFolder,
    actionCopyFolder,
    actionMoveFolderItem,
    actionCopyFolderItem,
    actionSortClick
}

export default connect(mapStateToProps, mapDispatchToProps)(FilterInstancesMenuView);
