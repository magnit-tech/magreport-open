import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_GET_DEPENDENCIES_LOADED,
    FOLDER_CONTENT_GET_DEPENDENCIES_START,
    FOLDER_CONTENT_ITEM_CLICK,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM
} from 'redux/reduxTypes';
import {FLOW_STATE_BROWSE_FOLDER, datasetsMenuViewFlowStates} from './flowStates';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER,
    // При разработке можно установить нужное исходное состояние:
    //flowState : datasetsMenuViewFlowStates.datasetDesigner,
    editDatasetId : null
}

export function datasetsMenuViewReducer(state = initialState, action){
    switch(action.type){
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.dataset){
                return{
                    ...state,
                    flowState : datasetsMenuViewFlowStates.datasetViewer,
                    viewDatasetId : action.itemId
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.dataset){
                return{
                    ...state,
                    flowState : datasetsMenuViewFlowStates.datasetDesigner,
                    editDatasetId : null
                }
            }
            else{
                return state;
            }
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.dataset){
                return {
                    ...state,
                    flowState : datasetsMenuViewFlowStates.datasetDesigner,
                    editDatasetId : action.itemId
                }
            }
            else{
                return state
            }
        
        case FOLDER_CONTENT_GET_DEPENDENCIES_START:
            return state
        
        case FOLDER_CONTENT_GET_DEPENDENCIES_LOADED:
            if(action.itemsType === FolderItemTypes.dataset){
                return {
                    ...state,
                    flowState : datasetsMenuViewFlowStates.datasetDependenciewView,
                    datasetId : action.itemId,
                    dependenciesData: action.dependenciesData,
                }
            }
            else{
                return state
            }
        default:
            return folderStateReducer(state, action, SidebarItems.development.subItems.datasets, FolderItemTypes.dataset);
    }
    
}
