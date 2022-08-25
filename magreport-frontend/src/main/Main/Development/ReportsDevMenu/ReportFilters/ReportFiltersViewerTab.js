import React from 'react';

// //local
import GroupFiltersViewer from './GroupFiltersViewer'

/**
 * Вкладка для просмотра фильтров отчета
 * @param {Object} props - свойства компонента
 * @param {Number} props.reportId - id отчёта
 * @param {Array} props.reportFields - массив полей отчёта
 * @param {Object} props.childGroupInfo - все группы фильтров
 */
export default function ReportFiltersViewerTab(props){

    return (
        <GroupFiltersViewer
            level={0}
            reportId={props.reportId}
            reportFields={props.reportFields}
            childGroupInfo={props.childGroupInfo}
        />
    )
}
