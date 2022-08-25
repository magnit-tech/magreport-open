export const PredefinedRanges = {
    yesterday: {name: 'Вчерашний день', fn: getYesterdayRange},
    today: {name: 'Сегодняшний день', fn: getTodayRange},
    currentWeek: {name: 'Текущая неделя', fn: getCurrentWeekRange},
    currentMonth: {name: 'Текущий месяц', fn: getCurrentMonthRange},
    last7Days: {name: 'Последние 7 дней', fn: getLast7DaysRange},
}

export const PredefinedValues = {
    today: {name: 'Сегодня', fn: getTodayDate},
    yesterday: {name: 'Вчера', fn: getYesterdayDate},
    monday: {name: 'Первый день недели', fn: getFirstDayOfWeek},
    firstDayOfMonth: {name: 'Первый день месяца', fn: getFirstDayOfMonth},
}

function addDays(dt, cntDays){
    const date = new Date()
    date.setDate(dt.getDate() + cntDays)
    return date
}

function getTodayDate(){
    return new Date()
}

function getYesterdayDate(){
    let date = addDays(new Date(), -1)
    return date
}

function getFirstDayOfWeek(){
    let date = new Date()
    const day = date.getDay()
    let monday = new Date()
    if(date.getDay() === 0){
        monday = addDays(date,-6)
    }
    else{
        monday.setDate(date.getDate() - (day-1))
    }
    return monday
}

function getLastDayOfWeek(){
    let date = new Date()
    const day = date.getDay()
    let sunday = new Date()
    if(date.getDay() === 0){
        sunday = date
    }
    else{
        sunday.setDate(date.getDate() + 7 - day)
    }
    return sunday
}

function getFirstDayOfMonth(){
    const date = new Date();
    return new Date(date.getFullYear(), date.getMonth(), 1);
}

function getLastDayOfMonth(){
    const date = new Date();
    return  new Date(date.getFullYear(), date.getMonth() + 1, 0);
}

function getYesterdayRange(){
    const dt = getYesterdayDate()
    return {
        startDate: dt,
        endDate: dt,
    }
}

function getTodayRange(){
    const dt = getTodayDate()
    return {
        startDate: dt,
        endDate: dt,
    }
}

function getCurrentWeekRange(){
    return {
        startDate: getFirstDayOfWeek(),
        endDate: getLastDayOfWeek(),
    }
}

function getCurrentMonthRange(){
    return {
        startDate: getFirstDayOfMonth(),
        endDate: getLastDayOfMonth(),
    }
}

function getLast7DaysRange(){
    const dt = getYesterdayDate()
    
    return {
        startDate: addDays(new Date(), -7),
        endDate: dt,
    }
}