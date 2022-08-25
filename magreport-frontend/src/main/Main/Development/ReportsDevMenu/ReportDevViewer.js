import React, {useState} from 'react';
import {connect} from "react-redux";
import {useSnackbar} from 'notistack';

// local
import ViewerPage from 'main/Main/Development/Viewer/ViewerPage';
import ViewerTextField from 'main/Main/Development/Viewer/ViewerTextField';
import ViewerChildCard from "main/Main/Development/Viewer/ViewerChildCard";
import ReportFieldsViewer from './ReportFieldsViewer';
import ReportTemplatesViewer from './ReportTemplatesViewer'
import ReportFiltersViewerTab from './ReportFilters/ReportFiltersViewerTab'
import PageTabs from 'main/PageTabs/PageTabs';
import DataLoader from 'main/DataLoader/DataLoader';
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

// dataHub
import dataHub from 'ajax/DataHub';

// actions
import {actionLoaded, actionLoadedFailed} from "redux/actions/developer/actionReportTemplates";

// functions
import {createViewerPageName} from "../Viewer/viewerHelpers";

/**
 * @callback onOkClick
 */
/**
 * @callback actionLoaded
 * @param {Number} id
 * @param {Object} data
 */

/**
 * @callback actionLoadedFailed
 * @param {Number} id
 * @param {Object} data
 */
/**
 * Компонент просмотра объекта-отчета
 * @param {Object } props - параметры компонента
 * @param {Number} props.reportId - id просматриваемого объекта
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки ОК
 * @param {actionLoaded} props.actionLoaded - action, вызываемый при загрузке данных отчета
 * @param {actionLoadedFailed} props.actionLoadedFailed - action, вызываемый при ошибке загрузки данных отчета
 */
function ReportDevViewer({reportId = -1, onOkClick = f => f, actionLoaded = f=>f, actionLoadedFailed = f=>f}) {

    const {enqueueSnackbar} = useSnackbar();
    const classes = ViewerCSS();

    const [data, setData] = useState({});
    const [dataSet, setDataSet] = useState({});

    /*
        Data loading
    */

    let loadFunc = dataHub.reportController.get;
    let loadParams = [reportId, undefined];

    function handleDataLoaded(loadedData) {
        setData({
            ...data,
            ...loadedData,
        });
    }

    function handleDataSetLoaded(loadedData) {
        setDataSet({
            ...dataSet,
            ...loadedData
        })
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении данных возникла ошибка: ${message}`,
            {variant: "error"});
    }

    // constructing component
    // tabs
    const tabs = []

    // headers tab
    tabs.push({
        tablabel: "Заголовки",
        tabcontent:
            <div className={classes.viewerTabPage}>
                <ViewerTextField
                    label={"Название отчета"}
                    value={data.name}
                />

                <ViewerTextField
                    label={"Описание отчета"}
                    value={data.description}
                />

                <ViewerChildCard
                    id={dataSet.id}
                    itemType={FolderItemTypes.dataset}
                    name={dataSet.name}
                />

                <ViewerTextField
                    label={"Ссылка на реестр требований"}
                    value={data.requirementsLink}
                />
            </div>
    });

    // fields tab
    tabs.push({
        tablabel: "Поля",
        tabcontent:
            <div className={classes.viewerTabPage}>
                <ReportFieldsViewer
                    fields={data.fields || []}
                    dataSet={dataSet}
                />
            </div>
    });

    // filters tab
    tabs.push({
        tablabel: "Фильтры",
        tabcontent:
            <div className={classes.viewerTabPage}>
                <ReportFiltersViewerTab
                    childGroupInfo={data.filterGroup || {}}
                    reportId={reportId}
                    reportFields={data.fields || []}
                />
            </div>
    });

    // report templates tab
    tabs.push({
        tablabel: "Шаблоны отчетов",
        tabcontent:
            <div className={classes.viewerTabPage}>
                <ReportTemplatesViewer
                    reportId={reportId}
                />
            </div>
    });

    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={handleDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
            <DataLoader
                loadFunc={dataHub.datasetController.get}
                loadParams={[data.dataSetId]}
                onDataLoaded={handleDataSetLoaded}
                onDataLoadFailed={handleDataLoadFailed}
            >
                <DataLoader
                    loadFunc={dataHub.excelTemplateController.get}
                    loadParams={[reportId]}
                    onDataLoaded={loadedData => actionLoaded(reportId, loadedData)}
                    onDataLoadFailed={loadedData => actionLoadedFailed(reportId, loadedData)}
                >
                <ViewerPage
                    id={data.id}
                    itemType={FolderItemTypes.reportsDev}
                    onOkClick={onOkClick}
                    disabledPadding={true}
                >
                    <PageTabs
                        tabsdata={tabs}
                        pageName={createViewerPageName(FolderItemTypes.reportsDev, data.name)}
                    />
                </ViewerPage>
                </DataLoader>
            </DataLoader>
        </DataLoader>
    );
}

const mapStateToProps = state => {
    return {};
}

const mapDispatchToProps = {
    actionLoaded,
    actionLoadedFailed,
}

export default connect(mapStateToProps, mapDispatchToProps)(ReportDevViewer);
