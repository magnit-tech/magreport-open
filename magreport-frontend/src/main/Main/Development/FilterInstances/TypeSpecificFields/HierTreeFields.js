import React from 'react';
import {useState, useRef} from 'react';

// material-ui
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';

import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

//local
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import DesignerFolderItemPicker from '../../Designer/DesignerFolderItemPicker'
import DesignerSelectField from '../../Designer/DesignerSelectField';
import DesignerTextField from '../../Designer/DesignerTextField';
import { Typography } from '@material-ui/core';
import DraggableItemsList from 'main/DragAndDrop/DraggableItemsList';
import StyleConsts from '../../../../../StyleConsts';
import {convertHierTreeLocalToFilterData, convertHierTreeFilterToLocalData} from "./converters";

// utils
import ReorderList from 'utils/ReorderList'

// styles
import {HierTreeFieldsCSS} from '../FilterInstanceCSS';

/**
 * @callback onChange
 * @param {Object} filterInstance - Экземпляр фильтра
 * @param {boolean} errorExists - флаг наличия ошибок заполнения
 */
/**
 * Компонент для редактирования полей шаблона фильтра  типа HIERARCHY
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterInstanceData - объект filterInstance
 * @param {onChange} props.onChange - callback для изменения значений полей
 */
export default function HierTreeFields(props){
    const classes = HierTreeFieldsCSS();
    let localData = convertHierTreeFilterToLocalData(props.filterInstanceData);

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
            dataSetId : id,
            levels : [emptyLevel()]
        }
        handleChangeData();
    }

    /**
     * 
     * @param {*} level - 0-based level
     * @param {*} fieldName - composed field name devided by "."
     * @param {*} value - new field value
     */
    function handleChangeField(level, fieldName, value){
        let parts = fieldName.split('.');

        if(parts[1] === 'dataSetFieldId'){
            let oldDatasetFieldName = datasetFieldsNameMap.current.get(localData.levels[level][parts[0]].dataSetFieldId);
            let newDatasetFieldName = datasetFieldsNameMap.current.get(value);
            let currentFieldName = localData.levels[level][parts[0]].name;
            let currentFieldDescription = localData.levels[level][parts[0]].description;
            if(currentFieldName.trim() === '' || currentFieldName === oldDatasetFieldName){
                localData.levels[level][parts[0]].name = newDatasetFieldName;
            }
            if(currentFieldDescription.trim() === '' || currentFieldDescription === oldDatasetFieldName){
                localData.levels[level][parts[0]].description = newDatasetFieldName;
            }            
        }
        if (parts[1] === 'expand'){
            if (value) {
                localData.levels.map( (v, i) => {
                    return localData.levels[i][parts[0]][parts[1]] = i <= level ? value : !value;
                })
            }
            else {
                localData.levels.map( (v, i) => {
                    return localData.levels[i][parts[0]][parts[1]] = i < level ? !value : value;

                })
            }
        }
        else {

            localData.levels[level][parts[0]][parts[1]] = value;
        };

        handleChangeData();
    }

    function emptyLevel(){

        let emptyField = {
            datasetFieldId : null,
            datasetFieldIdError : true,
            name : '',
            nameError : true,
            description : '',
            descriptionError : true,
            expand: false
        }

        let emptyLevel = {
            idField : {...emptyField},
            nameField : {...emptyField}
        };
        
        return emptyLevel;

    }

    function handleChangeLevelClick(operation, index){

        let newLevels = localData.levels.slice();
        if (operation === 'add'){
            let newEmptyLevel = emptyLevel();
            newLevels.push(newEmptyLevel);
        }
        else {
            newLevels.splice(index, 1);
        }

        localData = {
            ...localData,
            levels: newLevels
        }

        handleChangeData();
    }

    function handleChangeData(){
        let {filterInstanceData, errors} = convertHierTreeLocalToFilterData(props.filterInstanceData, localData);
        props.onChange(filterInstanceData, errors);
    }

    function handleDataLoadFailed(message){

    }

    function handleElementMove(sourceGroupId, sourceIndex, targetGroupId, targetIndex){
        localData = {
            ...localData,
            levels: ReorderList(localData.levels, sourceIndex, targetIndex)
        }
        
        handleChangeData();
    }

    return (
        <div style={{minWidth: StyleConsts.designerTextFieldMinWidth}}>
            <DataLoader
                loadFunc = {loadFuncDataset}
                loadParams = {[props.filterInstanceData.dataSetId]}
                onDataLoaded = {handleDatasetLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
            >
                <DesignerFolderItemPicker
                    minWidth = {StyleConsts.designerTextFieldMinWidth}
                    label = "Набор данных"
                    value = {datasetData && datasetData.name ?  datasetData.schemaName + '.' + datasetData.name + ' (' + datasetData.dataSource.name  +')' : null}
                    itemType = {FolderItemTypes.dataset}
                    onChange = {handleChangeDataset}
                    displayBlock
                    fullWidth
                    error = {localData.dataSetIdError}
                />

                {datasetData && 
                    <div style = {{display: 'flex', flex: 1, flexDirection: 'column'}}>
                        <FormGroup>
                            <DraggableItemsList
                                items = {localData.levels.map( (v, i) => {
                                    let levelsLength = localData.levels.length;
                                    let checkedExpand = (i+1 === levelsLength) ? false :
                                        Boolean(v.idField.expand) || 
                                        (localData.levels[i+1] === undefined ? false: Boolean(localData.levels[i+1].idField.expand));
                                    return (
                                        <Paper elevation={3} key={i} className={classes.levelFields}>
                                            <Grid container>
                                                <Grid container justifyContent="space-between" spacing={2} style={{marginBottom: '4px'}}>
                                                        <Grid item xs={6} style={{display: 'flex', alighItems: 'center'}}>
                                                            <Typography variant='h6'>
                                                                Уровень {parseInt(i) + 1}  
                                                                { props.filterTemplateType === 'HIERARCHY' &&                                                          
                                                                    <FormControlLabel
                                                                        style={{marginLeft: '16px'}}
                                                                        control={
                                                                            <Checkbox
                                                                                style={{marginRight: '16px'}}
                                                                                name={v.idField.name}
                                                                                checked={checkedExpand}
                                                                                disabled={i+1 === levelsLength}
                                                                                onChange={(event) => { handleChangeField(i,'idField.expand', event.target.checked )}}
                                                                            />
                                                                        }
                                                                        label = {checkedExpand ? "Прокидываемый" : "Не прокидываемый"}
                                                                    />
                                                                }
                                                            </Typography>
                                                        </Grid>
                                                        <Grid item xs={6}>
                                                            <IconButton
                                                                aria-label="remove"
                                                                color="primary"
                                                                style={{float:"right"}}
                                                                onClick={() => handleChangeLevelClick('remove', i)}
                                                            >
                                                                <DeleteIcon/>
                                                            </IconButton>
                                                        </Grid>
                                                </Grid>
                                
                                                
                                
                                                <Grid item xs={4} style={{paddingRight: '16px'}}>
                                               { /* <FormControlLabel value="female" control={<Radio />} label="Female" /> */}
                                               
                                                    <DesignerTextField
                                                        label = "Название поля ID"
                                                        value = {v.idField.name}
                                                        fullWidth
                                                        displayBlock
                                                        onChange = {(value) => {handleChangeField(i,'idField.name',value)}}
                                                        error = {v.idField.nameError}
                                                    />
                                                </Grid>
                                
                                                <Grid item xs={4} style={{paddingRight: '16px'}}>
                                                    <DesignerTextField
                                                        label = "Описание поля ID"
                                                        value = {v.idField.description}
                                                        fullWidth
                                                        displayBlock
                                                        onChange = {(value) => {handleChangeField(i, 'idField.description',value)}}
                                                        error = {v.idField.descriptionError}
                                                    />
                                                </Grid>                                          
                                
                                                <Grid item xs={4}>
                                                    <DesignerSelectField
                                                        label = "Поле ID набора данных"
                                                        data = {datasetData ? datasetData.fields : []}
                                                        value = {v.idField.dataSetFieldId}
                                                        fullWidth
                                                        displayBlock
                                                        onChange = {(value) => {handleChangeField(i, 'idField.dataSetFieldId', value)}}
                                                        error = {v.idField.dataSetFieldIdError}
                                                    />
                                                </Grid>
                                
                                            </Grid>
                                
                                            <Grid container>
                                
                                                <Grid item xs={4} style={{paddingRight: '16px'}}>
                                                    <DesignerTextField
                                                        label = "Название поля NAME"
                                                        value = {v.nameField.name}
                                                        fullWidth
                                                        displayBlock
                                                        onChange = {(value) => {handleChangeField(i,'nameField.name',value)}}
                                                        error = {v.nameField.nameError}
                                                    />
                                                </Grid>
                                
                                                <Grid item xs={4} style={{paddingRight: '16px'}}>
                                                    <DesignerTextField
                                                        label = "Описание поля NAME"
                                                        value = {v.nameField.description}
                                                        fullWidth
                                                        displayBlock
                                                        onChange = {(value) => {handleChangeField(i, 'nameField.description',value)}}
                                                        error = {v.nameField.descriptionError}
                                                    />
                                                </Grid>                                          
                                
                                                <Grid item xs={4}>
                                                    <DesignerSelectField
                                                        label = "Поле NAME набора данных"
                                                        data = {datasetData ? datasetData.fields : []}
                                                        value = {v.nameField.dataSetFieldId}
                                                        fullWidth
                                                        displayBlock
                                                        onChange = {(value) => {handleChangeField(i, 'nameField.dataSetFieldId', value)}}
                                                        error = {v.nameField.dataSetFieldIdError}
                                                    />
                                                </Grid>
                                
                                            </Grid> 
                                            
                                        </Paper>)
                                    })}
                                groupId = "group_hier_levels"
                                onMove={handleElementMove}
                                disableGutters = {true}
                                style={{marginBottom:'0px'}}
                            />
                        </FormGroup>
                        <div className={classes.htfCenterBtn}>
                            <IconButton
                                aria-label="add"
                                color="primary"
                                onClick={() => handleChangeLevelClick('add', 0)}
                            >
                                <AddCircleOutlineIcon fontSize='large'/>
                            </IconButton>
                        </div>
                    </div>
                }

            </DataLoader>         
        </div>
    )
}