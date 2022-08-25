import React from 'react';
import { connect } from 'react-redux';

// dataHub
import dataHub from 'ajax/DataHub';

// хук вывода всплывающих сообщений
import { useSnackbar } from 'notistack';

// actions
import {actionLoadedSettings, actionGetJournal, actionGetFullJournal, actionSetSetting, 
    actionSettingValueChanged, actionSettingDisableChanged, actionCloseHistory,
} from 'redux/actions/admin/actionSettings';

// local components
import DataLoader from 'main/DataLoader/DataLoader';
import SettingsMenuViewer from './SettingsMenuViewer';

// styles
//import {ServerSettingsCSS} from './ServerSettingsCSS'

function SettingsMenuView(props){

    //const classes = ServerSettingsCSS();

    const { enqueueSnackbar } = useSnackbar();

    return(
        <DataLoader
            loadFunc = {dataHub.serverSettings.get}
            loadParams = {[]}
            onDataLoaded = {data => props.actionLoadedSettings(data)}
            onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить настройки сервера: ${message}`, {variant: "error"})}
        >
            {props.folders &&
                <SettingsMenuViewer
                    folders = {props.folders}
                    history = {props.history}
                    actionCloseHistory = {props.actionCloseHistory}
                    actionSettingValueChanged = {props.actionSettingValueChanged}
                    actionSetSetting = {props.actionSetSetting}
                    actionSettingDisableChanged = {props.actionSettingDisableChanged}
                    actionGetJournal = {props.actionGetJournal}
                    actionGetFullJournal = {props.actionGetFullJournal}
                    getFullJournal = {props.getFullJournal}
                />
            }
        </DataLoader>
    )
}

const mapStateToProps = state => {
    return {
        folders: state.serverSettings.folders,
        history: state.serverSettings.history,
        getFullJournal: state.serverSettings.getFullJournal,
    }
}

const mapDispatchToProps = {
    actionLoadedSettings, 
    actionGetJournal, 
    actionGetFullJournal, 
    actionSetSetting,
    actionSettingValueChanged, 
    actionSettingDisableChanged,
    actionCloseHistory,
}

export default connect(mapStateToProps, mapDispatchToProps)(SettingsMenuView);
