import {FolderItemTypes} from "../../main/FolderContent/FolderItemTypes";

const CONTROLLER_URL = '/server-mail-template';
const METHOD = 'POST';

const SETTINGS_GET_MAIL_TEMPLATE = CONTROLLER_URL + '/get';
const SETTINGS_GET_TYPE_MAIL_TEMPLATE = CONTROLLER_URL + 'get-by-type';
const SETTINGS_GET_ALL_MAIL_TEMPLATE = CONTROLLER_URL + 'get-all';
const SETTINGS_EDIT_MAIL_TEMPLATE = CONTROLLER_URL + '/edit';
const SETTINGS_GET_MAIL_TEMPLATE_TYPE = CONTROLLER_URL + '/type-get';
const SETTINGS_GET_ALL_MAIL_TEMPLATE_TYPE = CONTROLLER_URL + '/type-get-all';


export default function ServerMailTemplateController(dataHub) {

    this.getMailTemplate = function (id, callback) {
        const body = {id,};
        return dataHub.requestService(SETTINGS_GET_MAIL_TEMPLATE, METHOD, body, callback)
    }

    this.getAllMailTemplate = function (callback) {
        const body = {};
        return dataHub.requestService(SETTINGS_GET_ALL_MAIL_TEMPLATE, METHOD, body, callback)
    }

    this.getMailTemplateByType = function (callback) {
        const body = {};
        return dataHub.requestService(SETTINGS_GET_TYPE_MAIL_TEMPLATE, METHOD, body, callback)
    }

    this.editMailTemplate = function (id, name, description, subject, body, callback) {
        const request = {
            id,
            name,
            description,
            subject,
            body
        };
        return dataHub.requestService(SETTINGS_EDIT_MAIL_TEMPLATE, METHOD, request, callback)
    }


    this.getMailTemplateType = function (id, callback) {

        const body = {
            id,
        };

        function modifyResponse(resp) {
            if (resp.ok) {
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
            SETTINGS_GET_MAIL_TEMPLATE_TYPE,
            METHOD,
            body,
            resp => callback(modifyResponse(resp)),
            data => dataHub.localCache.setFolderData(FolderItemTypes.systemMailTemplates, {
                ...data,
                childFolders: data.childTypes
            }));

    }

    this.getAllMailTemplateType = function (callback) {
        const body = {};
        return dataHub.requestService(SETTINGS_GET_ALL_MAIL_TEMPLATE_TYPE, METHOD, body, callback);
    }


}
