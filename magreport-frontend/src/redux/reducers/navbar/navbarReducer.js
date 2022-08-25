import React from "react";
import {Edit, NoteAdd, Pageview} from "@material-ui/icons"

import dataHub from 'ajax/DataHub';
import {
    ASM_EDIT_ITEM_CLICK,
    ASM_VIEW_ITEM_CLICK,
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ROLE_USER_CLICK,
    FOLDER_CONTENT_FOLDER_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    FOLDER_CONTENT_LOADED,
    REPORT_START,
    SIDEBAR_ITEM_CHANGED,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM,
} from 'redux/reduxTypes';
import SidebarItems from '../../../main/Main/Sidebar/SidebarItems';
import {folderItemTypeName, FolderItemTypes} from 'main/FolderContent/FolderItemTypes.js';
import actionSetSidebarItem from '../../actions/sidebar/actionSetSidebarItem';
import {actionFolderClick, actionItemClick} from 'redux/actions/menuViews/folderActions';

import {initialSidebarItem} from '../sidebar/initialSidebarItem';
import {actionViewerEditItem, actionViewerViewItem} from "redux/actions/actionViewer";

const initialState = {
    items: [{
        text: initialSidebarItem.text,
        icon: initialSidebarItem.icon,
        isLast: true,
        callbackFunc: actionSetSidebarItem(initialSidebarItem),
    }]
}

function buildNavigationPathToFolder(itemsType, folderId, isLastPosition){
    let path = [];
    let id = folderId;
    let localCache = dataHub.getLocalCache();
    let isLast = isLastPosition;
    while((id !== null)&&(id !== undefined)){
        if (itemsType !== FolderItemTypes.userJobs && itemsType !== FolderItemTypes.job){
            
            let data = localCache.getFolderData(itemsType, id);
            path.push({
                text : data.name,
                callbackFunc: actionFolderClick(itemsType, id),
                isLast
            });
            isLast = false;
            id = data.parentId;
        }
        else {
            let data = localCache.getJobInfo(id)
            path.push({
                text : data.name,
                callbackFunc: actionFolderClick(itemsType, id),
                isLast,
            })
            break
        }
    }

    let sidebarItem = itemsType === FolderItemTypes.report ? SidebarItems.reports
                     :itemsType === FolderItemTypes.favorites ? SidebarItems.favorites
                     :itemsType === FolderItemTypes.datasource ? SidebarItems.development.subItems.datasources
                     :itemsType === FolderItemTypes.dataset ? SidebarItems.development.subItems.datasets
                     :itemsType === FolderItemTypes.filterTemplate ? SidebarItems.development.subItems.filterTemplates
                     :itemsType === FolderItemTypes.filterInstance ? SidebarItems.development.subItems.filterInstances
                     :itemsType === FolderItemTypes.reportsDev ? SidebarItems.development.subItems.reportsDev
                     :itemsType === FolderItemTypes.roles ? SidebarItems.admin.subItems.roles
                     :itemsType === FolderItemTypes.securityFilters ? SidebarItems.admin.subItems.securityFilters
                     :itemsType === FolderItemTypes.userJobs ? SidebarItems.admin.subItems.userJobs
                     :itemsType === FolderItemTypes.job ? SidebarItems.jobs
                     :itemsType === FolderItemTypes.asm ? SidebarItems.admin.subItems.ASMAdministration
                     :itemsType === FolderItemTypes.schedules ? SidebarItems.schedule.subItems.schedules
                     :itemsType === FolderItemTypes.scheduleTasks ? SidebarItems.schedule.subItems.scheduleTasks
                     :itemsType === FolderItemTypes.systemMailTemplates ? SidebarItems.admin.subItems.mailTexts
                     :itemsType === FolderItemTypes.theme ? SidebarItems.admin.subItems.theme
                     :  SidebarItems.reports;
    path.push({
        text : sidebarItem.text,
        icon : sidebarItem.icon,
        callbackFunc : actionSetSidebarItem(sidebarItem),
        isLast
    });

    return path.reverse();
}

