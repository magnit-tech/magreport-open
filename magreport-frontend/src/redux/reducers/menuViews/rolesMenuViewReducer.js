import {FLOW_STATE_BROWSE_FOLDER, rolesMenuViewFlowStates} from './flowStates';
import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ROLE_USER_CLICK, 
    FOLDER_CONTENT_BACK_EDIT_ROLE_USER_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    VIEWER_EDIT_ITEM, VIEWER_VIEW_ITEM
} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER,
    editRoleId : null
}

export function rolesMenuViewReducer(state = initialState, action){
    switch(action.type){
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.roles){
                return{
                    ...state,
                    flowState : rolesMenuViewFlowStates.roleDesigner,
                    editRoleId : null
                }
            }
            else{
                return state;
            }
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.roles){
                return{
                    ...state,
                    flowState : rolesMenuViewFlowStates.roleViewer,
                    viewRoleId : action.itemId,
                    viewRoleName: action.itemName
                }
            }
            else{
                return state;
            }

        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.roles){
                return{
                    ...state,
                    flowState : rolesMenuViewFlowStates.roleDesigner,
                    editRoleId : action.itemId,
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_EDIT_ROLE_USER_CLICK:
            if(action.itemType === FolderItemTypes.roles){
                return{
                    ...state,
                    flowState : rolesMenuViewFlowStates.roleDesigner,
                    editRoleId : action.itemId,
                    currentFolderId: action.folderId,
                    needReload: true,
                    fromUsers: true
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_BACK_EDIT_ROLE_USER_CLICK:
            if(action.itemType === FolderItemTypes.roles){
                return{
                    ...state,
                    fromUsers: false
                }
            }
            else{
                return state;
            }
        default:
            return folderStateReducer(state, action, SidebarItems.admin.subItems.roles, FolderItemTypes.roles);
    }
}
