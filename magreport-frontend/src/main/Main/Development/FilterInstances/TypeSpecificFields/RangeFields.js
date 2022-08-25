import React from 'react';

// material-ui
import Grid from '@material-ui/core/Grid';

// local
import DesignerTextField from '../../Designer/DesignerTextField';
import {convertDateRangeFilterToLocalData, convertDateRangeLocalToFilterData} from "./converters";
import {getRangeFieldsLabels} from "./helpers";

/**
 * @callback onChange
 * @param {Object} filterInstance - Экземпляр фильтра
 * @param {boolean} errorExists - флаг наличия ошибок заполнения
 */
/**
 * Компонент для редактирования полей шаблона фильтра RANGE, DATE_RANGE
 * @param {Object} props - свойства компонента
 * @param {string} props.filterTemplateType - тип шаблона фильтра (RANGE/DATE_RANGE)
 * @param {Object} props.filterInstanceData - объект filterInstance
 * @param {onChange} props.onChange - callback для изменения значений полей
 */
export default function RangeFields(props){

    let localData = convertDateRangeFilterToLocalData(props.filterInstanceData);
    const fieldLabels = getRangeFieldsLabels(props.filterTemplateType);

    function handleChangeField(fieldName, value){
        let parts = fieldName.split('.');
        localData[parts[0]][parts[1]] = value;
        handleDataChange();
    }

    function handleDataChange(){
        let {filterInstanceData, errors} = convertDateRangeLocalToFilterData(props.filterInstanceData, localData);
        props.onChange(filterInstanceData, errors);
    }

    return(<div>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <DesignerTextField
                        label = {fieldLabels.fieldName}
                        value = {localData.idField.name}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('idField.name',value)}}
                        error = {localData.idField.name.trim() === ''}
                    />
                </Grid>

                <Grid item xs={6}>
                    <DesignerTextField
                        label = {fieldLabels.fieldDescription}
                        value = {localData.idField.description}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('idField.description',value)}}
                        error = {localData.idField.description.trim() === ''}
                    />
                </Grid>                
            </Grid>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <DesignerTextField
                        label = {fieldLabels.startFieldName}
                        value = {localData.fromField.name}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('fromField.name',value)}}
                        error = {localData.fromField.name.trim() === ''}
                    />
                </Grid>

                <Grid item xs={6}>
                    <DesignerTextField
                        label = {fieldLabels.startFieldDescription}
                        value = {localData.fromField.description}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('fromField.description',value)}}
                        error = {localData.fromField.description.trim() === ''}
                    />
                </Grid>                
            </Grid>

            <Grid container>
                <Grid item xs={6} style={{paddingRight: '16px'}}>
                    <DesignerTextField
                        label = {fieldLabels.endFieldName}
                        value = {localData.toField.name}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('toField.name',value)}}
                        error = {localData.toField.name.trim() === ''}
                    />
                </Grid>

                <Grid item xs={6}>
                    <DesignerTextField
                        label = {fieldLabels.endFieldDescription}
                        value = {localData.toField.description}
                        fullWidth
                        displayBlock
                        onChange = {(value) => {handleChangeField('toField.description',value)}}
                        error = {localData.toField.description.trim() === ''}
                    />
                </Grid>                
            </Grid>            
        </div>)
}