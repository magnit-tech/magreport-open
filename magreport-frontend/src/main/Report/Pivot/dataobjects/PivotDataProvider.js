import dataHub from 'ajax/DataHub'

import {FieldsLists} from './FieldsLists'
import {TableData} from './TableData'


/**
 * 
 * @param {*} jobId - id задания
 * @param {*} onTableDataReady - обработчик события готовности данных
 * @param {*} onTableDataLoadFailed - обработчик события ошибки загрузки данных
 */
export default function PivotDataProvider(jobId, onTableDataReady, onTableDataLoadFailed) {

    let fieldsLists = new FieldsLists();
    let tableData = new TableData();
    let fieldIdToNameMapping = {};

    // Id последнего запроса
    let requestId = 0;

    // Статус выполнения загрузки: 0 - загрузка не выполняется, 1 - выполняется упреждающая загрузка, 
    // 2 - выполняется обычная загрузка для смещенного окна, 3 - выполняется обычная загрузка для изменённых списков полей
    let loadingStatus = 0;

    /*
        Кэширование
    */
    let cacheTableData = new TableData();
    // Множители:
    // Множетилель на размеры кэшируемого окна
    const cacheFactor = 3;
    // Когда до края границы загруженной зоны остаются меньше, чем cachReloadFactor * count - выполняется новая загрузка
    const cacheReloadFactor = 0.5;

    // Запрошенное и возврашенное окно
    let reqWindow = new Window(0, 0, 0, 0);
    // Закэшированное окно
    let cacheWindow = new Window(0, 0, 0, 0);
    // Загружаемое окно
    let loadingWindow = new Window(0, 0, 0, 0);

    function buildCacheByReqWindow(reqWindow) {
        let columnCount = cacheFactor * reqWindow.columnCount;
        let leftPadding = Math.floor( (columnCount - reqWindow.columnCount) / 2 );
        let columnFrom = Math.max(reqWindow.columnFrom - leftPadding, 0);

        let rowCount = cacheFactor * reqWindow.rowCount;
        let topPadding = Math.floor( (rowCount - reqWindow.rowCount) / 2 );
        let rowFrom = Math.max(reqWindow.rowFrom - topPadding, 0);

        return new Window(columnFrom, columnCount, rowFrom, rowCount);

    }

    this.setFieldIdToNameMapping = (newFieldIdToNameMapping) => {
        fieldIdToNameMapping = newFieldIdToNameMapping;
    }

    /*
        Предоставление данных
    */

    // Обязательная загрузка новых данных для новых списков полей
    this.loadDataForNewFieldsLists = (newFieldsLists, columnFrom, columnCount, rowFrom, rowCount) => {
        fieldsLists = newFieldsLists;
        reqWindow = new Window(columnFrom, columnCount, rowFrom, rowCount);
        
        loadDataForRecWindow(3);
    }

    // Загрузка данных
    function loadDataForRecWindow(loadingType) {
        loadingWindow = buildCacheByReqWindow(reqWindow);
        let request = buildOlapRequest(fieldsLists, loadingWindow);
        requestId = dataHub.olapController.getCube(request, handleDataLoaded);
        loadingStatus = loadingType;
    }

    // Движение окна просмотра
    // Возвращает true если callback для получения данных был вызван сразу и false - если предполагается отложенный вызов callback
    this.changeWindow = (columnFrom, columnCount, rowFrom, rowCount) => {
        reqWindow = new Window(columnFrom, columnCount, rowFrom, rowCount);

        if(loadingStatus < 3){
            reqWindow.updateSizeByReturnedData(cacheTableData.totalColumns, cacheTableData.totalRows);

            if(cacheWindow.columnFrom <= reqWindow.columnFrom && reqWindow.columnFrom + reqWindow.columnCount <= cacheWindow.columnFrom + cacheWindow.columnCount &&
                cacheWindow.rowFrom <= reqWindow.rowFrom && reqWindow.rowFrom + reqWindow.rowCount <= cacheWindow.rowFrom + cacheWindow.rowCount){
    
                tableData = cacheTableData.subTable(reqWindow.columnFrom, reqWindow.columnCount, reqWindow.rowFrom, reqWindow.rowCount);
                if(tableData.metrics.length > 0) tableData.metrics.map( (v, index) => (fieldsLists.setDataType(index, v.dataType)) )
                onTableDataReady(tableData);
    
                proactiveLoadIfNeeded();
    
                return true;
            }
            else{
                loadDataForRecWindow(2);
                return false;
            }    
        }
        else{
            // То есть выполняется загрузка новых данных для новых списков полей - в этом случае нужно понять, если новое окно укладывается в загружаемое окно, 
            // то просто ничего не делаем, иначе - иниицируем новую загрузку
            if(reqWindow.columnFrom + reqWindow.columnCount <= loadingWindow.columnFrom + loadingWindow.columnCount &&
                loadingWindow.rowFrom <= reqWindow.rowFrom && reqWindow.rowFrom + reqWindow.rowCount <= loadingWindow.rowFrom + loadingWindow.rowCount){
                // ничего не делаем
            }
            else{
                this.loadDataForNewFieldsLists(fieldsLists, columnFrom, columnCount, rowFrom, rowCount);
            }
            return false;
        }
        
    }


    /*
        *****************************
        Проактивная загрузка окна
        *****************************
    */

    function proactiveLoadIfNeeded(){
        if(loadingStatus === 0){
            let rl = reqWindow.columnFrom;
            let rr = reqWindow.columnFrom + reqWindow.columnCount;
            let rw = reqWindow.columnCount;
            let rt = reqWindow.rowFrom;
            let rb = reqWindow.rowFrom + reqWindow.rowCount;
            let rh = reqWindow.rowCount;
            let cl = cacheWindow.columnFrom;
            let cr = cacheWindow.columnFrom + cacheWindow.columnCount;
            let ct = cacheWindow.rowFrom;
            let cb = cacheWindow.rowFrom + cacheWindow.rowCount;
            let leftSideApproaching = rw > 0 && cl > 0 && (rl - cl) / rw < cacheReloadFactor;
            let rightSideApproaching = rw > 0 && cr < tableData.totalColumns && (cr - rr) / rw < cacheReloadFactor;
            let topSideApproaching = rh > 0 && ct > 0 && (rt - ct) / rh < cacheReloadFactor;
            let botSideApproaching = rh > 0 && cb < tableData.totalRows && (cb - rb) / rh < cacheReloadFactor;
    
            if(leftSideApproaching || rightSideApproaching || topSideApproaching || botSideApproaching){
                loadDataForRecWindow(1);
            }    
        }
    }

    /*
    ****************
    *** Request
    ****************
    */

    function buildOlapRequest(fieldsLists, win){
        let rqst = {
            jobId : jobId,
            columnFields : fieldsLists.columnFields.map( (v) => (v.fieldId)),
            rowFields : fieldsLists.rowFields.map( (v) => (v.fieldId)),
            metrics : fieldsLists.metricFields.map( (v) => ({fieldId: v.fieldId, aggregationType : v.aggFuncName}) ),
            columnsInterval : {from: win.columnFrom, count: win.columnCount},
            rowsInterval : {from: win.rowFrom, count: win.rowCount}
        };

        if (fieldsLists.filterFields.length > 0) {
            rqst['filterGroup'] = {
                operationType: 'AND',
                invertResult: false,
                childGroups: [],
                filters: fieldsLists.filterFields.map((v) => ({
                    fieldId : v.fieldId,
                    filterType: v.filter.filterType,
                    invertResult: v.filter.invertResult,
                    values:  v.filter.values 
                })) 
            }
        }
        return (rqst)
    }

    function handleDataLoaded (response) {
        if(response.requestId === requestId){
            if(response.ok){
                cacheTableData = new TableData();

                cacheWindow = new Window(loadingWindow.columnFrom, loadingWindow.columnCount, loadingWindow.rowFrom, loadingWindow.rowCount);
                cacheWindow.updateSizeByReturnedData(response.data.totalColumns, response.data.totalRows);
                reqWindow.updateSizeByReturnedData(response.data.totalColumns, response.data.totalRows);

                cacheTableData.setDataFromResponse(fieldsLists, 
                    fieldIdToNameMapping, 
                    response, 
                    cacheWindow.columnFrom, 
                    cacheWindow.columnCount,
                    cacheWindow.rowFrom,
                    cacheWindow.rowCount
                );

                if(loadingStatus >= 2){
                    tableData = cacheTableData.subTable(reqWindow.columnFrom, reqWindow.columnCount, reqWindow.rowFrom, reqWindow.rowCount);
                    if(tableData.metrics.length > 0) tableData.metrics.map( (v, index) => (fieldsLists.setDataType(index, v.dataType)) ) 
                    onTableDataReady(tableData);
                }
            }
            else{
                if(loadingStatus >= 2){
                    onTableDataLoadFailed(response.data);
                }
            }
            loadingStatus = 0;
        }
    }
    
}

function Window(columnFrom, columnCount, rowFrom, rowCount) {
    this.columnFrom = columnFrom;
    this.columnCount = columnCount;
    this.rowFrom = rowFrom;
    this.rowCount = rowCount;

    this.updateSizeByReturnedData = (totalColumns, totalRows) => {
        if(totalColumns < this.columnFrom + this.columnCount){
            this.columnCount = totalColumns - this.columnFrom;
        }
        if(totalRows < this.rowFrom + this.rowCount){
            this.rowCount = totalRows - this.rowFrom;
        }
    }
}