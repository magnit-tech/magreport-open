import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

const CONTROLLER_URL = '/theme';
const METHOD = 'POST';
const THEME_ADD_URL = CONTROLLER_URL + '/add';
const THEME_EDIT_URL = CONTROLLER_URL + '/edit';
const THEME_GET_URL = CONTROLLER_URL + '/get';
const THEME_GET_ALL_URL = CONTROLLER_URL + '/get-all';
const THEME_DELETE_URL = CONTROLLER_URL + '/delete';


export default function ThemeController(dataHub){

    this.add = function (data, description, favorite, id, name, typeId, userId,  callback){

        const body = {
            data, 
            description, 
            favorite, 
            id, 
            name, 
            typeId, 
            userId
        }
        return dataHub.requestService(THEME_ADD_URL, METHOD, body, callback);
    }

    this.edit = function (data, description, favorite, id, name, typeId, userId,  callback){

        const body = {
            data, 
            description, 
            favorite, 
            id, 
            name, 
            typeId, 
            userId
        }
        return dataHub.requestService(THEME_EDIT_URL, METHOD, body, callback);
    }

    this.get = function(id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(THEME_GET_URL, METHOD, body, callback);
    }

    this.getAll = function (callback){
        const body = ""
        return dataHub.requestService(THEME_GET_ALL_URL, METHOD, body, callback,
            (data) => dataHub.localCache.setFolderData(FolderItemTypes.theme,
            {theme: data, childFolders: []}));
            
    }

    this.delete = function (id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(THEME_DELETE_URL, METHOD, body, callback);
    };
}