function buildNavigationPathToItem(itemType, itemId){
    let path = [];
    let localCache = dataHub.getLocalCache();
    let data

    if (itemType === FolderItemTypes.job || itemType === FolderItemTypes.userJobs || itemType === SidebarItems.jobs.key){

        data = localCache.getJobInfo(itemId)

        path.push({
            text : data.report.name,
            isLast: true,
            callbackFunc: actionItemClick(itemType, itemId)
        });

       // let sidebarItem = itemType === FolderItemTypes.job ? SidebarItems.jobs : SidebarItems.admin.subItems.userJobs

        let sidebarItem = itemType === FolderItemTypes.job ? SidebarItems.jobs
                        : itemType === SidebarItems.jobs.key ? SidebarItems.jobs
                        : itemType === FolderItemTypes.userJobs ? SidebarItems.admin.subItems.userJobs
                        : null;
        path.push({
            text : sidebarItem.text,
            icon : sidebarItem.icon,
            isLast: false,
            callbackFunc : actionSetSidebarItem(sidebarItem)
        });
        path.reverse()
    }
    else {
       // debugger
        let icon = <Edit/>;
        if (itemType === FolderItemTypes.report) {
            icon = null;
        }
        let service = itemType === FolderItemTypes.report ? localCache.getReportInfo
                    : itemType === FolderItemTypes.favorites ? localCache.getReportInfo
                    : itemType === FolderItemTypes.dataset ? localCache.getDatasetInfo
                    : itemType === FolderItemTypes.reportsDev ? localCache.getReportInfo
                    : itemType === FolderItemTypes.roles ? localCache.getUserRolesInfo 
                    : itemType === FolderItemTypes.datasource ? localCache.getDatasourceInfo
                    : itemType === FolderItemTypes.securityFilters ? localCache.getSecurityFilterInfo
                    : itemType === FolderItemTypes.filterInstance ? localCache.getFilterInstanceInfo
                    : itemType === FolderItemTypes.filterTemplate ? localCache.getFilterTemplateInfo
                    : itemType === FolderItemTypes.asm ? localCache.getASMInfo
                    : itemType === FolderItemTypes.schedules ? localCache.getScheduleInfo
                    : itemType === FolderItemTypes.scheduleTasks ? localCache.getScheduleTasksInfo
                    : itemType === FolderItemTypes.systemMailTemplates ? localCache.getSystemMailTemplateIdInfo
                    : itemType === FolderItemTypes.theme ? localCache.getThemeInfo
                    : null;
        
        data = service(itemId) 
        if (!data){
            data = localCache.getItemData(itemType, itemId)
        }
        path = buildNavigationPathToFolder(itemType, data.folderId, false) 
        path.push({
            icon,
            text : data.name,
            isLast : true,
            callbackFunc: () => {}
        })
    }
    
    return path
}

function buildNavigationPathToItemViewerEditor(items, itemType, itemId, itemName, isViewer) {
    const viewerIcon = <Pageview/>; //TODO: move icon constants to a separate script?
    const editorIcon = <Edit/>;

    let localCache = dataHub.getLocalCache();
    let service = itemType === FolderItemTypes.securityFilters ? localCache.getSecurityFilterInfo
                : itemType === FolderItemTypes.filterInstance ? localCache.getFilterInstanceInfo
                : itemType === FolderItemTypes.filterTemplate ? localCache.getFilterTemplateInfo
                : itemType === FolderItemTypes.dataset ? localCache.getDatasetInfo
                : itemType === FolderItemTypes.datasource ? localCache.getDatasourceInfo
                : itemType === FolderItemTypes.roles ? localCache.getRoleInfo
                : itemType === FolderItemTypes.reportsDev ? localCache.getReportInfo
                : itemType === FolderItemTypes.asm ? localCache.getASMInfo
                : itemType === FolderItemTypes.schedules ? localCache.getScheduleInfo
                : itemType === FolderItemTypes.scheduleTasks ? localCache.getScheduleTasksInfo
                : itemType === FolderItemTypes.theme ? localCache.getthemeInfo 
                : null;
    let data;
    try {
        data = service(itemId);
        
        if (!data) {
            data = localCache.getItemData(itemType, itemId);

            if(!data) {
                data = {name: itemName || folderItemTypeName(itemType)}; //stub for cases when item data is not in cache
            }
            
        }
    } catch (e) {
        data = {name: itemName || folderItemTypeName(itemType)}; //stub for cases when item data is not in cache
    }

    let newCallbackFunc;
    if (isViewer) {
        newCallbackFunc = actionViewerViewItem(itemType, itemId, itemName);
    } else {
        newCallbackFunc = actionViewerEditItem(itemType, itemId, itemName);
    }

    const newItem = {
        icon: isViewer ? viewerIcon : editorIcon,
        text: data.hasOwnProperty('name') ? data.name : itemName,
        isLast: true,
        callbackFunc: newCallbackFunc
    };

    const existItemIndex = items.findIndex(i =>
        i.callbackFunc.type === newCallbackFunc.type
        && i.callbackFunc.itemType === newCallbackFunc.itemType
        && i.callbackFunc.itemId === newCallbackFunc.itemId
    );

    const newItems = [
        ...items.slice(0, existItemIndex !== -1 ? existItemIndex : items.length),
        newItem
    ];

    newItems.slice(0, newItems.length - 1).forEach(i => i.isLast = false);

    return newItems;
}

function buildNavigationPathToNewItem(items, itemsType, folderId) {
    const icon = <NoteAdd />;
    const itemName = folderItemTypeName(itemsType);
    const name = `Создание: ${itemName}`;

    const newItem = {
        icon,
        text : name,
        callbackFunc: () => {},
        isLast: true,
    }

    const newItems = [...items, newItem];
    newItems.slice(0, newItems.length - 1).forEach(i => i.isLast = false);
    return newItems;
}

