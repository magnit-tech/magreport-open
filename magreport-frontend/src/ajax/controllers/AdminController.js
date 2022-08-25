const CONTROLLER_URL = '/admin';
const METHOD = 'POST';

const ADMIN_GET_LOGS_URL = CONTROLLER_URL + '/logs';
const ADMIN_GET_OLAP_LOGS_URL = CONTROLLER_URL + '/olap-logs';



export default function AdminController(dataHub){

    this.getMainLogs = function (callback){
        const body = {};
        return dataHub.downloadFile(ADMIN_GET_LOGS_URL, METHOD, body, callback);
    }

    this.getOlapLogs = function (callback){
        const body = {};
        return dataHub.downloadFile(ADMIN_GET_OLAP_LOGS_URL, METHOD, body, callback);
    }

}

