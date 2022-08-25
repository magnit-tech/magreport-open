import dataHub from 'ajax/DataHub';

import {FLOW_STATE_BROWSE_FOLDER, favoritesMenuViewFlowStates} from './flowStates';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';

import {FOLDER_CONTENT_ITEM_CLICK,} from 'redux/reduxTypes';

const initialState = {
    ...folderInitialState,
    flowState : FLOW_STATE_BROWSE_FOLDER
}

export function favoritesMenuViewReducer(state = initialState, action){
    switch(action.type){

        case FOLDER_CONTENT_ITEM_CLICK:
            if(action.itemType === FolderItemTypes.favorites){
                const reportInfo = dataHub.localCache.getReportInfo(action.itemId)
                const reportName = reportInfo.name

                return{
                    ...state,
                    flowState : favoritesMenuViewFlowStates.startReport,
                    jobId : action.itemId,
                    reportName,
                    reportId: action.itemId,
                }
            }
            else{
                return state;
            }
        
        default:
            return folderStateReducer(state, action, SidebarItems.favorites, FolderItemTypes.favorites);
    }
}
