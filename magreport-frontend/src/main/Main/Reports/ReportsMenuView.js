import React from 'react';
import { connect } from 'react-redux';

// dataHub
import dataHub from 'ajax/DataHub';

import { useSnackbar } from 'notistack';

// actions
import {actionFolderLoaded, actionFolderLoadFailed, actionFolderClick, actionItemClick, actionAddFolder, 
    actionAddItemClick, actionEditFolder, actionDeleteFolderClick, actionDeleteItemClick, actionSearchClick, actionChangeParentFolder, actionCopyFolder, actionSortClick
} from 'redux/actions/menuViews/folderActions';
import {actionAddDeleteFavorites} from 'redux/actions/favorites/actionFavorites'

// components
import DataLoader from '../../DataLoader/DataLoader';
import FolderContent from '../../FolderContent/FolderContent';
import SidebarItems from '../Sidebar/SidebarItems'
import PublishReportDesigner from "./PublishReportDesigner";

// state const
import {FLOW_STATE_BROWSE_FOLDER, reportsMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import ReportStarter from 'main/Report/ReportStarter';

function ReportsMenuView(props){

    let state = props.state;
    
    const { enqueueSnackbar } = useSnackbar();
    
    let loadFunc = dataHub.folderController.getFolder;

    let reload = {needReload : state.needReload};
    let folderItemsType = SidebarItems.reports.folderItemType;
    let isSortingAvailable = true;

    function handleDesignerExit(){
        props.actionFolderClick(folderItemsType, state.currentFolderId);
    }

    function handleReportCancel(){
        props.actionFolderClick(folderItemsType, state.currentFolderId);
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
                        onItemClick = {(reportId) => {props.actionItemClick(folderItemsType, reportId)}}
                        onAddFolder = {(name, description) => {props.actionAddFolder(folderItemsType, state.currentFolderData.id, name, description)}}
                        onAddItemClick = {() => {props.actionAddItemClick(folderItemsType)}}
                        onEditFolderClick = {(folderId, name, description) => {props.actionEditFolder(folderItemsType, state.currentFolderData.id, folderId, name, description)}}
                        onEditItemClick = {(reportId) => {props.actionEditItemClick(folderItemsType, reportId)}}
                        onDeleteFolderClick = {(folderId) => {props.actionDeleteFolderClick(folderItemsType, state.currentFolderData.id, folderId)}}
                        onDeleteItemClick = {(reportId) => {props.actionDeleteItemClick(folderItemsType, state.currentFolderId, reportId)}}
                        onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, state.currentFolderId, searchParams)}}
                        onAddDeleteFavorites = {(index, folderId, reportId, isFavorite) => props.actionAddDeleteFavorites(folderItemsType, index, folderId, reportId, isFavorite, enqueueSnackbar)}
                        onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                        contextAllowed
                        copyAndMoveAllowed
                        copyAndMoveAllowedForReport
                        onChangeParentFolder={(itemsType, folderId, parentFolderId) => props.actionChangeParentFolder(itemsType, folderId, parentFolderId)}
                        onCopyFolder = {(itemsType, destFolderId, folderIds) => props.actionCopyFolder(itemsType, destFolderId, folderIds)}
                    />
                </DataLoader>

                

            : state.flowState === reportsMenuViewFlowStates.pudlishReportDesigner ?
                <PublishReportDesigner
                    folderId = {state.currentFolderId}
                    onExit = {handleDesignerExit}
                />

            : state.flowState === reportsMenuViewFlowStates.startReport ?
                <ReportStarter
                    reportId = {state.reportId}
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
        state : state.reportsMenuView
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
    actionDeleteItemClick,
    actionSearchClick,
    actionAddDeleteFavorites,
    actionChangeParentFolder,
    actionCopyFolder,
    actionSortClick
}

export default connect(mapStateToProps, mapDispatchToProps)(ReportsMenuView);
