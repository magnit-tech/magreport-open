import dataHub from 'ajax/DataHub';

export const FolderItemTypes = {
    report : "report",
    job : "job",
    datasource : "datasource",
    dataset : "dataset",
    filterTemplate : "filterTemplate",
    filterInstance : "filterInstance",
    reportsDev : "reportsDev",
    roles : "roles",
    securityFilters: "securityFilters",
    excelTemplates: "excelTemplates",
    userJobs: 'userJobs',
    favorites: 'favorites',
    asm: 'asm',
    schedules: 'schedules',
    systemMailTemplates: 'systemMailTemplates',
    scheduleTasks: 'scheduleTasks',
    theme: 'theme'
}

export function dataHubItemController(itemType){
    let controller = itemType === FolderItemTypes.report || itemType === FolderItemTypes.favorites ? dataHub.folderController
                    :itemType === FolderItemTypes.reportsDev ? dataHub.reportController
                    :itemType === FolderItemTypes.job || itemType === FolderItemTypes.userJobs ? dataHub.reportJobController
                    :itemType === FolderItemTypes.dataset ? dataHub.datasetController
                    :itemType === FolderItemTypes.datasource ? dataHub.datasourceController
                    :itemType === FolderItemTypes.filterTemplate ? dataHub.filterTemplateController
                    :itemType === FolderItemTypes.filterInstance ? dataHub.filterInstanceController
                    :itemType === FolderItemTypes.roles ? dataHub.roleController
                    :itemType === FolderItemTypes.securityFilters ? dataHub.securityFilterController
                    :itemType === FolderItemTypes.asm ? dataHub.asmController
                    :itemType === FolderItemTypes.schedules ? dataHub.scheduleController
                    :itemType === FolderItemTypes.scheduleTasks ? dataHub.scheduleController
                    :itemType === FolderItemTypes.systemMailTemplates ? dataHub.serverMailTemplateController
                    :itemType === FolderItemTypes.theme ? dataHub.themeController
                    : null;
    return controller;
}

export function folderItemTypeName(itemType, changeRest){
    let name =   itemType === FolderItemTypes.report ? changeRest ? "ссылка на отчёт" : "ссылку на отчёт"
                :itemType === FolderItemTypes.favorites ? "избранное"
                :itemType === FolderItemTypes.job || itemType === FolderItemTypes.userJobs ? "задание"
                :itemType === FolderItemTypes.reportsDev ? "отчёт"
                :itemType === FolderItemTypes.dataset ? "набор данных"
                :itemType === FolderItemTypes.datasource ? "источник данных"
                :itemType === FolderItemTypes.filterTemplate ? "шаблон фильтра"
                :itemType === FolderItemTypes.filterInstance ? "экземпляр фильтра"
                :itemType === FolderItemTypes.roles ? "роль"
                :itemType === FolderItemTypes.securityFilters ? "фильтр безопасности"
                :itemType === FolderItemTypes.asm ? "объект ASM"
                :itemType === FolderItemTypes.schedules ? "расписание"
                :itemType === FolderItemTypes.scheduleTasks ? "отчет на расписании"
                :itemType === FolderItemTypes.systemMailTemplates ? "шаблоны системных писем"
                :itemType === FolderItemTypes.theme ? "дизайн"
                :"???";
    return name;
}

export function folderItemTypesName(itemType, changeRest){
    let name =   itemType === FolderItemTypes.report ? "Отчёты"
                :itemType === FolderItemTypes.favorites ? "Избранное"
                :itemType === FolderItemTypes.job || itemType === FolderItemTypes.userJobs ? "Задания"
                :itemType === FolderItemTypes.reportsDev ? "Разработка отчётов"
                :itemType === FolderItemTypes.dataset ? "Наборы данных"
                :itemType === FolderItemTypes.datasource ? "Источники данных"
                :itemType === FolderItemTypes.filterTemplate ? "Шаблоны фильтров"
                :itemType === FolderItemTypes.filterInstance ? "Экземпляры фильтров"
                :itemType === FolderItemTypes.roles ? "Роли"
                :itemType === FolderItemTypes.securityFilters ? "Фильтры безопасности"
                :itemType === FolderItemTypes.asm ? "Объекты ASM"
                :itemType === FolderItemTypes.schedules ? "Расписания"
                :itemType === FolderItemTypes.scheduleTasks ? "Отчёты на расписании"
                :itemType === FolderItemTypes.systemMailTemplates ? "Шаблоны системных писем"
                :itemType === FolderItemTypes.theme ? "Дизайн"
                :"???";
    return name;
}


export function dataHubRightsController(itemType){
    let controller = itemType === FolderItemTypes.report || itemType === FolderItemTypes.favorites ? dataHub.folderController
                    :itemType === FolderItemTypes.reportsDev ? dataHub.reportController
                    :itemType === FolderItemTypes.dataset ? dataHub.datasetController
                    :itemType === FolderItemTypes.datasource ? dataHub.datasourceController
                    :itemType === FolderItemTypes.filterTemplate ? dataHub.filterTemplateController
                    :itemType === FolderItemTypes.filterInstance ? dataHub.filterInstanceController
                    :itemType === FolderItemTypes.excelTemplates ? dataHub.excelTemplateController
                    :itemType === FolderItemTypes.securityFilters ? dataHub.securityFilterController
                    :itemType === FolderItemTypes.systemMailTemplates ? dataHub.serverMailTemplateController
                    :itemType === FolderItemTypes.theme ? dataHub.themeController
                    : null;
    return controller;
}