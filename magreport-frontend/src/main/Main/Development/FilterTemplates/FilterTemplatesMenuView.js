import React from 'react';
import { connect } from 'react-redux';
import {useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionSearchClick
} from 'redux/actions/menuViews/folderActions';

// components
import DataLoader from '../../../DataLoader/DataLoader';
import FolderContent from '../../../FolderContent/FolderContent';
import SidebarItems from '../../Sidebar/SidebarItems'
import FilterTemplatesViewer from "./FilterTemplatesViewer";

// flowstates
import {FLOW_STATE_BROWSE_FOLDER, filterTemplatesMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';

/**
 * @callback actionFolderClick
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {Number} folderId - идентификатор папки с объектами
 */

/**
 * @callback actionFolderLoaded
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {Object} data - полученные данные
 */

/**
 * @callback actionFolderLoadFailed
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {String} message - сообщение об ошибке
 */

/**
 * @callback actionItemClick
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {Number} scheduleId - идентификатор расписания
 */

/**
 * @callback actionSearchClick
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {Number} currentFolderId - идентификатор текущей папки
 * @param {Object} searchParams - параметры поиска
 */

/**
 * Компонент просмотра шаблонов фильтров
 * @param {Object} props - свойства компонента
 * @param {Object} props.state - привязанное состояние со списком расписаний
 * @param {actionFolderClick} props.actionFolderClick - действие, вызываемое при нажатии на папку
 * @param {actionFolderLoaded} props.actionFolderLoaded - действие, вызываемое при успешной загрузке данных
 * @param {actionFolderLoadFailed} props.actionFolderLoadFailed - действие, вызываемое в случае ошибки при получении данных
 * @param {actionItemClick} props.actionItemClick - действие, вызываемое при нажатии на карточку объекта
 * @param {actionSearchClick} props.actionSearchClick - действие, вызываемое при нажатии на кнопку поиска
 * @return {JSX.Element}
 * @constructor
 */
function FilterTemplatesMenuView(props){

    const navigateBack = useNavigateBack();

    let state = props.state;

    let loadFunc = dataHub.filterTemplateController.getFolder;

    let reload = {needReload : state.needReload};
    let folderItemsType = SidebarItems.development.subItems.filterTemplates.folderItemType;

    function handleExit() {
        navigateBack();
    }

    return(
        <div style={{display: 'flex', flex: 1}}>
        {
        state.flowState === FLOW_STATE_BROWSE_FOLDER ?
            <DataLoader
                loadFunc = {loadFunc}
                loadParams = {[state.currentFolderId]}
                reload = {reload}
                onDataLoaded = {(data) => {props.actionFolderLoaded(folderItemsType, data)}}
                onDataLoadFailed = {(message) => {props.actionFolderLoadFailed(folderItemsType, message)}}
            >
                <FolderContent
                    itemsType = {folderItemsType}
                    showAddFolder = {false}
                    showAddItem = {false}
                    showItemControls={false}
                    data = {state.filteredFolderData ? state.filteredFolderData : state.currentFolderData}
                    searchParams = {state.searchParams || {}}
                    onFolderClick = {(folderId) => {props.actionFolderClick(folderItemsType, folderId)}}
                    onItemClick = {(filterTemplateId) => {props.actionItemClick(folderItemsType, filterTemplateId)}}
                    onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, state.currentFolderId, searchParams)}}
                />
            </DataLoader>
        : state.flowState === filterTemplatesMenuViewFlowStates.filterTemplatesViewer ?
            <FilterTemplatesViewer
                filterTemplateId = {state.viewFilterTemplateId}
                onOkClick={handleExit}
            />
        : <div>Неизвестное состояние</div>
        }
        </div>
    )
}

const mapStateToProps = state => {
    return {
        state : state.filtersMenuView
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionSearchClick
}

export default connect(mapStateToProps, mapDispatchToProps)(FilterTemplatesMenuView);
