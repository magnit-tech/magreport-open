import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/dataset';
const METHOD = 'POST';
const DATASET_ADD_URL = CONTROLLER_URL + '/add';
const DATASET_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const DATASET_CREATE_FROM_METADATA_URL = CONTROLLER_URL + '/create-from-metadata';
const DATASET_DELETE_URL = CONTROLLER_URL + '/delete';
const DATASET_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const DATASET_EDIT_URL = CONTROLLER_URL + '/edit';
const DATASET_GET_URL = CONTROLLER_URL + '/get';
const DATASET_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const DATASET_GET_TYPES_URL = CONTROLLER_URL + '/get-types';
const DATASET_GET_DATA_TYPES_URL = CONTROLLER_URL + '/get-data-types';
const DATASET_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const DATASET_REFRESH_URL = CONTROLLER_URL + '/refresh';
const DATASET_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const DATASET_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const DATASET_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';
const DATASET_GET_DEPENDENCIES_URL = CONTROLLER_URL + '/get-dependent-objects';
const DATASET_SEARCH_URL = CONTROLLER_URL + '/search';
const DATASET_COPY_OBJ = CONTROLLER_URL + '/copy';
const DATASET_MOVE_OBJ = CONTROLLER_URL + '/change-folder';
const DATASET_COPY_FOLDER = CONTROLLER_URL + '/copy-folder';

export default function DatasetController(dataHub){

    this.add = function (folderId, name, description, datasourceId, catalogName, schemaName, objectName, typeId, callback){
        const body = { 
            folderId: folderId, 
            name: name, 
            description: description, 
            dataSourceId: datasourceId, 
            catalogName: catalogName, 
            schemaName: schemaName, 
            objectName: objectName, 
            typeId: typeId
        }
        return dataHub.requestService(DATASET_ADD_URL, METHOD, body, callback);
    }

    this.addFolder = function (parentId, name, description, callback){
        const body = { 
            description,
            name,
            parentId,
        }
        return dataHub.requestService(DATASET_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.createFromMetadata =  function (catalogName, dataSourceId, description, folderId, name, objectName, schemaName, typeId, callback){
        const body = { 
            catalogName,
            dataSourceId,
            description,
            folderId,
            name,
            objectName,
            schemaName,
            typeId,
        }
        return dataHub.requestService(DATASET_CREATE_FROM_METADATA_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = {
            id,
        }
        return dataHub.requestService(DATASET_DELETE_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASET_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.edit = function (folderId, id, name, description, datasourceId, catalogName, schemaName, objectName, typeId, fields, callback){
        const body = { 
            folderId: folderId, 
            id : id,
            name: name, 
            description: description, 
            dataSourceId: datasourceId, 
            catalogName: catalogName, 
            schemaName: schemaName, 
            objectName: objectName, 
            typeId: typeId,
            fields: fields,
        }
        return dataHub.requestService(DATASET_EDIT_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASET_GET_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASET_GET_FOLDER_URL, METHOD, body, callback, (data) => {dataHub.localCache.setFolderData(FolderItemTypes.dataset, data)});
    }

    this.getTypes = function (callback){
        return dataHub.requestService(DATASET_GET_TYPES_URL, METHOD, {}, callback);
    }

    this.getDataTypes = function(callback) {
        return dataHub.requestService(DATASET_GET_DATA_TYPES_URL, METHOD, {}, callback);
    }

    this.renameFolder = function (description, id, name, callback){
        const body = {
            description, 
            id,
            name,
        }
        return dataHub.requestService(DATASET_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASET_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(DATASET_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.refresh = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASET_REFRESH_URL, METHOD, body, callback);
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(DATASET_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }

    this.getDependencies = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASET_GET_DEPENDENCIES_URL, METHOD, body, callback, dataHub.localCache.setDependenciesFolderData);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(DATASET_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.dataset, data));
    }

    this.copyObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(DATASET_COPY_OBJ, METHOD, body, callback);
    }

    this.moveObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(DATASET_MOVE_OBJ, METHOD, body, callback);
    }

    this.copyFolder = function (destFolderId, folderIds, callback){
        const body = {
            destFolderId,
            folderIds,
            "permissionDestFolder": true,
            "recursionPermissions": true
        };
        return dataHub.requestService(DATASET_COPY_FOLDER, METHOD, body, callback);
    }
}
