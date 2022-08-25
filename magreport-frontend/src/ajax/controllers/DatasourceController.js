import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/datasource';
const METHOD = 'POST';
const DATASOURCE_ADD_URL = CONTROLLER_URL + '/add';
const DATASOURCE_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const DATASOURCE_DELETE_URL = CONTROLLER_URL + '/delete';
const DATASOURCE_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const DATASOURCE_EDIT_URL = CONTROLLER_URL + '/edit'
const DATASOURCE_GET_URL = CONTROLLER_URL + '/get';
const DATASOURCE_GET_CATALOGS_URL = CONTROLLER_URL + '/get-catalogs';
const DATASOURCE_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const DATASOURCE_GET_OBJECT_FIELDS_URL = CONTROLLER_URL + '/get-object-fields';
const DATASOURCE_GET_OBJECTS_URL = CONTROLLER_URL + '/get-objects';
const DATASOURCE_GET_SCHEMAS_URL = CONTROLLER_URL + '/get-schemas';
const DATASOURCE_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const DATASOURCE_GET_TYPES_URL = CONTROLLER_URL + '/get-types';
const DATASOURCE_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const DATASOURCE_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const DATASOURCE_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';
const DATASOURCE_GET_DATASETS_URL = CONTROLLER_URL + '/get-datasets';
const DATASOURCE_SEARCH_URL = CONTROLLER_URL + '/search';
const DATASOURCE_CHECK = CONTROLLER_URL + '/check';
const DATASOURCE_CHECK_CONNECT = CONTROLLER_URL + '/check-connect';
const DATASOURCE_COPY_OBJ = CONTROLLER_URL + '/copy';
const DATASOURCE_MOVE_OBJ = CONTROLLER_URL + '/change-folder';
const DATASOURCE_COPY_FOLDER = CONTROLLER_URL + '/copy-folder';

export default function DatasourceController(dataHub){


    this.add = function (folderId, name, description, typeId, url, userName, password, poolSize, callback){
        const body = {
            folderId, 
            name, 
            description, 
            url,
            typeId,
            userName,
            password,
            poolSize,
        }
        return dataHub.requestService(DATASOURCE_ADD_URL, METHOD, body, callback);
    }

    this.addFolder = function (parentId, name, description, callback){
        const body = { 
            description,
            name,
            parentId,
        }
        return dataHub.requestService(DATASOURCE_ADD_FOLDER_URL, METHOD, body, callback)
    }

    this.delete = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASOURCE_DELETE_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASOURCE_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.edit = function (folderId, id, name, description, typeId, url, userName, password, poolSize, callback){
        const body = { 
            description,
            folderId,
            id,
            name,
            password,
            typeId, 
            url,
            userName,
            poolSize,
        }
        return dataHub.requestService(DATASOURCE_EDIT_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASOURCE_GET_URL, METHOD, body, callback);
    }

    this.getCatalogs = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASOURCE_GET_CATALOGS_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASOURCE_GET_FOLDER_URL, METHOD, body, callback, (data) => {dataHub.localCache.setFolderData(FolderItemTypes.datasource, data)});
    }

    this.getObjectFields = function (catalogName, id, objectName, schemaName, callback){
        const body = { 
            catalogName,
            id,
            objectName,
            schemaName,
        }
        return dataHub.requestService(DATASOURCE_GET_OBJECT_FIELDS_URL, METHOD, body, callback);
    }

    this.getObjects = function (catalogName,id, objectType, schemaName, callback){
        const body = { 
            catalogName,
            id,
            objectType,
            schemaName,
        }
        return dataHub.requestService(DATASOURCE_GET_OBJECTS_URL, METHOD, body, callback);
    }

    this.getSchemas = function (catalogName, id, callback){
        const body = { 
            catalogName,
            id,
        }
        return dataHub.requestService(DATASOURCE_GET_SCHEMAS_URL, METHOD, body, callback);
    }

    this.renameFolder = function (description, id, name, callback){
        const body = { 
            description,
            id,
            name,
        }
        return dataHub.requestService(DATASOURCE_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getTypes = function (callback){
        return dataHub.requestService(DATASOURCE_GET_TYPES_URL, METHOD, {}, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASOURCE_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(DATASOURCE_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(DATASOURCE_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }

    this.getDependencies = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(DATASOURCE_GET_DATASETS_URL, METHOD, body, callback, dataHub.localCache.setDependenciesFolderData);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };
        return dataHub.requestService(DATASOURCE_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.datasource, data));
    }

    this.check = function (id, callback){
        const body = { 
            id,
        }
        return dataHub.requestService(DATASOURCE_CHECK, METHOD, body, callback);
    }

    this.checkConnect = function (password, url, userName, callback){
        const body = { 
            password,
            url,
            userName
        }
        return dataHub.requestService(DATASOURCE_CHECK_CONNECT, METHOD, body, callback);
    }

    this.copyObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(DATASOURCE_COPY_OBJ, METHOD, body, callback);
    }

    this.moveObj = function (destFolderId, objIds, callback){
        const body = {
            destFolderId,
            objIds
        };
        return dataHub.requestService(DATASOURCE_MOVE_OBJ, METHOD, body, callback);
    }

    this.copyFolder = function (destFolderId, folderIds, callback){
        const body = {
            destFolderId,
            folderIds,
            "permissionDestFolder": true,
            "recursionPermissions": true
        };
        return dataHub.requestService(DATASOURCE_COPY_FOLDER, METHOD, body, callback);
    }
}
