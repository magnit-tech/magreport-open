import React, { useState, useRef, useEffect } from 'react';
import { DragDropContext } from  'react-beautiful-dnd';
import Measure from 'react-measure';
import Dialog from '@material-ui/core/Dialog';
import { Box, List, ListItem, Modal, Typography, CircularProgress, Menu, MenuItem } from '@material-ui/core';
import Grid from '@material-ui/core/Grid';
import { useSnackbar } from 'notistack';

// magreport
import dataHub from 'ajax/DataHub'
import DataLoader from 'main/DataLoader/DataLoader'
import PivotFieldsList from './PivotFieldsList'
import PivotTable from './PivotTable'
import PivotTools from './PivotTools'
import {PivotCSS} from './PivotCSS'
import TableRangeControl from './TableRangeControl'
import PivotDataProvider from './dataobjects/PivotDataProvider'
import {TableData} from './dataobjects/TableData'
import PivotFilterModal from './PivotFilterModal'
import {AggFunc} from '../../FolderContent/JobFilters/JobStatuses'
import ThresholdDialog from './ThresholdDialog'
import FormattingDialog from './FormattingDialog'
import PivotConfiguration from './dataobjects/PivotConfiguration';

//UI
import ConfigDialog from './UI/ConfigDialog';
import ConfigSaveDialog from './UI/ConfigSaveDialog';

//utils
import validateSaveConfig from 'utils/validateSaveConfig';
import OlapConfig from './dataobjects/OlapConfig';


/**
 * @param {*} props.jobId - id задания
 * @param {*} props.reportId - id отчёта
 * @param {*} props.fullScreen - признак, является ли режим отображения сводной полноэкранным
 * @param {*} props.onRestartReportClick - function() - callback перезапуска отчёта
 * @param {*} props.onViewTypeChange - function() - callback смена вида с сводной на простую таблицу
 * @param {*} props.onFullScreen - function - callback полноэкранный режим
 */

