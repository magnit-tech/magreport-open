import React from 'react';

// date components 
import {dateToStringFormat} from 'utils/dateFunctions'

// local
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";

// styles
import {DateValueCSS} from "../../filters/FiltersCSS";

import {getValueField, getValueFieldValue} from "utils/reportFiltersFunctions";


/**
 * Компонент просмотра фильтра по дате у отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * */
export default function DateValueViewer(props) {

    const classes = DateValueCSS();

    const {
        fieldId,
        fieldName
    } = getValueField(props.filterData);

    const value = getValueFieldValue(props.lastFilterValue, fieldId);

    const displayValue = dateToStringFormat(new Date(value));

    return (
        <div className={classes.root}>
            <ViewerTextField
                label={fieldName}
                value={displayValue}
            />
        </div>
    );
}
