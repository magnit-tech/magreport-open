import React, {useState} from "react";
import {useSnackbar} from "notistack";


// data hub
import dataHub from "ajax/DataHub";

// local components
import DataLoader from "main/DataLoader/DataLoader";
import DesignerSelectField from "main/Main/Development/Designer/DesignerSelectField";
import DesignerTimeField from "main/Main/Development/Designer/DesignerTimeField";

import {
    checkDayError,
    checkDayWeekError,
    checkDifferenceTimeError,
    checkHourError,
    checkMinuteSecondError,
    checkWeekMonthError,
    DAY_END_MONTH,
    days,
    EVERY_DAY,
    EVERY_MONTH,
    EVERY_WEEK,
    getScheduleTypeId,
    getScheduleTypeName,
    getScheduleTypesForSelect,
    initialData,
    MANUAL,
    scheduleTypesInitial,
    WEEK_END_MONTH,
    WEEK_MONTH,
    weekDays,
    weeks
} from "./scheduleUtil";

/**
 *
 * @callback onChangeScheduleParameters
 * @param {Object} parameters
 * @param {Object} errors
 */

/**
 * Компонент редактирования параметров расписания
 * @param {Object} props - свойства компонента
 * @param {String} props.scheduleType - тип расписания
 * @param {Object} props.data - исходные данные
 * @param {Object} props.errors - данные об ошибках
 * @param {onChangeScheduleParameters} props.onChange - callback, вызываемый при изменении полей
 * @return {JSX.Element}
 * @constructor
 */
