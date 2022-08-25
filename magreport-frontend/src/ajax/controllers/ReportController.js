import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/report';
const METHOD = 'POST';
const REPORT_ADD_URL = CONTROLLER_URL + '/add';
const REPORT_EDIT_URL = CONTROLLER_URL + '/edit';
const REPORT_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const REPORT_GET_URL = CONTROLLER_URL + '/get';
const REPORT_DELETE_URL = CONTROLLER_URL + '/delete';
const REPORT_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const REPORT_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const REPORT_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const REPORT_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const REPORT_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const REPORT_SEARCH_URL = CONTROLLER_URL + '/search';
const REPORT_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';
const REPORT_GET_FAVORITES = CONTROLLER_URL + '/get-favorites';
const REPORT_ADD_FAVORITES = CONTROLLER_URL + '/add-favorites';
const REPORT_DELETE_FAVORITES = CONTROLLER_URL + '/delete-favorites';
const REPORT_GET_DEPENDENCIES_URL = '/folder/get-published-report';
const REPORT_GET_SCHEDULE_URL = CONTROLLER_URL + '/get-schedule';
const REPORT_COPY_OBJ = CONTROLLER_URL + '/copy';
const REPORT_MOVE_OBJ = CONTROLLER_URL + '/change-folder';
const REPORT_COPY_FOLDER = CONTROLLER_URL + '/copy-folder';

export default function ReportController(dataHub){

    this.add = function (dataSetId, description, folderId, id, name, requirementsLink, callback){
        const body = {
            dataSetId,
            description, 
            folderId, 
            id, 
            name,
            requirementsLink,
        }
        return dataHub.requestService(REPORT_ADD_URL, METHOD, body, callback);
    }

    this.edit = function (id, name, description, requirementsLink, folderId, fields, filterGroup, callback){
        const body = {
            id, 
            name, 
            description, 
            requirementsLink, 
            folderId, 
            fields, 
            filterGroup
        }
        return dataHub.requestService(REPORT_EDIT_URL, METHOD, body, callback);
    }

    this.addFolder = function (parentId, name, description, callback){
        const body = {
            description,
            name,
            parentId,
        };
        return dataHub.requestService(REPORT_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.get = function (reportId, jobId, callback){
        const body = { 
            id : reportId,
            jobId : jobId
        }
        return dataHub.requestService(REPORT_GET_URL, METHOD, body, callback);
    }

    this.getScheduleReport = function (reportId, scheduleTaskId, callback){
        const body = {
            id : reportId,
            scheduleTaskId : scheduleTaskId
        }
        return dataHub.requestService(REPORT_GET_SCHEDULE_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(REPORT_DELETE_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(REPORT_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(REPORT_GET_FOLDER_URL, METHOD, body, callback, data => {dataHub.localCache.setFolderData(FolderItemTypes.reportsDev, data)});
    }

    this.renameFolder = function (description, id, name, callback){
        const body = { 
            description, 
            id, 
            name, 
        }
        return dataHub.requestService(REPORT_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(REPORT_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(REPORT_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(REPORT_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.reportsDev, data));
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(REPORT_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }

    this.addFavorites = function (folderId, reportId, callback){
        const body = {
            folderId,
            reportId,
        };
        return dataHub.requestService(REPORT_ADD_FAVORITES, METHOD, body, callback);
    }

    this.deleteFavorites = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(REPORT_DELETE_FAVORITES, METHOD, body, callback);
    }

    this.getFavorites = function (callback){
        const body = {};
        return dataHub.requestService(REPORT_GET_FAVORITES, METHOD, body, callback, data => dataHub.localCache.setFolderData(FolderItemTypes.report, data));
    }

    this.getDependencies = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(REPORT_GET_DEPENDENCIES_URL, METHOD, body, callback, dataHub.localCache.setDependenciesFolderData);
    }

    this.copyObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(REPORT_COPY_OBJ, METHOD, body, callback);
    }

    this.moveObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(REPORT_MOVE_OBJ, METHOD, body, callback);
    }

    this.copyFolder = function (destFolderId, folderIds, callback){
        const body = {
            destFolderId,
            folderIds,
            "permissionDestFolder": true,
            "recursionPermissions": true
        };
        return dataHub.requestService(REPORT_COPY_FOLDER, METHOD, body, callback);
    }
}
