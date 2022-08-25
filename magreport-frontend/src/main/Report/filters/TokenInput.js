import React, { useEffect, useRef, useState} from 'react';

// components
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { CircularProgress } from '@material-ui/core';

// local
import FilterStatus from './FilterStatus'

// dataHub
import dataHub from 'ajax/DataHub';

// styles
import { AllFiltersCSS } from "./FiltersCSS.js"

/**
 * @param {*} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {*} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {*} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 * @param {*} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 *                                                  filterValue - объект для передачи в сервис в массиве parameters
 * @param {*} props.disabled - отключен
 */
export default function  TokenInput(props){
    const timer = useRef(0);
    const classes = AllFiltersCSS()
    const mandatory = props.filterData.mandatory
    const [checkStatus, setCheckStatus] = useState("error")

    let codeFieldId = -1;
    let nameFieldId = -1;
    let nameFilterInstanceFieldId = -1;

    if(props.filterData && props.filterData.fields){
        for(let f of props.filterData.fields){
            if(f.type === "CODE_FIELD"){
                codeFieldId = f.id;
            }
            if(f.type === "NAME_FIELD"){
                nameFieldId = f.id;
                nameFilterInstanceFieldId = f.filterInstanceFieldId;
            }
        }
    }    

    const [toggleFilter, setToggleFilter] = React.useState(false);
    const [openAsyncEntity, setOpenAsyncEntity] = React.useState(false);
    const [optionsAsyncEntity, setOptionsAsyncEntity] = React.useState([]);
    const defaultInputValues = useRef([]);
    useEffect(() => {
        if (defaultInputValues.current.length === 0){
            defaultInputValues.current = getInputValuesFromLastValueAndSetNewParameters(props.lastFilterValue);
        }
    }, []) // eslint-disable-line
    
    const [inputValues, setInputValues] = useState(defaultInputValues.current);

    const loadingAsync = openAsyncEntity && optionsAsyncEntity.length === 0;

    /*
        Обработка изменения значений фильтра
    */
    function handleChangeValues(newValuesIDs, arr) {
        
        let parameters = [];
        let values = [];

        for(let id of newValuesIDs){
            values.push({
                fieldId: codeFieldId,
                value: id
            });
        }

        if(values.length > 0){
            parameters.push({
                values: values
            });    
        }        

        props.onChangeFilterValue({
            filterId : props.filterData.id,
            operationType: "IS_IN_LIST",
            validation: mandatory && !parameters.length ? "error" : "success",
            parameters: parameters,
            lastParametrs: arr
        });        
    }

    /*
        Получение сохранённого значения фильтра и задание этого значения в качестве результата выбора фильтра через onChangeFilterValue:
        необходимость этого специфична для фильтра типа TOKEN_INPUT - для данного типа фильтра объект lastParamters обогащается 
        дополнительным полями и не равен объекту parameters (отличается от него по формату).
    */
    function getInputValuesFromLastValueAndSetNewParameters(filterValues){
        let values = [];
        let valuesIds = [];

        if(filterValues && filterValues.lastParametrs) {
            for (let v of filterValues.lastParametrs) {
                valuesIds.push(v.value);
            }
            values = filterValues.lastParametrs
        } else if(filterValues && filterValues.parameters){
            for(let p of filterValues.parameters){
                let idValue = -1;
                let nameValue = '???';
                for(let v of p.values){
                    if(v.fieldId === codeFieldId){
                        idValue = v.value;
                        valuesIds.push(v.value);
                    }
                    else if(v.fieldId === nameFieldId){
                        nameValue = v.value;
                    }
                }
                values.push({
                    value: idValue,
                    label: nameValue
                });
            }
        }

        handleChangeValues(valuesIds, values);
        setCheckStatus(mandatory && !values.length ? "error" : "success")
        setInputValues(values)

        return values;
    }

    function getTokens(inputValue){
        let requestBody = {
            filterId : props.filterData.filterInstanceId,
            filterFieldId : nameFilterInstanceFieldId,
            searchValue : inputValue,
            likenessType : "CONTAINS",
            isCaseInsensitive : true, 
            maxCount : 15
        };

        dataHub.filterInstanceController.getValues(requestBody, handleGetTokenInputDataResponse);
    }

    function handleGetTokenInputDataResponse(magrepResponse) {
        
        let arr = [];
        if(magrepResponse.ok){

            let fieldIDs={}
            for (let f of magrepResponse.data.filter.fields){
                fieldIDs[f.type]=f.id
            }

            for (let element of magrepResponse.data.tuples){
                let code
                let name
                for (let i of element.values){
                    if (i.fieldId === fieldIDs.CODE_FIELD){
                        code = i.value;
                    }
                    else if (i.fieldId === fieldIDs.NAME_FIELD){
                        name = i.value;
                    };
                };

                arr.push({
                    value: code,
                    label: name        
                });
            };
            let sort_arr = arr.sort(
                function (a, b) {
                    if (a.label < b.label) {
                        return -1;
                    }
                    if (a.label > b.label) {
                        return 1;
                    }
                    return 0;
                }
            )
            setOptionsAsyncEntity(sort_arr);
        }
    };

    const handleInputChange = (e) => {
        const inputValue = e.target.value;
        if (inputValue.length>1){
            if (timer.current > 0){
              clearInterval(timer.current); // удаляем предыдущий таймер
            }
            timer.current = setTimeout(() => {
                getTokens(inputValue.toLowerCase());
            }, 1000);
        };
    }

    const handleChange = (e, value) => {
        let valuesIds = [];
        let arr = []

        if (value.length > 0) {
            for (let element of value){
                valuesIds.push(element.value);
                arr.push({
                    value: element.value,
                    label: element.label,        
                });
            }
        }

        handleChangeValues(valuesIds, arr)
        setCheckStatus(mandatory && !arr.length ? "error" : "success")
        setInputValues(arr)
    };

    useEffect(() => {
        if (props.toggleClearFilter !== toggleFilter){
            setInputValues([])  
            setToggleFilter(props.toggleClearFilter);
            setCheckStatus(mandatory ? "error" : "success")
            
            props.onChangeFilterValue({
                filterId : props.filterData.id,
                operationType: "IS_IN_LIST",
                validation: mandatory ? "error" : "success",
                parameters: []
            });
        }
    }, [props.toggleClearFilter]) // eslint-disable-line

    useEffect(() => {
        if(openAsyncEntity && optionsAsyncEntity.length === 0) {
            props.onChangeFilterValue(
                {
                    filterId : props.filterData.id,
                    operationType: "IS_IT_SEARCHING",
                    validation: 'waiting',
                    parameters: []
                }
            );
        } else {
            props.onChangeFilterValue(
                {
                    filterId : props.filterData.id,
                    operationType: "IS_IT_SEARCHING",
                    validation: 'success',
                    parameters: []
                }
            );
        }
    }, [loadingAsync]) // eslint-disable-line
    
    const name = props.filterData.mandatory ? <span>{props.filterData.name}<i className={classes.mandatory}>*</i></span> : <span>{props.filterData.name}</span>;

    return (
        <div className={classes.divAutocomplete}>
            <Autocomplete
                size="small"
                id={"tokenInput" + props.filterData.id}
                value={inputValues}
                disabled = {props.disabled}
                style={{ width: '100%' }}
                multiple
                open={openAsyncEntity}
                onOpen={() => {
                    setOpenAsyncEntity(true);
                }}
                onClose={() => {
                    setOpenAsyncEntity(false);
                }}
                getOptionSelected={(option, value) => option.value === value.value}
                options={optionsAsyncEntity}
                getOptionLabel={(option) => {return option.label;}}
                filterSelectedOptions
                loading={loadingAsync}
                loadingText={'Загрузка...'}
                noOptionsText={'Элементы не найдены'}
                clearText='Очистить фильтр'
                openText='Показать'
                onChange={(e, value) => {handleChange(e, value);}}
                renderInput={params => (
                    <TextField
                    {...params}
                    label={name}
                    fullWidth
                    variant="outlined"
                    onChange={(e) => {handleInputChange(e);}}
                    InputProps={{
                        ...params.InputProps,
                        endAdornment: (
                        <React.Fragment>
                            {loadingAsync ? <CircularProgress color="inherit" size={20} /> : null}
                            {
                                {...params.InputProps.endAdornment, 
                                    props: {...params.InputProps.endAdornment.props, 
                                        children: [...params.InputProps.endAdornment.props.children, <FilterStatus key="3" status={checkStatus} />]}}
                            }
                        </React.Fragment>
                        ),
                    }}
                    />
                )}
            />
        </div>
    );
};