export default function ScheduleParameters(props) {

    const {enqueueSnackbar} = useSnackbar();

    const data = {...initialData, ...(props.data || {})};
    const errors = props.errors || {};

    const time = new Date(data.year, data.month, data.day,
        data.hour, data.minute, data.second, 0);

    const [scheduleTypes, setScheduleTypes] = useState(scheduleTypesInitial);
    const [serverUTCOffset, setServerUTCOffset] = useState(3);

    let typeId = typeof (data.scheduleTypeId) === "number" ? data.scheduleTypeId
        : getScheduleTypeId(scheduleTypes, props.scheduleType);
    let scheduleTypeName = getScheduleTypeName(scheduleTypes, typeId);

    const scheduleTypeLoadFunc = dataHub.scheduleController.getTypes;
    const serverDateTimeLoadFunc = dataHub.serverSettings.getDateTime;

    if (typeof(typeId) === "number" && typeId !== data.scheduleTypeId) {
        handleChangeScheduleTypeId(typeId);
    }

    function handleScheduleTypesDataLoaded(loadedData) {
        setScheduleTypes(loadedData);
    }

    function handleServerDateTimeDataLoaded(loadedData) {
        loadedData = loadedData.trim().toUpperCase();
        if (loadedData.slice(-1) === "Z") {
            setServerUTCOffset(0);
            return;
        }
        const offsetRegExp = /([-+])(\d{2}):(\d{2})/;
        const offsetString = loadedData.slice(-6);
        if(!offsetRegExp.test(offsetString)) {
            enqueueSnackbar(`Не удалось получить часовой пояс сервера из строки ${loadedData}. Буду использовать значение ${serverUTCOffset}`,
                {variant: "warning"});
            return;
        }
        const [/* skip */, sign, hourOffset, minuteOffset] = loadedData.match(offsetRegExp);
        const newOffset = (sign === "-" ? -1 : 1) * (+hourOffset + (+minuteOffset) / 60);
        setServerUTCOffset(newOffset);
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При загрузке данных произошла ошибка: ${message}`, {variant: "error"});
    }

    function handleChange(newData) {
        const hasTypeChanged = typeof (newData.scheduleTypeId) === "number";

        if (hasTypeChanged) {
            typeId = newData.scheduleTypeId;
            scheduleTypeName = getScheduleTypeName(scheduleTypes, typeId);
        }

        let newParameters = {scheduleTypeId: typeId};
        let newErrors = {scheduleTypeId: typeof (typeId) !== "number"};

        const oldTimeParameters = {
            hour: data.hour,
            minute: data.minute,
            second: data.second,
            differenceTime: data.differenceTime
        };

        // parameters
        if (scheduleTypeName !== MANUAL) {
            newParameters = {...newParameters, ...oldTimeParameters};
        }

        switch (scheduleTypeName) {
            case EVERY_DAY:
                break;
            case EVERY_WEEK:
                newParameters = {...newParameters, dayWeek: data.dayWeek};
                break;
            case EVERY_MONTH:
                newParameters = {...newParameters, day: data.day};
                break;
            case DAY_END_MONTH:
                newParameters = {...newParameters, dayEndMonth: data.dayEndMonth};
                break;
            case WEEK_MONTH:
                newParameters = {...newParameters, dayWeek: data.dayWeek, weekMonth: data.weekMonth};
                break;
            case WEEK_END_MONTH:
                newParameters = {...newParameters, dayWeek: data.dayWeek, weekEndMonth: data.weekEndMonth};
                break;
            case MANUAL:
                newParameters = {...newParameters, hour: 0, minute: 0, second: 0, differenceTime: 0};
                break;
            default: 
                break;
        }

        newParameters = {...newParameters, ...newData};

        // errors
        if (scheduleTypeName !== MANUAL) {
            newErrors = {
                ...newErrors,
                hour: checkHourError(newParameters.hour),
                minute: checkMinuteSecondError(newParameters.minute),
                second: checkMinuteSecondError(newParameters.second),
                differenceTime: checkDifferenceTimeError(newParameters.differenceTime)
            };
        }

        switch (scheduleTypeName) {
            case EVERY_DAY:
                break;
            case EVERY_WEEK:
                newErrors = {...newErrors, dayWeek: checkDayWeekError(newParameters.dayWeek)};
                break;
            case EVERY_MONTH:
                newErrors = {...newErrors, day: checkDayError(newParameters.day)};
                break;
            case DAY_END_MONTH:
                newErrors = {...newErrors, dayEndMonth: checkDayError(newParameters.dayEndMonth)};
                break;
            case WEEK_MONTH:
                newErrors = {
                    ...newErrors,
                    dayWeek: checkDayWeekError(newParameters.dayWeek),
                    weekMonth: checkWeekMonthError(newParameters.weekMonth)
                };
                break;
            case WEEK_END_MONTH:
                newErrors = {
                    ...newErrors,
                    dayWeek: checkDayWeekError(newParameters.dayWeek),
                    weekEndMonth: checkWeekMonthError(newParameters.weekEndMonth)
                };
                break;
            case MANUAL:
                break;
            default:
                break;
        }

        props.onChange(newParameters, newErrors);
    }

    function handleChangeScheduleTypeId(scheduleTypeId) {
        handleChange({scheduleTypeId});
    }

    function handleChangeDay(day) {
        handleChange({day});
    }

    function handleChangeDayWeek(dayWeek) {
        handleChange({dayWeek});
    }

    function handleChangeDayEndMonth(dayEndMonth) {
        handleChange({dayEndMonth});
    }

    function handleChangeWeekMonth(weekMonth) {
        handleChange({weekMonth});
    }

    function handleChangeTime(value) {
        const parametersData = {
            hour: value.getHours(),
            minute: value.getMinutes(),
            second: value.getSeconds(),
            differenceTime: (-value.getTimezoneOffset() / 60) - serverUTCOffset
        };
        handleChange(parametersData);
    }

    // building component
    const fields = [];

    fields.push(
        <DesignerSelectField
            key={fields.length}
            fullWidth
            label="Тип расписания"
            value={typeId}
            data={getScheduleTypesForSelect(scheduleTypes)}
            error={errors.scheduleTypeId === undefined ? true : errors.scheduleTypeId}
            onChange={handleChangeScheduleTypeId}
        />
    );

    if (scheduleTypeName === EVERY_WEEK ||
        scheduleTypeName === WEEK_MONTH ||
        scheduleTypeName === WEEK_END_MONTH) {
        fields.push(
            <DesignerSelectField
                key={fields.length}
                fullWidth
                label="День недели"
                value={data.dayWeek}
                data={weekDays}
                error={errors.dayWeek}
                onChange={handleChangeDayWeek}
            />
        )
    }

    if (scheduleTypeName === EVERY_MONTH) {
        fields.push(
            <DesignerSelectField
                key={fields.length}
                fullWidth
                label="День месяца"
                value={data.day}
                data={days}
                error={errors.day}
                onChange={handleChangeDay}
            />
        )
    }

    if (scheduleTypeName === DAY_END_MONTH) {
        fields.push(
            <DesignerSelectField
                key={fields.length}
                fullWidth
                label="Дней до конца месяца"
                value={data.dayEndMonth}
                data={days}
                error={errors.dayEndMonth}
                onChange={handleChangeDayEndMonth}
            />
        );
    }

    if (scheduleTypeName === WEEK_MONTH) {
        fields.push(
            <DesignerSelectField
                key={fields.length}
                fullWidth
                label="Порядковый номер от начала месяца"
                value={data.weekMonth}
                data={weeks}
                error={errors.weekMonth}
                onChange={handleChangeWeekMonth}
            />
        );
    }

    if (scheduleTypeName === WEEK_END_MONTH) {
        fields.push(
            <DesignerSelectField
                key={fields.length}
                fullWidth
                label="Порядковый номер от конца месяца"
                value={data.weekEndMonth}
                data={weeks}
                error={errors.weekEndMonth}
                onChange={handleChangeWeekMonth}
            />
        );
    }

    if (scheduleTypeName === MANUAL) {
        // fields.push(
        //     <DesignerTextField
        //         key={fields.length}
        //         fullWidth
        //         label="Уникальный идентификатор расписания"
        //         value={""}
        //         error={true}
        //         onChange={f => f}
        //     />
        // );
    }
    if (scheduleTypeName && scheduleTypeName !== MANUAL) {
        fields.push(
            <DesignerTimeField
                key={fields.length}
                fullWidth
                label="Время"
                value={time}
                onChange={handleChangeTime}
            />
        );
    }

    return (
        <DataLoader
            loadFunc={serverDateTimeLoadFunc}
            loadParams={[]}
            onDataLoaded={handleServerDateTimeDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
            <DataLoader
                loadFunc={scheduleTypeLoadFunc}
                loadParams={[]}
                onDataLoaded={handleScheduleTypesDataLoaded}
                onDataLoadFailed={handleDataLoadFailed}
            >
                {fields}
            </DataLoader>
        </DataLoader>

    );
}
