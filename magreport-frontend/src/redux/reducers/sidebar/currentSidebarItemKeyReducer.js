import {SIDEBAR_ITEM_CHANGED, VIEWER_VIEW_ITEM, VIEWER_EDIT_ITEM, FOLDER_CONTENT_EDIT_ROLE_USER_CLICK /*, ASM_VIEW_ITEM_CLICK*/} from 'redux/reduxTypes'
import {initialSidebarItem} from './initialSidebarItem';
import SidebarItems from "main/Main/Sidebar/SidebarItems"
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

const initialState = initialSidebarItem.key;

export const currentSideBarItemKeyReducer = (state = initialState, action) => {
    switch (action.type){
        case SIDEBAR_ITEM_CHANGED:
            return  action.newSidebarItem.key;
        case VIEWER_VIEW_ITEM:
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ROLE_USER_CLICK:
       // case FOLDER_CONTENT_BACK_EDIT_ROLE_USER_CLICK:
            let newSidebarItemKey;

            switch (action.itemType) {
                case FolderItemTypes.asm:
                    newSidebarItemKey = SidebarItems.admin.subItems.ASMAdministration.key;
                    break;
                case FolderItemTypes.roles:
                    newSidebarItemKey = SidebarItems.admin.subItems.roles.key;
                    break;
                case FolderItemTypes.datasource:
                    newSidebarItemKey = SidebarItems.development.subItems.datasources.key;
                    break;
                case FolderItemTypes.dataset:
                    newSidebarItemKey = SidebarItems.development.subItems.datasets.key;
                    break
                case FolderItemTypes.securityFilters:
                    newSidebarItemKey = SidebarItems.admin.subItems.securityFilters.key;
                    break;
                case FolderItemTypes.filterInstance:
                    newSidebarItemKey = SidebarItems.development.subItems.filterInstances.key;
                    break;
                case FolderItemTypes.filterTemplate:
                    newSidebarItemKey = SidebarItems.development.subItems.filterTemplates.key;
                    break;
                case FolderItemTypes.reportsDev:
                    newSidebarItemKey = SidebarItems.development.subItems.reportsDev.key;
                    break;
                case FolderItemTypes.schedules:
                    newSidebarItemKey = SidebarItems.schedule.subItems.schedules.key;
                    break;
                case FolderItemTypes.scheduleTasks:
                    newSidebarItemKey = SidebarItems.schedule.subItems.scheduleTasks.key;
                    break;
                case FolderItemTypes.systemMailTemplates:
                    newSidebarItemKey = SidebarItems.admin.subItems.mailTexts.key;
                    break;
                case FolderItemTypes.theme:
                    newSidebarItemKey = SidebarItems.admin.subItems.theme;
                    break;
                default:
                    newSidebarItemKey = state;
                    break;
            }

            return newSidebarItemKey;

        default:
            return state
    }
}