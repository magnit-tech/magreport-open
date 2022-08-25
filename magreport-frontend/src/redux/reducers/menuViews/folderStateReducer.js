import { SIDEBAR_ITEM_CHANGED, 
    FOLDER_CONTENT_FOLDER_CLICK, 
    FOLDER_CONTENT_LOAD_FAILED, 
    FOLDER_CONTENT_LOADED,
    FOLDER_CONTENT_FOLDER_ADDED,
    FOLDER_CONTENT_FOLDER_EDITED,
    FOLDER_CONTENT_FOLDER_DELETED,
    FOLDER_CONTENT_ITEM_ADDED,
    FOLDER_CONTENT_ITEM_EDITED,
    FOLDER_CONTENT_ITEM_DELETED,
    FOLDER_CONTENT_SEARCH_CLICK, 
    FOLDER_CONTENT_SORT_CLICK,
    FOLDER_CONTENT_SEARCH_RESULTS_LOADED,
    FOLDER_CONTENT_PARENT_FOLDER_CHANGED,
    FOLDER_CONTENT_FOLDER_COPIED,
    FOLDER_CONTENT_ITEM_MOVED,
    FOLDER_CONTENT_ITEM_COPIED,
    FAVORITES_ADD_START, 
    FAVORITES_ADDED, 
    FAVORITES_DELETE_START, 
    FAVORITES_DELETED,
} from 'redux/reduxTypes';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {FLOW_STATE_BROWSE_FOLDER} from './flowStates';    

const getItemName = itemsType => {
    return  itemsType === FolderItemTypes.report ? "reports" :
            itemsType === FolderItemTypes.favorites ? "reports" :
            itemsType === FolderItemTypes.job || itemsType === FolderItemTypes.userJobs ? "jobs" :
            itemsType === FolderItemTypes.datasource ? "dataSources" :
            itemsType === FolderItemTypes.dataset ? "dataSets" :
            itemsType === FolderItemTypes.filterTemplate ? "filterTemplates" :
            itemsType === FolderItemTypes.filterInstance ? "filterInstances" :
            itemsType === FolderItemTypes.reportsDev ? "reports" :
            itemsType === FolderItemTypes.roles ? "roles" :
            itemsType === FolderItemTypes.securityFilters ? "securityFilters" :
            itemsType === FolderItemTypes.schedules ? "schedules" :
            itemsType === FolderItemTypes.scheduleTasks ? "scheduleTasks" :
            itemsType === FolderItemTypes.systemMailTemplates ? "systemMailTemplates":
            itemsType === FolderItemTypes.theme ? "themes" :
            "";
    
}

