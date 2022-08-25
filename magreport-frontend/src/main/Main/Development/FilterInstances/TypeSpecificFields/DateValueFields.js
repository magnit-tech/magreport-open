
import React from 'react';

// material-ui
import Grid from '@material-ui/core/Grid';

// local
import DesignerTextField from '../../Designer/DesignerTextField';
import {convertDateValueLocalToFilterData, convertDateValueFilterToLocalData} from "./converters";

/**
 * @callback onChange
 * @param {Object} filterInstance - Экземпляр фильтра
 * @param {boolean} errorExists - флаг наличия ошибок заполнения
 */
/**
 * Компонент для редактирования полей шаблона фильтра DATE_VALUE
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterInstanceData - объект filterInstance
 * @param {onChange} props.onChange - callback для изменения значений полей
 */
export default function DateValueFields(props){

    let localData = convertDateValueFilterToLocalData(props.filterInstanceData);

    function handleChangeField(fieldName, value){
        let parts = fieldName.split('.');
        localData[parts[0]][parts[1]] = value;
        handleDataChange();
    }

    function handleDataChange(){
        let {filterInstanceData, errors} = convertDateValueLocalToFilterData(props.filterInstanceData, localData);
        props.onChange(filterInstanceData, errors);
    }

    return(<div>

            {/*<Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <DesignerTextField
                        label = "Название поля даты в отчёте"
                        value = {localData.idField.name}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('idField.name',value)}}
                        error = {localData.idField.name.trim() === ''}
                    />
                </Grid>

                <Grid item xs={6}>
                    <DesignerTextField
                        label = "Описание поля даты в отчёте"
                        value = {localData.idField.description}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('idField.description',value)}}
                        error = {localData.idField.description.trim() === ''}
                    />
                </Grid>                
            </Grid>*/}

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <DesignerTextField
                        label = "Название поля даты"
                        value = {localData.dateField.name}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('dateField.name',value)}}
                        error = {localData.dateField.name.trim() === ''}
                    />
                </Grid>

                <Grid item xs={6}>
                    <DesignerTextField
                        label = "Описание поля даты"
                        value = {localData.dateField.description}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('dateField.description',value)}}
                        error = {localData.dateField.description.trim() === ''}
                    />
                </Grid>                
            </Grid>

        </div>)
}