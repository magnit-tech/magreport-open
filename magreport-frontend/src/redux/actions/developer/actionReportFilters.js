import { 
    REPORT_FILTERS_LOADED,
    REPORT_FILTER_CHANGED,
    REPORT_FILTER_ADDED,
    REPORT_FILTER_DELETED,
    REPORT_FILTER_MOVED_BEFORE,
    REPORT_FILTER_MOVED_INTO,
    REPORT_GROUP_FILTER_CHANGED,
    REPORT_GROUP_FILTER_ADDED,
    REPORT_GROUP_FILTER_DELETED,
    REPORT_FILTERS_ORDINAL_CHANGED,
} from '../../reduxTypes'

export const actionFiltersLoaded = (data, reportId) => {
    return {
        type: REPORT_FILTERS_LOADED,
        data,
        reportId,
    }
}

export const actionFiltersAdd = (childGroupId, item, reportFields) => {
    return {
        type: REPORT_FILTER_ADDED,
        childGroupId,
        item,
        reportFields,
    }
}

export const actionFiltersGroupAdd = (childGroupId, reportId) => {
    return {
        type: REPORT_GROUP_FILTER_ADDED,
        childGroupId,
        reportId,
    }
}

export const actionFiltersDelete = (childGroupId, index, elementType) => {

    return {
        type: elementType === 'filters' ? REPORT_FILTER_DELETED : REPORT_GROUP_FILTER_DELETED,
        childGroupId,
        index,
    }
}

export const actionFiltersGroupChange = (childGroupId, index, field, value) => {
    return {
        type: REPORT_GROUP_FILTER_CHANGED,
        childGroupId,
        index, 
        field, 
        value,
    }
}

export const actionFiltersChange = (childGroupId, index, filterItem) => {
    return {
        type: REPORT_FILTER_CHANGED,
        childGroupId,
        index, 
        filterItem,
    }
}

export const actionFiltersChangeOrdinal = (childGroupId, listObjects) => {
    return {
        type: REPORT_FILTERS_ORDINAL_CHANGED,
        childGroupId,
        listObjects,
    }
}

export const actionMoveBefore = (beforeId, entityId) => {
    return {
        type: REPORT_FILTER_MOVED_BEFORE,
        beforeId, 
        entityId, 
    }
}

export const actionMoveInto = (targetId, entityId) => {
    return {
        type: REPORT_FILTER_MOVED_INTO,
        targetId, 
        entityId, 
    }
}
