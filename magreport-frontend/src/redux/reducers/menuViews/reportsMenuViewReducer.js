import {FLOW_STATE_BROWSE_FOLDER, reportsMenuViewFlowStates} from './flowStates';
import {FOLDER_CONTENT_ITEM_CLICK, FOLDER_CONTENT_ADD_ITEM_CLICK} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function reportsMenuViewReducer(state = initialState, action){
    switch(action.type){
        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.report){
                return{
                    ...state,
                    flowState : reportsMenuViewFlowStates.startReport,
                    reportId : action.itemId
                }
            }
            else{
                return state;
            }
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if(action.itemsType === FolderItemTypes.report){
                return{
                    ...state,
                    flowState : reportsMenuViewFlowStates.pudlishReportDesigner,
                    editReportId : null
                }
            }
            else{
                return state;
            }
        default:
            return folderStateReducer(state, action, SidebarItems.reports, FolderItemTypes.report);
    }
}
