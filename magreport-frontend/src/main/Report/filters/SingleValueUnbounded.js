import React, {useState, useEffect} from 'react';

// MUI
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import InputAdornment from '@material-ui/core/InputAdornment';
import Tooltip from '@material-ui/core/Tooltip';
import ClearIcon from '@material-ui/icons/Clear';

//local
import {SingleValueUnboundedCSS, AllFiltersCSS} from './FiltersCSS'
import {/*buildTextFromLastValues,*/ getCodeFieldId} from "utils/reportFiltersFunctions";
import FilterStatus from './FilterStatus';

/**
 * @callback onChangeFilterValue
 */
/**
 * Компонент настройки фильтра со списком значений у отчета
 * @param {Object} props - свойства компонента
 * @param {boolean} props.disabled - компонент используется для просмотра
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {boolean} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 * @param {onChangeFilterValue} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 */
export default function SingleValueUnbounded(props) {

    const commonClasses = AllFiltersCSS();
    const classes = SingleValueUnboundedCSS();

    const mandatory = props.filterData.mandatory
    const filterName = mandatory ? <span>{props.filterData.name}<i className={commonClasses.mandatory}>*</i></span> : <span>{props.filterData.name}</span>;
    
    /*
        Вычислить тип операции по предыдущему значению
    */
    function getOperationType(lastParameters){
        let operationType = 'IS_IN_LIST';
        if(lastParameters){
            operationType = lastParameters.operationType;
        }
        return operationType;
    }

    let operationType = getOperationType(props.lastFilterValue);

    /*
        Вычисляем поле фильтра с типом CODE_FIELD
    */
    const codeFieldId = getCodeFieldId(props.filterData);
    
    // Вычисляем значение с предыдущего запуска

    let tf = props.lastFilterValue?.parameters[0].values.find((item) => item.fieldId === codeFieldId).value;

    const [textValue, setTextValue] = useState(tf ? tf : '');
    const [checkStatus, setCheckStatus] = useState(mandatory && !textValue.length ? "error" : "success");

    // Необходимость очистки фильтров
    const [toggleFilter, setToggleFilter] = React.useState(false);
/*
    useEffect(() => {
        setTextValue(buildTextFromLastValues('SINGLE_VALUE_UNBOUNDED', props.lastFilterValue, codeFieldId, ''))

    }, [props.filterData, props.lastFilterValue]);
*/
    useEffect(() => {

        if (props.toggleClearFilter !== toggleFilter){
            setToggleFilter(props.toggleClearFilter);
            handleTextChange('');
        }
    }, [props.toggleClearFilter]) // eslint-disable-line

    function handleTextChange(newText){

        let parameters = [];
        
        if (newText.length>0) {
            parameters = [{
                values : [{
                    fieldId: codeFieldId,
                    value: newText
                }]
            }];
        }
        
        props.onChangeFilterValue(
            {
                filterId : props.filterData.id,
                operationType: operationType,
                validation: mandatory && !parameters.length ? "error" : "success",
                parameters: parameters
            }
        );

        setTextValue(newText);
        setCheckStatus(mandatory && !newText.length ? "error" : "success");
    }

    return (
        <TextField
            size = "small"
            disabled = {props.disabled}
            className={classes.textField}
            id="input-with-icon-textfield"
            label={filterName}
            value={textValue}
            variant="outlined"
            onChange={e => handleTextChange(e.target.value)}
            InputProps={!props.disabled ? 
                {endAdornment: (
                    <InputAdornment position="end"> 
                        <Tooltip title="Очистить фильтр" placement="top">
                            <IconButton
                                style={{marginRight: '4px'}}
                                edge='end'
                                size="small"
                                aria-label="clear"
                                onClick={() =>  handleTextChange('')}
                            >
                                <ClearIcon size="small" style={{fontSize: 22}}/>
                            </IconButton>
                        </Tooltip>
                        <FilterStatus key="3" status={checkStatus} />
                    </InputAdornment>
                    
                )} : null}
        />
    )
}