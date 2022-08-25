import React from 'react';
import {useState} from 'react';
import { useSnackbar } from 'notistack';

// components
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';

// local
import DesignerPage from '../Designer/DesignerPage';
import DesignerTextField from '../Designer/DesignerTextField';
import DesignerSelectField from '../Designer/DesignerSelectField';
import DesignerMultipleSelectField from '../Designer/DesignerMultipleSelectField';
import StyleConsts from '../../../../StyleConsts';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';
import { CircularProgress } from '@material-ui/core';

/**
 * 
 * @param {*} props.mode : 'edit', 'create' - режим редактирования или создания нового объекта
 * @param {*} props.datasourceId : id объекта при редактировании (имеет значение только при mode == 'edit')
 * @param {*} props.folderId : id папки в которой размещается объект при создании (имеет значение только при mode == 'create')
 * @param {*} props.onExit : callback при выходе
 */
export default function FilterTemplatesDesigner(props){

    const { enqueueSnackbar } = useSnackbar();

    const [data, setData] = useState({
        filterTemplateName : '',
        filterTemplateDescription : '',
        filterTemplateType : '',
        filterTemplateOperations : [],
        filterTemplateFields : [],
    });

    const [typesData, setTypesData] = useState([]);
    const [typesOperations, setTypesOperations] = useState([]);
    const [fieldTypes, setFieldTypes] = useState([]);
    const [fieldsCcount, setFieldsCcount] = useState(0)

    const fieldLabels = {
        filterTemplateName : "Название",
        filterTemplateDescription : "Описание",
        filterTemplateType : "Тип шаблона фильтра",
        filterTemplateOperations : "Поддерживаемые операции",
        filterTemplateFields : "Поля шаблона фильтра"  
    }

    const [pagename, setPagename] = useState(props.mode === 'create' ? "Создание шаблона фильтра" : "Редактирование шаблона фильтра");

    const [uploading, setUploading] = useState(false);
    const [errorField, setErrorField] = useState({});

    let loadFunc;
    let loadParams = [];
    

    if(props.mode === 'edit'){
        loadFunc = dataHub.filterTemplateController.get;
        loadParams = [props.filterTemplateId];
    }

    /*
        Data loading
    */

    function handleDataLoaded(loadedData){
        let arr=[]
        loadedData.fields.forEach(item => {
            arr.push({
                description: item.description,
                name: item.name,
                id: item.id,
                linkedId: item.linkedId,
                type: item.type,
                level: item.level
            })
        })

        setData({
            ...data,
            filterTemplateName : loadedData.name,
            filterTemplateDescription : loadedData.description,
            filterTemplateType : loadedData.type.name,
            filterTemplateOperations : loadedData.supportedOperations,
            filterTemplateFields : arr,
        });
        setPagename("Редактирование шаблона фильтра: " + loadedData.name);
        setFieldsCcount(arr.length)
    }

    function handleTypesLoaded(data){
        setTypesData(data.map((v) => ({id: v.id, name: v.name})));
    }

    function handleOperationsLoaded(data){
        setTypesOperations(data.map((v) => ({id: v.id, name: v.name})));
    }

    function handleFieldTypesLoaded(data){
        setFieldTypes(data.map((v) => ({id: v.id, name: v.name})));
    }

    function handleDataLoadFailed(message){
        
    }

    /*
        Data editing
    */

    function handleChange(key, value){
        let newValue = value;
        if (key === 'filterTemplateType'){
            newValue = typesData.filter(item => item.id === value)[0].name
        }
        setData({
            ...data,
            [key]: newValue
        });
        setErrorField({
            ...errorField,
            [key]: false
        });
    }

    /*
        Save and cancel
    */

    function handleSave(){
        
        let errors = {};
        let errorExists = false;
        
        // Проверка корректности заполнения полей
        Object.entries(data)
            .filter( ([fieldName, fieldValue]) => 
                ( fieldName !== "filterTemplateId" && (fieldValue === null || (typeof fieldValue === "string" && fieldValue.trim() === "") ) ) )
            .reverse()
            .forEach( ([fieldName, fieldValue]) => 
                {
                    errors[fieldName] = true;
                    enqueueSnackbar("Недопустимо пустое значение в поле " + fieldLabels[fieldName], {variant : "error"});
                    errorExists = true;
                } );
        
        if(errorExists){
            setErrorField(errors);
        }
        else{
            if(props.mode === 'create'){
                dataHub.filterTemplateController.add(
                    data.filterTemplateDescription,
                    data.filterTemplateFields,
                    props.folderId, 
                    null, 
                    data.filterTemplateName,
                    data.filterTemplateOperations,
                    data.filterTemplateType,
                    handleAddEditAnswer);
            }
            else{
                dataHub.filterTemplateController.edit(
                    data.filterTemplateDescription,
                    data.filterTemplateFields,
                    props.folderId, 
                    props.filterTemplateId, 
                    data.filterTemplateName,
                    data.filterTemplateOperations,
                    data.filterTemplateType,
                    handleAddEditAnswer);
            }
            setUploading(true);
        }
    }

    function handleCancel(){
        props.onExit();
    }

    function handleAddEditAnswer(magrepResponse){
        setUploading(false);
        if(magrepResponse.ok){
            props.onExit();
        }
        else{
            let actionWord = props.mode === 'create' ? "создании" : "обновлении";
            enqueueSnackbar("Ошибка при " + actionWord + " объекта: " + magrepResponse.data, {variant : "error"});
        }
    }

    function handleAddField(){
        setFieldsCcount(fieldsCcount+1)
        let arr = [...data.filterTemplateFields]
        arr.push({
            "description": "",
            "id": 0,
            "level": 0,
            "linkedId": 0,
            "name": "",
            "type": ""
        })
        setData({
            ...data,
            filterTemplateFields: arr
        })
    }

    function handleDeleteField(index){
        if (fieldsCcount>0){
            data.filterTemplateFields.splice(index, 1)
            let arr = [...data.filterTemplateFields]
            setData({
                ...data,
                filterTemplateFields: arr
            })
            setFieldsCcount(fieldsCcount-1)
        }
    }
    
    function handleFieldsChange(index, key, value){
        let arr = [...data.filterTemplateFields]
        let item = {...arr[index], [key]: value}
        if (key === "type"){
            const typeField = fieldTypes.filter(item => item.id === value)[0]
            item = {...arr[index], [key]: typeField.name}
        }
        
        arr.splice(index, 1, item)
        setData({
            ...data,
            filterTemplateFields: arr
        })
    }

    let fieldsItems = []
    for(let i=0; i<fieldsCcount; i++){
        fieldsItems.push(
            <Grid 
                key={i} 
                container 
                spacing={1}
                style={{marginTop:'10px', borderBottom:'2px solid #CCC'}}
            >
                <Grid container spacing={1}>
                    <Grid item xs={4}>
                        <DesignerTextField
                            label = "ID"
                            value = {data.filterTemplateFields[i].id}
                            onChange = {value => handleFieldsChange(i, 'id', value)}
                            type = "number"
                            fullWidth
                            displayBlock
                        />
                    </Grid>
                    <Grid item xs={4}>
                        <DesignerTextField
                            label = "Название"
                            value = {data.filterTemplateFields[i].name}
                            fullWidth
                            displayBlock
                            onChange = {value => handleFieldsChange(i, 'name', value)}
                        />
                    </Grid>
                    <Grid item xs={4}>
                        <DesignerSelectField
                            label = "Тип поля"
                            data = {fieldTypes}
                            value = {data.filterTemplateFields[i].type ? fieldTypes.filter(item => data.filterTemplateFields[i].type === item.name)[0].id : ""}
                            fullWidth
                            displayBlock
                            onChange = {value => handleFieldsChange(i, 'type', value)}
                            error = {errorField.filterTemplateType}
                        />
                    </Grid>
                </Grid>
                <Grid item xs={12}>
                    <DesignerTextField
                        label = "Описание"
                        value = {data.filterTemplateFields[i].description}
                        fullWidth
                        displayBlock
                        onChange = {value => handleFieldsChange(i, 'description', value)}
                    />
                </Grid>
                <Grid container spacing={1}>
                    <Grid item xs={4}>
                        <DesignerTextField
                            label = "Уровень"
                            type = "number"
                            value = {data.filterTemplateFields[i].level}
                            onChange = {value => handleFieldsChange(i, 'level', value)}
                        />
                    </Grid>
                    <Grid item xs={4}>
                        <DesignerTextField
                            label = "Связанное поле"
                            type = "number"
                            value = {data.filterTemplateFields[i].linkedId}
                            onChange = {value => handleFieldsChange(i, 'linkedId', value)}
                        />
                    </Grid>
                    <Grid item xs={4}>
                        <IconButton 
                                aria-label="delete" 
                                color="primary"
                                style={{float:'right'}}
                                onClick={() => handleDeleteField(i)}
                            >
                                <RemoveIcon 
                                    fontSize='large'
                                />
                            </IconButton>
                    </Grid>
                </Grid>
            </Grid>
        )
    }

    return(
        
        <DataLoader
            loadFunc = {dataHub.filterTemplateController.getFieldTypes}
            loadParams = {[]}
            onDataLoaded = {handleFieldTypesLoaded}
            onDataLoadFailed = {handleDataLoadFailed}
        >
            <DataLoader
                loadFunc = {dataHub.filterTemplateController.getFilterTypes}
                loadParams = {[]}
                onDataLoaded = {handleTypesLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
            >
                <DataLoader
                    loadFunc = {dataHub.filterTemplateController.getOperationTypes}
                    loadParams = {[]}
                    onDataLoaded = {handleOperationsLoaded}
                    onDataLoadFailed = {handleDataLoadFailed}
                >
                    <DataLoader
                        loadFunc = {loadFunc}
                        loadParams = {loadParams}
                        onDataLoaded = {handleDataLoaded}
                        onDataLoadFailed = {handleDataLoadFailed}
                    >
                
                    {uploading ? <CircularProgress/> :

                    <DesignerPage 
                        onSaveClick={handleSave}
                        onCancelClick={handleCancel}
                        name = {pagename}
                    >
                        <DesignerTextField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.filterTemplateName}
                            value = {data.filterTemplateName}
                            onChange = {data => {handleChange('filterTemplateName', data)}}
                            displayBlock
                            fullWidth
                            error = {errorField.filterTemplateName}
                        />

                        <DesignerTextField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.filterTemplateDescription}
                            value = {data.filterTemplateDescription}
                            onChange = {data => handleChange('filterTemplateDescription', data)}
                            displayBlock
                            fullWidth
                            error = {errorField.filterTemplateDescription}
                        />

                        <DesignerSelectField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.filterTemplateType}
                            data = {typesData}
                            value = {data.filterTemplateType ? typesData.filter(item => data.filterTemplateType === item.name)[0].id : ""}
                            onChange = {data => handleChange('filterTemplateType', data)}
                            displayBlock
                            fullWidth
                            error = {errorField.filterTemplateType}
                        />

                        <DesignerMultipleSelectField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.filterTemplateOperations}
                            value = {typesOperations.filter(item => data.filterTemplateOperations.indexOf(item.name)>=0 )}
                            data = {typesOperations}
                            onChange = {data => {handleChange('filterTemplateOperations', data)}}
                            displayBlock
                            fullWidth
                            error = {errorField.filterTemplateOperations}
                        />
                        <Typography 
                            align="left"
                            color="textSecondary"
                        >
                            Поля шаблона фильтра:
                        </Typography>
                        
                        {fieldsItems}
                        
                        <IconButton 
                            aria-label="add" 
                            color="primary"
                            onClick={handleAddField}
                        >
                            <AddIcon 
                                fontSize='large'
                            />
                        </IconButton>
                        
                        
                        

                    </DesignerPage>
                    }
                    </DataLoader>
                </DataLoader>
            </DataLoader>
        </DataLoader>
    );
}

