import {folderInitialState} from "../menuViews/folderInitialState";
import {FLOW_STATE_BROWSE_FOLDER, mailTemplateMenuViewFlowStates} from "../menuViews/flowStates";
import {folderStateReducer} from "../menuViews/folderStateReducer";
import SidebarItems from "../../../main/Main/Sidebar/SidebarItems";
import {FolderItemTypes} from "../../../main/FolderContent/FolderItemTypes";
import {
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM
} from "../../reduxTypes";


const initialState = {
    ...folderInitialState,
    flowState: FLOW_STATE_BROWSE_FOLDER,
    viewMailTemplateId: null
}

export function mailTemplatesMenuViewReducer(state = initialState, action) {

    if (action.itemType !== FolderItemTypes.systemMailTemplates) {
        return folderStateReducer(state, action, SidebarItems.admin.subItems.mailTexts, FolderItemTypes.systemMailTemplates)
    }

    switch (action.type) {

        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:

            return {
                ...state,
                flowState: mailTemplateMenuViewFlowStates.mailTemplateViewer,
                viewMailTemplateId: action.itemId,
            }

        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            return {
                ...state,
                flowState: mailTemplateMenuViewFlowStates.mailTemplateDesigner,
                viewMailTemplateId: action.itemId,
            }

        default:
            return folderStateReducer(state, action, SidebarItems.admin.subItems.mailTexts, FolderItemTypes.systemMailTemplates)
    }
}