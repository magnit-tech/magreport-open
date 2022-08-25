import {FLOW_STATE_BROWSE_FOLDER, filterInstancesMenuViewFlowStates} from './flowStates';
import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    FOLDER_CONTENT_GET_DEPENDENCIES_LOADED,
    FOLDER_CONTENT_GET_DEPENDENCIES_START,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM,
} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function filterInstancesMenuViewReducer(state = initialState, action){
    switch(action.type){
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.filterInstance) {
                return {
                    ...state,
                    flowState: filterInstancesMenuViewFlowStates.filterInstancesViewer,
                    viewFilterInstanceId: action.itemId,
                };
            } else {
                return state;
            }
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.filterInstance){
                return{
                    ...state,
                    flowState : filterInstancesMenuViewFlowStates.filterInstancesDesigner,
                    editFilterInstanceId : null
                }
            }
            else{
                return state;
            }
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.filterInstance){
                return{
                    ...state,
                    flowState : filterInstancesMenuViewFlowStates.filterInstancesDesigner,
                    editFilterInstanceId : action.itemId
                }
            }
            else{
                return state;
            }
            case FOLDER_CONTENT_GET_DEPENDENCIES_START:
                return state

            case FOLDER_CONTENT_GET_DEPENDENCIES_LOADED:
                if(action.itemsType === FolderItemTypes.filterInstance){
                    return {
                        ...state,
                        flowState : filterInstancesMenuViewFlowStates.filterInstanceDependenciesView,
                        datasetId : action.itemId,
                        dependenciesData: action.dependenciesData,
                    }
                }
                else{
                    return state
                }
        default:
            return folderStateReducer(state, action, SidebarItems.development.subItems.filterInstances, FolderItemTypes.filterInstance);
    }
}
