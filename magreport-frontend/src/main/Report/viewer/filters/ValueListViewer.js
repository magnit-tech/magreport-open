import React from 'react';

// local
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";

import {buildTextFromLastValues, getCodeFieldId} from "utils/reportFiltersFunctions";

/**
 * Компонент просмотра фильтра со списком значений у отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterData - данные фильтра (объект ответа от сервиса)
 * @param {Object} props.lastFilterValue - объект со значениями фильтра из последнего запуска (как приходит от сервиса)
 */
export default function ValueListViewer(props) {

    const codeFieldId = getCodeFieldId(props.filterData);

    const filterName = props.filterData.name

    const textValue = buildTextFromLastValues( props.filterData.type, props.lastFilterValue, codeFieldId, "; ", props.filterData.fields);

    return (
        <ViewerTextField
            label={filterName}
            value={textValue}
            multiline
        />
    );
}
