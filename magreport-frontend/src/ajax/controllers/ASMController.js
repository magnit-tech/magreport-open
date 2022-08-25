import {FolderItemTypes} from "../../main/FolderContent/FolderItemTypes";

const CONTROLLER_URL = "/asm-securities";
const METHOD = "POST";
const ASM_ADD_URL = CONTROLLER_URL + "/add";
const ASM_EDIT_URL = CONTROLLER_URL + "/edit";
const ASM_DELETE_URL = CONTROLLER_URL + "/delete";
const ASM_GET_URL = CONTROLLER_URL + "/get";
const ASM_GET_ALL_URL = CONTROLLER_URL + "/get-all";
const ASM_REFRESH_URL = CONTROLLER_URL + "/refresh";

export default function ASMController(dataHub) {

    this.add = function(roleTypeId, name, description, securitySources, callback) {
        const body = {
            roleTypeId,
            name,
            description,
            securitySources,
        };
        return dataHub.requestService(ASM_ADD_URL, METHOD, body, callback);
    };

    this.edit = function(id, roleTypeId, name, description, securitySources, callback) {
        const body = {
            id,
            roleTypeId,
            name,
            description,
            securitySources,
        };
        return dataHub.requestService(ASM_EDIT_URL, METHOD, body, callback);
    }

    this.delete = function(id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(ASM_DELETE_URL, METHOD, body, callback);
    };

    this.get = function(id, callback) {
        const body = {
            id,
        };
        return dataHub.requestService(ASM_GET_URL, METHOD, body, callback);
    }

    this.getAll = function(callback) {
        const body = "";
        return dataHub.requestService(ASM_GET_ALL_URL, METHOD, body, callback,
            (data) => dataHub.localCache.setFolderData(FolderItemTypes.asm,
                {asm: data, childFolders: []}));
    }

    this.refresh = function(idList, callback) {
        const body = {
            idList
        }
        return dataHub.requestService(ASM_REFRESH_URL, METHOD, body, callback);
    }

}
