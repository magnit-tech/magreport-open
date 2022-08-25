import React from 'react';

// //local
import GroupFilters from './GroupFilters'

/**
 * @callback onAddFilter
 * @param {Number} id
 * @param {Object} item
 * @param {Array} reportFields
 */
/**
 * @callback onAddChildGroup
 * @param {Number} id
 * @param {Number} reportId
 */
/**
 * @callback onChangeFiltersGroup
 * @param {Number} id
 * @param {Number} index
 * @param {String} type
 * @param {Object} value
 */
/**
 * @callback onChangeFilterField
 * @param {Number} id
 * @param {Number} startIndex
 * @param {Object} filterItem
 */
/**
 * @callback onChangeOrdinal 
 * @param {Number} id
 * @param {Array} newElements
 */
/**
 * @callback onDelete
 * @param {Number} id
 * @param {Number} index
 * @param {String} type
 */
/**
 * @callback onMoveBefore
 * @param {Number} beforeId
 * @param {Number} entityId
 */
/**
 * @callback onMoveInto
 * @param {Number} targetId
 * @param {Number} entityId
 */
/**
 * Вкладка для редактирования фильтров отчета
 * @param {Object} props - свойства компонента
 * @param {Number} props.reportId - id отчёта
 * @param {Array} props.reportFields - массив полей отчёта
 * @param {Object} props.childGroupsMap - Map со всеми группами фильтров
 * @param {onAddFilter} props.onAddFilter - callback при добавлении фильтра
 * @param {onAddChildGroup} props.onAddChildGroup - callback при добавлении группы фильтров
 * @param {onChangeFiltersGroup} props.onChangeFiltersGroup - callback при изменении группы фильтров
 * @param {onChangeFilterField} props.onChangeFilterField - callback при изменении свзяи полей фильтра с полями отчета
 * @param {onChangeOrdinal} props.onChangeOrdinal - callback при изменении порядка фильтров
 * @param {onDelete} props.onDelete - callback при удалении
 * @param {onMoveBefore} props.onMoveBefore - callback при перетаскивании элемента перед другим элементом
 * @param {onMoveInto} props.onMoveInto - callback при перетаскивании элемента внутрь другого элемента
 */
export default function ReportFiltersTab(props){

    return (
        <GroupFilters 
            level={0}
            reportId={props.reportId}
            reportFields={props.reportFields}
            childGroupInfo={props.childGroupsMap.get(0)}
            childGroupsMap={props.childGroupsMap}
            onAddFilter={props.onAddFilter}
            onAddChildGroup={props.onAddChildGroup}
            onDelete={props.onDelete}
            onChangeFiltersGroup={props.onChangeFiltersGroup}
            onChangeFilterField={props.onChangeFilterField}
            onChangeOrdinal={props.onChangeOrdinal}
            onMoveBefore={props.onMoveBefore}
            onMoveInto={props.onMoveInto}
            datasetType={props.datasetType}
        />
    )
};
