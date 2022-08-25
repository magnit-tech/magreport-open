import { APPLOGOUT } from 'redux/reduxTypes'
import { combineReducers } from 'redux'
import { loginReducer } from './loginReducer'
import { loaderReducer } from './loaderReducer'
import { snackbarReducer } from './snackbarReducer'
import { alertReducer } from './alertReducer'
import { alertDialogReducer} from './alertDialogReducer'
import { currentSideBarItemKeyReducer } from './sidebar/currentSidebarItemKeyReducer'
import { drawerReducer } from './sidebar/drawerReducer'
import { mainReducer } from './mainReducer'
import { folderTreeReducer } from './sidebar/folderTreeReducer'
import { rootMenuReducer } from './sidebar/rootMenuReducer'
import { navbarReducer } from './navbar/navbarReducer'
import { jobSqlReducer } from  './menuViews/jobSqlReducer'
import { reportsMenuViewReducer } from './menuViews/reportsMenuViewReducer'
import { jobsMenuViewReducer } from './menuViews/jobsMenuViewReducer'
import { datasetsMenuViewReducer } from './menuViews/datasetsMenuViewReducer'
import { datasourcesMenuViewReducer } from './menuViews/datasourcesMenuViewReducer'
import { filtersMenuViewReducer } from './menuViews/filterTemplatesMenuViewReducer'
import { filterInstancesMenuViewReducer } from './menuViews/filterInstancesMenuViewReducer'
import { rolesMenuViewReducer } from './menuViews/rolesMenuViewReducer'
import { reportsDevMenuViewReducer } from './menuViews/reportsDevMenuViewReducer'
import { securityFiltersMenuViewReducer } from './menuViews/securityFiltersMenuViewReducer'
import { usersJobsMenuViewReducer } from './menuViews/usersJobsMenuViewReducer'
import { schedulesMenuViewReducer } from "./menuViews/schedulesMenuViewReducer";
import { scheduleTasksMenuViewReducer } from "./menuViews/scheduleTasksMenuViewReducer";
import { asmAdministrationMenuViewReducer } from "./menuViews/asmAdministrationMenuViewReducer"
import { favoritesMenuViewReducer } from './menuViews/favoritesMenuViewReducer'

import { usersReducer } from './admin/usersReducer'
import { rolesReducer } from './admin/rolesReducer'
import { asmDesignerRootReducer } from "./admin/asmDesigner/asmDesignerRootReducer";
import { securityFiltersReducer } from "./admin/securityFiltersReducer";
import { serverSettingsReducer } from './admin/serverSettingsReducer'
import { reportFiltersReducer } from './developer/reportFiltersReducer'
import { reportTemplatesReducer } from './developer/reportTemplatesReducer'
import { themesMenuViewReducer } from './menuViews/themesMenuViewReducer'
import { mailTemplatesMenuViewReducer } from "./admin/MailTemplateReducer";

const appReducer = combineReducers({
    alert: alertReducer,
    alertDialog: alertDialogReducer,
    login: loginReducer,
    currentSidebarItemKey : currentSideBarItemKeyReducer,
    snackbar: snackbarReducer,
    navbar: navbarReducer,
    
    // menuViews
    reportsMenuView : reportsMenuViewReducer,
    favoritesMenuView : favoritesMenuViewReducer,
    jobsMenuView : jobsMenuViewReducer,
    datasetsMenuView : datasetsMenuViewReducer,
    datasourcesMenuView : datasourcesMenuViewReducer,
    filtersMenuView : filtersMenuViewReducer,
    filterInstancesMenuView : filterInstancesMenuViewReducer,
    reportsDevMenuView : reportsDevMenuViewReducer,
    rolesMenuView : rolesMenuViewReducer,
    securityFiltersMenuView : securityFiltersMenuViewReducer,
    usersJobsMenuView : usersJobsMenuViewReducer,
    asmAdministrationMenuView : asmAdministrationMenuViewReducer,
    schedulesMenuView : schedulesMenuViewReducer,
    scheduleTasksMenuView: scheduleTasksMenuViewReducer,
    jobSql: jobSqlReducer,
    mailTemplateMenuView: mailTemplatesMenuViewReducer,
    themesMenuView: themesMenuViewReducer,

    // **********************************
    users: usersReducer,
    roles: rolesReducer,
    asmDesigner: asmDesignerRootReducer,
    securityFilters: securityFiltersReducer,
    serverSettings: serverSettingsReducer,

    reportFilters: reportFiltersReducer,
    reportTemplates: reportTemplatesReducer, 

    drawer: drawerReducer,
    loader: loaderReducer,
    mainState: mainReducer,
    folderTree: folderTreeReducer,
    rootMenu: rootMenuReducer,

})

export const rootReducer = (state, action) => {
    if (action.type === APPLOGOUT) {
        state = undefined
    }

    return appReducer(state, action)
}