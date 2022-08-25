const CONTROLLER_URL = '/excel-template';
const METHOD = 'POST';
const EXCEL_TEMPLATE_ADD_URL = CONTROLLER_URL + '/add';
const EXCEL_TEMPLATE_ADD_TO_REPORT_URL = CONTROLLER_URL + '/add-to-report';
const EXCEL_TEMPLATE_DELETE_URL = CONTROLLER_URL + '/delete';
const EXCEL_TEMPLATE_SET_DEFAULT_URL = CONTROLLER_URL + '/set-default';
const EXCEL_TEMPLATE_GET_URL = CONTROLLER_URL + '/get-to-report';
const EXCEL_TEMPLATE_GET_FILE_URL = CONTROLLER_URL + '/get-file';
const EXCEL_TEMPLATE_ADD_FOLDER_URL = CONTROLLER_URL + '/add-folder';
const EXCEL_TEMPLATE_DELETE_FOLDER_URL = CONTROLLER_URL + '/delete-folder';
const EXCEL_TEMPLATE_GET_FOLDER_URL = CONTROLLER_URL + '/get-folder';
const EXCEL_TEMPLATE_RENAME_FOLDER_URL = CONTROLLER_URL + '/rename-folder';
const EXCEL_TEMPLATE_GET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/get-permissions';
const EXCEL_TEMPLATE_SET_PERMISSIONS_URL = CONTROLLER_URL + '/admin/set-permissions';
const EXCEL_TEMPLATE_CHANGE_PARENT_FOLDER_URL = CONTROLLER_URL + '/change-parent-folder';

export default function ExcelTemplateController(dataHub){


    this.add = function (data, callback){
        dataHub.uploadFile(EXCEL_TEMPLATE_ADD_URL, METHOD, data, callback);
    }

    this.addToReport = function (excelTemplateId, reportId, callback){
        const body = {
            excelTemplateId,
            reportId,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_ADD_TO_REPORT_URL, METHOD, body, callback);
    }

    this.delete = function (id, callback){
        const body = {
            id,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_DELETE_URL, METHOD, body, callback);
    }

    this.get = function (id, callback){
        const body = {
            id,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_GET_URL, METHOD, body, callback);
    }

    this.getFile = function (id, callback) {
        const body = {
            id
        };

        return dataHub.downloadFile(EXCEL_TEMPLATE_GET_FILE_URL, METHOD, body, callback);
    }


    this.setDefault = function (excelTemplateId, reportId,  callback){
        const body = {
            excelTemplateId,
            reportId,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_SET_DEFAULT_URL, METHOD, body, callback);
    }

    this.addFolder = function (parentId, name, description, callback){
        const body = {
            description,
            name,
            parentId,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_ADD_FOLDER_URL, METHOD, body, callback);
    }

    this.deleteFolder = function (id, callback){
        const body = {
            id,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_DELETE_FOLDER_URL, METHOD, body, callback);
    }

    this.getFolder = function (id, callback){
        const body = {
            id,
        }
        return dataHub.requestService(EXCEL_TEMPLATE_GET_FOLDER_URL, METHOD, body, callback);
    }

    this.renameFolder = function (description, id, name, callback){
        const body = {
            description,
            id, 
            name, 
        }
        return dataHub.requestService(EXCEL_TEMPLATE_RENAME_FOLDER_URL, METHOD, body, callback);
    }

    this.getPermissions = function (id, callback){
        const body = {
            id,
        };
        return dataHub.requestService(EXCEL_TEMPLATE_GET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.setPermissions = function (folderId, roles, callback){
        const body = {
            folderId,
            roles,
        };
        return dataHub.requestService(EXCEL_TEMPLATE_SET_PERMISSIONS_URL, METHOD, body, callback);
    }

    this.changeParentFolder = function (id, parentId, callback){
        const body = {
            id,
            parentId,
        };
        return dataHub.requestService(EXCEL_TEMPLATE_CHANGE_PARENT_FOLDER_URL, METHOD, body, callback);
    }
}