import React from 'react';
import {connect} from 'react-redux';
import {useNavigateBack} from "main/Main/Navbar/navbarHooks";

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionItemClick,
    actionEditItemClick,
    actionDeleteItemClick,
    actionAddItemClick,
    actionSearchClick,
    actionSortClick
} from 'redux/actions/menuViews/folderActions';

// const
import {FLOW_STATE_BROWSE_FOLDER, themesMenuViewFlowStates} from 'redux/reducers/menuViews/flowStates';
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

// local components
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';
import ThemeDesigner from './ThemeDesigner'

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
 * @param {Number} themeId - идентификатор расписания
 */

/**
 * @callback actionAddItemClick
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 */

/**
 * @callback actionEditItemClick
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {Number} themeId - идентификатор расписания
 */

/**
 * @callback actionDeleteItemClick
 * @param {String} folderItemsType - тип объекта из FolderItemsType
 * @param {Number} parentFolderId - идентификатор родительской папки (для расписаний всегда null)
 * @param {Number} themeId - идентификатор расписания
 */

/**
 * Компонент просмотра и редактирования расписаний
 * @param {Object} props - свойства компонента
 * @param {Object} props.state - привязанное состояние со списком расписаний
 * @param {actionFolderClick} props.actionFolderClick - действие, вызываемое при нажатии на папку
 * @param {actionFolderLoaded} props.actionFolderLoaded - действие, вызываемое при успешной загрузке данных
 * @param {actionFolderLoadFailed} props.actionFolderLoadFailed - действие, вызываемое в случае ошибки при получении данных
 * @param {actionItemClick} props.actionItemClick - действие, вызываемое при нажатии на карточку объекта
 * @param {actionAddItemClick} props.actionAddItemClick - действие, вызываемое при нажатии кнопки добавления нового расписания
 * @param {actionEditItemClick} props.actionEditItemClick - действие, вызываемое при нажатии кнопки редактирования расписания
 * @param {actionDeleteItemClick} props.actionDeleteItemClick - действие, вызываемое при нажатии кнопки удаления расписания
 * @return {JSX.Element}
 * @constructor
 */
function ThemesMenuView(props) {

    const navigateBack = useNavigateBack();

    const state = props.state;

    let designerMode = typeof (state.editThemeId) === "number" ? "edit" : "create";

    let loadFunc = dataHub.themeController.getAll;

    let reload = {needReload: state.needReload};
    let folderItemsType = FolderItemTypes.theme;
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
                    props.actionFolderLoaded(folderItemsType, {themes: data, childFolders: []}, isSortingAvailable)
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
                    onItemClick={(themeId) => {
                        props.actionItemClick(folderItemsType, themeId)
                    }}
                    onAddItemClick={() => {
                        props.actionAddItemClick(folderItemsType)
                    }}
                    onEditItemClick={(themeId) => {
                        props.actionEditItemClick(folderItemsType, themeId)
                    }}
                    onDeleteItemClick={(themeId) => {
                        props.actionDeleteItemClick(folderItemsType, null, themeId)
                    }}
                    onSearchClick ={searchParams => {props.actionSearchClick(folderItemsType, [], searchParams)}}
                    contextAllowed
                    sortParams = {state.sortParams || {}}
                    onSortClick ={sortParams => {props.actionSortClick(folderItemsType, state.currentFolderId, sortParams)}}
                />
                
            </DataLoader>
        );
    } else if (state.flowState === themesMenuViewFlowStates.themeDesigner) {
        component = <ThemeDesigner
            mode={designerMode}
            themeId={state.editThemeId}
            onExit={handleExit}
        />

        /*
        component = <ScheduleDesigner
            mode={designerMode}
            themeId={state.editthemeId}
            onExit={handleExit}
        />;
    */} else if (state.flowState === themesMenuViewFlowStates.themeViewer) {/*
        component = <ScheduleViewer
            themeId={state.viewthemeId}
            onOkClick={handleExit}
        />;
    */ } else {
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
        state: state.themesMenuView
    };
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
    actionSortClick
};

export default connect(mapStateToProps, mapDispatchToProps)(ThemesMenuView);
