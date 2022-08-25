import React from 'react';
import {MainCSS} from './MainCSS'

import Header from '../../header/Header/Header';
import SnackbarInfo from '../SnackbarInfo/SnackbarInfo';
import AlertDialog from '../AlertDialog/AlertDialog';
import { connect } from 'react-redux';
import { showSnackbar } from '../../redux/actions/actionSnackbar';
import { showAlertDialog, hideAlertDialog } from '../../redux/actions/actionsAlertDialog';
import Sidebar from './Sidebar/Sidebar';
import SidebarItems from './Sidebar/SidebarItems';
import Navbar from './Navbar/Navbar'

import ReportsMenuView from './Reports/ReportsMenuView';
import JobsMenuView from './Jobs/JobsMenuView';
import RolesMenuView from './Administration/Roles/RolesMenuView';
import UsersMenuView from './Administration/Users/UsersMenuView';
import UserJobsMenuView from './Administration/UsersJobs/UsersJobsMenuView';
import SecurityFiltersMenuView from './Administration/SecurityFilters/SecurityFiltersMenuView';
import ASMAdministrationMenuView from './Administration/ASMAdministration/ASMAdministrationMenuView';
import LogsMenuView from './Administration/Logs/LogsMenuView';
import SettingsMenuView from './Administration/ServerSettings/SettingsMenuView';
import DatasourcesMenuView from './Development/DatasourcesMenu/DatasourcesMenuView';
import DatasetsMenuView from './Development/DatasetsMenu/DatasetsMenuView';
import FilterTemplatesMenuView from './Development/FilterTemplates/FilterTemplatesMenuView';
import FilterInstancesMenuView from './Development/FilterInstances/FilterInstancesMenuView';
import ReportsDevMenuView from './Development/ReportsDevMenu/ReportsDevMenuView';
import FavoritesMenuView from './Favorites/FavoritesMenuView';
import SchedulesMenuView from "./Schedule/Schedules/SchedulesMenuView";
import ScheduleTasksMenuView from './Schedule/SchedulerReports/ScheduleTasksMenuView';
import MailTemplatesMenuView from './Administration/ServerMailTemplate/ServerMailTemplateMenuView';
import EmailMenuView from './Administration/MailSender/EmailMenuView';
import ThemesMenuView from './Administration/Theme/ThemesMenuView';

function Main(props){

    const classes = MainCSS();

    const { currentSidebarItemKey } = props;

    return (
        <main className={classes.main}>
            <Sidebar />
            <div className={classes.workspace}>
                <Header version={props.version}/>
                <Navbar />
              
                    
                    {
                        // Отчёты
                        currentSidebarItemKey === SidebarItems.reports.key ?
                        <ReportsMenuView /> :
                        
                        // Избранное
                        currentSidebarItemKey === SidebarItems.favorites.key ?
                        <FavoritesMenuView /> :
                    
                        // Задания
                        currentSidebarItemKey === SidebarItems.jobs.key ?
                        <JobsMenuView /> :
                    
                        // Администрирование
                        currentSidebarItemKey === SidebarItems.admin.subItems.roles.key ?
                        <RolesMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.users.key ?
                        <UsersMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.userJobs.key ?
                        <UserJobsMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.securityFilters.key ?
                        <SecurityFiltersMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.ASMAdministration.key ?
                        <ASMAdministrationMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.logs.key ?
                        <LogsMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.settings.key ?
                        <SettingsMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.mailTexts.key ?
                        <MailTemplatesMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.mailSender.key ?
                        <EmailMenuView /> :
                        currentSidebarItemKey === SidebarItems.admin.subItems.theme.key ?
                        <ThemesMenuView/> :
                        // Разработка
                        currentSidebarItemKey === SidebarItems.development.subItems.datasources.key ?
                        <DatasourcesMenuView /> :
                        currentSidebarItemKey === SidebarItems.development.subItems.datasets.key ?
                        <DatasetsMenuView /> :
                        currentSidebarItemKey === SidebarItems.development.subItems.filterTemplates.key ?
                        <FilterTemplatesMenuView /> :
                        currentSidebarItemKey === SidebarItems.development.subItems.filterInstances.key ?
                        <FilterInstancesMenuView /> : 
                        currentSidebarItemKey === SidebarItems.development.subItems.reportsDev.key ?
                        <ReportsDevMenuView /> :
                        currentSidebarItemKey === SidebarItems.schedule.subItems.schedules.key ?
                        <SchedulesMenuView /> :
                        currentSidebarItemKey === SidebarItems.schedule.subItems.scheduleTasks.key ?
                        <ScheduleTasksMenuView /> :
                        <div> Неизвестный пункт меню </div>
                    }
                    
                    <SnackbarInfo />
                    <AlertDialog />
               
            </div>
        </main>
    );
}

const mapStateToProps = state => {
    return {
        currentSidebarItemKey: state.currentSidebarItemKey
    }
}

const mapDispatchToProps = {
    showAlertDialog,
    hideAlertDialog,
    showSnackbar,
}

export default connect(mapStateToProps, mapDispatchToProps)(Main);
