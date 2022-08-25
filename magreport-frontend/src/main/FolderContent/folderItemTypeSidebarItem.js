

import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from './FolderItemTypes';

export function folderItemTypeSidebarItem(itemType){
    let sidebarItem = itemType === FolderItemTypes.report ? SidebarItems.reports
                    :itemType === FolderItemTypes.favorites ? SidebarItems.favorites
                    :itemType === FolderItemTypes.roles ? SidebarItems.admin.subItems.roles
                    :itemType === FolderItemTypes.reportsDev ? SidebarItems.development.subItems.reportsDev
                    :itemType === FolderItemTypes.dataset ? SidebarItems.development.subItems.datasets
                    :itemType === FolderItemTypes.datasource ? SidebarItems.development.subItems.datasources
                    :itemType === FolderItemTypes.filterTemplate ? SidebarItems.development.subItems.filterTemplates
                    :itemType === FolderItemTypes.filterInstance ? SidebarItems.development.subItems.filterInstances
                    :itemType === FolderItemTypes.securityFilters ? SidebarItems.admin.subItems.securityFilters
                    :itemType === FolderItemTypes.userJobs ? SidebarItems.admin.subItems.userJobs
                    :itemType === FolderItemTypes.job ? SidebarItems.jobs
                    :itemType === FolderItemTypes.schedules ? SidebarItems.schedule.subItems.schedules
                    :itemType === FolderItemTypes.scheduleTasks ? SidebarItems.schedule.subItems.scheduleTasks
                    :itemType === FolderItemTypes.systemMailTemplates ? SidebarItems.admin.subItems.mailTexts
                    :itemType === FolderItemTypes.theme ? SidebarItems.admin.subItems.theme
                    :null;
    return sidebarItem;
}
