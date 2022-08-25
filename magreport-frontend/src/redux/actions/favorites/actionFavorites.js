import store from 'redux/store';
import dataHub from 'ajax/DataHub';

import { 
    FAVORITES_ADD_START, 
    FAVORITES_ADDED, 
    FAVORITES_ADD_FAIL,
    FAVORITES_DELETE_START, 
    FAVORITES_DELETED, 
    FAVORITES_DELETE_FAIL,
} from '../../reduxTypes'

export function actionAddDeleteFavorites(itemsType, index, folderId, reportId, favorite, callback){
    let type = FAVORITES_ADD_START
    if (favorite){
        dataHub.reportController.deleteFavorites(reportId, m => handleAddDeleteFavorites(itemsType, index, folderId, reportId, favorite, m, callback))
        type = FAVORITES_DELETE_START
    }
    else {
        dataHub.reportController.addFavorites(folderId, reportId, m => handleAddDeleteFavorites(itemsType, index, folderId, reportId, favorite, m, callback))
    }

    return {
        type,
        itemsType,
        favorite,
        folderId, 
        reportId,
    }
}

function handleAddDeleteFavorites(itemsType, index, folderId, reportId, favorite, m, callback){
    
    let type
    let text
    let variant = "error"
    if (favorite){
        if (m.ok) {
            type = FAVORITES_DELETED
            text = "Отчет удален из Избранного"
            variant = "success"
        }
        else {
            type = FAVORITES_DELETE_FAIL
            text = `Не удалось удалить отчет из Избранного: ${m.data}`
        }
    }
    else {
        if (m.ok){
            type = FAVORITES_ADDED
            text = "Отчет добавлен в Избранное"
            variant = "success"
        }
        else {
            type = FAVORITES_ADD_FAIL
            text = `Не удалось добавить отчет в Избранное: ${m.data}`
        }
    }

    callback(text, {variant})
    
    store.dispatch({
        type,
        index,
        itemsType,
        favorite,
        folderId, 
        reportId,
    })
}