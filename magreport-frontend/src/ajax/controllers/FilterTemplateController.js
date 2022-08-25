import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/filter-template';
const METHOD = 'POST';
const FILTER_TEMPLATE_ADD_URL = CONTROLLER_URL + '/add';
const FILTER_TEMPLATE_EDIT_URL = CONTROLLER_URL + '/edit';
const FILTER_TEMPLATE_GET_URL = CONTROLLER_URL + '/get';
const FILTER_TEMPLATE_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const FILTER_TEMPLATE_DELETE_URL = CONTROLLER_URL + '/delete';
const FILTER_TEMPLATE_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const FILTER_TEMPLATE_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const FILTER_TEMPLATE_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const FILTER_TEMPLATE_GET_FILTER_TYPES_URL = CONTROLLER_URL + '/get-filter-types';
const FILTER_TEMPLATE_GET_FIELD_TYPES_URL = CONTROLLER_URL + '/get-field-types';
const FILTER_TEMPLATE_GET_OPERATION_TYPES_URL = CONTROLLER_URL + '/get-operation-types';
const FILTER_TEMPLATE_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const FILTER_TEMPLATE_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const FILTER_TEMPLATE_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';
const FILTER_TEMPLATE_SEARCH_URL = CONTROLLER_URL + '/search'

export default function FilterTemplaterController(dataHub){

    this.add = function (description, fields, folderId, id, name, supportedOperations, type, callback){
        const body = { 
            description, 
            fields, 
            folderId, 
            id, 
            name, 
            supportedOperations, 
            type,
        }
        return dataHub.requestService(FILTER_TEMPLATE_ADD_URL, METHOD, body, callback);
    }

    this.edit = function (description, fields, folderId, id, name, supportedOperations, type, callback){
        const body = { 
            description, 
            fields, 
            folderId, 
            id, 
            name, 
            supportedOperations, 
            type,
        }
        return dataHub.requestService(FILTER_TEMPLATE_EDIT_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(FILTER_TEMPLATE_GET_URL, METHOD, body, callback);
    }

    this.addFolder = function (parentId, name, description, callback){
        const body = { 
            description, 
            name, 
            parentId,
        }
        return dataHub.requestService(FILTER_TEMPLATE_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(FILTER_TEMPLATE_DELETE_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = { 
            id, 
        }
        return dataHub.requestService(FILTER_TEMPLATE_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FILTER_TEMPLATE_GET_FOLDER_URL, METHOD, body, callback, (data) => {dataHub.localCache.setFolderData(FolderItemTypes.filterTemplate, data)});
    }

    this.renameFolder = function (description, id, name, callback){
        const body = { 
            description,
            id, 
            name,
        }
        return dataHub.requestService(FILTER_TEMPLATE_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getFieldTypes = function (callback){
        const body = {}
        return dataHub.requestService(FILTER_TEMPLATE_GET_FIELD_TYPES_URL, METHOD, body, callback);
    }

    this.getFilterTypes = function (callback){
        const body = {}
        return dataHub.requestService(FILTER_TEMPLATE_GET_FILTER_TYPES_URL, METHOD, body, callback);
    }

    this.getOperationTypes = function (callback){
        const body = {}
        return dataHub.requestService(FILTER_TEMPLATE_GET_OPERATION_TYPES_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FILTER_TEMPLATE_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(FILTER_TEMPLATE_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(FILTER_TEMPLATE_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(FILTER_TEMPLATE_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.filterTemplate, data));
    }
}
