export const JobStatuses = {
    SCHEDULED : "SCHEDULED",
    RUNNING : "RUNNING", 
    COMPLETE : "COMPLETE",
    FAILED : "FAILED",
    CANCELING : "CANCELING",
    CANCELED : "CANCELED", 
    EXPORT : "EXPORT",
};

export const JobStatusMap = new Map([
    ['SCHEDULED', 'В ОЧЕРЕДИ'],
    ['RUNNING', 'ВЫПОЛНЯЕТСЯ'],
    ['COMPLETE', 'ЗАВЕРШЕН'],
    ['FAILED', 'ЗАВЕРШЕН С ОШИБКОЙ'],
    ['CANCELING', 'ОТМЕНЯЕТСЯ'],
    ['CANCELED', 'ОТМЕНЕН'],
    ['EXPORT', 'ЭКСПОРТ'],
]); 

export const ScheduleStatuses = {
    SCHEDULED: "SCHEDULED",
    RUNNING  : "RUNNING",  
    COMPLETE : "COMPLETE", 
    FAILED   : "FAILED",   
    EXPIRED  : "EXPIRED",  
    CHANGED  : "CHANGED",
    INACTIVE: "INACTIVE"
};

export const ScheduleStatusMap = new Map([
    ["SCHEDULED", "Ожидает выполнения"],
    ["RUNNING", "Выполняется"],
    ["COMPLETE", "Успешно выполнен"],
    ["FAILED", "Завершен с ошибкой"],
    ["EXPIRED", "Просрочен срок действия"],
    ["CHANGED", "Изменены параметры отчета"],
    ['INACTIVE', 'Неактивен']
]);

export const ScheduleTaskTypeMap = new Map([
  [ "EMAIL", "0"],
  [ "USER_TASK", "1"]  
]);	

  export const AggFunc = new Map([
    ['SUM', 'Сумма'], 
    ['COUNT', 'Количество'], 
    ['COUNT_DISTINCT', 'Кол-во уникальных'], 
    ['MIN', 'Мин.'], 
    ['MAX', 'Макс.'], 
    ['AVG', 'Средн.'] 
  ]);