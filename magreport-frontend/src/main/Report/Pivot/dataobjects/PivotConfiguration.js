import {FieldsLists} from './FieldsLists';
import restoreObjectData from 'utils/restoreObjectData';

/**
 * Объект конфигурации сводной - содержит все метаданные, определяющие вид сводной таблицы
 */
export default function PivotConfiguration(pivotConfiguration){
    
    /*
    **************
    Поля
    **************
    */

    this.fieldsLists = pivotConfiguration ? pivotConfiguration.fieldsLists : new FieldsLists();

    this.columnFrom = pivotConfiguration ? pivotConfiguration.columnFrom : 0;
    this.columnCount = pivotConfiguration ? pivotConfiguration.columnCount : 0;
    this.rowFrom = pivotConfiguration ? pivotConfiguration.rowFrom : 0;
    this.rowCount = pivotConfiguration ? pivotConfiguration.rowCount : 0;

    this.columnsMetricPlacement = pivotConfiguration ? pivotConfiguration.columnsMetricPlacement : false; // метрики размещаются по столбцам

    this.mergeMode = pivotConfiguration ? pivotConfiguration.mergeMode : false; // объединять ли ячейки с одинаковыми значениями

    this.sortOrder = {}; // Пока не используется

    /*
    **************
    **************
    Методы
    **************
    **************
    */

    // Используется для создания конфигурации для новой выгрузки
    // pivotConfiguration - может быть ранее сохранённой конфигурацией или пустым объектом
    // Сначала происходит восстановление конфигурации, затем приведение её в соответсвие с актуальным объектом reportMetada
    // Таким образом поддерживается следующая логика: сохранённый объект конфигурации может содержать набор полей, отличающийся от актуального
    // в этом случае удаляются отсутствиющие в актуальном отчёте поля и добавляются новые
    this.create = (pivotConfiguration, reportMetada) => {
        restoreObjectData(this, pivotConfiguration);
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.updateByReportFields(reportMetada.fields);
    };

    // Используется для загрузки сохранённой конфигурации
    this.restore = (pivotSaveConfig) => {
        restoreObjectData(this, pivotSaveConfig); // В pivotSaveConfig.fieldsLists отсуствуют allFields
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.actualizeUnusedFields(pivotSaveConfig.fieldsLists);
    };

    /*
    *********************
    Сериализация
    *********************
    */

    function replacer(key, value) {
        if (typeof value === 'object' && value.hasOwnProperty('forSave')) {
            return value.forSave()
        } else {
            return value
        }
    }

    this.stringify = () => {
        return JSON.stringify(this, replacer)
    }

    /*
    *********************
    Работа с полями
    *********************
    */

    /*
        Перемещение полей отчёта
    */

    // Перемещение поля при Drag And Drop из одного списка в другой
    // Возвращает индекс места в списке нового поля (может не совпадать в отдельных случаях с dropListFieldIndex)    
    this.dragAndDropField = (dragListName, dropListName, dragListFieldIndex, dropListFieldIndex) => {
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        return this.fieldsLists.dragAndDropField(dragListName, dropListName, dragListFieldIndex, dropListFieldIndex);
    }

    // Получить поле из списка по индексу
    this.getFieldByIndex = (listName, fieldIndex) => {
        return this.fieldsLists.getFieldByIndex(listName, fieldIndex);
    }

    // Задание агрегирующей фукнции для метрики
    this.setAggMetricByIndex = (aggFuncName, index) => {
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.setAggMetricByIndex(aggFuncName, index);
    }

    // Задание фильтра на поле
    this.setFieldFilterByFieldId = (fieldId, filterObject) => {
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.setFieldFilterByFieldId(fieldId, filterObject);
    }

    // Задание фильтра на поле по индексу в списке фильтров
    this.setFieldFilterByFieldIndex = (index, filterObject) => {
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.setFieldFilterByFieldIndex(index, filterObject);
    }    

    /*
    *********************
    Диапазон просмотра
    *********************
    */

    this.setColumnFrom = (newColumnFrom) => {
        this.columnFrom = newColumnFrom;
    }
        
    this.setRowFrom = (newRowFrom) => {
        this.rowFrom = newRowFrom;
    }

    this.setColumnAndRowCount = (newColumnCount, newRowCount) => {
        this.columnCount = newColumnCount;
        this.rowCount = newRowCount;
    }

    /*
    ***************
    Форматирование
    ***************
    */

    this.changeMetricCellFormatBold = ({fieldIndex, style}) => {
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.changeMetricCellFormatBold(fieldIndex, style);
    }

    this.changeFieldDataStyle = ({fieldIndex, style}) => {
        this.fieldsLists = new FieldsLists(this.fieldsLists);
        this.fieldsLists.changeFieldDataStyle(fieldIndex, style);
    }
}