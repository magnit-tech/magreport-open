import React from 'react';
import {useState} from 'react';
import { useSnackbar } from 'notistack';

// material ui
import { CircularProgress } from '@material-ui/core';

// local
import DesignerPage from '../Designer/DesignerPage';
import DesignerTextField from '../Designer/DesignerTextField';
import DesignerFolderItemPicker from '../Designer/DesignerFolderItemPicker';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import StyleConsts from '../../../../StyleConsts';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

import UnboundedValueFields from './TypeSpecificFields/UnboundedValueFields';
import ValueListFields from './TypeSpecificFields/ValueListFields';
import HierTreeFields from './TypeSpecificFields/HierTreeFields';
import TokenInputFields from './TypeSpecificFields/TokenInputFields';
import RangeFields from './TypeSpecificFields/RangeFields';
import DateValueFields from './TypeSpecificFields/DateValueFields';

/**
 * 
 * @param {*} props.mode : 'edit', 'create' - режим редактирования или создания нового объекта
 * @param {*} props.filterInstanceId : id объекта при редактировании (имеет значение только при mode == 'edit')
 * @param {*} props.folderId : id папки в которой размещается объект при создании (имеет значение только при mode == 'create')
 * @param {*} props.onExit : callback при выходе
 */
export default function FilterInstanceDesigner(props){

    const { enqueueSnackbar } = useSnackbar();

    const [pageName, setPagename] = useState(props.mode === 'create' ? "Создание экземпляра фильтра" : "Редактирование экземпляра фильтра");

    const [uploading, setUploading] = useState(false);
    const [filterInstanceData, setFilterInstanceData] = useState({
        id: props.filterInstanceId,
        folderId: props.folderId,
        templateId: 0,
        name: '',
        code: "",
        description: '',
        fields : []
    });
    const [filterTemplateData, setFilterTemplateData] = useState({
        id : 0,
        name : '',
        type : {
            name : ''
        }
    });

    const [errorFields, setErrorFields] = useState({});

    const fieldLabels = {
        name : "Название",
        code : "Код",
        description : "Описание",
        templateId : "Шаблон фильтра" ,
        fields: "Поля датасета",
    }    

    let loadFunc;
    let loadFuncFilterTemplate;
    let loadParams = [];
    
    if(props.mode === 'edit'){
        loadFunc = dataHub.filterInstanceController.get;
        loadFuncFilterTemplate = dataHub.filterTemplateController.get;
        loadParams = [props.filterInstanceId];
    }

    function handleFilterInstanceDataLoaded(filterInstanceData){
        setFilterInstanceData(filterInstanceData);
        setErrorFields({});
        setPagename("Редактирование экземпляра фильтра: " + filterInstanceData.name);
    }

    function handleDataLoadFailed(message){
        enqueueSnackbar("Ошибка загрузки данных: " + message, {variant : "error"});
    }

    function handleFilterTemplateLoaded(filterTemplateData){
        setFilterTemplateData(filterTemplateData);
        
    }

    function handleChangeFilterTemplate(filterTemplateId, filterTemplateData, folderId){
        if(filterTemplateId !== filterInstanceData.templateId){
            setFilterTemplateData(filterTemplateData);
            setFilterInstanceData({
                id: props.filterInstanceId,
                folderId: props.folderId,
                templateId: filterTemplateId,
                type: filterTemplateData.type,
                name: filterInstanceData.name,
                code: filterInstanceData.code,
                description: filterInstanceData.description,
                fields: []
            });

            setErrorFields({
                filterInstanceName: errorFields.filterInstanceName,
                filterInstanceCode: errorFields.filterInstanceCode,
                filterInstanceDescription: errorFields.filterInstanceDescription,
                templateId : false,
                specificFields : "Не заполнены все необходимые поля фильтра"
            });  
        }
    }

    /*
        Data editing
    */

    function handleChangeField(field, value){
        setFilterInstanceData({
            ...filterInstanceData,
            [field] : value
        });
        setErrorFields({
            ...errorFields,
            [field]: (value === '') ? "Поле " + fieldLabels[field] + " не может быть пустым" : false
        });
    }

    function handleChangeSpecificField(newFilterInstanceData, errors){
        setFilterInstanceData(newFilterInstanceData);
        setErrorFields({
            filterInstanceName: errorFields.filterInstanceName,
            filterInstanceCode: errorFields.filterInstanceCode,
            filterInstanceDescription: errorFields.filterInstanceDescription,
            templateId : errorFields.filterInstanceDescription,
            ...errors
        });
    }

    /*
        Save and cancel
    */

    function handleSave(){
        let errors = {};
        let errorExists = false;

        // Проверка корректности заполнения полей
        Object.entries(filterInstanceData)
            .filter( ([fieldName, fieldValue]) => 
                (   fieldName !== "id" && 
                    (
                        (fieldValue === null && filterInstanceData.type.name !== 'DATE_RANGE' 
                                            && filterInstanceData.type.name !== 'RANGE'
                                            && filterInstanceData.type.name !== 'DATE_VALUE'
                                            && filterInstanceData.type.name !== 'SINGLE_VALUE_UNBOUNDED'
                                            && filterInstanceData.type.name !== 'VALUE_LIST_UNBOUNDED') || 
                        (typeof fieldValue === "string" && fieldValue.trim() === "") ||
                        (fieldName === "templateId" && fieldValue === 0) || 
                        (fieldName === "fields" && fieldValue.length === 0) || 
                        (fieldName === "fields" && fieldValue.filter(obj => !obj.name || !obj.description || 
                            (!obj.dataSetFieldId    && filterInstanceData.type.name !== 'SINGLE_VALUE_UNBOUNDED'
                                                    && filterInstanceData.type.name !== 'RANGE'
                                                    && filterInstanceData.type.name !== 'VALUE_LIST_UNBOUNDED'
                                                    && filterInstanceData.type.name !== 'DATE_RANGE' 
                                                    && filterInstanceData.type.name !== 'RANGE'
                                                    && filterInstanceData.type.name !== 'DATE_VALUE') ).length > 0)
                    ) 
                ))
            .reverse()
            .forEach( ([fieldName, fieldValue]) => 
                {
                    errors[fieldName] = true;
                    enqueueSnackbar("Недопустимо пустое значение в поле " + fieldLabels[fieldName], {variant : "error"});
                    errorExists = true;
                } );
        
        if(errorExists){
            setErrorFields(errors);
        }
        else{
            let func = props.mode === 'create' ? dataHub.filterInstanceController.add : dataHub.filterInstanceController.edit;
            setUploading(true);
            func(filterInstanceData, handleAddEditResult);
        }
    }

    function handleCancel(){
        props.onExit();
    }

    function handleAddEditResult(magrepResponse){
        if(magrepResponse.ok){
            props.onExit();
            enqueueSnackbar("Экземпляр фильтра успешно сохранен", {variant : "success"});
        }
        else{
            setUploading(false);
            let word = (props.mode === 'create') ? 'создания' : 'редактирования';
            enqueueSnackbar("Ошибка " + word + " экземпляра фильтра: " + magrepResponse.message, {variant : "error"});
        }
    }

    return(
        <DataLoader
            loadFunc = {loadFunc}
            loadParams = {loadParams}
            onDataLoaded = {handleFilterInstanceDataLoaded}
            onDataLoadFailed = {handleDataLoadFailed}
        >
            <DataLoader
                loadFunc = {loadFuncFilterTemplate}
                loadParams = {[filterInstanceData.templateId]}
                onDataLoaded = {handleFilterTemplateLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
            >
                {uploading ? <CircularProgress/> :

                    <DesignerPage 
                        onSaveClick={handleSave}
                        onCancelClick={handleCancel}
                        name = {pageName}
                    >
                        <DesignerTextField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.name}
                            value = {filterInstanceData.name}
                            onChange = {data => {handleChangeField('name', data)}}
                            //displayBlock
                            fullWidth
                            error = {errorFields.name}
                        />

                        <DesignerTextField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.code}
                            value = {filterInstanceData.code}
                            onChange = {data => {handleChangeField('code', data)}}
                            //displayBlock
                            fullWidth
                            error = {errorFields.code}
                        />

                        <DesignerTextField
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.description}
                            value = {filterInstanceData.description}
                            onChange = {data => handleChangeField('description', data)}
                           // displayBlock
                            fullWidth
                            error = {errorFields.description}
                        />

                        <DesignerFolderItemPicker
                            minWidth = {StyleConsts.designerTextFieldMinWidth}
                            label = {fieldLabels.templateId}
                            value = {filterTemplateData.name}
                            itemType = {FolderItemTypes.filterTemplate}
                            onChange = {handleChangeFilterTemplate}
                           // displayBlock
                            fullWidth
                            error = {errorFields.templateId}
                        />
                        {
                            filterTemplateData.type.name === 'SINGLE_VALUE_UNBOUNDED' || filterTemplateData.type.name === 'VALUE_LIST_UNBOUNDED'?
                                <UnboundedValueFields
                                    filterInstanceData = {filterInstanceData}
                                    onChange = {handleChangeSpecificField}
                                />
                            :
                            filterTemplateData.type.name === 'VALUE_LIST' ?
                                <ValueListFields
                                    filterInstanceData = {filterInstanceData}
                                    onChange = {handleChangeSpecificField}
                                />
                            :
                            filterTemplateData.type.name === 'HIERARCHY' || filterTemplateData.type.name === 'HIERARCHY_M2M'?
                                <HierTreeFields
                                    filterTemplateType = {filterTemplateData.type.name}
                                    filterInstanceData = {filterInstanceData}
                                    onChange = {handleChangeSpecificField}
                                />
                            :
                            filterTemplateData.type.name === 'DATE_RANGE' || filterTemplateData.type.name === 'RANGE'?
                                <RangeFields
                                    filterTemplateType = {filterTemplateData.type.name}
                                    filterInstanceData = {filterInstanceData}
                                    onChange = {handleChangeSpecificField}
                                />
                            :
                            filterTemplateData.type.name === 'DATE_VALUE'?
                                <DateValueFields
                                    filterInstanceData = {filterInstanceData}
                                    onChange = {handleChangeSpecificField}
                                />
                            :
                            filterTemplateData.type.name === 'TOKEN_INPUT'?
                                <TokenInputFields
                                    filterInstanceData = {filterInstanceData}
                                    onChange = {handleChangeSpecificField}
                                />
                            :                            
                                <div></div>
                        }
                    </DesignerPage>
                }
            </DataLoader>
        </DataLoader>
    );
}
