import React from 'react';
import {useState, useRef} from 'react';

// material-ui

import Grid from '@material-ui/core/Grid';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

//local
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import DesignerFolderItemPicker from '../../Designer/DesignerFolderItemPicker'
import DesignerSelectField from '../../Designer/DesignerSelectField';
import DesignerTextField from '../../Designer/DesignerTextField';
import {convertTokenInputLocalToFilterData, convertTokenInputFilterToLocalData} from "./converters";

/**
 * @callback onChange
 * @param {Object} filterInstance - Экземпляр фильтра
 * @param {boolean} errorExists - флаг наличия ошибок заполнения
 */
/**
 * Компонент для редактирования полей шаблона фильтра TOKEN_INPUT
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterInstanceData - объект filterInstance
 * @param {onChange} props.onChange - callback для изменения значений полей
 */
export default function TokenInputFields(props){

    let {localData, errorFields} = convertTokenInputFilterToLocalData(props.filterInstanceData);

    const [datasetData, setDatasetData] = useState(null);
    const datasetFieldsNameMap = useRef(new Map());

    let loadFuncDataset;

    if(props.filterInstanceData.dataSetId){
        loadFuncDataset = dataHub.datasetController.get;
    }

    function buildDatasetFieldsNameMap(datasetData){
        datasetFieldsNameMap.current = new Map();
        for(let f of datasetData.fields){
            datasetFieldsNameMap.current.set(f.id, f.name);
        }
    }

    function handleDatasetLoaded(datasetData){
        buildDatasetFieldsNameMap(datasetData);
        setDatasetData(datasetData);
    }

    function handleChangeDataset(id, datasetData, folderId){
        buildDatasetFieldsNameMap(datasetData);
        setDatasetData(datasetData);
        localData = {
            ...localData,
            dataSetId : id
        }
        handleChangeData();
    }

    function handleChangeField(fieldName, value){
        let parts = fieldName.split('.');

        if(parts[1] === 'id'){
            let oldDatasetFieldName = datasetFieldsNameMap.current.get(localData.datasetFields[parts[0]][parts[1]]);
            let newDatasetFieldName = datasetFieldsNameMap.current.get(value);
            let currentFieldName = localData.datasetFields[parts[0]].name;
            let currentFieldDescription = localData.datasetFields[parts[0]].description;
            if(currentFieldName.trim() === '' || currentFieldName === oldDatasetFieldName){
                localData.datasetFields[parts[0]].name = newDatasetFieldName;
            }
            if(currentFieldDescription.trim() === '' || currentFieldDescription === oldDatasetFieldName){
                localData.datasetFields[parts[0]].description = newDatasetFieldName;
            }            
        }

        localData.datasetFields[parts[0]][parts[1]] = value;
        handleChangeData();
    }

    function handleChangeData(){
        let {filterInstanceData, errors} = convertTokenInputLocalToFilterData(props.filterInstanceData, localData);
        props.onChange(filterInstanceData, errors);
    }

    function handleDataLoadFailed(message){

    }

    return(
        <div>

            <DataLoader
                loadFunc = {loadFuncDataset}
                loadParams = {[props.filterInstanceData.dataSetId]}
                onDataLoaded = {handleDatasetLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
                disabledScroll = {true}
            >
                <DesignerFolderItemPicker
                    label = "Набор данных"
                    value = {datasetData && datasetData.name ? datasetData.name : null}
                    itemType = {FolderItemTypes.dataset}
                    onChange = {handleChangeDataset}
                    displayBlock
                    fullWidth
                    error = {errorFields.dataSetId}
                />

                {datasetData && 
                <div>
                    <Grid container>

                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <DesignerTextField
                                label = "Название поля ID"
                                value = {localData.datasetFields.idField.name}
                                fullWidth
                                displayBlock
                                onChange = {(value) => {handleChangeField('idField.name',value)}}
                                error = {errorFields.idField.name}
                            />
                        </Grid>

                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <DesignerTextField
                                label = "Описание поля ID"
                                value = {localData.datasetFields.idField.description}
                                fullWidth
                                displayBlock
                                onChange = {(value) => {handleChangeField('idField.description',value)}}
                                error = {errorFields.idField.description}
                            />
                        </Grid>                                          
                        
                        <Grid item xs={4}>
                            <DesignerSelectField
                                label = "Поле ID набора данных"
                                data = {datasetData ? datasetData.fields : []}
                                value = {localData.datasetFields.idField.id}
                                fullWidth
                                displayBlock
                                onChange = {(value) => {handleChangeField('idField.id',value)}}
                                error = {errorFields.idField.id}
                            />
                        </Grid>

                    </Grid>

                    <Grid container>
                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <DesignerTextField
                                label = "Название поля NAME"
                                value = {localData.datasetFields.nameField.name}
                                fullWidth
                                displayBlock
                                onChange = {(value) => {handleChangeField('nameField.name',value)}}
                                error = {errorFields.nameField.name}
                            />
                        </Grid>

                        <Grid item xs={4} style={{paddingRight: '16px'}}>
                            <DesignerTextField
                                label = "Описание поля NAME"
                                value = {localData.datasetFields.nameField.description}
                                fullWidth
                                displayBlock
                                onChange = {(value) => {handleChangeField('nameField.description',value)}}
                                error = {errorFields.nameField.description}
                            />
                        </Grid>                                          
                        
                        <Grid item xs={4}>
                            <DesignerSelectField
                                label = "Поле NAME набора данных"
                                data = {datasetData ? datasetData.fields : []}
                                value = {localData.datasetFields.nameField.id}
                                fullWidth
                                displayBlock
                                onChange = {(value) => {handleChangeField('nameField.id',value)}}
                                error = {errorFields.nameField.id}
                            />
                        </Grid>
                    </Grid>                    
                </div>
                }             

            </DataLoader>

        </div>
    )
}