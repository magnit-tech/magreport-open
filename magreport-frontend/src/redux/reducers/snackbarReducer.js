import {folderItemTypeName} from 'main/FolderContent/FolderItemTypes';

import { SHOWSNACKBAR, HIDESNACKBAR,
        FOLDER_CONTENT_ADD_FOLDER_FAILED, FOLDER_CONTENT_FOLDER_ADDED, FOLDER_CONTENT_FOLDER_EDITED, 
        FOLDER_CONTENT_FOLDER_DELETED, FOLDER_CONTENT_DELETE_FOLDER_FAILED, FOLDER_CONTENT_EDIT_FOLDER_FAILED,
        USERS_LIST_DELETE_FAILED, USERS_LIST_ADD_FAILED, USERS_LIST_ADDED, USERS_LIST_DELETED, ROLE_USERS_LIST_LOAD_FAILED,
        FOLDER_CONTENT_ITEM_DELETED, FOLDER_CONTENT_ITEM_DELETE_FAILED, JOB_CANCEL_FAILED, FOLDER_CONTENT_SEARCH_RESULTS_FAILED, 
        FOLDER_CONTENT_PARENT_FOLDER_CHANGED_FAIL, FOLDER_CONTENT_PARENT_FOLDER_CHANGED, FOLDER_CONTENT_FOLDER_COPIED, FOLDER_CONTENT_FOLDER_COPIED_FAILED,
        FOLDER_CONTENT_ITEM_MOVED_FAILED, FOLDER_CONTENT_ITEM_MOVED, FOLDER_CONTENT_ITEM_COPIED_FAILED, FOLDER_CONTENT_ITEM_COPIED,
        USERS_LIST_LOAD_FAILED, USERS_LIST_MANAGE_FAILED, USERS_LIST_MANAGE, USERS_LIST_ALL_CHECKED, USERS_LIST_ROLE_DELETED, 
        USERS_LIST_ROLE_DELETE_FAILED, FOLDERS_TREE_PARENT_CHANGED_FAIL, JOB_SQL_LOAD_FAILED, 
        REPORT_TEMPLATES_LOAD_FAILED, REPORT_TEMPLATES_ADDED, REPORT_TEMPLATES_ADD_FAILED, REPORT_TEMPLATES_DELETED, REPORT_TEMPLATES_DELETE_FAILED,
        REPORT_TEMPLATES_SET_DEFAULT_SUCCESS, REPORT_TEMPLATES_SET_DEFAULT_FAILED, SCHEDULE_TASK_RUN_FAILED, SCHEDULE_TASK_RUN_OK,
        TASK_SWITCH_FAILED
    } from 'redux/reduxTypes';
    
const initialState = {
    isOpen: false,
    text: "",
    color: "",
}

const info = {...initialState, isOpen: true,}

const errorInfo = {...info, color: "error",}

const successInfo = {...info, color: "success",}

