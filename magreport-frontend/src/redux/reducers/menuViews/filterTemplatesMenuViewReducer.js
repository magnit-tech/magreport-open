import {FLOW_STATE_BROWSE_FOLDER, filterTemplatesMenuViewFlowStates} from './flowStates';
import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_ITEM_CLICK, VIEWER_EDIT_ITEM, VIEWER_VIEW_ITEM
} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function filtersMenuViewReducer(state = initialState, action){
    switch(action.type){
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.filterTemplate) {
                return {
                    ...state,
                    flowState : filterTemplatesMenuViewFlowStates.filterTemplatesViewer,
                    viewFilterTemplateId : action.itemId
                }
            }
            else {
                return state;
            }
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.filterTemplate){
                return{
                    ...state,
                    flowState : filterTemplatesMenuViewFlowStates.filterTemplatesDesigner,
                    editFilterTemplateId : null
                }
            }
            else{
                return state;
            }
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.filterTemplate){
                return{
                    ...state,
                    flowState : filterTemplatesMenuViewFlowStates.filterTemplatesDesigner,
                    editFilterTemplateId : action.itemId
                }
            }
            else{
                return state;
            }
        default:
            return folderStateReducer(state, action, SidebarItems.development.subItems.filterTemplates, FolderItemTypes.filterTemplate);
    }
}
