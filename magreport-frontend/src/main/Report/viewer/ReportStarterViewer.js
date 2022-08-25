import React, {useState, useRef} from 'react';

//local
import FilterGroupViewer from './FilterGroupViewer';
import DataLoader from 'main/DataLoader/DataLoader';
import FilterValues from "../FilterValues";

// styles
import {ReportStarterCSS} from "../ReportCSS";

/**
 * Просмотр параметров запуска отчета
 * @param {Object} props - свойства компонента
 * @param {number} props.reportId - id отчёта
 * @param {number} props.scheduleTaskId - id задания по расписанию
 * @param {Array} props.parameters - фильтры отчета для отчетов на расписании
 * @param {*} props.onDataLoadFunction - функция загрузки отчета
 */
export default function ReportStarterViewer(props) {

    const classes = ReportStarterCSS();

    const [reportMetadata, setReportMetadata] = useState({});
    const reloadReportMetadata = {needReload: false};

    const lastFilterValues = useRef(new FilterValues());

    const loadFunc = props.onDataLoadFunction;
    const loadParams = [props.reportId, props.scheduleTaskId];
    function handleReportMetadataLoaded(data) {
        const lastParameters = (props.parameters?.length > 0) ? props.parameters : data.lastParameters
        lastFilterValues.current = new FilterValues();
        lastFilterValues.current.buildOnParametersObject(lastParameters, true);

        setReportMetadata(data);
    }

    return <DataLoader
        loadFunc={loadFunc}
        loadParams={loadParams}
        reload={reloadReportMetadata}
        onDataLoaded={handleReportMetadataLoaded}
    >
        {reportMetadata.filterGroup && reportMetadata.filterGroup.id !== null &&
        <div className={classes.reportStarterRelative}>
            <div className={classes.reportStarterAbsolute}>
                <div className={classes.filterRoot}>
                    <FilterGroupViewer
                        groupData={reportMetadata.filterGroup}
                        lastFilterValues={lastFilterValues.current}
                    />
                </div>
            </div>
        </div>
        }
    </DataLoader>
}
