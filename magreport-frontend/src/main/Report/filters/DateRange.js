import 'date-fns';
import React, {useEffect, useState} from 'react';

// date utils
import RuLocalizedUtils from 'utils/RuLocalizedUtils'
import ruLocale from "date-fns/locale/ru";
import {MuiPickersUtilsProvider, KeyboardDatePicker,} from '@material-ui/pickers';
import {dateToStringFormat} from 'utils/dateFunctions'

// components
import Box from '@material-ui/core/Box';

// local
import PredefinedMenu from "./PredefinedMenu";
import FilterStatus from './FilterStatus'

// styles
import { DatePickersCSS } from "./FiltersCSS";

import {getRangeFields, getRangeFieldsValues} from "utils/reportFiltersFunctions";
import { Typography } from '@material-ui/core';

/**
 * @callback onChangeFilterValue
 */

/**
 * Компонент настройки фильтра диапазона дат отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {boolean} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 * @param {onChangeFilterValue} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 *                                                  filterValue - объект для передачи в сервис в массиве parameters
 * */
export default function DatesRange(props) {

    const [startDate, setStartDate] = React.useState(null);
    const [endDate, setEndDate] = React.useState(null);
    const [toggleFilter, setToggleFilter] = React.useState(false);
    const [checkStatus, setCheckStatus] = useState("error")
    const classes = DatePickersCSS();

    let valueList = React.useRef({});

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
        if (valueList.current.startDate === undefined){

            const {
                startValue: defaultStartDate,
                endValue: defaultEndDate
            } = getRangeFieldsValues(props.lastFilterValue, startFieldId, endFieldId);

            valueList.current.startDate = defaultStartDate;
            valueList.current.endDate = defaultEndDate;
            setCheckStatus(defaultStartDate || ! mandatory ? 'success' : 'error')
            setStartDate(defaultStartDate);
            setEndDate(defaultEndDate);
            setValue(defaultStartDate, defaultEndDate);
        }
    }, []) // eslint-disable-line

    useEffect(() => {
        if (props.toggleClearFilter !== toggleFilter){
            valueList.current.startDate = null;
            valueList.current.endDate = null;
            setCheckStatus(mandatory ? 'error' : 'success')
            setStartDate(null);
            setEndDate(null);
            setToggleFilter(props.toggleClearFilter);
            setValue(null, null);
        }
    }, [props.toggleClearFilter]) // eslint-disable-line

    function handleDateChange(date, period){
        if (period === 'start'){
            valueList.current.startDate = date ? dateToStringFormat(date) : null;
            setStartDate(date);
        }
        if (period === 'end'){
            valueList.current.endDate = date ? dateToStringFormat(date) : null;
            setEndDate(date);
        };
        if (!period){
            let {startDate, endDate} = date
            valueList.current.startDate = dateToStringFormat(startDate);
            valueList.current.endDate = dateToStringFormat(endDate);
            setStartDate(startDate);
            setEndDate(endDate);
        };
        if ((valueList.current.startDate && valueList.current.endDate) || (!mandatory && !valueList.current.startDate && !valueList.current.endDate)){
            setCheckStatus('success')
        }
        else {
            setCheckStatus('error')
        }
        setValue(valueList.current.startDate, valueList.current.endDate); 
    };

    function setValue(st, en){
        let parameters = []
        if (st && en){
            parameters= [
                {
                    values: [{
                        fieldId : startFieldId,
                        value : st
                    },
                    {
                        fieldId : endFieldId,
                        value : en
                    }]
                }
            ]
        }

        props.onChangeFilterValue({
            filterId : props.filterData.id,
            operationType: "IS_BETWEEN",
            validation: (st && en) || (!mandatory && !st && !en) ? 'success' : 'error',
            parameters,
        });      
    }

    return (
        <MuiPickersUtilsProvider utils={RuLocalizedUtils} locale={ruLocale}>
            <div className={classes.rangeRoot}>
                <Typography> {props.filterData.name}  </Typography>
                <div className={classes.cDiv}>
                    <div className={classes.fieldsDiv}>
                        <Box 
                            display="flex"
                            alignItems="center"
                            justifyContent="center"
                            marginRight = "16px"
                        >
                            <KeyboardDatePicker
                                size="small"
                                autoOk
                                views={["year", "month", "date"]}
                                openTo="date"
                                variant="inline"
                                inputVariant="outlined"
                                format="dd.MM.yyyy"
                                id="dateStart"
                                label={startFieldName}
                                value={startDate}
                                onChange={(date) => handleDateChange(date, "start")}
                                KeyboardButtonProps={{
                                    'aria-label': 'change date',
                                }}
                            />
                        </Box>
                        <Box 
                            display="flex"
                            alignItems="center"
                            justifyContent="center"
                            marginRight = "16px"
                        >
                            <KeyboardDatePicker
                                size="small"
                                autoOk
                                views={["year", "month", "date"]}
                                openTo="date"
                                variant="inline"
                                inputVariant="outlined"
                                format="dd.MM.yyyy"
                                id="dateEnd"
                                label={endFieldName}
                                minDateMessage={`${endFieldName} должна быть не ранее ${startFieldName}`}
                                value={endDate}
                                minDate={startDate}
                                onChange={(date) => handleDateChange(date, "end")}
                                KeyboardButtonProps={{
                                    'aria-label': 'change date',
                                }}
                            />
                        </Box>
                    </div>
                    <PredefinedMenu 
                        filterType = 'DATE_RANGE'
                        onClickValue = {dates => handleDateChange(dates, null)}
                    />
                    <span className={classes.status}>
                        <FilterStatus status={checkStatus} />
                    </span>
                </div>
            </div>
        </MuiPickersUtilsProvider>
    );
}