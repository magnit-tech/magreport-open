import React, { useState } from 'react';
import PlainTablePanel from './PlainTablePanel';
import PivotPanel from './Pivot/PivotPanel';

/**
 * @param {*} props.jobId - id задания
 * @param {*} props.reportId - id отчёта
 * @param {*} props.onRestartReportClick - function() - callback перезапуска отчёта
 */
export default function ReportJobData(props){
    const [viewType, setViewType] = useState('PlainTable');

    function handleChangeViewType(value){
        setViewType(value);
    };

    return (
        <div style={{ display: 'flex', flex: 1}}>
            { (viewType === 'PlainTable') ? 
                <PlainTablePanel
                    canExecute = {props.canExecute}
                    jobId = {props.jobId}
                    excelTemplates = {props.excelTemplates}
                    onRestartReportClick = {props.onRestartReportClick}
                    onViewTypeChange = {handleChangeViewType}
                />
                :
                <PivotPanel
                    reportId = {props.reportId}
                    jobId = {props.jobId}
                    onRestartReportClick = {props.onRestartReportClick}
                    onViewTypeChange = {handleChangeViewType}
                />
            }
        </div>
    );

}
