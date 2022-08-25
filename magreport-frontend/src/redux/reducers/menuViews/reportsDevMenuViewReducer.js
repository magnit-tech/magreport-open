import {FLOW_STATE_BROWSE_FOLDER, reportsDevMenuViewFlowStates} from './flowStates';
import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    FOLDER_CONTENT_GET_DEPENDENCIES_LOADED,
    FOLDER_CONTENT_GET_DEPENDENCIES_START,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM
} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function reportsDevMenuViewReducer(state = initialState, action){
    switch(action.type){
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.reportsDev){
                return{
                    ...state,
                    flowState : reportsDevMenuViewFlowStates.reportDevDesigner,
                    editReportId : null
                }
            }
            else{
                return state;
            }
        case VIEWER_VIEW_ITEM:
            if(action.itemType === FolderItemTypes.reportsDev) {
                return {
                    ...state,
                    flowState: reportsDevMenuViewFlowStates.reportDevViewer,
                    viewReportId: action.itemId,
                };
            } else {
                return state;
            }
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.reportsDev){
                return{
                    ...state,
                    flowState : reportsDevMenuViewFlowStates.reportDevDesigner,
                    editReportId : action.itemId
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.reportsDev){
                return{
                    ...state,
                    flowState : reportsDevMenuViewFlowStates.startReport,
                    reportId : action.itemId
                }
            }
            else{
                return state;
            }
            case FOLDER_CONTENT_GET_DEPENDENCIES_START:
                return state

            case FOLDER_CONTENT_GET_DEPENDENCIES_LOADED:
                if(action.itemsType === FolderItemTypes.reportsDev){
                    return {
                        ...state,
                        flowState : reportsDevMenuViewFlowStates.reportDevDependenciesView ,
                        datasetId : action.itemId,
                        dependenciesData: action.dependenciesData,
                    }
                }
                else{
                    return state
                }
        default:
            return folderStateReducer(state, action, SidebarItems.development.subItems.reportsDev, FolderItemTypes.reportsDev);
    }
}
