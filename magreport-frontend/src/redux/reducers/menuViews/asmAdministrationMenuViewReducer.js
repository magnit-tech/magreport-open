import {
    ASM_LIST_LOADED,
    ASM_LIST_LOAD_FAILED,
    ASM_ADD_ITEM_CLICK,
    ASM_EDIT_ITEM_CLICK,
    ASM_ADDED,
    ASM_EDITED,
    ASM_DELETED,
    ASM_LIST_SHOW,
    ASM_REFRESH_START,
    ASM_REFRESH_FINISH,
    SIDEBAR_ITEM_CHANGED,
    ASM_VIEW_ITEM_CLICK,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM
} from "redux/reduxTypes";
import {asmAdministrationMenuViewFlowStates} from "./flowStates";
import SidebarItems from "main/Main/Sidebar/SidebarItems";
import {ASM_DESIGNER_CREATE_MODE, ASM_DESIGNER_EDIT_MODE} from "utils/asmConstants";

const initialState = {
    refresh: false,
    data: [],
    flowState: asmAdministrationMenuViewFlowStates.externalSecurityList,
    needReload: true
};

export const asmAdministrationMenuViewReducer = (state = initialState, action) => {
    switch (action.type) {
        case SIDEBAR_ITEM_CHANGED:
            if (action.newSidebarItem.key === SidebarItems.admin.subItems.ASMAdministration.key) {
                return {
                    ...state,
                    needReload: true,
                    flowState: asmAdministrationMenuViewFlowStates.externalSecurityList
                }
            } else {
                return state;
            }
        case ASM_LIST_SHOW:
            return {
                ...state,
                needReload: false,
                flowState: asmAdministrationMenuViewFlowStates.externalSecurityList
            }
        case ASM_LIST_LOADED:
            return {...state, needReload: false, data: action.data};
        case ASM_LIST_LOAD_FAILED:
            return {...state, needReload: false, data: action.error};
        case VIEWER_VIEW_ITEM:
        case ASM_VIEW_ITEM_CLICK:

            return {
                ...state,
                needReload: false,
                viewASMId: action.itemId,
                flowState: asmAdministrationMenuViewFlowStates.externalSecurityViewer,
            }
        case VIEWER_EDIT_ITEM:
        case ASM_EDIT_ITEM_CLICK:

            return {
                ...state,
                needReload: false,
                flowState: asmAdministrationMenuViewFlowStates.externalSecurityDesigner,
                editASMId: action.itemId,
                designerMode: ASM_DESIGNER_EDIT_MODE
            };
        case ASM_ADD_ITEM_CLICK:
            return {
                ...state,
                needReload: false,
                flowState: asmAdministrationMenuViewFlowStates.externalSecurityDesigner,
                designerMode: ASM_DESIGNER_CREATE_MODE
            };
        case ASM_ADDED:
        case ASM_DELETED:
        case ASM_EDITED:
            return {
                ...state,
                needReload: true,
                flowState: asmAdministrationMenuViewFlowStates.externalSecurityList
            }
        case ASM_REFRESH_START:
        case ASM_REFRESH_FINISH:
            return {...state, refresh: action.refresh}
        default:
            return state;
    }
}