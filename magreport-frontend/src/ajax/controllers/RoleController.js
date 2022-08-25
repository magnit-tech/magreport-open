import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

const CONTROLLER_URL = '/role';
const METHOD = 'POST';
const ADMIN_URL = '/admin'

const ROLE_ADD_URL = CONTROLLER_URL + '/add';
const ROLE_DELETE_URL = CONTROLLER_URL + '/delete';
const ROLE_EDIT_URL = CONTROLLER_URL + '/edit';
const ROLE_GET_URL = CONTROLLER_URL + '/get';
const ROLE_GET_ALL_URL = CONTROLLER_URL + '/get-all';
const ROLE_GET_TYPE_URL = CONTROLLER_URL + '/get-type';
const ROLE_GET_ALL_TYPES_URL = CONTROLLER_URL + '/get-all-types';
const ROLE_GET_USERS_URL = CONTROLLER_URL + '/get-users';
const ROLE_SET_USERS_URL = CONTROLLER_URL + '/set-users';
const ROLE_ADD_USERS_URL = CONTROLLER_URL + '/add-users';
const ROLE_DEL_USERS_URL = CONTROLLER_URL + '/delete-users';
const ROLE_GET_DOMAIN_GROUPS_URL = CONTROLLER_URL + '/get-domain-groups';
const ROLE_ADD_DOMAIN_GROUPS_URL = CONTROLLER_URL + '/add-domain-groups';
const ROLE_DEL_DOMAIN_GROUPS_URL = CONTROLLER_URL + '/delete-domain-groups';
const ROLE_SEARCH_URL = CONTROLLER_URL + '/search-roles';
const ROLE_GET_PERMITTED_FOLDERS_URL = ADMIN_URL + CONTROLLER_URL + '/get-permitted-folders';
const ROLE_ADD_PERMISSION = CONTROLLER_URL + ADMIN_URL + '/add-permission';
const ROLE_DELETE_PERMISSION = CONTROLLER_URL + ADMIN_URL + '/delete-permission';

export default function RoleController(dataHub){

    this.add = function (id, typeId, name, description, callback){
        const body = {
            id, 
            typeId, 
            name, 
            description
        };
        return dataHub.requestService(ROLE_ADD_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(ROLE_DELETE_URL, METHOD, body, callback);
    }

    this.edit = function (id, typeId, name, description, callback){
        const body = {
            id, 
            typeId, 
            name, 
            description
        };
        return dataHub.requestService(ROLE_EDIT_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(ROLE_GET_URL, METHOD, body, callback);
    }

    this.getAll = function (callback){
        const body = {
        };
        return dataHub.requestService(ROLE_GET_ALL_URL, METHOD, body, callback);
    }

    this.getType = function (id, callback){
        const body = {
            id,
        };

        function modifyResponse(resp){
            if (resp.ok){
                return {
                    ...resp, 
                    data: {
                        ...resp.data, 
                        childFolders: resp.data.childTypes
                    }
                }
            }
            return resp
        }

        return dataHub.requestService(
                ROLE_GET_TYPE_URL, 
                METHOD, 
                body, 
                resp => callback(modifyResponse(resp)), 
                data => dataHub.localCache.setFolderData(FolderItemTypes.roles, {...data, childFolders: data.childTypes}));
    }

    this.getAllTypes = function(callback) {
        const body = {};

        return dataHub.requestService(
            ROLE_GET_ALL_TYPES_URL,
            METHOD,
            body,
            callback
        );
    }

    this.getFolder = this.getType

    this.getUsers = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(ROLE_GET_USERS_URL, METHOD, body, callback);
    }

    this.setUsers = function (id, users, callback){
        const body = {
            id,
            users
        };
        return dataHub.requestService(ROLE_SET_USERS_URL, METHOD, body, callback);
    }

    this.addUsers = function (id, users, callback){
        const body = {
            id,
            users
        };
        return dataHub.requestService(ROLE_ADD_USERS_URL, METHOD, body, callback);
    }

    this.deleteUsers = function (id, users, callback){
        const body = {
            id,
            users
        };
        return dataHub.requestService(ROLE_DEL_USERS_URL, METHOD, body, callback);
    }

    this.getDomainGroups = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(ROLE_GET_DOMAIN_GROUPS_URL, METHOD, body, callback);
    }

    this.addDomainGroups = function (id, domainGroups, callback){
        const body = {
            domainGroups,
            id
        };
        return dataHub.requestService(ROLE_ADD_DOMAIN_GROUPS_URL, METHOD, body, callback);
    }

    this.deleteDomainGroups = function (id, domainGroups, callback){
        const body = {
            id,
            domainGroups
        };
        return dataHub.requestService(ROLE_DEL_DOMAIN_GROUPS_URL, METHOD, body, callback);
    }

    this.search = function (likenessType, recursive, rootFolderId, searchString, callback){
        const body = {
            likenessType,
            recursive,
            rootFolderId, 
            searchString
        };

        return dataHub.requestService(ROLE_SEARCH_URL, METHOD, body, callback, data => dataHub.localCache.setSearchFolderData(FolderItemTypes.roles, data));
    }

    this.getPertmittedFolders = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(ROLE_GET_PERMITTED_FOLDERS_URL, METHOD, body, callback);
    }

    this.addPermission = function (folderId, permissions, roleId, type, callback){
        const body = {
            folderId, permissions, roleId, type
        }
        return dataHub.requestService(ROLE_ADD_PERMISSION, METHOD, body, callback);
    }

    this.deletePermission = function (folderId, roleId, type, callback){
        const body = {
            folderId, roleId, type
        }
        return dataHub.requestService(ROLE_DELETE_PERMISSION, METHOD, body, callback);
    }
}


