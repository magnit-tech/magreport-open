import React from 'react';
import {useState} from 'react';
import { useSnackbar } from 'notistack';

// components
import { CircularProgress } from '@material-ui/core';
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import IconButton from '@material-ui/core/IconButton';
import Box from '@material-ui/core/Box';

// local
import DesignerPage from '../../Development/Designer/DesignerPage';
import DesignerTextField from '../../Development/Designer/DesignerTextField';
import DesignerFolderItemPicker from '../../Development/Designer/DesignerFolderItemPicker';
import PageTabs from 'main/PageTabs/PageTabs';
import DataLoader from 'main/DataLoader/DataLoader';
import DatasetItem from './DatasetItem'
import SecurityFilterRoles from "./SecurityFilterRoles";
import { DatasetItemCSS } from "./SecurityFilterCSS";

// dataHub
import dataHub from 'ajax/DataHub';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

/**
 * @callback onExit
 */

/**
 * Дизайнер Фильтра безопасности
 * @param {Object} props - параметры компонента
 * @param {String} props.mode - 'edit', 'create' - режим редактирования или создания нового объекта
 * @param {Number} props.securityFilterId - id объекта при редактировании (имеет значение только при mode == 'edit')
 * @param {Number} props.folderId - id папки в которой размещается объект при создании (имеет значение только при mode == 'create')
 * @param {onExit} props.onExit - callback при выходе
 */
