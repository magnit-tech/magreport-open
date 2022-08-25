import React from 'react';

import FolderOpenIcon from '@material-ui/icons/FolderOpen';
import AlarmIcon from '@material-ui/icons/Alarm';
import BuildIcon from '@material-ui/icons/Build';
import CodeIcon from '@material-ui/icons/Code';
import StarsIcon from '@material-ui/icons/Stars';
import ScheduleIcon from '@material-ui/icons/Schedule';
import {FolderItemTypes} from '../../FolderContent/FolderItemTypes';


 const SidebarItems = {
    reports : {
        key : "REPORTS_ITEM",
        text : "Отчёты",
        icon : (<FolderOpenIcon/>),
        folderTree : true,
        folderItemType: FolderItemTypes.report
    },
    favorites: {
        key: 'FAVORITES_ITEM',
        text: 'Избранное',
        icon: (<StarsIcon />),
        folderTree : false,
        folderItemType: FolderItemTypes.favorites
    },
    jobs : {
        key : "TASKS_ITEM",
        text : "Задания",
        icon : (<AlarmIcon/>),
        folderTree : false,
        folderItemType : FolderItemTypes.job
    },
    admin : {
        key : "ADMIN_ITEM",
        text : "Администрирование",
        icon : (<BuildIcon/>),
        permission : "ADMIN",
        subItems : {
            roles : {
                key : "ROLES_ITEM",
                text : "Роли",
                folderTree : true,
                folderItemType: FolderItemTypes.roles
            },
            users : {
                key : "USERS_ITEM",
                text : "Пользователи"
            },
            securityFilters : {
                key : "SECURITY_FILTERS_ITEM",
                text : "Фильтры безопасности",
                folderTree : true,
                folderItemType: FolderItemTypes.securityFilters
            },
            userJobs : {
                key : "USER_TASKS_ITEM",
                text : "Задания пользователей",
                folderTree : false,
                folderItemType : FolderItemTypes.userJobs,
            },
            ASMAdministration : {
                key : "ASM_ADMIN_ITEM",
                text : "Управление ASM"
            },
            logs : {
                key : "LOGS_ITEM",
                text : "Логи"
            },
            settings : {
                key : "ADMIN_SETTINGS",
                text : "Настройки"
            },
            mailTexts : {
                key : "SYSTEM_MAIL_TEXT_ITEM",
                text : "Тексты системных писем",
                folderTree : true,
                folderItemType: FolderItemTypes.systemMailTemplates
            },
            mailSender : {
                key : "MAIL_SENDER",
                text : "Рассылка писем"
            },
            theme : {
                key : 'DESIGNER',
                text : 'Дизайн',
                folderTree : false,
                folderItemType: FolderItemTypes.theme
            }

        }
    },
    development : {
        key : "DEVELOPMENT_ITEM",
        text : "Разработка",
        icon : (<CodeIcon/>),
        permission : "DEVELOPER",
        subItems : {
            datasources : {
                key : "DATASOURCES_ITEM",
                text : "Источники данных",
                folderTree : true,
                folderItemType: FolderItemTypes.datasource
            },
            datasets : {
                key : "DATASETS_ITEM",
                text : "Наборы данных",
                folderTree : true,
                folderItemType: FolderItemTypes.dataset
            },
            filterTemplates : {
                key : "FILTERTEMPLATES_ITEM",
                text : "Шаблоны фильтров",
                folderTree : true,
                folderItemType: FolderItemTypes.filterTemplate
            },            
            filterInstances : {
                key : "FILTERINSTANCES_ITEM",
                text : "Экземпляры фильтров",
                folderTree : true,
                folderItemType: FolderItemTypes.filterInstance
            },
            reportsDev : {
                key : "REPORTSDEV_ITEM",
                text : "Разработка отчётов",
                folderTree : true,
                folderItemType: FolderItemTypes.reportsDev
            }                 
        }
    },
    schedule : {
        key : "SCHEDULE_ITEM",
        text : "Расписание",
        icon : (<ScheduleIcon />),
        permission : "ADMIN",
        subItems : {
            schedules: {
                key: "SCHEDULES_ITEM",
                text: "Расписания",
                folderTree: false,
                folderItemType: FolderItemTypes.schedules,
            },
            scheduleTasks : {
                key : "SCHEDULED_REPORTS",
                text : "Отчеты на расписании",
                folderTree : false,
                folderItemType: FolderItemTypes.scheduleTasks
            },             
        }
    }
};

export default SidebarItems;