export const navbarReducer = (state = initialState, action) => {
    switch (action.type){
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            return {
                items: buildNavigationPathToNewItem(state.items, action.itemsType, action.folderId)
            };
        case SIDEBAR_ITEM_CHANGED: 

            const item = {
                text: action.newSidebarItem.text,
                icon: action.newSidebarItem.icon,
                isLast: true,
                callbackFunc: actionSetSidebarItem(action.newSidebarItem),
            }

            return {
                items: [item]
            }
        case FOLDER_CONTENT_FOLDER_CLICK:
            if(action.isFolderItemPicker) {
                return state;
            }
            return {
                items : buildNavigationPathToFolder(action.itemsType, action.folderId, true)
            }
        case FOLDER_CONTENT_LOADED:
            if(action.isFolderItemPicker) {
                return state;
            }
            return {
                items : buildNavigationPathToFolder(action.itemsType, action.folderData.id, true)
            }
        case FOLDER_CONTENT_ITEM_CLICK:
            // if (action.isBreadcrumb) {
            //     return state;
            // }
            if (action.itemType === FolderItemTypes.job || 
                action.itemType === FolderItemTypes.userJobs || 
                action.itemType === FolderItemTypes.favorites ||
                action.itemType === FolderItemTypes.report ||
                action.itemType === FolderItemTypes.reportsDev){
                return {
                    items : buildNavigationPathToItem(action.itemType, action.itemId)
                }
            } else if (action.itemType === FolderItemTypes.securityFilters ||
                       action.itemType === FolderItemTypes.filterInstance ||
                       action.itemType === FolderItemTypes.filterTemplate ||
                       action.itemType === FolderItemTypes.dataset ||
                       action.itemType === FolderItemTypes.datasource ||
                       action.itemType === FolderItemTypes.roles ||
                       action.itemType === FolderItemTypes.schedules ||
                       action.itemType === FolderItemTypes.scheduleTasks ||
                       action.itemType === FolderItemTypes.schedules ||
                       action.itemType === FolderItemTypes.systemMailTemplates ||
                       action.itemType === FolderItemTypes.theme
            ) {
                return {
                    items: buildNavigationPathToItemViewerEditor(state.items, action.itemType, action.itemId, action.itemName, true)
                };
            }
            else {
                return state
            }
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if (action.itemType === FolderItemTypes.dataset ||
                action.itemType === FolderItemTypes.datasource ||
                action.itemType === FolderItemTypes.securityFilters ||
                action.itemType === FolderItemTypes.filterInstance ||
                action.itemType === FolderItemTypes.filterTemplate ||
                action.itemType === FolderItemTypes.roles ||
                action.itemType === FolderItemTypes.reportsDev ||
                action.itemType === FolderItemTypes.schedules ||
                action.itemType === FolderItemTypes.scheduleTasks ||
                action.itemType === FolderItemTypes.systemMailTemplates ||
                action.itemType === FolderItemTypes.theme
                ) {
                return {
                    items : buildNavigationPathToItem(action.itemType, action.itemId)
                }
            }
            else {
                return state
            }
        case VIEWER_EDIT_ITEM:
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_EDIT_ROLE_USER_CLICK:
            if (action.itemType === FolderItemTypes.securityFilters ||
                action.itemType === FolderItemTypes.filterInstance ||
                action.itemType === FolderItemTypes.filterTemplate ||
                action.itemType === FolderItemTypes.dataset ||
                action.itemType === FolderItemTypes.datasource ||
                action.itemType === FolderItemTypes.roles ||
                action.itemType === FolderItemTypes.reportsDev ||
                action.itemType === FolderItemTypes.asm ||
                action.itemType === FolderItemTypes.schedules ||
                action.itemType === FolderItemTypes.scheduleTasks ||
                action.itemType === FolderItemTypes.systemMailTemplates ||
                action.itemType === FolderItemTypes.theme
                ) {

                return {
                    items: buildNavigationPathToItemViewerEditor(state.items, action.itemType, action.itemId, action.itemName,
                        action.type === VIEWER_VIEW_ITEM)
                };
            } else {
                return state;
            }
        case ASM_VIEW_ITEM_CLICK:
            return {
                items: buildNavigationPathToItemViewerEditor(state.items, action.itemType, action.itemId, action.itemName, true)
            };
        case ASM_EDIT_ITEM_CLICK:
            return {
                items: buildNavigationPathToItem(action.itemType, action.itemId)
            };
        case REPORT_START:
            if (action.itemType === FolderItemTypes.job || action.itemType === FolderItemTypes.userJobs ){
                return {
                    items : buildNavigationPathToItem(action.itemType, action.jobId)
                }
            }
            else {
                return state
            }
        default:
            return state
    }
}