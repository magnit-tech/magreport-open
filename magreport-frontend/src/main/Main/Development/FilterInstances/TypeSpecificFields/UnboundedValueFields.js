import React from 'react'

// material-ui

import Grid from '@material-ui/core/Grid';

// local

import DesignerTextField from '../../Designer/DesignerTextField';

/**
 *
 * @callback onChange
 * @param {Object} filterInstance - Экземпляр фильтра
 * @param {boolean} errorExists - флаг наличия ошибок заполнения
 */
/**
 * Компонент для редактирования полей шаблона фильтра VALUE_LIST
 * @param {Boolean} props.readOnly - только просмотр
 * @param {Object} props.filterInstanceData - объект filterInstance
 * @param {onChange} props.onChange - callback для изменения значений полей
 */

export default function UnboundedValueFields(props){
    let {localData, errorFields} = convertFilterToLocalData(props.filterInstanceData);

    function handleChangeField(fieldName, value) {

        localData.idField[fieldName] = value;

        handleChangeData();
    }

    function handleChangeData(){
        let {filterInstanceData, errors} = convertLocalToFilterData(props.filterInstanceData, localData);
        props.onChange(filterInstanceData, errors);
    }

    return (
        <Grid container >
            <Grid item xs={6} style={{paddingRight: '16px'}}>
                <DesignerTextField
                    label = "Название параметра"
                    disabled = {props.readOnly}
                    value = {localData.idField.name}
                    fullWidth
                    //displayBlock
                    onChange = {(value) => {handleChangeField('name',value)}}
                    error = {errorFields.idField.name}
                />
            </Grid>

            <Grid item xs={6}>
                <DesignerTextField
                    label = "Описание параметра"
                    disabled = {props.readOnly}
                    value = {localData.idField.description}
                    fullWidth
                    //displayBlock
                    onChange = {(value) => {handleChangeField('description',value)}}
                    error = {errorFields.idField.description}
                />
            </Grid>
        </Grid>
    )

}

function convertFilterToLocalData(filterInstanceData) {
    let idField = {
        name : '',
        description : ''
    };

    for(let f of filterInstanceData.fields){
        if(f.type === 'ID_FIELD'){
            idField.name = f.name;
            idField.description = f.description;
        }
    }

    let localData = {
        idField : idField
    }

    let errorFields = {
        idField:{
            name : (localData.idField.name.trim() === ''),
            description : (localData.idField.description.trim() === '')
        }
    }

    return {
        localData,
        errorFields
    }
}

function convertLocalToFilterData(filterInstanceData, localData) {
    let newFilterInstanceData = {
        ...filterInstanceData,
        fields : [
            {
                level : 1,
                type : "ID_FIELD",
                name : localData.idField.name,
                description : localData.idField.description
            },
            {
                level : 1,
                type : "CODE_FIELD",
                name : localData.idField.name,
                description : localData.idField.description
            }            
        ]
    }

    let errors = {
        fieldIdName : localData.idField.name.trim() === '' ? "Не задано имя поля типа ID" : undefined,
        fieldIdDescription : localData.idField.description.trim() === '' ? "Не задано описание поля типа ID" : undefined,
    }

    return {
        filterInstanceData: newFilterInstanceData,
        errors: errors
    };
}