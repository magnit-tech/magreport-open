import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/security-filter';
const METHOD = 'POST';
const SF_ADD_URL = CONTROLLER_URL + '/add';
const SF_EDIT_URL = CONTROLLER_URL + '/edit';
const SF_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const SF_GET_URL = CONTROLLER_URL + '/get';
const SF_DELETE_URL = CONTROLLER_URL + '/delete';
const SF_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const SF_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const SF_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const SF_GET_ROLE_SETTINGS_URL = CONTROLLER_URL + '/get-role-settings';
const SF_SET_ROLE_SETTINGS_URL = CONTROLLER_URL + '/set-role-settings';
const SF_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const SF_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const SF_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';
const SF_SEARCH_URL = CONTROLLER_URL + '/search-filter';

export default function ReportController(dataHub){

    this.add = function (id, folderId, name, description, operationType, filterInstanceId, dataSets, callback){
        const body = {
            id,
            folderId, 
            name, 
            description,
            operationType, 
            filterInstanceId, 
            dataSets,
        }
        return dataHub.requestService(SF_ADD_URL, METHOD, body, callback);
    }

    this.edit = function (id, folderId, name, description, operationType, filterInstanceId, dataSets, callback){
        const body = {
            id,
            folderId, 
            name, 
            description,
            operationType, 
            filterInstanceId, 
            dataSets,
        }
        return dataHub.requestService(SF_EDIT_URL, METHOD, body, callback);
    }    

    this.addFolder = function (parentId, name, description, callback){
        const body = {
            description,
            name,
            parentId,
        };
        return dataHub.requestService(SF_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(SF_GET_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(SF_DELETE_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(SF_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(SF_GET_FOLDER_URL, METHOD, body, callback, data => {dataHub.localCache.setFolderData(FolderItemTypes.securityFilters, data)});
    }

    this.renameFolder = function (description, id, name, callback){
        const body = { 
            description, 
            id, 
            name, 
        }
        return dataHub.requestService(SF_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getRoleSettings = function (id, callback){
        const body = { 
            id
        }
        return dataHub.requestService(SF_GET_ROLE_SETTINGS_URL, METHOD, body, callback);
    }

    this.setRoleSettings = function (securityFilterId, roleSettings, callback){
        const body = { 
            securityFilterId, 
            roleSettings 
        }
        return dataHub.requestService(SF_SET_ROLE_SETTINGS_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(SF_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(SF_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(SF_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(SF_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.securityFilters, data));
    }
}
