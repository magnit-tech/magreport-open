import React, {useState} from "react";
import {useSnackbar} from "notistack";

// data hub
import dataHub from "ajax/DataHub";

// local components
import DataLoader from "main/DataLoader/DataLoader";
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";

import {
    DAY_END_MONTH,
    EVERY_MONTH,
    EVERY_WEEK,
    getScheduleTypeId,
    getScheduleTypeName,
    getScheduleTypeDescription,
    getWeekDayName,
    MANUAL,
    scheduleTypesInitial,
    WEEK_END_MONTH,
    WEEK_MONTH,
    weekDays,
} from "./scheduleUtil";


/**
 * Компонент просмотра параметров расписания
 * @param {Object} props - свойства компонента
 * @param {String} props.scheduleType - тип расписания
 * @param {Object} props.data - исходные данные
 * @return {JSX.Element}
 * @constructor
 */
export default function ScheduleParametersViewer(props) {

    const {enqueueSnackbar} = useSnackbar();

    const [scheduleTypes, setScheduleTypes] = useState(scheduleTypesInitial);

    const data = props.data || {};

    let scheduleTypeId = typeof (data.scheduleTypeId) === "number" ? data.scheduleTypeId
        : getScheduleTypeId(scheduleTypes, props.scheduleType);
    let scheduleTypeName = getScheduleTypeName(scheduleTypes, scheduleTypeId);

    const loadFunc = dataHub.scheduleController.getTypes;

    function handleDataLoaded(loadedData) {
        setScheduleTypes(loadedData);
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При загрузке данных произошла ошибка: ${message}`, {variant: "error"});
    }

    // building component
    const fields = [];

    fields.push(
        <ViewerTextField
            key={fields.length}
            label="Тип расписания"
            value={getScheduleTypeDescription(scheduleTypes, scheduleTypeId)}
        />
    );

    if (scheduleTypeName === EVERY_WEEK ||
        scheduleTypeName === WEEK_MONTH ||
        scheduleTypeName === WEEK_END_MONTH) {
        fields.push(
            <ViewerTextField
                key={fields.length}
                label="День недели"
                value={getWeekDayName(weekDays, data.dayWeek)}
            />
        )
    }

    if (scheduleTypeName === EVERY_MONTH) {
        fields.push(
            <ViewerTextField
                key={fields.length}
                label="День месяца"
                value={data.day}
            />
        )
    }

    if (scheduleTypeName === DAY_END_MONTH) {
        fields.push(
            <ViewerTextField
                key={fields.length}
                label="Дней до конца месяца"
                value={data.dayEndMonth}
            />
        );
    }

    if (scheduleTypeName === WEEK_MONTH) {
        fields.push(
            <ViewerTextField
                key={fields.length}
                label="Порядковый номер от начала месяца"
                value={data.weekMonth}
            />
        );
    }

    if (scheduleTypeName === WEEK_END_MONTH) {
        fields.push(
            <ViewerTextField
                key={fields.length}
                label="Порядковый номер от конца месяца"
                value={data.weekEndMonth}
            />
        );
    }


    if (scheduleTypeName && scheduleTypeName !== MANUAL) {
        const hourString = (data.hour < 10 ? "0" : "") + data.hour;
        const minuteString = (data.minute < 10 ? "0" : "") + data.minute;
        const secondString = (data.second < 10 ? "0" : "") + data.second;
        const timeString = `${hourString}:${minuteString}:${secondString}`;
        fields.push(
            <ViewerTextField
                key={fields.length}
                label="Время"
                value={timeString}
            />
        );
    }

    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={[]}
            onDataLoaded={handleDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
            {fields}
        </DataLoader>
    );
}
