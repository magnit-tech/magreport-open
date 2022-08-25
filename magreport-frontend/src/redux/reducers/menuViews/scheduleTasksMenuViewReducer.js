import {FLOW_STATE_BROWSE_FOLDER, scheduleTasksMenuViewFlowStates} from './flowStates';
import {
    FOLDER_CONTENT_ADD_ITEM_CLICK,
    FOLDER_CONTENT_EDIT_ITEM_CLICK,
    FOLDER_CONTENT_ITEM_CLICK,
    VIEWER_EDIT_ITEM,
    VIEWER_VIEW_ITEM,
    TASK_SWITCHED
} from 'redux/reduxTypes';
import SidebarItems from 'main/Main/Sidebar/SidebarItems';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import {folderInitialState} from './folderInitialState';
import {folderStateReducer} from './folderStateReducer';

const initialState = {
    ...folderInitialState,
    flowState: FLOW_STATE_BROWSE_FOLDER,
}

export function scheduleTasksMenuViewReducer(state = initialState, action={}) {
    switch (action.type) {
        case VIEWER_VIEW_ITEM:
        case FOLDER_CONTENT_ITEM_CLICK:
            if (action.itemType === FolderItemTypes.scheduleTasks) {
                return {
                    ...state,
                    flowState: scheduleTasksMenuViewFlowStates.scheduleTasksViewer,
                    viewScheduleTaskId: action.itemId,
                };
            } else {
                return state;
            }
        case FOLDER_CONTENT_ADD_ITEM_CLICK:
            if (action.itemsType === FolderItemTypes.scheduleTasks) {
                return {
                    ...state,
                    flowState: scheduleTasksMenuViewFlowStates.scheduleTasksDesigner,
                    editScheduleTaskId: null
                }
            } else {
                return state;
            }
        case VIEWER_EDIT_ITEM:
        case FOLDER_CONTENT_EDIT_ITEM_CLICK:
            if (action.itemType === FolderItemTypes.scheduleTasks) {
                return {
                    ...state,
                    flowState: scheduleTasksMenuViewFlowStates.scheduleTasksDesigner,
                    editScheduleTaskId: action.itemId
                }
            } else {
                return state;
            }
            case TASK_SWITCHED:
                if(action.itemsType === FolderItemTypes.scheduleTasks){
                    let newState = {...state}
                    const fullArr = [...state.currentFolderData.scheduleTasks]
                    const filteredArr = [...state.filteredFolderData.scheduleTasks]

                    let newTask = {};

                    if (state.searchParams){
                        newTask = {...filteredArr[action.taskIndex], status: action.status}
                        let indexFullArr = fullArr.findIndex(item => item.id === action.taskId)
                        filteredArr.splice(action.taskIndex, 1, newTask)
                        fullArr.splice(indexFullArr, 1, newTask)
                        
                    }
                    else {
                        newTask = {...fullArr[action.taskIndex], status: action.status}
                        fullArr.splice(action.taskIndex, 1, newTask)
                        filteredArr.splice(action.taskIndex, 1, newTask)
                    }
                    newState.filteredFolderData.scheduleTasks = filteredArr;
                    newState.currentFolderData.scheduleTasks = fullArr;
                    return newState
                }
                else {
                    return state;
                }
    
        default:
            return folderStateReducer(state, action, SidebarItems.schedule.subItems.scheduleTasks, FolderItemTypes.scheduleTasks);
    }
}
