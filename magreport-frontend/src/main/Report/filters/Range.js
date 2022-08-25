import 'date-fns';
import React, {useEffect, useState} from 'react';

// components 
import Box from '@material-ui/core/Box';

// local
import FilterStatus from './FilterStatus'

// styles
import {DatePickersCSS} from "./FiltersCSS";
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";

import {getRangeFields, getRangeFieldsValues} from "utils/reportFiltersFunctions";
import { Typography } from '@material-ui/core';

/**
 * @callback onChangeFilterValue
 * @param {number} filterValue - значение
 */

/**
 * Компонент настройки значений для фильтра типа RANGE
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {boolean} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 * @param {onChangeFilterValue} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 *                                                  filterValue - объект для передачи в сервис в массиве parameters
 * */
export default function Range(props) {

    const [startValue, setStartValue] = useState(null);
    const [endValue, setEndValue] = useState(null);
    const [toggleFilter, setToggleFilter] = useState(false);
    const [checkStatus, setCheckStatus] = useState("error")
    const classes = DatePickersCSS();

    let valueList = React.useRef({startValue: null, endValue: null});

    const mandatory = props.filterData.mandatory

    // Вычисляем id полей
    const {
        startFieldId,
        startFieldName,
        endFieldId,
        endFieldName
    } = getRangeFields(props.filterData);

    // Задаём значения по-умолчанию
    useEffect(() => {
        if (valueList.current.startValue === null) {

            const {
                startValue: defaultStartValue,
                endValue: defaultEndValue
            } = getRangeFieldsValues(props.lastFilterValue, startFieldId, endFieldId);

            valueList.current.startValue = defaultStartValue;
            valueList.current.endValue = defaultEndValue;
            setCheckStatus(defaultStartValue || !mandatory ? 'success' : 'error')
            setStartValue(defaultStartValue);
            setEndValue(defaultEndValue);
            setValue(defaultStartValue, defaultEndValue);
        }
    }, []) // eslint-disable-line

    useEffect(() => {
        if (props.toggleClearFilter !== toggleFilter) {
            valueList.current.startValue = null;
            valueList.current.endValue = null;
            setCheckStatus(mandatory ? 'error' : 'success')
            setStartValue(null);
            setEndValue(null);
            setToggleFilter(props.toggleClearFilter);
            setValue(null, null);
        }
    }, [props.toggleClearFilter]) // eslint-disable-line

    function handleValueChange(value, period) {
        if (period) {
            value = Number.parseFloat(value);
            value = Number.isNaN(value) ? null : value;
        }
        if (period === 'start') {
            valueList.current.startValue = value;
            setStartValue(value);
        }
        if (period === 'end') {
            valueList.current.endValue = value;
            setEndValue(value);
        }
        if ((valueList.current.startValue !== null && valueList.current.endValue !== null && valueList.current.endValue >= valueList.current.startValue)
            || (!mandatory && valueList.current.startValue === null && valueList.current.endValue === null)) {
            setCheckStatus('success')
        } else {
            setCheckStatus('error')
        }
        setValue(valueList.current.startValue, valueList.current.endValue);
    }

    function setValue(newStartValue, newEndValue) {
        newStartValue = Number.parseFloat(newStartValue);
        newEndValue = Number.parseFloat(newEndValue);
        let parameters = [];
        const isStartValid = !Number.isNaN(newStartValue);
        const isEndValid = !Number.isNaN(newEndValue);

        if (isStartValid && isEndValid) {
            parameters = [
                {
                    values: [
                        {
                            fieldId: startFieldId,
                            value: newStartValue
                        },
                        {
                            fieldId: endFieldId,
                            value: newEndValue
                        }
                    ]
                }
            ];
        }

        props.onChangeFilterValue({
            filterId: props.filterData.id,
            operationType: "IS_BETWEEN",
            validation: (isStartValid && isEndValid && newEndValue >= newStartValue)
            || (!mandatory && !isStartValid && !isEndValid) ? 'success' : 'error',
            parameters,
        });
    }

    return (
        <div className={classes.rangeRoot}>
            <Typography> {props.filterData.name} </Typography>
            <div className={classes.cDiv}>
                <Box
                    display="flex"
                    alignItems="center"
                    justifyContent="center"
                    marginRight="16px"
                >
                    <DesignerTextField
                        size="small"
                        type="number"
                        label={startFieldName}
                        value={startValue}
                        onChange={(value) => handleValueChange(value, "start")}
                    />
                </Box>
                <Box
                    display="flex"
                    alignItems="center"
                    justifyContent="center"
                    marginRight="16px"
                >
                    <DesignerTextField
                        size="small"
                        type="number"
                        label={endFieldName}
                        value={endValue}
                        onChange={(value) => handleValueChange(value, "end")}
                    />
                </Box>
                <span className={classes.status}>
                    <FilterStatus status={checkStatus}/>
                </span>
            </div>
        </div>
    );
}