export default function SecurityFilterDesigner(props){

    // const filterDetailsTabIndex = 0;
    // const dataSetsTabIndex = 1;
    const rolesTabIndex = 2;
    
    const { enqueueSnackbar } = useSnackbar();
    const classes = DatasetItemCSS();
    const [isAdd, setIsAdd] = useState(props.mode === 'edit' ? false : true)

    const [data, setData] = useState({
        securityFilterName : '',
        securityFilterDescription : '',
        securityFilterInstanceId : null,
        dataSets : [],
    });

    const fieldLabels = {
        securityFilterName : "Название",
        securityFilterDescription : "Описание",
        securityFilterInstanceId : "Экземпляр фильтра",
        securityFilterDataSets : "Наборы данных"  
    }

    const [pagename, setPagename] = useState(props.mode === 'create' ? "Создание фильтра безопасности" : "Редактирование фильтра безопасности");

    const [uploading, setUploading] = useState(false);
    const [errorField, setErrorField] = useState({});
    const [selectedFilterInstance, setSelectedFilterInstance] = useState(null)
    const [emptyFields, setEmptyFields] = useState([])
    const [SFId, setSFId] = useState(props.securityFilterId)

    let loadFunc;
    let loadParams = [];

    if(props.mode === 'edit'){
        loadFunc = dataHub.securityFilterController.get;
        loadParams = [SFId];
    }

    /*
        Data loading
    */

    function handleDataLoaded(loadedData){

        // FilterInstance

        loadedData.filterInstance.filterInstanceId = loadedData.filterInstance.id;
        let fields = []
        for( let f of loadedData.filterInstance.fields){
            f.filterInstanceFieldId = f.id;
            if(f.type === 'CODE_FIELD'){
                fields.push({
                    filterInstanceFieldId: f.id,
                    dataSetFieldId: null
                })
            }
        }

        setData({
            ...loadedData,
            securityFilterName : loadedData.name,
            securityFilterDescription : loadedData.description,
            securityFilterInstanceId : loadedData.filterInstance.id,
        });   
        setSelectedFilterInstance(loadedData.filterInstance)
        setEmptyFields(fields)
        setPagename("Редактирование фильтра безопасности: " + loadedData.name);
    }

    function handleDataLoadFailed(message){
        
    }

    /*
        Data editing
    */

    function handleChange(key, value){
        setData({
            ...data,
            [key]: value
        });
        setErrorField({
            ...errorField,
            [key]: (value === '') ? "Поле " + fieldLabels[key] + " не может быть пустым" : false
        });
    }

    function handleChangeFilterInstance(itemId, item, folderId){
        item.filterInstanceId = item.id;

        /*
            Формируем базовый набор полей для маппинга на dataSets
        */
        let fields = []
        for( let f of item.fields){
            f.filterInstanceFieldId = f.id;
            if(f.type === 'CODE_FIELD'){
                fields.push({
                    filterInstanceFieldId: f.id,
                    dataSetFieldId: null
                })
            }
        }

        /*
            Обнуляем у всех dataSet поля
        */
        let arrDatasets = [...data.dataSets];
        for(let d of arrDatasets){
            d.fields = fields;
        }

        setEmptyFields(fields)
        setSelectedFilterInstance(item)
        setData({...data, securityFilterInstanceId: itemId, dataSets: arrDatasets})
        setErrorField({
            ...errorField,
            securityFilterInstanceId: false
        });
    }

    /*
        Save and cancel
    */

    function handleSave(needExit=true){

        let errorExists = checkHasErrors();

        if(!errorExists) {
            let operationType;
            if(selectedFilterInstance.type === 'DATE_RANGE' || selectedFilterInstance.type === 'RANGE'){
                operationType = 'IS_BETWEEN';
            }
            else if (selectedFilterInstance.type === 'DATE_VALUE'){
                operationType = 'IS_EQUAL';
            }
            else{
                operationType = 'IS_IN_LIST';
            }
            let dataSets =  data.dataSets.map( (v) => ({dataSetId : v.dataSet.id, fields: v.fields}) ); 

            if(props.mode === 'create' && isAdd === true){
                setIsAdd(false);
                dataHub.securityFilterController.add(
                    null,
                    props.folderId,
                    data.securityFilterName,
                    data.securityFilterDescription,
                    operationType,
                    data.securityFilterInstanceId,
                    dataSets,
                    magrepResponse => handleAddEditAnswer(needExit, magrepResponse)
                    );
                    setUploading(true);
            }
            else{
                dataHub.securityFilterController.edit(
                    props.securityFilterId || SFId,
                    props.folderId,
                    data.securityFilterName,
                    data.securityFilterDescription,
                    operationType,
                    data.securityFilterInstanceId,
                    dataSets,
                    magrepResponse => handleAddEditAnswer(needExit, magrepResponse));
                    setUploading(true);
            }
        }
    }

    function handleCancel(){
        props.onExit();
    }

    function handleAddEditAnswer(needExit, magrepResponse){
        setUploading(false);
        if(magrepResponse.ok){
            if (needExit){
                props.onExit();
            }
            else {
                setSFId(magrepResponse.data.id)
            }
            
        }
        else{
            let actionWord = props.mode === 'create' ? "создании" : "обновлении";
            enqueueSnackbar("Ошибка при " + actionWord + " объекта: " + magrepResponse.data, {variant : "error"});
        }
    }

    function handleAddDataset(){
        let arr = [...data.dataSets]

        arr.push({
            dataSet: {id : null, fields: []},
            fields : emptyFields
        });
        setData({...data, dataSets: arr})
    }

    function handleSelectDataset(index, id, dataset){
        let arr = [...data.dataSets]
        let newObj = {dataSet: dataset, fields: emptyFields}
        arr.splice(index, 1, newObj)

        setData({...data, dataSets: arr})
    }

    function handleChangeFieldDataset(index, filterInstanceFieldId, dataSetFieldId){
        let arr = [...data.dataSets]
        let fieldArr = []
        
        for(let f of arr[index].fields){
            if(f.filterInstanceFieldId !== filterInstanceFieldId){
                fieldArr.push(f)
            }
            else{
                fieldArr.push({
                    filterInstanceFieldId,
                    dataSetFieldId
                })
            }
        }

        let newObj = {...arr[index], fields: fieldArr}
        arr.splice(index, 1, newObj)
        setData({...data, dataSets: arr})
    }

    function handleDatasetDelete(index){
        let arr = [...data.dataSets]
        arr.splice(index, 1)
        setData({...data, dataSets: arr})
    }

    function handleTabChange(){
        handleSave(false);
    }

    function checkHasErrors() {

        let errors = {};
        let errorExists = false;

        // validations
        if (data.securityFilterName.trim() === ''){
            errors.securityFilterName = true;
            errorExists = true;
        }
        if (data.securityFilterDescription.trim() === ''){
            errors.securityFilterDescription = true;
            errorExists = true;
        }
        if (!data.securityFilterInstanceId){
            errors.securityFilterInstanceId = true;
            errorExists = true;
        }
        if (data.dataSets.length){
            let dsError = false
            for (let ds of data.dataSets){
                if (ds.dataSet.id === null){
                    dsError = true;
                    ds.error = true;
                }
                else{
                    for(let f of ds.fields){
                        if (!f.dataSetFieldId && f.dataSetFieldId !== 0){
                            f.error=true;
                            dsError = true;
                        }
                    }
                }
            }
            if (dsError){
                errorExists = true;
                errors.securityFilterDataSets = true;
            }

        }

        if (errorExists){
            for (let e of Object.keys(errors)){
                enqueueSnackbar("Недопустимо пустое значение в поле " + fieldLabels[e], {variant : "error"});
            }
        }

        if(errorExists){
            setErrorField(errors);
        }

        return errorExists;
    }

    function isTabChangeAllowed(newTab) {
        if(newTab === rolesTabIndex) {
            if(checkHasErrors()) {
                return false;
            }
        }
        return true;
    }

    let tabs = []

    tabs.push({
        tablabel: "Детали фильтра",
        tabcontent: uploading ? <CircularProgress className={classes.progress}/> :
        <DesignerPage 
            onSaveClick={handleSave}
            onCancelClick={handleCancel}
            //name = {pagename}
        >
            <DesignerTextField
                label = {fieldLabels.securityFilterName}
                value = {data.securityFilterName}
                onChange = {value => {handleChange('securityFilterName', value)}}
                displayBlock
                fullWidth
                error = {errorField.securityFilterName}
            />

            <DesignerTextField
                label = {fieldLabels.securityFilterDescription}
                value = {data.securityFilterDescription}
                onChange = {value => handleChange('securityFilterDescription', value)}
                displayBlock
                fullWidth
                error = {errorField.securityFilterDescription}
            />

            <DesignerFolderItemPicker
                label = {fieldLabels.securityFilterInstanceId}
                value = {selectedFilterInstance ? selectedFilterInstance.name : null}
                itemType = {FolderItemTypes.filterInstance}
                onChange = {handleChangeFilterInstance}
                displayBlock
                fullWidth
                error = {errorField.securityFilterInstanceId}
                disabled={!!SFId}
            />
        </DesignerPage>
    })
    tabs.push({
        tablabel: "Наборы данных",
        //tabdisabled: data.securityFilterName && data.securityFilterDescription && selectedFilterInstance ? false: true,
        tabcontent: uploading ? <CircularProgress className={classes.progress}/>:
        <DesignerPage
            onSaveClick={handleSave}
            onCancelClick={handleCancel}
        >
            <div style={{marginTop: '8px'}}>
            {data.dataSets.map((item, index) => 
                <DatasetItem 
                    key={index}
                    name={item.dataSet.name}
                    filterInstanceFields = {selectedFilterInstance ? selectedFilterInstance.fields.filter( f => (f.type === 'CODE_FIELD') ) : []}
                    datasetFields={item.dataSet.fields}
                    fieldsMapping={item.fields}
                    onDelete={() => {handleDatasetDelete(index)}}
                    onSelectDataset={(id, dataset)=>{handleSelectDataset(index, id, dataset)}}
                    onChangeFieldDataset={(filterInstanceFieldId, dataSetFieldId)=>{handleChangeFieldDataset(index, filterInstanceFieldId, dataSetFieldId)}}
                />
            )}
            </div>        
            <Box align="center" m={1} >
                <IconButton
                    aria-label="add" 
                    color="primary"
                    onClick={handleAddDataset}
                    disabled={!data.securityFilterInstanceId}
                >
                    <AddCircleOutlineIcon 
                        fontSize='large'
                    />
                </IconButton>
            </Box>
        </DesignerPage>
    })

    tabs.push({
        tablabel: "Привязка роли",
        //tabdisabled: data.securityFilterName && data.securityFilterDescription && selectedFilterInstance ? false: true,
        tabcontent: uploading ? <CircularProgress className={classes.progress}/> :
            <SecurityFilterRoles 
                securityFilterId={SFId}
                filterInstance={{...selectedFilterInstance}}
                onExit={props.onExit}
                mode='edit'
            />
    })

    return(
        <DataLoader
            loadFunc = {loadFunc}
            loadParams = {loadParams}
            onDataLoaded = {handleDataLoaded}
            onDataLoadFailed = {handleDataLoadFailed}
        >   
            <div className={classes.rel}> 
                <div className={classes.abs}>
                    <PageTabs
                        tabsdata={tabs}
                        onTabChange={handleTabChange}
                        pageName={pagename}
                        isTabChangeAllowed={isTabChangeAllowed}
                    />
                </div>
            </div>
        </DataLoader>
    );
}

