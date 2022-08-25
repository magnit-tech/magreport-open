import ConfigLocal from "./config/Config-local";
import ConfigProd from "./config/Config-prod";
import MagrepResponse from "./MagrepResponse";
import LocalCache from './localcache/LocalCache';

import DatasetController from './controllers/DatasetController';
import DatasourceController from './controllers/DatasourceController';
import ExcelTemplateController from './controllers/ExcelTemplateController';
import FilterInstanceController from './controllers/FilterInstanceController';
import FilterTemplateController from './controllers/FilterTemplateController';
import FolderController from './controllers/FolderController';
import ReportController from './controllers/ReportController';
import ReportJobController from './controllers/ReportJobController';
import UserController from './controllers/UserController';
import RoleController from './controllers/RoleController';
import SecurityFilterController from './controllers/SecurityFilterController';
import AdController from './controllers/AdController';
import AdminController from './controllers/AdminController';
import ASMController from "./controllers/ASMController";
import ServerSettingsController from "./controllers/ServerSettingsController";
import FilterReportController from "./controllers/FilterReportController";
import ScheduleController from "./controllers/ScheduleController";
import ServerMailTemplateController from "./controllers/ServerMailTemplateController";
import OlapController from "./controllers/OlapController";
import EmailController from "./controllers/EmailController";
import ThemeController from "./controllers/ThemeController";

