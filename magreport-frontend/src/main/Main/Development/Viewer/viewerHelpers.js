import React from "react";
import ViewerTextField from "./ViewerTextField";
import {folderItemTypeName} from "main/FolderContent/FolderItemTypes";

/**
 * Функция для создания компонентов ViewerTextField из массива данных
 * @param {Array.<{label: String, value: *}>} data - массив объектов c заголовками полей (label) и значением (value)
 * @return {Array.<ViewerTextField>}
 */
export function createViewerTextFields(data) {
    const result = [];

    data.forEach((item, idx) =>
        result.push(<ViewerTextField
            key={idx}
            label={item.label}
            value={item.value}
        />)
    );

    return result;
}

/**
 * Создает заголовок для страницы просмотра объекта
 * @param {String} itemType - тип объекта из FolderItemTypes
 * @param {String} itemName - имя объекта
 * @return {string}
 */
export function createViewerPageName(itemType, itemName) {
    return `Просмотр ${folderItemTypeName(itemType)}: ${itemName}`;
}
