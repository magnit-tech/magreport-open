import React from 'react';
import { connect } from 'react-redux';

// dataHub
import dataHub from 'ajax/DataHub';

import { useSnackbar } from 'notistack';

// redux
import {FLOW_STATE_BROWSE_FOLDER, favoritesMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import {actionFolderLoaded, actionFolderLoadFailed, actionItemClick, actionFolderClick, actionSortClick} from 'redux/actions/menuViews/folderActions';
import {actionJobCancel} from 'redux/actions/jobs/actionJobs';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';
import {actionAddDeleteFavorites} from 'redux/actions/favorites/actionFavorites'

// components
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';

import SidebarItems from '../Sidebar/SidebarItems';
import ReportStarter from 'main/Report/ReportStarter';

function FavoritesMenuView(props){

    const { enqueueSnackbar } = useSnackbar();

    let state = props.state;
    let reload = {needReload : state.needReload};

    let folderItemsType = SidebarItems.favorites.folderItemType;
    let isSortingAvailable = true;

    function handleReportCancel(){
        props.actionSetSidebarItem(SidebarItems.favorites);
    }

    function handleFolderClick(folderId){
        props.actionSetSidebarItem(SidebarItems.reports);
        props.actionFolderClick(SidebarItems.reports.folderItemType, folderId)
        
    }

    return(
        <div style={{display: 'flex', flex: 1}}>
        {
            state.flowState === FLOW_STATE_BROWSE_FOLDER ?
            <DataLoader
                loadFunc = {dataHub.reportController.getFavorites}
                loadParams = {[]}
                reload = {reload}
                onDataLoaded = {data => props.actionFolderLoaded(folderItemsType, data, isSortingAvailable)}
                onDataLoadFailed = {(message) => {props.actionFolderLoadFailed(folderItemsType, message)}}
            >
                <FolderContent
                    itemsType = {folderItemsType}
                    data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                    filters = {props.filters}
                    showAddFolder = {false}
                    showAddItem = {false}
                    showItemControls = {false}
                    pagination = {false}
                    onFolderClick = {handleFolderClick}
                    onItemClick = {reportId => props.actionItemClick(folderItemsType, reportId)}
                    onJobCancelClick = {(jobIndex, jobId) => props.actionJobCancel(folderItemsType, jobIndex, jobId)}
                    onAddDeleteFavorites = {(index, folderId, reportId, isFavorite) => props.actionAddDeleteFavorites(folderItemsType, index, folderId, reportId, isFavorite, enqueueSnackbar)}
                    contextAllowed
                    sortParams = {state.sortParams || {}}
                    onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                />
            </DataLoader>

            : state.flowState === favoritesMenuViewFlowStates.startReport ?
            <ReportStarter
                reportId = {state.reportId}
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
        state : state.favoritesMenuView,
        currentFolderData : state.favoritesMenuView.currentFolderData,
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionItemClick,
    actionSetSidebarItem,
    actionJobCancel,
    actionAddDeleteFavorites,
    actionFolderClick,
    actionSortClick
}

export default connect(mapStateToProps, mapDispatchToProps)(FavoritesMenuView);
