import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
 
const CONTROLLER_URL = '/filter-instance';
const METHOD = 'POST';
const FILTER_INSTANCE_ADD_URL = CONTROLLER_URL + '/add';
const FILTER_INSTANCE_EDIT_URL = CONTROLLER_URL + '/edit';
const FILTER_INSTANCE_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const FILTER_INSTANCE_DELETE_URL = CONTROLLER_URL + '/delete';
const FILTER_INSTANCE_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const FILTER_INSTANCE_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const FILTER_INSTANCE_GET_URL = CONTROLLER_URL + '/get';
const FILTER_INSTANCE_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const FILTER_INSTANCE_GET_VALUES_URL = CONTROLLER_URL + '/get-values';
const FILTER_INSTANCE_GET_NODES_URL = CONTROLLER_URL + '/get-child-nodes';
const FILTER_INSTANCE_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const FILTER_INSTANCE_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const FILTER_INSTANCE_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';
const FILTER_INSTANCE_SEARCH_URL = CONTROLLER_URL + '/search';
const FILTER_INSTANCE_DEPENDENCIES_URL = CONTROLLER_URL + '/get-dependent-objects';
const FILTER_INSTANCE_COPY_OBJ = CONTROLLER_URL + '/copy';
const FILTER_INSTANCE_MOVE_OBJ = CONTROLLER_URL + '/change-folder';
const FILTER_INSTANCE_COPY_FOLDER = CONTROLLER_URL + '/copy-folder';

export default function FilterInstanceController(dataHub){

    this.add =  function (filterInstanceData, callback){

        return dataHub.requestService(FILTER_INSTANCE_ADD_URL, METHOD, filterInstanceData, callback);
    }

    this.edit =  function (filterInstanceData, callback){

        return dataHub.requestService(FILTER_INSTANCE_EDIT_URL, METHOD, filterInstanceData, callback);
    }

    this.addFolder = function (parentId, name, description, callback){
        const body = {
            description, 
            name, 
            parentId
        }
        return dataHub.requestService(FILTER_INSTANCE_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = {
            id, 
        }
        return dataHub.requestService(FILTER_INSTANCE_DELETE_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = {
            id, 
        }
        return dataHub.requestService(FILTER_INSTANCE_GET_URL, METHOD, body, callback);
    }
        
    this.deleteFolder = function (id, callback){
        const body = {
            id, 
        }
        return dataHub.requestService(FILTER_INSTANCE_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FILTER_INSTANCE_GET_FOLDER_URL, METHOD, body, callback, (data) => {dataHub.localCache.setFolderData(FolderItemTypes.filterInstance, data)});
    }

    this.renameFolder = function (description, id, name, callback){
        const body = {
            description,
            id, 
            name,
        }
        return dataHub.requestService(FILTER_INSTANCE_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getValues = function (body, callback){
        return dataHub.requestService(FILTER_INSTANCE_GET_VALUES_URL, METHOD, body, callback);
    }

    this.getChildNodes = function (body, callback){
        return dataHub.requestService(FILTER_INSTANCE_GET_NODES_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FILTER_INSTANCE_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(FILTER_INSTANCE_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(FILTER_INSTANCE_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }

    this.getDependencies = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(FILTER_INSTANCE_DEPENDENCIES_URL, METHOD, body, callback, dataHub.localCache.setDependenciesFolderData);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(FILTER_INSTANCE_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.filterInstance, data));
    }

    this.copyObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(FILTER_INSTANCE_COPY_OBJ, METHOD, body, callback);
    }

    this.moveObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(FILTER_INSTANCE_MOVE_OBJ, METHOD, body, callback);
    }

    this.copyFolder = function (destFolderId, folderIds, callback){
        const body = {
            destFolderId,
            folderIds,
            "permissionDestFolder": true,
            "recursionPermissions": true
        };
        return dataHub.requestService(FILTER_INSTANCE_COPY_FOLDER, METHOD, body, callback);
    }
}