export function folderStateReducer(state, action, sidebarItem, folderItemsType){
    switch(action.type){
        case SIDEBAR_ITEM_CHANGED:
            if(action.newSidebarItem.key === sidebarItem.key){
                return {
                    ...state,
                    flowState : FLOW_STATE_BROWSE_FOLDER,
                    currentFolderId : null,
                    needReload : true
                }
            }
            else{
                return state;
            }                 
        case FOLDER_CONTENT_LOADED:
            if(action.itemsType === folderItemsType){
                delete state.searchParams;

                let itemsName = getItemName(action.itemsType),
                    sortParams = JSON.parse(localStorage.getItem("sortParams")),
                    childFolders = action.folderData.childFolders,
                    items = action.folderData[itemsName];

                if(!sortParams) {
                    const defaultSortParams = { key: 'name', direction: 'ascending' };
                    localStorage.setItem("sortParams", JSON.stringify(defaultSortParams));
                    sortParams = defaultSortParams;
                }
                    
                if (action.isSortingAvailable && (childFolders || items)) {
                    if (childFolders && childFolders.length > 0) {
                        childFolders.sort((a, b) => {
                            if (a[sortParams.key] < b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? -1 : 1;
                            }
                            if (a[sortParams.key] > b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? 1 : -1;
                            }
                                return 0;
                        });
                    }
    
                    if (items && items.length > 0) {
                        items.sort((a, b) => {
                            if (a[sortParams.key] < b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? -1 : 1;
                            }
                            if (a[sortParams.key] > b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? 1 : -1;
                            }
                                return 0;
                        });
                    }

                    const filteredFolderData = {
                        ...action.folderData,
                        childFolders,
                        [itemsName]: items
                    }
    
                    return{
                        ...state,
                        needReload : false,
                        currentFolderData : action.folderData,
                        filteredFolderData,
                        sortParams
                    } 
                } else {
                    return{
                        ...state,
                        needReload : false,
                        currentFolderData : action.folderData,
                    }
                } 
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_LOAD_FAILED:
            if(action.itemsType === folderItemsType){
                return{
                    ...state,
                    needReload : false,
                    folderContentLoadErrorMessage : action.errorMessage
                }                  
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_FOLDER_ADDED:
        case FOLDER_CONTENT_FOLDER_EDITED:
        case FOLDER_CONTENT_FOLDER_DELETED:
            if(action.itemsType === folderItemsType && action.parentFolderId === state.currentFolderId){
                return{
                    ...state,
                    needReload : true
                }
            }
            else{
                return state;
            }            
        case FOLDER_CONTENT_ITEM_ADDED:
        case FOLDER_CONTENT_ITEM_EDITED:
        case FOLDER_CONTENT_ITEM_DELETED:
            if(action.itemType === folderItemsType && action.parentFolderId === state.currentFolderId){
                return{
                    ...state,
                    needReload : true
                }
            }
            else{
                return state;
            }

        case FOLDER_CONTENT_FOLDER_CLICK:
            if(action.itemsType === folderItemsType){
                delete state.filteredFolderData
                delete state.searchParams
                return{
                    ...state,
                    flowState : FLOW_STATE_BROWSE_FOLDER,
                    needReload : true,
                    currentFolderId : action.folderId
                } 
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_SEARCH_CLICK:
            if(action.itemsType === folderItemsType){
                let findStr = action.searchParams.searchString.toLowerCase().trim(),
                    sortParams = state.sortParams,
                    itemsName = getItemName(action.itemsType);

                if(!sortParams) {
                    const defaultSortParams = { key: 'name', direction: 'ascending' };
                    localStorage.setItem("sortParams", JSON.stringify(defaultSortParams));
                    sortParams = defaultSortParams;
                }

                if (findStr.length > 0){
                    const childFolders = state.currentFolderData.childFolders.filter(folder => folder.name.toLowerCase().includes(findStr)),
                          items = state.currentFolderData[itemsName].filter(item => item.name.toLowerCase().includes(findStr));

                    if (childFolders.length > 0) {
                        childFolders.sort((a, b) => {
                            if (a[sortParams.key] < b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? -1 : 1;
                            }
                            if (a[sortParams.key] > b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? 1 : -1;
                            }
                                return 0;
                        });
                    }
    
                    if (items.length > 0) {
                        items.sort((a, b) => {
                            if (a[sortParams.key] < b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? -1 : 1;
                            }
                            if (a[sortParams.key] > b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? 1 : -1;
                            }
                                return 0;
                        });
                    }

                    const filteredFolderData = {
                        ...state.currentFolderData,
                        childFolders,
                        [itemsName]: items
                    }

                    return {...state, filteredFolderData, searchParams: action.searchParams}
                }
                else {
                    const childFolders = state.currentFolderData.childFolders,
                          items = state.currentFolderData[itemsName];

                    if (childFolders.length > 0) {
                        childFolders.sort((a, b) => {
                            if (a[sortParams.key] < b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? -1 : 1;
                            }
                            if (a[sortParams.key] > b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? 1 : -1;
                            }
                                return 0;
                        });
                    }
    
                    if (items.length > 0) {
                        items.sort((a, b) => {
                            if (a[sortParams.key] < b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? -1 : 1;
                            }
                            if (a[sortParams.key] > b[sortParams.key]) {
                                return sortParams.direction === 'ascending' ? 1 : -1;
                            }
                                return 0;
                        });
                    }
                    
                    const filteredFolderData = {
                        ...state.currentFolderData,
                        childFolders,
                        [itemsName]: items
                    }

                    delete state.searchParams
                    return {...state, filteredFolderData}
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_SORT_CLICK:
            if(action.itemsType === folderItemsType){

                localStorage.setItem("sortParams", JSON.stringify(action.sortParams));

                let itemsName = getItemName(action.itemsType),
                    key = action.sortParams.key,
                    direction = action.sortParams.direction,
                    childFolders = null,
                    items = null;

                if (state.searchParams) {
                    childFolders = [...state.filteredFolderData.childFolders];
                    items = [...state.filteredFolderData[itemsName]]
                } else {
                    childFolders = [...state.currentFolderData.childFolders];
                    items = [...state.currentFolderData[itemsName]]
                }
                

                if (childFolders.length > 0) {
                    childFolders.sort((a, b) => {
                        if (a[key] < b[key]) {
                            return direction === 'ascending' ? -1 : 1;
                        }
                        if (a[key] > b[key]) {
                            return direction === 'ascending' ? 1 : -1;
                        }
                            return 0;
                    });
                }

                if (items.length > 0) {
                    items.sort((a, b) => {
                        if (a[key] < b[key]) {
                            return direction === 'ascending' ? -1 : 1;
                        }
                        if (a[key] > b[key]) {
                            return direction === 'ascending' ? 1 : -1;
                        }
                            return 0;
                    });
                }

                const filteredFolderData = {
                    ...state.currentFolderData,
                    childFolders,
                    [itemsName]: items
                }

                return {...state, filteredFolderData, sortParams: action.sortParams}
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_SEARCH_RESULTS_LOADED:
            if(action.itemsType === folderItemsType){
                const findStr = action.searchParams.searchString.toLowerCase().trim()
                if (findStr.length > 0){
                    let itemsName = getItemName(action.itemsType)
                    const childFolders = action.data.folders.map(f => {
                        return {
                            id: f.folder.id,
                            name: f.folder.name,
                            description: f.folder.name,
                            created: f.folder.created,
                            modified: f.folder.modified,
                            path: f.path
                        }
                    })
                    const objects = action.data.objects.map(o => {return {...o['element'], path: o['path'] }} );
                    const filteredFolderData = {
                        childFolders,
                        [itemsName]: objects
                    }
                    return {...state, filteredFolderData, searchParams: action.searchParams}
                }
                else {
                    delete state.filteredFolderData
                    delete state.searchParams
                    return {...state}
                }
            }
            else {
                return state;
            }
        case FOLDER_CONTENT_PARENT_FOLDER_CHANGED:
            if(action.itemsType === folderItemsType){
                return{
                    ...state,
                    needReload : true
                }
            }
            else{
                return state;
            } 
        case FOLDER_CONTENT_FOLDER_COPIED:
            if(action.itemsType === folderItemsType){
                return{
                    ...state,
                    needReload : true
                }
            }
            else{
                return state;
            } 
        case FOLDER_CONTENT_ITEM_MOVED:
            if(action.itemsType === folderItemsType){
                return{
                    ...state,
                    needReload : true
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_ITEM_COPIED:
            if(action.itemsType === folderItemsType){
                return{
                    ...state,
                    needReload : true
                }
            }
            else{
                return state;
            }
        case FAVORITES_ADD_START:
        case FAVORITES_DELETE_START:
            return state
        
        case FAVORITES_ADDED:
        case FAVORITES_DELETED:
            if(action.itemsType === folderItemsType){
                let folderData = {...state.currentFolderData}
                if (state.searchParams){
                    folderData = {...state.filteredFolderData}
                }
                
                // const reportIndex = folderData.reports.findIndex(r => r.id === action.reportId)
                const reportIndex = action.index
                let report = {...folderData.reports[reportIndex]}
                report.favorite = !report.favorite
                
                if (action.favorite && action.itemsType === FolderItemTypes.favorites){
                    folderData.reports.splice(reportIndex, 1)
                }
                else {
                    folderData.reports.splice(reportIndex, 1, report)
                }
                if (state.searchParams){
                    return {...state, filteredFolderData: folderData}
                }
                return {...state, currentFolderData: folderData}
            }
            else {
                return state
            }
        
        default:
            return state;
    }    
}
