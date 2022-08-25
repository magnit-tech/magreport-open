import React from 'react';

// date utils
import {dateToStringFormat} from 'utils/dateFunctions'


// local
import ViewerFieldMapping from "main/Main/Development/Viewer/ViewerFieldMapping";

// styles
import {getRangeFields, getRangeFieldsValues} from "utils/reportFiltersFunctions";

/**
 * Компонент просмотра фильтра по диапазонам значений у отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 * @param {boolean} props.parseDate - если true, то выполняет парсинг значений как дат и отображения в формате по умолчанию
 * */
export default function RangeViewer(props) {

    const {
        startFieldId,
        startFieldName,
        endFieldId,
        endFieldName
    } = getRangeFields(props.filterData);

    const {
        startValue,
        endValue
    } = getRangeFieldsValues(props.lastFilterValue, startFieldId, endFieldId);

    const startDisplayValue = props.parseDate ? dateToStringFormat(new Date(startValue)) : startValue;
    const endDisplayValue = props.parseDate ? dateToStringFormat(new Date(endValue)) : endValue;

    return (
        <ViewerFieldMapping
            leftLabel={startFieldName}
            leftValue={startDisplayValue}
            rightLabel={endFieldName}
            rightValue={endDisplayValue}
        />
    );
}
