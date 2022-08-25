const CONTROLLER_URL = '/users';
const USER_SET_STATUS_URL = CONTROLLER_URL + '/set-status';
const USER_GET_ONE_URL = CONTROLLER_URL + '/get-one';
const USER_WHO_AM_I_URL = CONTROLLER_URL + '/who-am-i';
const USER_LOGOFF_URL = CONTROLLER_URL + '/logoff';
const USER_LOGOFF_ALL_URL = CONTROLLER_URL + '/logoff-all';
const USER_GET_BY_ROLE_URL = CONTROLLER_URL + '/get-by-role';
const USER_GET_ACTUAL = CONTROLLER_URL + '/actual';
const METHOD = 'POST';

export default function UserController(dataHub){

    this.users = function (callback){
        const body = {};
        return dataHub.requestService(CONTROLLER_URL, METHOD, body, callback);
    }

    this.getActualUsers = function (callback){
        const body = {};
        return dataHub.requestService(USER_GET_ACTUAL, METHOD, body, callback);
    }

    this.setStatus = function (status, userNames, callback){
        const body = {
            status,
            userNames,
        };
        return dataHub.requestService(USER_SET_STATUS_URL, METHOD, body, callback);
    }

    this.logoff = function (userNames, callback){
        const body = userNames
        return dataHub.requestService(USER_LOGOFF_URL, METHOD, body, callback);
    }

    this.logoffAll = function (callback){
        const body = {}
        return dataHub.requestService(USER_LOGOFF_ALL_URL, METHOD, body, callback);
    }

    this.getOne = function (userName, callback){
        const body = {userName: userName}
        return dataHub.requestService(USER_GET_ONE_URL, METHOD, body, callback, dataHub.localCache.setUserInfo);
    }

    this.whoAmI = function (callback){
        const body = {}
        return dataHub.requestService(USER_WHO_AM_I_URL, METHOD, body, callback, dataHub.localCache.setUserInfo);
    }

    this.getUserByRole = function (roleId,callback){
        const body = {id: roleId}
        return dataHub.requestService(USER_GET_BY_ROLE_URL, METHOD, body, callback);
    }
}


