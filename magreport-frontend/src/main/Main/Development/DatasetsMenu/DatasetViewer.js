import React, {useState} from "react";
import {useSnackbar} from "notistack";

import dataHub from "ajax/DataHub";
import DataLoader from "main/DataLoader/DataLoader";

import PageTabs from "main/PageTabs/PageTabs";

import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";

import ViewerPage from "main/Main/Development/Viewer/ViewerPage";
import ViewerChildCard from "main/Main/Development/Viewer/ViewerChildCard";
import ViewerTable from "main/Main/Development/Viewer/ViewerTable";
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";

import {createViewerTextFields,
    createViewerPageName} from "main/Main/Development/Viewer/viewerHelpers";


/**
 * @callback onOkClick
 */
/**
 * Компонент просмотра набора данных
 * @param {Object} props - параметры компонента
 * @param {Number} props.datasetId - ID набора данных
 * @param {onOkClick} props.onOkClick - callback вызываемый при нажатии кнопки ОК
 * @constructor
 */
export default function DatasetViewer(props) {

    const classes = ViewerCSS();

    const {enqueueSnackbar} = useSnackbar();

    const [data, setData] = useState({});

    const [typeById, setTypeById] = useState({});

    let loadFunc = dataHub.datasetController.get;
    let loadParams = [props.datasetId];

    function handleDataLoaded(loadedData) {
        setData(loadedData);
    }

    function handleTypesLoaded(loadedData) {
        setTypeById(Object.fromEntries(loadedData.map(t => [t.id, t.name])));
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении набора данных возникла ошибка: ${message}`,
            {variant: "error"});
    }

    // build component

    // settings tab
    const settingsPreDataSourceData = [
        {label: "Название набора данных", value: data.name},
        {label: "Описание", value: data.description},
    ];

    let dataSourceCard = data.dataSource ? (
        <ViewerChildCard
            id={data.dataSource.id}
            itemType={FolderItemTypes.datasource}
            name={data.dataSource.name}
        />
    ) : "";

    const settingsPostDataSourceData = [
        {label: "Каталог", value: data.catalogName},
        {label: "Схема", value: data.schemaName},
        {label: "Объект", value: data.objectName},
        {label: "Тип объекта", value: typeById[data.typeId]},
    ];

    const settingsTab = {
        tablabel: "Настройки",
        tabcontent: (
            <div className={classes.viewerTabPage}>
                {createViewerTextFields(settingsPreDataSourceData)}
                {dataSourceCard}
                {createViewerTextFields(settingsPostDataSourceData)}
            </div>
        )
    };

    // fields tab
    const fieldsTableColumns = [
        {label: "ID поля", key: "id"},
        {label: "Название поля", key: "name"},
        {label: "Тип поля", key: "typeName"},
        {label: "Описание", key: "description"},
    ];
    const fieldsTableRows = data.fields;

    const fieldsTab = {
        tablabel: "Поля",
        tabcontent: (
            <div className={classes.viewerTabPage}>
                <ViewerTable
                    columns={fieldsTableColumns}
                    rows={fieldsTableRows}
                    checkIsValidRow={(row) => row.isValid}
                />
            </div>
        )
    };

    // edit check
    let userInfo = dataHub.localCache.getUserInfo();

    let isAdmin = userInfo.isAdmin
    let isDeveloper = userInfo.isDeveloper
    let authority = data.authority

    let hasRWRight = isAdmin || (isDeveloper && authority === "WRITE");

    // component
    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={handleDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
            <DataLoader
                loadFunc = {dataHub.datasetController.getTypes}
                loadParams = {[]}
                onDataLoaded = {handleTypesLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
            >
                <ViewerPage
                    id={data.id}
                    name={data.name}
                    itemType={FolderItemTypes.dataset}
                    disabledPadding={true}
                    onOkClick={props.onOkClick}
                    readOnly={!hasRWRight}
                >
                    <PageTabs
                        pageName={createViewerPageName(FolderItemTypes.dataset, data.name)}
                        tabsdata={[
                            settingsTab,
                            fieldsTab,
                        ]}
                    />
                </ViewerPage>
            </DataLoader>
        </DataLoader>
    );
}
