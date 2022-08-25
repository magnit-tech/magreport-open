import React, {useState} from "react";
import {useSnackbar} from "notistack";

// dataHub
import dataHub from "ajax/DataHub";

// local components
import DataLoader from "main/DataLoader/DataLoader";
import PageTabs from "main/PageTabs/PageTabs";
import ViewerPage from "main/Main/Development/Viewer/ViewerPage";
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";
import ScheduleParametersViewer from "./ScheduleParametersViewer";
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";

// functions
import {createViewerPageName} from "main/Main/Development/Viewer/viewerHelpers";

// constants
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

/**
 * @callback onOkClick
 */
/**
 * Компонент просмотра расписаний
 * @param {Object} props - параметры компонента
 * @param {Number} props.scheduleId - идентификатор расписания
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки ОК
 * @return {JSX.Element}
 * @constructor
 */
export default function ScheduleViewer(props) {

    const {enqueueSnackbar} = useSnackbar();

    const classes = ViewerCSS();

    const [data, setData] = useState({tasks: []});

    const loadFunc = dataHub.scheduleController.get;
    const loadParams = [props.scheduleId];


    function handleDataLoaded(loadedData) {
        setData(loadedData);
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении данных возникла ошибка: ${message}`,
            {variant: "error"});
    }

    // edit check
    let userInfo = dataHub.localCache.getUserInfo();

    let isAdmin = userInfo.isAdmin
    let isDeveloper = userInfo.isDeveloper
    let authority = data.authority

    let hasRWRight = isAdmin || (isDeveloper && authority === "WRITE");

    // building component
    const tabs = [];

    // general
    tabs.push({
        tablabel: "Общие",
        tabcontent:
            <div className={classes.viewerTabPage}>
                <ViewerTextField
                    label="Название"
                    value={data.name}
                />
                <ViewerTextField
                    label="Описание"
                    value={data.description}
                />
                <ScheduleParametersViewer
                    scheduleType={data.type}
                    data={data}
                />
            </div>
    })

    return <DataLoader
        loadFunc={loadFunc}
        loadParams={loadParams}
        onDataLoaded={handleDataLoaded}
        onDataLoadFailed={handleDataLoadFailed}
    >
        <ViewerPage
            itemType={FolderItemTypes.schedules}
            id={props.scheduleId}
            onOkClick={props.onOkClick}
            disabledPadding={true}
            readOnly={!hasRWRight}
        >
            <PageTabs
                pageName={createViewerPageName(FolderItemTypes.schedules, data.name)}
                tabsdata={tabs}
            />
        </ViewerPage>

    </DataLoader>
}
