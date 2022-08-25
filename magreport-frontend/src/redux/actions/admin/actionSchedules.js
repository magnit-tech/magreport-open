import store from 'redux/store';
import dataHub from 'ajax/DataHub';

import { TASK_SWITCH, TASK_SWITCHED, TASK_SWITCH_FAILED } from '../../reduxTypes'

export function actionScheduleTaskSwitch(itemsType, taskIndex, taskId){
    dataHub.scheduleController.taskSwitch(taskId, m => handleScheduleTaskSwitchResponse(itemsType, taskIndex, taskId, m))
    return {
        type: TASK_SWITCH,
        itemsType,
        taskIndex,
        taskId
    }
}

function handleScheduleTaskSwitchResponse(itemsType, taskIndex, taskId, m){
    let type = m.ok ? TASK_SWITCHED : TASK_SWITCH_FAILED
    store.dispatch({
        type,
        itemsType,
        taskIndex,
        taskId,
        status: m.data
    })
}