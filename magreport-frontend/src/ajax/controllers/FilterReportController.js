const CONTROLLER_URL = '/report-filter';
const METHOD = 'POST';
const FILTER_REPORTS_CHECK_VALUES_URL = CONTROLLER_URL + '/check-values';
const FILTER_REPORTS_GET_NODES_URL = CONTROLLER_URL+ '/get-child-nodes';

export default function FilterReportController(dataHub){

    this.checkValues =  function (filterId, values, callback){
        const body ={
            filterId,
            values,
        }
        return dataHub.requestService(FILTER_REPORTS_CHECK_VALUES_URL, METHOD, body, callback);
    }

    this.getChildNodes = function (body, callback){
        return dataHub.requestService(FILTER_REPORTS_GET_NODES_URL, METHOD, body, callback);
    }
}