function DataHub(){
    this.authorization = '';
    this.localCache = new LocalCache();

    this.getLocalCache = () => {return this.localCache};

    const config = (process.env.NODE_ENV === 'production' ? new ConfigProd() : new ConfigLocal() );

    const BASE_URL = config.BASE_URL;
    const LOGIN_URL = config.LOGIN_URL;

    /*
        unautorizedHandler
    */
    
    this.setUnautorizedHandler = (unautorizedHandler) => {
        this.unautorizedHandler = unautorizedHandler;
    }
    /*
        Логин
        Отличается от общей схемы, поэтому сделан отдельно.
    */

    this.login = (login, password, callback) => {
        fetch(LOGIN_URL,{
                method: 'POST',
                body: '{"userName":"' + login + '", "password":"' + password +'"}'
            })
            .then( 
                (response) => {
                    let authorization = '';
                    let ok = response.ok;
                    if(ok){
                        authorization = response.headers.get('Authorization');
                        this.authorization = authorization;
                    }
                    let data = {
                        authtoken : authorization,
                        status : response.status
                    };
                    callback(new MagrepResponse(ok, data));
                }
            )
            .catch(error => {
                callback(new MagrepResponse(false, error.message));
            });
    }

    /*
        Общая схема выполнения запроса к сервису
    */
    this.requestService = (serviceUrl, method, body, callback, setCache) => {

        let request = {
            method: method,
            headers: {
                'Authorization' : this.authorization,
                'Content-Type' : 'application/json'
            }
        };

        let requestId = Date.now() + '-' + Math.floor(Math.random()*1000000);

        if(method === 'POST'){
            request.body = JSON.stringify(body);
        }

        fetch(BASE_URL + serviceUrl, request)
        .then(      
            (response) => { 
                let ok = response.ok;
                if(ok){
                    response.json().then(
                        (json) => {
                            if(json.success){
                                if(setCache){
                                    setCache(json.data);
                                }
                                let data = json.data
                                if (serviceUrl === '/report-job/get-excel-report'){
                                    data.urlFile = `${BASE_URL}/report-job/excel-report/`
                                }
                                callback(new MagrepResponse(true, data, requestId));
                            }
                            else{
                                callback(new MagrepResponse(false, json.message, requestId))
                            }
                        }
                    );
                }
                else{
                    if(response.status === 401){
                        if (this.unautorizedHandler){
                            this.unautorizedHandler();
                        }
                    }
                    else {
                        callback(new MagrepResponse(false, "Request failed. Response status: " + response.status, requestId));
                    }
                }
            }            
        )
        .catch(error => {
            callback(new MagrepResponse(false, error.message, requestId));
        });

        return requestId;
    }


    this.downloadFile = (serviceUrl, method, body, callback) => {

        let request = {
            method: method,
            headers: {
                'Authorization' : this.authorization,
                'Content-Type' : 'application/json',
                'Content-Length' : 0,
            }
        };  
        
        let requestId = Date.now()+ '-' + Math.floor(Math.random()*1000000);


        if(method === 'POST'){
            request.body = JSON.stringify(body);
        }    
        
        fetch(BASE_URL + serviceUrl, request)
        .then(response => {
            callback(response.ok ? response : '')
        })
        .catch(e => {
            callback('')
        });
        
        return requestId;
    } 

    this.uploadFile = (serviceUrl, method, data, callback) => {

        let request = {
            method: method,
            headers: {
                'Authorization' : this.authorization,
            }, 
            body: data
        };  
        
        let requestId = Date.now()+ '-' + Math.floor(Math.random()*1000000);

        fetch(BASE_URL + serviceUrl, request)
        .then(response => { 
                if(response.ok){
                    response.json()
                    .then(
                        json => {
                            if(json.success){
                                callback(new MagrepResponse(true, json.data, requestId));
                            }
                            else{
                                callback(new MagrepResponse(false, json.message, requestId))
                            }
                        }
                    );
                }
                else{
                    if(response.status === 401){
                        if (this.unautorizedHandler){
                            this.unautorizedHandler();
                        }
                    }
                    else {
                        response.json().then(json => {
                            callback(new MagrepResponse(false, json.message, requestId));
                        });
                    }
                }
            }            
        )
        .catch(error => {
            callback(new MagrepResponse(false, error.message, requestId));
        });

        return requestId;
    }
    /*
        Выполнение множества запросов с общим callback.
        requests - массив объектов
            {
                serviceUrl,
                method,
                body,
                callback,
                setCache
            }
        callback - общий callback по завершению всех запросов
        Если все запросы завершились успешно, то в общий callback передаётся
        magrepResponse последнего запроса (с максимальным индексом), если хоть один
        запрос завершился неуспешно, в callback передаётся magrepResponse неуспешного
        запроса с минимальным индексом.
    */

    this.doMultipleRequests = (requests, callback) => {
        let responses = new Array(requests.length);
        let successCnt = 0;
        let failedCnt = 0;
        let minFailedNum = requests.length;

        function processResponse(reqNum, magrepResponse){
            responses[reqNum] = magrepResponse;
            if(magrepResponse.ok){
                successCnt++;
            }
            else{
                failedCnt++;
                if(reqNum < minFailedNum){
                    minFailedNum = reqNum;
                }
            }
            if(requests[reqNum].callback){
                requests[reqNum].callback();
            }
            if(successCnt + failedCnt === requests.length){
                if(failedCnt === 0){
                    callback(responses[requests.length - 1]);
                }
                else{
                    callback(responses[minFailedNum]);
                }
            }
        }

        for(let i = 0; i < requests.length; i++){
            this.requestService(requests[i].serviceUrl, requests[i].method, requests[i].body, 
                                (magrepResponse)=>{
                                    processResponse(i, magrepResponse);
                                }, 
                                requests[i].setCache);
        }
    }

    /*
        Выполнить цепочку запросов - последовательно выполняет цепочку запросов, 
        предоставляя возможность формировать тело очередного запроса на основе ответов на предыдущие.
        Параметры:
            requestChain - массив объектов
                {
                    serviceUrl,         - URL сервиса
                    method,             - метод
                    buildBody,          - function(magrepReponseList) - magrepReponseList - список ответов 
                                            на все предыдущие запросы в цепочке.
                                            Возвращает объект body
                    callback,           - callback для данного запроса
                    setCache            - установка локального кэша по результатам запроса (может отсутствовать)
                },
            buildCommonResponse         - function(magrepResponseList) - magrepReponseList - список ответов
                                            на все запросы в цепочке. 
                                            Возвращает объект magrepResponse
            callback                    - callback по результату выполнения всей цепочки. 
                                            Если все запросы выполнились успешно, вызывается от результата buildCommonResponse
                                            Если был неуспешный запрос - вызывается от его magrepResponse

    */
    this.doChainRequests = (requestChain, buildCommonResponse, callback) => {
        if(requestChain.length > 0){
            let responses = [];

            let req = requestChain[requestChain.length - 1];

            let callStack = [];

            callStack.push( () => {
                this.requestService(req.serviceUrl, req.method, req.buildBody(responses), 
                        (magrepResponse) => {
                            responses.push(magrepResponse);
                            if(req.callback){
                                req.callback(magrepResponse);
                            }
                            if(magrepResponse.ok){
                                callback(buildCommonResponse(responses));
                            }
                            else{
                                callback(magrepResponse);
                            }
                        },
                        req.setCache
                );
            });
    
            for(let i = requestChain.length - 2; i >= 0; i--){
                let req = requestChain[i];
                let nextRequestCall = callStack[callStack.length - 1];
                callStack.push( () => {
                    this.requestService(req.serviceUrl, req.method, req.buildBody(responses),
                        (magrepResponse) => {
                            responses.push(magrepResponse);
                            if(req.callback){
                                req.callback(magrepResponse);
                            }
                            if(magrepResponse.ok){
                                nextRequestCall();
                            }
                            else{
                                callback(magrepResponse);
                            }
                        },
                        req.setCache
                    );
                });
            }

            callStack[callStack.length - 1].call();
        }
        else{
            callback(new MagrepResponse(true, {}));
        }
    }
    
    this.datasetController = new DatasetController(this);
    this.datasourceController = new DatasourceController(this);
    this.excelTemplateController = new ExcelTemplateController(this);
    this.filterInstanceController = new FilterInstanceController(this);
    this.filterTemplateController = new FilterTemplateController(this);                    
    this.folderController = new FolderController(this);
    this.reportController = new ReportController(this);
    this.reportJobController = new ReportJobController(this);   
    this.userController = new UserController(this);
    this.roleController = new RoleController(this);  
    this.securityFilterController = new SecurityFilterController(this);
    this.adController = new AdController(this);
    this.adminController = new AdminController(this);
    this.asmController = new ASMController(this);
    this.serverSettings = new ServerSettingsController(this);
    this.filterReportController = new FilterReportController(this);
    this.scheduleController = new ScheduleController(this);
    this.serverMailTemplateController = new ServerMailTemplateController(this);
    this.emailController = new EmailController(this);
    this.themeController = new ThemeController(this);
    this.olapController = new OlapController(this);
}

const dataHub = new DataHub();

export default dataHub;