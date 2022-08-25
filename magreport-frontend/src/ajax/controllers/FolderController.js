import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/folder';
const METHOD = 'POST';
const FOLDER_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const FOLDER_ADD_REPORT_URL = CONTROLLER_URL + '/add-report';
const FOLDER_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const FOLDER_DELETE_REPORT_URL = CONTROLLER_URL + '/delete-report';
const FOLDER_GET_URL = CONTROLLER_URL + '/get-folder';
const FOLDER_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const FOLDER_SET_PERMISSION_URL = CONTROLLER_URL + '/set-permission';
const FOLDER_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const FOLDER_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const FOLDER_SEARCH_URL = CONTROLLER_URL + '/search';
const FOLDER_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';

export default function FolderController(dataHub){

    this.addFolder = function (parentId, name, description, callback){
        const body = {
            description,
            name,
            parentId,
        };
        return dataHub.requestService(FOLDER_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.addReport = function (folderId, reportId, callback){
        const body = {
            folderId,
            reportId,
        };
        return dataHub.requestService(FOLDER_ADD_REPORT_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FOLDER_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.delete = function (folderId, reportId, callback){
        const body = {
            folderId,
            reportId,
        };
        return dataHub.requestService(FOLDER_DELETE_REPORT_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FOLDER_GET_URL, METHOD, body, callback, (data) => {dataHub.localCache.setFolderData(FolderItemTypes.report, data)});
    }

    this.renameFolder = function (description, id, name, callback){
        const body = {
            description,
            id,
            name,
        };
        return dataHub.requestService(FOLDER_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.setPermission = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(FOLDER_SET_PERMISSION_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FOLDER_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(FOLDER_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(FOLDER_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.report, data));
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(FOLDER_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }
}
