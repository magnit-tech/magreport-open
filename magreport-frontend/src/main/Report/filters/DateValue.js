import 'date-fns';
import React, {useEffect, useState} from 'react';

// components 
import Box from '@material-ui/core/Box';

// date components 
import RuLocalizedUtils from 'utils/RuLocalizedUtils'
import ruLocale from "date-fns/locale/ru";
import {MuiPickersUtilsProvider, KeyboardDatePicker,} from '@material-ui/pickers';
import {dateToStringFormat} from 'utils/dateFunctions'

// local
import PredefinedMenu from "./PredefinedMenu";
import FilterStatus from './FilterStatus'

// styles
import { DateValueCSS } from "./FiltersCSS";

import {getValueField, getValueFieldValue} from "utils/reportFiltersFunctions";

/**
 * @callback onChangeFilterValue
 */
/**
 * Компонент настройки фильтра по дате у отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {boolean} props.toggleClearFilter - при изменении значения данного свойства требуется очистить выбор в фильтре
 * @param {onChangeFilterValue} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра
 *                                                  filterValue - объект для передачи в сервис в массиве parameters
 * */
export default function DateValue(props) {

    const [dt, setDt] = React.useState(null);
    const [toggleFilter, setToggleFilter] = React.useState(false);
    const [checkStatus, setCheckStatus] = useState("error")
    const classes = DateValueCSS();
    const mandatory = props.filterData.mandatory

    // Вычисляем id полей
    const {
        fieldId,
        fieldName
    } = getValueField(props.filterData);

    // Задаём значения по-умолчанию
    useEffect(() => {
        if (dt === null){
            const defaultDate = getValueFieldValue(props.lastFilterValue, fieldId);

            setCheckStatus(defaultDate || !mandatory ? 'success' : 'error');
            setDt(defaultDate);
            setValue(defaultDate);
        }
    }, []) // eslint-disable-line

    useEffect(() => {
        if (props.toggleClearFilter !== toggleFilter){
            setToggleFilter(props.toggleClearFilter);
            setCheckStatus(mandatory ? 'error' : 'success')
            handleDateChange(null)
        }
    }, [props.toggleClearFilter]) // eslint-disable-line

    function handleDateChange(date){
        setDt(date);
        if (date){
            setValue(date ? dateToStringFormat(date) : null)
        }
        else {
            setValue(null)
        }
        setCheckStatus(date || !mandatory ? 'success' : 'error')
    };

    function setValue(dt){
        let parameters = []
        if (dt){
            parameters = [
                {
                    values: [{
                        fieldId : fieldId,
                        value : dt
                    }]
                }
            ]
        }

        props.onChangeFilterValue({
            filterId : props.filterData.id,
            operationType: "IS_EQUAL",
            validation: dt || !mandatory ? 'success' : 'error',
            parameters,
        });      
    }

    return (
        <MuiPickersUtilsProvider utils={RuLocalizedUtils} locale={ruLocale}>
            <div className = {classes.root}>
                <KeyboardDatePicker
                    size="small"
                    autoOk
                    views={["year", "month", "date"]}
                    openTo="date"
                    variant="inline"
                    inputVariant="outlined"
                    format="dd.MM.yyyy"
                    id="dateValue"
                    label={fieldName}
                    value={dt}
                    onChange={handleDateChange}
                    KeyboardButtonProps={{
                        'aria-label': 'change date',
                    }}
                />
                <Box 
                    display="flex"
                    alignItems="center"
                    justifyContent="center"
                >
                    <PredefinedMenu 
                        filterType = 'DATE_VALUE'
                        onClickValue = {handleDateChange}
                    />
                    <FilterStatus status={checkStatus} />
                </Box>
            </div>
        </MuiPickersUtilsProvider>
    );
}