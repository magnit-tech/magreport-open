// constants
export const EVERY_DAY = "EVERY_DAY";
export const EVERY_WEEK = "EVERY_WEEK";
export const EVERY_MONTH = "EVERY_MONTH";
export const DAY_END_MONTH = "DAY_END_MONTH";
export const WEEK_MONTH = "WEEK_MONTH";
export const WEEK_END_MONTH = "WEEK_END_MONTH";
export const MANUAL = "MANUAL";

export const scheduleTypesInitial = [
    {id: 0, name: EVERY_DAY, description: "каждый день в заданное время"},
    {id: 1, name: EVERY_WEEK, description: "по указанному дню недели в заданное время"},
    {id: 2, name: EVERY_MONTH, description: "по указанному дню месяца в заданное время"},
    {id: 3, name: DAY_END_MONTH, description: "по указанному дню до конца месяца"},
    {
        id: 4,
        name: WEEK_MONTH,
        description: "по дню недели с соответствующим порядковым номером от начала месяца в заданное время"
    },
    {
        id: 5,
        name: WEEK_END_MONTH,
        description: "по дню недели с соответствующим порядковым номером от конца месяца в заданное время"
    },
    {id: 6, name: MANUAL, description: "По требованию"},
];

export const initialData = {
    scheduleTypeId: null,
    day: 1,
    month: 1,
    year: 2000,
    hour: 0,
    minute: 0,
    second: 0,
    dayWeek: 1,
    dayEndMonth: 1,
    weekEndMonth: 1,
    weekMonth: 1,
    differenceTime: 0,
};

export const weekDays = [
    {id: 1, name: "Понедельник"},
    {id: 2, name: "Вторник"},
    {id: 3, name: "Среда"},
    {id: 4, name: "Четверг"},
    {id: 5, name: "Пятница"},
    {id: 6, name: "Суббота"},
    {id: 7, name: "Воскресенье"},
];

export const days = [...Array(31).keys()].map(i => ({
    id: i + 1, name: "" + (i + 1)
}));

export const weeks = [...Array(5).keys()].map(i => ({
    id: i + 1, name: "" + (i + 1)
}));

// utility functions
export function getScheduleTypesForSelect(scheduleTypes) {
    return scheduleTypes.map(t => ({id: t.id, name: t.description}));
}

export function getScheduleTypeName(scheduleTypes, scheduleTypeId) {
    return (scheduleTypes.find(t => t.id === scheduleTypeId) || {}).name;
}

export function getScheduleTypeId(scheduleTypes, name) {
    name = name || "";
    return (scheduleTypes.find(t => t.name === name) || {}).id;
}

export function getScheduleTypeDescription(scheduleTypes, scheduleTypeId) {
    return (scheduleTypes.find(t => t.id === scheduleTypeId) || {}).description;
}

export function getWeekDayName(weekDaysParam, weekDayNumber) {
    return (weekDaysParam.find(d => d.id === weekDayNumber) || {name: "???"}).name;
}

export function checkDayWeekError(dayWeek) {
    return typeof (dayWeek) !== "number" || dayWeek < 1 || dayWeek > 7;
}

export function checkWeekMonthError(weekMonth) {
    return typeof (weekMonth) !== "number" || weekMonth < 1 || weekMonth > 5;
}

export function checkDayError(day) {
    return typeof (day) !== "number" || day < 1 || day > 31;
}

export function checkHourError(hour) {
    return typeof (hour) !== "number" || hour < 0 || hour > 24;
}

export function checkMinuteSecondError(minuteSecond) {
    return typeof (minuteSecond) !== "number" || minuteSecond < 0 || minuteSecond > 59;
}

export function checkDifferenceTimeError(differenceTime) {
    return typeof (differenceTime) !== "number";
}
