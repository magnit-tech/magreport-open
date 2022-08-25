const CONTROLLER_URL = '/server-settings';
const SETTINGS_SET_URL = CONTROLLER_URL + '/set';
const SETTINGS_GET_URL = CONTROLLER_URL + '/get';
const SETTINGS_GET_JOURNAL_URL = CONTROLLER_URL + '/get-journal';
const SETTINGS_GET_DATE_TIME_URL = CONTROLLER_URL + '/get-date-time';
const METHOD = 'POST';

export default function ServerSettingsController(dataHub){

    this.get = function (callback){
        const body = {};
        return dataHub.requestService(SETTINGS_GET_URL, METHOD, body, callback);
    }

    this.set = function (settingId, value, callback){
        const body = {
            settingId,
            value,
        };
        return dataHub.requestService(SETTINGS_SET_URL, METHOD, body, callback);
    }

    this.getJournal = function (settingId, callback){
        const body = {
            settingId,
        };
        return dataHub.requestService(SETTINGS_GET_JOURNAL_URL, METHOD, body, callback);
    }

    this.getFullJournal = function (callback){
        const body = {};
        return dataHub.requestService(SETTINGS_GET_JOURNAL_URL, METHOD, body, callback);
    }

    this.getDateTime = function (callback) {
        const body = {};
        return dataHub.requestService(SETTINGS_GET_DATE_TIME_URL, METHOD, body, callback);
    }
}


