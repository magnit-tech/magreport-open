import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

const CONTROLLER_URL = "/schedule";
const METHOD = "POST";
const SCHEDULE_ADD_URL = CONTROLLER_URL + "/add";
const SCHEDULE_ADD_DATE_URL = CONTROLLER_URL + "/add-date";
const SCHEDULE_DELETE_URL = CONTROLLER_URL + "/delete";
const SCHEDULE_EDIT_URL = CONTROLLER_URL + "/edit";
const SCHEDULE_GET_URL = CONTROLLER_URL + "/get";
const SCHEDULE_GET_ALL_URL = CONTROLLER_URL + "/get-all";
const SCHEDULE_GET_TYPES_URL = CONTROLLER_URL + "/get-types";
const SCHEDULE_TASK_ADD_URL = CONTROLLER_URL + "/task-add";
const SCHEDULE_TASK_EDIT_URL = CONTROLLER_URL + "/task-edit";
const SCHEDULE_TASK_DELETE_URL = CONTROLLER_URL + "/task-delete";
const SCHEDULE_TASK_GET_URL = CONTROLLER_URL + "/task-get";
const SCHEDULE_TASK_GET_ALL_URL = CONTROLLER_URL + "/task-get-all";
const SCHEDULE_TASK_MANUAL_START_URL = CONTROLLER_URL + "/task-manual-start";
const SCHEDULE_TASK_RUN_URL = CONTROLLER_URL + "/task-run";
const SCHEDULE_TASK_SWITCH_URL = CONTROLLER_URL + "/task-switch";
const SCHEDULE_TASK_GET_LINK_URL = CONTROLLER_URL + "/task-get-manual-link";

//TODO: add /prolongation
export default function ScheduleController(dataHub) {

    this.add = function (name, description, parameters, callback) {
        const body = {
            ...parameters,
            name,
            description,
        };
        return dataHub.requestService(SCHEDULE_ADD_URL, METHOD, body, callback);
    };

    this.addDate = function (countDays, callback) {
        const body = {
            countDays
        };
        return dataHub.requestService(SCHEDULE_ADD_DATE_URL, METHOD, body, callback);
    };

    this.delete = function (id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(SCHEDULE_DELETE_URL, METHOD, body, callback);
    };

    this.edit = function (id, name, description, parameters, callback) {
        const body = {
            ...parameters,
            id,
            name,
            description
        };
        return dataHub.requestService(SCHEDULE_EDIT_URL, METHOD, body, callback);
    }

    this.get = function (id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(SCHEDULE_GET_URL, METHOD, body, callback);
    }

    this.getAll = function (callback) {
        const body = "";
        return dataHub.requestService(SCHEDULE_GET_ALL_URL, METHOD, body, callback,
            (data) => dataHub.localCache.setFolderData(FolderItemTypes.schedules,
                {schedules: data, childFolders: []}));
    }

    this.getTypes = function(callback) {
        const body = "";
        return dataHub.requestService(SCHEDULE_GET_TYPES_URL, METHOD, body, callback);
    }

    this.taskAdd = function (data, callback) {
        const body = {
            ...data
        };
        return dataHub.requestService(SCHEDULE_TASK_ADD_URL, METHOD, body, callback);
    };

    this.taskEdit = function(data, callback) {
        const body = {
            ...data
        }
        return dataHub.requestService(SCHEDULE_TASK_EDIT_URL, METHOD, body, callback);
    }

    this.taskDelete = function (id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(SCHEDULE_TASK_DELETE_URL, METHOD, body, callback);
    };

    this.taskGet = function (id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(SCHEDULE_TASK_GET_URL, METHOD, body, callback);
    }

    this.taskGetAll = function (callback) {
        const body = "";
        return dataHub.requestService(SCHEDULE_TASK_GET_ALL_URL, METHOD, body, callback,
            (data) => dataHub.localCache.setFolderData(FolderItemTypes.scheduleTasks,
                {scheduleTasks: data, childFolders: []})
        );
    }

    this.taskManualStart = function(id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(SCHEDULE_TASK_MANUAL_START_URL, METHOD, body, callback);
    }

    this.taskRun = function (id, callback) {
        const body = {
            id
        };
        return dataHub.requestService(SCHEDULE_TASK_RUN_URL, METHOD, body, callback);
    };

    this.taskSwitch = function (id, callback) {
        const body = {
            id
        };
        return dataHub.requestService(SCHEDULE_TASK_SWITCH_URL, METHOD, body, callback);
    };
    this.taskGetManualLink = function (callback) {
        const body = "";
        return dataHub.requestService(SCHEDULE_TASK_GET_LINK_URL, METHOD, body, callback);
    };

}