export default function PivotPanel(props){

    const styles = PivotCSS();
    const { enqueueSnackbar } = useSnackbar();

    //hook для отстлеживания мыши
    const useMousePos = () => {
        const [mousePos, setMousePos] = useState({});
    
        useEffect(() => {
            const getMousePos = e => {
                const posX = e.clientX;
                const posY = e.clientY;
                setMousePos({ posX, posY });
            };
            if(watchForMouse) {
                document.addEventListener("mousemove", getMousePos);
            }
    
            return () => {
                document.removeEventListener("mousemove", getMousePos);
            };
        }, [watchForMouse]);
    
        return mousePos;
    };

    // отслеживание движения мыши
    const [ watchForMouse, setWatchForMouse ] = useState(false);
    const { posX, posY } = useMousePos();

    /*
        Конфигурация самого инструмента
    */

    // Видимость панелей с полями
    const [fieldsVisibility, setFieldsVisibility] = useState(true);

    function handleFieldsVisibility(value){
        setFieldsVisibility(value);
    }

    //Выводить только неиспользуемые поля или все поля
    const [onlyUnused, setOnlyUnused] = useState(true);

    function handleOnlyUnusedClick() {
        setOnlyUnused(!onlyUnused);
    }

    // Полноэкранный режим
    const [pivotFullScreen, setPivotFullScreen] = useState(false);

    function handleFullScreen(value){
        setPivotFullScreen(value);
    }

    const oldAndNewConfiguration = useRef({
        newFieldIndex : 0,
        oldConfiguration: null,
        newConfiguration: null
    })

    // модальное окно выбора агрегирующей функции
    const [aggModalOpen, setAggModalOpen] = useState({open: false, index: 0, type: 'add'});
    const [aggModalStyle, setAggModalStyle] = useState('');

    const aggFuncList = [];

    for (let f of AggFunc.keys()){
        aggFuncList.push(
            <ListItem 
                key={AggFunc.get(f)}
                disableGutters 
                dense 
                button 
                onClick = {() => {handleChooseAggForMetric(f, aggModalOpen.index)}}
            >
                <Box 
                    fontSize={12} 
                    fontWeight={"fontWeightMedium"}
                >
                    {AggFunc.get(f)}
                </Box>
            </ListItem>
        )
    }

    // модальное окно задания фильтра
    const [filterModalOpen, setFilterModalOpen] = useState({open: false, type: 'CONTAINS_CI', values: []});
    const [filterModalStyle, setFilterModalStyle] = useState('');

    // Индекс поля для которого настраивается фильтрация в списке полей фильтрации
    const [filterFieldIndex, setFilterFieldIndex] = useState(undefined);

    // фильтр куба
    //const [cubeFilter, setCubeFilter] = useState(null);// !!! В конфигурацию

    //Диалоговое окна условного форматирования
    const [thresholdDialogOpen, setThresholdDialogOpen] = useState(false);

    function handleThresholdDialog(value) {
        setThresholdDialogOpen(value);
    }
    /*
        Данные и отображение данных
    */

    // Объект конфигурации
    const [pivotConfiguration, setPivotConfiguration] = useState(new PivotConfiguration());

    // Предоставление данных для таблицы - dataProvider
    const dataProviderRef = useRef(new PivotDataProvider(props.jobId, handleTableDataReady, handleTableDataLoadFailed/*, initialColumnCount, initialRowCount*/));

    // загрузка данных таблицы: 0 - данные загружены успешно, 1 - идёт загрузка, 2 - загрузка завершена с ошибкой
    const [tableDataLoadStatus, setTableDataLoadStatus] = useState(0);

    const [tableDataLoadErrorMessage, setTableDataLoadErrorMessage] = useState("");

    // запрос данных и данные для отображения
    const [tableData, setTableData] = useState(new TableData());

    // Размер области для сводной таблицы
    const [tableSize, setTableSize] = useState({
        dimensions: {
            width: -1,
            height: -1,
            left: 0
        }
    }) 
    
    // Размер сводной таблицы
    const [innerTableSize, setInnerTableSize] = useState({
        dimensions: {
            width: 0,
            height: 0,
            left: 0
        }
     })
    
    function handleChangeInnerTableSize(value){
    
        if (value.dimensions.width >0 && value.dimensions.height >0 ){
            let cc = columnCount; 
            let rc = rowCount;
            if (tableSize.dimensions.width - value.dimensions.width < 0 && columnCount >3) {
                cc = columnCount - 1;
            }

            if (/*tableSize.dimensions.height - value.dimensions.height <0 &&* rowCount >3*/ true){
                let avgHeight = value.dimensions.height/(rowCount + 1 + pivotConfiguration.fieldsLists.columnFields.length)
                let h = Math.abs(value.dimensions.height - tableSize.dimensions.height);
                
                if (innerTableSize.dimensions.height < value.dimensions.height && value.dimensions.height > tableSize.dimensions.height){
                    
                    rc = rowCount - Math.floor(h/avgHeight);
                    rc=Math.max(3, Math.min( rc, rowCount));
                    //console.log('Высота увеличилась и нет места для увеличение');

                } else if (innerTableSize.dimensions.height > value.dimensions.height && value.dimensions.height < tableSize.dimensions.height){
                    //console.log('Высота уменьшилась и есть место')
                    rc = rowCount + Math.floor(h/avgHeight);
                }

                if (rc<3) {rc=3}
            }

            setInnerTableSize(value);

            if ((columnCount !== cc || rowCount !== rc ) && tableDataLoadStatus===0){
                setColumnCount(cc);
                setRowCount(rc);
                if(!dataProviderRef.current.changeWindow(pivotConfiguration.columnFrom, cc, pivotConfiguration.rowFrom, rc)){
                    setTableDataLoadStatus(1);
                }
            }
        }
    }

    const [columnCount, setColumnCount] = useState(0);
    const [rowCount, setRowCount] =useState(0);

    /*
        **************************************
        Загрузка данных
        **************************************
    */

    function handleMetadataLoaded(data){
        let fieldIdToNameMapping = new Map();
        for(let v of data.fields){
            fieldIdToNameMapping[v.id] = v.name;
        }
        dataProviderRef.current.setFieldIdToNameMapping(fieldIdToNameMapping);

        let newConfiguration = new PivotConfiguration(pivotConfiguration);
        newConfiguration.create({}, data);

        // Загрузка текущей конфигурации
        dataHub.olapController.getCurrentConfig(props.jobId, ({ok, data}) => ok && handleGetCurrentConfig(data, newConfiguration))
    }

    function handleTableDataReady(newTableData){
        setTableDataLoadStatus(0);
        setTableData(newTableData);
    }

    function handleTableDataLoadFailed(message){
        setTableDataLoadStatus(2);
        setTableDataLoadErrorMessage(message);
    }

    /*
        **************************************
        Конфигурация
        **************************************
    */

    // Объект OLAP-конфигурации
    const configOlap = useRef(new OlapConfig());

    // Обновление DataLoader
    const [resetComponentKey, setResetComponentKey] = useState(false);
    const resetDataLoader = () => setResetComponentKey(!resetComponentKey);

    const [avaibleConfigs, setAvaibleConfigs] = useState(null);

    const [showConfigDialog, setShowConfigDialog] = useState(false);
    const [showConfigSaveDialog, setShowConfigSaveDialog] = useState(false);

    // Взаимодействие с окнами конфигураций 
    function handleSetConfigDialog(action){
        switch(action) {

            case 'openConfigDialog':
                handleGetAvailableConfigs('ConfigDialog')
                break;

            case 'closeConfigDialog':
                setShowConfigDialog(false)
                break;

            case 'openConfigSaveDialog':
                handleGetAvailableConfigs('ConfigSaveDialog')
                break;

            case 'closeConfigSaveDialog':
                setShowConfigSaveDialog(false)
                break;

            default:
                return false
        }
    }

    // Получение текущей конфигурации
    function handleGetCurrentConfig(responseData, newConfiguration){

        configOlap.current.createLists(responseData)

        if (responseData.olapConfig.data.length > 0) {

            const configData = JSON.parse(responseData.olapConfig.data),
                  { columnFrom, columnCount, rowFrom, rowCount } = configData

            // Проверка на валидацию сохраненных полей конфигурации olapConfig с полями из Metadata
            const isSaveConfigValide = !validateSaveConfig(configData.fieldsLists, newConfiguration.fieldsLists.allFields)

            if (isSaveConfigValide) {
                newConfiguration.restore(configData)

                dataProviderRef.current.loadDataForNewFieldsLists(newConfiguration.fieldsLists, columnFrom, columnCount, rowFrom, rowCount);
    
                oldAndNewConfiguration.current = {
                    oldConfiguration: new PivotConfiguration(pivotConfiguration),
                    newConfiguration: new PivotConfiguration(newConfiguration)
                }
            } else {
                enqueueSnackbar('Не удалось загрузить конфигурацию', {variant : "error"});
                handleDeleteConfig({id: responseData.reportOlapConfigId})
            }

        }

        setPivotConfiguration(newConfiguration);
    }

    // Cохранение текущей конфигурации
    function handleSaveCurrentConfig(pivotConfigForSave){
        configOlap.current.saveCurrentConfig(pivotConfigForSave)
    }

    // Cохранение конфигурации (не текущей)
    function handleSaveConfig(obj){

        const { type } = obj

        // Cохранение новой конфигурации
        if ( type === 'saveNewConfig') {
            return configOlap.current.saveNewConfig(obj, props.reportId, (ok, name) => {
                if (ok) {
                    handleGetAvailableConfigs();
                    return enqueueSnackbar('Конфигурация "' + name + '" успешно сохранена' , {variant : "success"});
                }
    
                return enqueueSnackbar('Не удалось сохранить конфигурацию "' + name + '"', {variant : "error"});
            })
        }

        // Cохранение выбранной конфигурации
        return configOlap.current.saveExistingConfig(obj, props.reportId, (ok, name) => {
            if (ok) {
                handleGetAvailableConfigs();
                return enqueueSnackbar('Конфигурация "' + name + '" успешно обновлена' , {variant : "success"});
            }

            return enqueueSnackbar('Не удалось обновить конфигурацию "' + name + '"', {variant : "error"});
        })
    }

    // Получение списка доступных конфигураций пользователю
    function handleGetAvailableConfigs(type){
        let configsArr = [];

        dataHub.olapController.getAvailableConfigs(props.jobId, ({ok, data}) => {
            if (ok) {

                if (type === 'ConfigDialog') {
                    setAvaibleConfigs(data)
                    return setShowConfigDialog(true)
                } else if (type === 'ConfigSaveDialog') {
                    for (var key in data) {
                        data[key].map(item => configsArr.push(item))
                    }
                    setAvaibleConfigs(configsArr)
                    return setShowConfigSaveDialog(true)
                } 

                for (var itemKey in data) {
                    data[itemKey].map(item => configsArr.push(item))
                }

                return setAvaibleConfigs(configsArr)
            } 

        })
    }

    // Удаляем конфигурацию
    function handleDeleteConfig({id, name, type}) {
        if (type === 'ConfigDialog') {
            dataHub.olapController.deleteConfig(id, ({ok}) => {

                if (ok) {
                    handleGetAvailableConfigs(type);
                    return enqueueSnackbar('Конфигурация "' + name + '" успешно удалена' , {variant : "success"})
                }
    
                return enqueueSnackbar('Не удалось удалить конфигурацию "' + name + '"', {variant : "error"});
            })
        } else if (type === 'errorChooseConfig') {
            return dataHub.olapController.deleteConfig(id, ({ok}) => { ok && handleSetConfigDialog('closeConfigDialog')})
        } else {
            return dataHub.olapController.deleteConfig(id, () => { resetDataLoader() })
        }   
    }

    // При выборе определенной конфигураций и записываем olapConfigData в текущую конфигурацию => обновляем DataLoader
    function handleLoadCertainConfig({data, name, reportOlapConfigId}) {

        const isCertainConfigValide = !validateSaveConfig(JSON.parse(data).fieldsLists, pivotConfiguration.fieldsLists.allFields)

        if (isCertainConfigValide) {
            return configOlap.current.loadChosenConfig(data, (ok) => {
                if (ok) {
                    resetDataLoader()
                    handleSetConfigDialog('closeConfigDialog')
                    return enqueueSnackbar('Конфигурация "' + name + '" успешно загружена' , {variant : "success"});
                }
                return enqueueSnackbar('Не удалось загрузить конфигурацию "' + name + '". Некорректные данные.2', {variant : "error"});
            })
        }

        enqueueSnackbar('Не удалось загрузить конфигурацию. Некорректные данные.', {variant : "error"})
        return configOlap.current.loadChosenConfig(reportOlapConfigId)
    }

    // Сохраняем по умолчанию для отчета/задания
    function handleSaveConfigByDefault(item) {

        const { reportOlapConfigId } = item


        dataHub.olapController.setDefault(reportOlapConfigId, ({ok}) => {

            if (ok) {
                handleGetAvailableConfigs("ConfigDialog");
                return enqueueSnackbar('Конфигурация "' + item.olapConfig.name + '" успешно сохранена по умолчанию для отчета' , {variant : "success"});
            }

            return enqueueSnackbar('Не удалось сохранить конфигурацию "' + item.olapConfig.name + '" по умолчанию для отчета', {variant : "error"});
        })
    }
    
    /*
    **************************************
        Размещение метрик и слияние ячеек
    **************************************
    */

    function handleSetMetricPlacement(placeColumn){
        let newPivotConfiguration = new PivotConfiguration(pivotConfiguration);
        newPivotConfiguration.columnsMetricPlacement = placeColumn;
        setPivotConfiguration(newPivotConfiguration);
    }

    function handleSetMergeMode(mergeMode){
        let newPivotConfiguration = new PivotConfiguration(pivotConfiguration);
        newPivotConfiguration.mergeMode = mergeMode;
        setPivotConfiguration(newPivotConfiguration);
    }    

    // Drag&Drop
    function handleDragStart() {
        setWatchForMouse(true);
    }

    function handleDragEnd(result){

        const { destination, source, draggableId } = result;

        if(destination && 
          (destination.droppableId !== source.droppableId 
            || destination.index !== source.index 
            || destination.droppableId === "metricFields")){
            
            // Формируем новый объект конфигурации
            let newPivotConfiguration = new PivotConfiguration(pivotConfiguration);
            let destIndex = newPivotConfiguration.dragAndDropField(source.droppableId, destination.droppableId, source.index, destination.index);

            // Обрабатываем осоебенности использования полей в некоторых списках
            if(destination.droppableId === "metricFields"){
                // Если поле помещено в список метрик - выставляем метрику по умолчанию и открываем диалоговое окно выбора агрегирующей функции
                // Кроме того запоминаем старые списки полей, чтобы можно было восстановить их в случае отказа

                oldAndNewConfiguration.current = {
                    newFieldIndex : destIndex,
                    oldConfiguration: new PivotConfiguration(pivotConfiguration),
                    newConfiguration: new PivotConfiguration(newPivotConfiguration)
                }

                setAggModalStyle({
                    top: posY,
                    left: posX,
                    transform: `translate(-20%, -20%)`,
                })

                setAggModalOpen({open: true, index: destIndex, type: 'add'});
            }
            else if(destination.droppableId === "filterFields"){
                // Находим поле в новом списке полей и если в нём не задан объект фильтра задаём его пустым значением
                //let field = newPivotConfiguration.getFieldByIndex(destination.droppableId, destIndex);
                //setMovedField(field);

                setFilterFieldIndex(destIndex);

                /*if(field.filter === null){
                    field.filter = {};
                }*/

                // Сохраняем старую конфигурацию для отмены и открываем модальное окно задания фильтров

                oldAndNewConfiguration.current = {
                    newFieldIndex : destIndex,
                    oldConfiguration: new PivotConfiguration(pivotConfiguration),
                    newConfiguration: new PivotConfiguration(newPivotConfiguration)
                }
                setFilterModalStyle({
                    top: posY,
                    left: posX,
                    transform: `translate(0%, -20%)`,
                })
                setFilterModalOpen({...filterModalOpen, open: true, values: []});
            }

            // Если меняется состав полей сводной - обнуляем начальные столбец и строку
            if( !( destination.droppableId === 'filterFields' || destination.droppableId === "metricFields" 
                || (destination.droppableId === "unusedFields" && source.droppableId === "unusedFields") ) ) {

                newPivotConfiguration.setColumnFrom(0);
                newPivotConfiguration.setRowFrom(0);
                setTableDataLoadStatus(1);
                let cc = Math.ceil(tableSize.dimensions.width/85) - 1 - newPivotConfiguration.fieldsLists.rowFields.length ;
                let rc = Math.ceil(tableSize.dimensions.height/20) + 1 - newPivotConfiguration.fieldsLists.columnFields.length;

                if (destination.droppableId !== 'unusedFields' || source === 'filterFields'){
                    rc = Math.min(rc, rowCount);
                }
                setColumnCount(cc);
                setRowCount(rc);
                newPivotConfiguration.setColumnAndRowCount(cc, rc);
                dataProviderRef.current.loadDataForNewFieldsLists(newPivotConfiguration.fieldsLists, 0, cc, 0, rc);
            
                oldAndNewConfiguration.current = {
                    newFieldIndex : destIndex,
                    oldConfiguration: new PivotConfiguration(pivotConfiguration),
                    newConfiguration: new PivotConfiguration(newPivotConfiguration)
                }
                handleSaveCurrentConfig(newPivotConfiguration.stringify())

            }  

            //Если убираем поле из фильтров в неиспользуемые поля, очищаем
            /*if (source.droppableId === 'filterFields' && (destination.droppableId === 'unusedFields' || destination.droppableId === 'allFields')){
                setCubeFilter( {
                    operationType: 'AND',
                    invertResult: false,
                    childGroups: [],
                    filters: newPivotConfiguration.fieldsLists.filterFields.map((v) => ({
                        fieldId : v.fieldId,
                        filterType: v.filter.filterType,
                        invertResult: v.filter.invertResult,
                        values:  v.filter.values 
                    })) 
                })
            }*/
            setWatchForMouse(false);
            setPivotConfiguration(newPivotConfiguration);
        }
    }

    /*
        ***********************************************
        Модальное окно выбора агрегирующей функции
        ***********************************************
    */
      
    // Закрытие модального окна без выбора метрики
    function handleAggModalClose(){
        if (aggModalOpen.type === 'add'){
            setPivotConfiguration(oldAndNewConfiguration.current.oldConfiguration);
        }
        setAggModalOpen({...aggModalOpen, open: false});

    }
    function handleChooseAggForMetric(funcName, index){
        oldAndNewConfiguration.current.newConfiguration.setAggMetricByIndex(funcName, index);
        oldAndNewConfiguration.current.newConfiguration.setColumnFrom(0);
        oldAndNewConfiguration.current.newConfiguration.setRowFrom(0);
        oldAndNewConfiguration.current.newConfiguration.setColumnAndRowCount(columnCount, rowCount);
        setPivotConfiguration(new PivotConfiguration(oldAndNewConfiguration.current.newConfiguration));
        handleSaveCurrentConfig(oldAndNewConfiguration.current.newConfiguration.stringify())
        setAggModalOpen({...aggModalOpen, open: false});
        setTableDataLoadStatus(1);
        dataProviderRef.current.loadDataForNewFieldsLists(oldAndNewConfiguration.current.newConfiguration.fieldsLists, 0, columnCount, 0, rowCount);
    }

    /*
        ***************************************************
        Перемещение окна отображения по строкам и столбцам
        ***************************************************
    */

    function handleColumnFromChange(newColumnFrom) {

        let newConfiguration = new PivotConfiguration(pivotConfiguration);
        newConfiguration.setColumnFrom(newColumnFrom);
        setPivotConfiguration(newConfiguration);

        if(!dataProviderRef.current.changeWindow(newColumnFrom, columnCount, newConfiguration.rowFrom, rowCount)){
            setTableDataLoadStatus(1);
        }
    }

    function handleRowFromChange(newRowFrom) {

        let newConfiguration = new PivotConfiguration(pivotConfiguration);
        newConfiguration.setRowFrom(newRowFrom);;
        setPivotConfiguration(newConfiguration);

        if(!dataProviderRef.current.changeWindow(newConfiguration.columnFrom, columnCount, newRowFrom, rowCount)){
            setTableDataLoadStatus(1);
        }
    }

    /*
        ***************************************************
        Фильтрация значений
        ***************************************************
    */

    // Вызывается по нажатию на значение измерения в таблице
    function handleDimensionValueFilter(fieldId, fieldValue) {

        let newConfiguration = new PivotConfiguration(pivotConfiguration);
        let filterObject = {
            filterType: "IN_LIST",
            invertResult: false,
            values : [fieldValue]
        };
        newConfiguration.setFieldFilterByFieldId(fieldId, filterObject);
        setPivotConfiguration(newConfiguration);
        setTableDataLoadStatus(1);
        dataProviderRef.current.loadDataForNewFieldsLists(newConfiguration.fieldsLists, 0, columnCount, 0, rowCount);
    }

    // Закрытие модального окна без выбора значения фильтра
    function handleFilterModalClose(){
        setPivotConfiguration(oldAndNewConfiguration.current.oldConfiguration);
        setFilterModalOpen({...filterModalOpen, open: false});
    }
    
    // Вызывается по подтверждению задания объекта фильтрации в модальном окне
    function handleSetFilterValue(filterObject){

            oldAndNewConfiguration.current.newConfiguration.setFieldFilterByFieldIndex(oldAndNewConfiguration.current.newFieldIndex, filterObject);
            oldAndNewConfiguration.current.newConfiguration.setColumnFrom(0);
            oldAndNewConfiguration.current.newConfiguration.setRowFrom(0);
            setPivotConfiguration(new PivotConfiguration(oldAndNewConfiguration.current.newConfiguration));
            setFilterModalOpen({...filterModalOpen, open: false});
            setTableDataLoadStatus(1);
            dataProviderRef.current.loadDataForNewFieldsLists(oldAndNewConfiguration.current.newConfiguration.fieldsLists, 0, columnCount, 0, rowCount);            
            
            /*
            setCubeFilter( {
                operationType: 'AND',
                invertResult: false,
                childGroups: [],
                filters: oldAndNewFieldsLists.current.newFieldsLists.filterFields.map((v) => ({
                    fieldId : v.fieldId,
                    filterType: v.filter.filterType,
                    invertResult: v.filter.invertResult,
                    values:  v.filter.values 
                })) 
            
            })*/

    }

    // Вызывается по нажатию кнопки фильтрации на поле в зоне фильтров
    function handleFilterFieldButtonClick(event, i){

        // TODO

        oldAndNewConfiguration.current = {
            newFieldIndex : i,
            oldConfiguration: new PivotConfiguration(pivotConfiguration),
            newConfiguration: new PivotConfiguration(pivotConfiguration)
        }
        setFilterFieldIndex(i);
        const field = pivotConfiguration.fieldsLists.filterFields[i];

        /*setCubeFilter( {
            operationType: 'AND',
            invertResult: false,
            childGroups: [],
            filters: oldAndNewFieldsLists.current.newFieldsLists.filterFields.filter((item, index)=>index !== i).map((v) => ({
                fieldId : v.fieldId,
                filterType: v.filter.filterType,
                invertResult: v.filter.invertResult,
                values:  v.filter.values 
            })) 
        
        })*/

        setFilterModalOpen({open: true, index: i, type: field.filter.filterType, values: field.filter.values});

        setFilterModalStyle({
            top: event.screenY,
            left: event.screenX,
            transform: `translate(0%, -20%)`,
        })
    }

    function handleMetricFieldButtonClick(event, i){
        setAggModalOpen({open: true, index: i, type: 'change'});
        setAggModalStyle({
            top: event.screenY,
            left: event.screenX,
            transform: `translate(-20%, -20%)`,
        })
    }

    /*
        !!! ПРИМЕР !!!
    */
    function handleMetricNameCellClick(data){
        let newPivotConfiguration = new PivotConfiguration(pivotConfiguration);
        newPivotConfiguration.changeMetricCellFormatBold(data);
        setPivotConfiguration(newPivotConfiguration);
    }

    function handleChangeFieldDataStyle(data){

        let newData = data;
        newData.style.personalSettings = true;

        let newPivotConfiguration = new PivotConfiguration(pivotConfiguration);
        newPivotConfiguration.changeFieldDataStyle(newData);
        setPivotConfiguration(newPivotConfiguration);
        handleFormattingDialog(false)
    }

    // contextMenu для форматирования
    const [showContextMenu, setShowContextMenu] = useState(false);
    const [contextPosition, setContextPosition] = useState({
        mouseX: null,
        mouseY: null,
    });

    const handleContextClick = (event, type, values) => {
        if (type === 'metricValues') {
            event.preventDefault();
        
            setContextPosition({
                mouseX: event.clientX - 2,
                mouseY: event.clientY - 4,
            });
    
            setShowContextMenu(true)
            setItemFormattingValues(values)
        }
        return false
    };

    const handleContextClose = () => {
        setContextPosition({
            mouseX: null,
            mouseY: null,
        });
        setShowContextMenu(false)
    }

    //Диалоговое окно форматирования
    const [formattingDialogOpen, setFormattingDialogOpen] = useState(false);
    const [itemFormattingValues, setItemFormattingValues] = useState('');

    const handleFormattingDialog = (value) => {
        if(value === true) {
            handleContextClose();
        }
        setFormattingDialogOpen(value)
    }

    function result(){ 
        return (
            <DragDropContext
                onDragStart = {handleDragStart}
                onDragEnd = {handleDragEnd}
            >
                <div className={styles.gragDropDiv}>
                    <Grid container className={styles.dragDropGridContainer}>
                        <Grid item xs={1} className={styles.gridForFilters}>
                            {fieldsVisibility &&
                                <PivotFieldsList
                                    name = "Фильтры"
                                    droppableId = "filterFields"
                                    fields = {pivotConfiguration.fieldsLists.filterFields}
                                    direction = "vertical"
                                    onButtonClick = {(event, i) => handleFilterFieldButtonClick(event, i)}
                                />
                            }
                        </Grid>
                        <Grid item xs={11} className={styles.gridForTools}>
                            <PivotTools
                                columnsMetricPlacement = {pivotConfiguration.columnsMetricPlacement}
                                onMetricPlacementChange = {handleSetMetricPlacement}
                                mergeMode = {pivotConfiguration.mergeMode}
                                fullScreen = {pivotFullScreen}
                                onMergeModeChange = {handleSetMergeMode}
                                onViewTypeChange = {props.onViewTypeChange}
                                onFullScreen = {handleFullScreen}
                                fieldsVisibility = {fieldsVisibility}
                                onFieldsVisibility = {handleFieldsVisibility}
                                onThresholdDialog = {handleThresholdDialog}
                                onConfigDialog = {handleSetConfigDialog}
                            />
                            {fieldsVisibility &&
                                <PivotFieldsList
                                    name = "Неиспользуемые поля"
                                    droppableId = {onlyUnused ? "unusedFields": "allFields"}
                                    fields = {onlyUnused ? pivotConfiguration.fieldsLists.unusedFields: pivotConfiguration.fieldsLists.allFields}
                                    direction = "horizontal"
                                    onlyUnused = {onlyUnused}
                                    onOnlyUnusedClick = {handleOnlyUnusedClick}
                                />
                            }

                            {fieldsVisibility &&
                                <PivotFieldsList
                                    name = "Столбцы"
                                    droppableId = "columnFields"
                                    fields = {pivotConfiguration.fieldsLists.columnFields}
                                    direction = "horizontal"
                                />
                            }
                            {pivotConfiguration.columnsMetricPlacement && fieldsVisibility &&
                                <PivotFieldsList
                                    name = "Метрики"
                                    droppableId = "metricFields"
                                    fields = {pivotConfiguration.fieldsLists.metricFields}
                                    direction = "horizontal"
                                    onButtonClick = {handleMetricFieldButtonClick}
                                />
                            }
                            <TableRangeControl
                                orientation = "horizontal"
                                total = {tableData.totalColumns}
                                position = {pivotConfiguration.columnFrom}
                                count = {columnCount}
                                onPositionChange = {handleColumnFromChange}
                            />
                        </Grid>
                        <Grid item xs={1} className={styles.verticalListTableCell}>
                            <Box display="flex" flexDirection="row">
                                {fieldsVisibility &&
                                    <PivotFieldsList
                                        name = "Строки"
                                        droppableId = "rowFields"
                                        fields = {pivotConfiguration.fieldsLists.rowFields}
                                        direction = "vertical"
                                    />
                                }
                                {!pivotConfiguration.columnsMetricPlacement && fieldsVisibility &&
                                    <PivotFieldsList
                                        name = "Метрики"
                                        droppableId = "metricFields"
                                        fields = {pivotConfiguration.fieldsLists.metricFields}
                                        direction = "vertical"
                                        onButtonClick = {handleMetricFieldButtonClick}
                                    />
                                }
                                <TableRangeControl
                                    orientation = "vertical"
                                    total = {tableData.totalRows}
                                    position = {pivotConfiguration.rowFrom}
                                    count = {rowCount}
                                    onPositionChange = {handleRowFromChange}
                                />
                            </Box>
                        </Grid>
                        <Measure
                            bounds
                            onResize={contentRect => {      

                                let cc = Math.ceil(contentRect.bounds.width/85) - 1 - pivotConfiguration.fieldsLists.rowFields.length ;
                                let rc = Math.ceil(contentRect.bounds.height/14) + 1 - pivotConfiguration.fieldsLists.columnFields.length;
                                if (tableSize.dimensions.height >= contentRect.bounds.height){
                                    rc = Math.max(3, Math.min(rc, rowCount));
                                    
                                }
                                if (tableSize.dimensions.width >= contentRect.bounds.width){
                                    cc = Math.min(cc, columnCount);
                                }

                                setTableSize({ dimensions: contentRect.bounds });

                                if ((cc !== columnCount || rc !== rowCount) && tableDataLoadStatus===0){
                                    setColumnCount(cc);
                                    setRowCount(rc);
                                    if(!dataProviderRef.current.changeWindow(pivotConfiguration.columnFrom, cc, pivotConfiguration.rowFrom, rc)){
                                        setTableDataLoadStatus(1);
                                    }
                                
                                }
                                
                            }}
                        >
                            {({ measureRef }) => {
                                return (
                                        <Grid ref={measureRef}item xs={11} className={styles.gridMeasure}>
                                            {tableDataLoadStatus === 1 ?
                                                <CircularProgress/>
                                                    :
                                             tableDataLoadStatus === 2 ?
                                                <Typography>{tableDataLoadErrorMessage}</Typography>
                                                    :
                                                <PivotTable
                                                    onContextMenu={handleContextClick}
                                                    tableData = {tableData}
                                                    pivotConfiguration = {pivotConfiguration}
                                                    onDimensionValueFilter = {handleDimensionValueFilter}
                                                    onChangeInnerTableSize = {handleChangeInnerTableSize}
                                                    onMetricNameCellClick = {handleMetricNameCellClick}
                                                /> 
                                             }
                                        </Grid>
                            )}}
                                
                        </Measure>
                    </Grid>
                </div>
            </DragDropContext>
        )
    }

    return (
        <div  style={{display: 'flex', flex: 1}}>
            <DataLoader
                loadFunc = {dataHub.olapController.getJobMetadata}
                loadParams = {[props.jobId]}
                onDataLoaded = {handleMetadataLoaded}
                key = {resetComponentKey}
            >
                {pivotFullScreen ?
                    <Dialog fullScreen open={pivotFullScreen} classes={{paper: styles.dialog}}>
                        {result()}
                    </Dialog>
                    :
                    <div style={{display: 'flex', flex: 1}} > 
                        {result()} 
                    </div>
                }
            </DataLoader>
            <Modal
                aria-labelledby="aggFunc"
                open = {aggModalOpen.open}
                onClose = {handleAggModalClose}
            >
                <div style={aggModalStyle} className = {styles.modal}>
                    <List>
                        {aggFuncList}
                    </List>
                </div>
            </Modal>
            <PivotFilterModal
                filterType = {filterModalOpen.type}
                filterValues = {filterModalOpen.values}
                open = {filterModalOpen.open}
                style = {filterModalStyle}
                jobId = {props.jobId}
                //field = {movedField} 
                //pivotTableFilters = {cubeFilter}
                pivotConfiguration = {pivotConfiguration}
                filterFieldIndex = {filterFieldIndex}
                onClose = {handleFilterModalClose}
                onOK = {handleSetFilterValue}
            />
            <Menu
                keepMounted
                open={showContextMenu}
                onClose={handleContextClose}
                anchorReference="anchorPosition"
                anchorPosition={
                contextPosition.mouseY !== null && contextPosition.mouseX !== null
                    ? { top: contextPosition.mouseY, left: contextPosition.mouseX }
                    : undefined
                }
            >   
                <MenuItem key="formatting" onClick={() => handleFormattingDialog(true)}> Форматирование </MenuItem>
            </Menu>
            {formattingDialogOpen && 
                <FormattingDialog
                    open = {formattingDialogOpen}
                    format = {itemFormattingValues}
                    updateData = {handleChangeFieldDataStyle}
                    onCancel = {handleFormattingDialog}
                />
            }
            <ThresholdDialog
                open = {thresholdDialogOpen}
                onCancel = {handleThresholdDialog}
                allFields = {pivotConfiguration.fieldsLists.allFields}
            
            />
            {showConfigDialog &&
                <ConfigDialog
                    open = {showConfigDialog}
                    configs = {avaibleConfigs}
                    onCancel = {handleSetConfigDialog}
                    onDelete = {handleDeleteConfig}
                    onChooseConfig = {handleLoadCertainConfig}
                    onMakeDefault = {handleSaveConfigByDefault}
                />
            }
            {showConfigSaveDialog &&
                <ConfigSaveDialog
                    open = {showConfigSaveDialog}
                    configs = {avaibleConfigs}
                    onCancel = {handleSetConfigDialog}
                    onSave = {handleSaveConfig}
                />
            }
        </div>
    )
        
 }