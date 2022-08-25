const CONTROLLER_URL = '/report-job';
const METHOD = 'POST';
const JOB_ADD_URL = CONTROLLER_URL + '/add';
const JOB_GET_URL = CONTROLLER_URL + '/get';
const JOB_GET_ALL_USER_JOBS_URL = CONTROLLER_URL + '/get-all-jobs';
const JOB_GET_USER_JOBS_URL = CONTROLLER_URL + '/get-all-my-jobs';
const JOB_GET_DATA_PAGE = CONTROLLER_URL + '/get-data-page';
const JOB_EXCEL_REPORT = CONTROLLER_URL + '/get-excel-report';
const JOB_CANCEL_URL = CONTROLLER_URL + '/cancel';
const JOB_SQL_URL = CONTROLLER_URL + '/get-sql-query';

export default function ReportJobController(dataHub){

    this.add = function(reportId, parameters, callback){
        const body = {
            reportId : reportId,
            parameters : parameters
        }
        return dataHub.requestService(JOB_ADD_URL, METHOD, body, callback);
    }        

    this.getMyJobs = (callback) => {
        const body = {};
        return dataHub.requestService(JOB_GET_USER_JOBS_URL, METHOD, body , magrepResponse => handleExtReponse(magrepResponse, callback));
    }

    this.get = (jobId, callback) => {
        const body = {
            jobId : jobId
        }

        return dataHub.requestService(JOB_GET_URL, METHOD, body, callback, data => {dataHub.localCache.setJobInfo(data)});    
    }

    this.getDataPage = (jobId, rowsPerPage, pageNumber, callback) => {
        const body = {
            jobId : jobId,
            rowsPerPage : rowsPerPage,
            pageNumber : pageNumber
        }

        return dataHub.requestService(JOB_GET_DATA_PAGE, METHOD, body, callback);   
    }

    // this.getExcelReport = (jobId, callback) => {
    //     const body = {
    //         id : jobId
    //     };

    //     return dataHub.downloadFile(JOB_EXCEL_REPORT, METHOD, body, callback);          
    // }

    this.getExcelReport = (excelTemplateId, jobId, callback) => {
        const body = {
            excelTemplateId,
            id : jobId
        };
        return dataHub.requestService(JOB_EXCEL_REPORT, METHOD, body, callback);          
    }

    this.getAllUsersJobs = (callback) => {
        const body = {};
        return dataHub.requestService(JOB_GET_ALL_USER_JOBS_URL, METHOD, body , magrepResponse => handleExtReponse(magrepResponse, callback));   
    }

    this.jobCancel = (jobId, callback) => {
        const body = {
            jobId
        };

        return dataHub.requestService(JOB_CANCEL_URL, METHOD, body, callback);          
    }

    this.getSqlQuery = (jobId, callback) => {
        const body = {
            jobId
        };

        return dataHub.requestService(JOB_SQL_URL, METHOD, body, callback); 
    }

    function handleExtReponse(m, callback){
        let arr = [...m.data]
        arr.sort((a,b) => {
            if (a.created > b.created){
                return -1
            }
            return 1
        })
        m.data = {}
        m.data.jobs = arr
        for (let j of m.data.jobs){
            dataHub.localCache.setJobInfo(j)
        }
        callback(m)
    }
}