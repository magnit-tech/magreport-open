import {FLOW_STATE_BROWSE_FOLDER, themesMenuViewFlowStates} from './flowStates';
import { DESIGN_SETTINGS_SET_VALUE, APPLOGOUT, DARKTHEME, LIGHTTHEME } from '../../reduxTypes';

import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM,
} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

import defaultTheme from  '../../../themes/defaultTheme';
import defaultDarkTheme from  '../../../themes/defaultDarkTheme'; 

const isDarkTheme = localStorage.getItem('isDarkTheme')==='true' ? true: false;

const initialState = {
        ...folderInitialState,
        flowState : FLOW_STATE_BROWSE_FOLDER,
        theme: isDarkTheme ? defaultDarkTheme : defaultTheme,
        darkTheme: isDarkTheme
}
    
export const themesMenuViewReducer = (state = initialState, action) => {
    switch (action.type){
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.theme) {
                return {
                    ...state,
                    flowState: themesMenuViewFlowStates.themeViewer,
                    viewThemeId: action.itemId,
                };
            } else {
                return state;
            }
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.theme){
                return{
                    ...state,
                    flowState : themesMenuViewFlowStates.themeDesigner,
                    editThemeId : null
                }
            }
            else{
                return state;
            }
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if (action.itemType === FolderItemTypes.theme) {
                return {
                    ...state,
                    flowState: themesMenuViewFlowStates.themeDesigner,
                    editThemeId: action.itemId
                }
            } else {
                return state;
            }


        case DESIGN_SETTINGS_SET_VALUE: 
            return {...state, theme: action.theme}
        case APPLOGOUT: 
            return {...state, darkTheme: action.darkTheme}
        case DARKTHEME:
            return {...state, darkTheme: action.darkTheme, theme: defaultDarkTheme}
        case LIGHTTHEME: 
            return {...state, darkTheme: action.darkTheme, theme: defaultTheme}
        default:
            return folderStateReducer(state, action, SidebarItems.admin.subItems.theme, FolderItemTypes.theme);
    }
    
    
}