export const snackbarReducer = (state = initialState, action) => {
    switch (action.type){
        case SHOWSNACKBAR:
            return {
                ...info, 
                text: action.text,
                color: action.color
            }
        
        case HIDESNACKBAR:
            return {
                isOpen: false,
                text: action.text,
                color: action.color
            }
        
        case FOLDER_CONTENT_ADD_FOLDER_FAILED:
            return {...errorInfo, text : action.data}
        
        case FOLDER_CONTENT_FOLDER_ADDED:
            return {...successInfo, text : "Каталог создан: " + action.data.name}
        
        case FOLDER_CONTENT_EDIT_FOLDER_FAILED:
            return {...errorInfo, text: action.data}
        
        case FOLDER_CONTENT_FOLDER_EDITED:
            return {...successInfo, text : "Каталог изменен: " + action.data.name}
        
        case FOLDER_CONTENT_DELETE_FOLDER_FAILED:
            const textForNotEmpty = 'Нельзя удалить непустой каталог',
                  textForOtherError = "Ошибка удаления каталога " + action.folderData.name + ": " + action.errorMessage;
            return {
                ...errorInfo, 
                text: action.errorMessage.indexOf('is not empty') !== -1 ? textForNotEmpty : textForOtherError
            }
        
        case FOLDER_CONTENT_FOLDER_DELETED:
            return {...successInfo, text : "Каталог " + action.folderData.name + " удален"}

        case FOLDER_CONTENT_PARENT_FOLDER_CHANGED_FAIL:
            return {...errorInfo, text : `Не удалось переместить каталог: ${action.data.name}`}
        
        case FOLDER_CONTENT_PARENT_FOLDER_CHANGED:
            return {...successInfo, text : "Каталог " + action.data.name + " перемещен"}

        case FOLDER_CONTENT_FOLDER_COPIED_FAILED:
            return {...errorInfo, text : `Не удалось скопировать каталог: ${action.data.name}`}
        
        case FOLDER_CONTENT_FOLDER_COPIED:
            return {...successInfo, text : "Каталог " + action.data.name + " скопирован"}

        case FOLDER_CONTENT_ITEM_MOVED_FAILED:
            return {...errorInfo, text : `Не удалось переместить ${action.textForSnackbar}`}
        
        case FOLDER_CONTENT_ITEM_MOVED:
            return {...successInfo, text : action.textForSnackbar + " перемещен"}

        case FOLDER_CONTENT_ITEM_COPIED_FAILED:
            return {...errorInfo, text : `Не удалось скопировать ${action.textForSnackbar}`}
        
        case FOLDER_CONTENT_ITEM_COPIED:
            return {...successInfo, text : action.textForSnackbar + " скопирован"}

        case USERS_LIST_ADDED:
            return {...successInfo, text : "Роль добавлена!"}
        
        case USERS_LIST_ADD_FAILED:
            return {...errorInfo, text : `Не удалось добавить роль: ${action.data}`}
        
        case USERS_LIST_DELETED:
            return {...successInfo, text : "Пользователь удален"}
        
        case USERS_LIST_DELETE_FAILED:
            return {...errorInfo, text : `Не удалось удалить пользователя: ${action.data}`}
        
        case USERS_LIST_ROLE_DELETED:
            return {...successInfo, text : "Роль удалена у пользователя"}
        
        case USERS_LIST_ROLE_DELETE_FAILED:
            return {...errorInfo, text: `Не удалось удалить роль у пользователя: ${action.data}`}
        
        case ROLE_USERS_LIST_LOAD_FAILED:
            return {...errorInfo, text: action.data}
        
        case FOLDER_CONTENT_ITEM_DELETED:
            return {...successInfo, text: "Успешно удален(а): " + folderItemTypeName(action.itemType, 1) + ' "' + action.itemData.name + '"'}
        
        case FOLDER_CONTENT_ITEM_DELETE_FAILED:
            return {...errorInfo, text: "Ошибка при удалении: " + folderItemTypeName(action.itemType, 1) + ' "' + action.itemData.name + '": ' + action.errorMessage} 
        
        case JOB_CANCEL_FAILED:
            return {...errorInfo, text: `Не удалось отменить задание с id = ${action.jobId}`}
        
        case FOLDER_CONTENT_SEARCH_RESULTS_FAILED:
            return {...errorInfo, text: `Не удалось загрузить отфильтрованные данные: ${action.errorMessage}` }
        
        case USERS_LIST_LOAD_FAILED:
            return {...errorInfo, text: `Не удалось загрузить список пользователей: ${action.errorMessage}`}
        
        case USERS_LIST_MANAGE_FAILED:
            const rest = action.users.length > 1 ? "ей" : "я",
                  operation = action.operation === "DISABLED" ? "заблокировать" : 
                              action.operation === "ACTIVE" ? "разблокировать" : "завершить сессию"

            return {...errorInfo, text: `Не удалось ${operation} пользовател${rest}: ${action.data}`}
        
        case USERS_LIST_MANAGE:            
            return {...successInfo, text: "Операция выполнена успешно!" }
        
        case FOLDERS_TREE_PARENT_CHANGED_FAIL:
            return {...errorInfo, text: `Не удалось переместить каталог: ${action.data}!`}
        
        case USERS_LIST_ALL_CHECKED:
            {
                let text = "Выделение снято со всех пользователей"
                if (action.operation) text="Выделены все пользователи, кроме администраторов"
                return {...successInfo, text}
            }
        
        case JOB_SQL_LOAD_FAILED: 
            return {...errorInfo, text: "Не удалось получить SQL-запрос отчета"}
        
        case REPORT_TEMPLATES_LOAD_FAILED:
            return {...errorInfo, text: `Не удалось получить информацию о шаблонах отчета: ${action.data}`}
        
        case REPORT_TEMPLATES_ADDED:
            return {...successInfo, text: "Шаблон загружен на сервер"}
        
        case REPORT_TEMPLATES_ADD_FAILED:
            return {...errorInfo, text: `Не удалось загрузить файл на сервер: ${action.data}`}
        
        case REPORT_TEMPLATES_DELETED:
            return {...successInfo, text: "Шаблон отчета удален"}
        
        case REPORT_TEMPLATES_DELETE_FAILED:
            return {...errorInfo, text: `Не удалось удалить шаблон отчета: ${action.data}`}
        
        case REPORT_TEMPLATES_SET_DEFAULT_SUCCESS:
            return {...successInfo, text: "Выбранный шаблон установлен по-умолчанию"}
        
        case REPORT_TEMPLATES_SET_DEFAULT_FAILED:
            return {...errorInfo, text: `Не удалось установить выбранный шаблон по-умолчанию: ${action.data}`}

        case SCHEDULE_TASK_RUN_OK:
            return {...successInfo, text: "Отчет отправлен на выполнение вне очереди"}

        case SCHEDULE_TASK_RUN_FAILED: 
            return {...errorInfo, text: `Не удалось запустить отчет на расписании вне очереди`}
            
        case TASK_SWITCH_FAILED:
            return {...errorInfo, text: "Не удалось изменить статус отчета на расписании"}

        default:
            return state
    }
}