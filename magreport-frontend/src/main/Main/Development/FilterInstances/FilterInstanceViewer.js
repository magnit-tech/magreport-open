import React, {useState} from "react";
import {useSnackbar} from "notistack";

import dataHub from "ajax/DataHub";
import DataLoader from "main/DataLoader/DataLoader";

import ViewerPage from "main/Main/Development/Viewer/ViewerPage";

import {createViewerTextFields, createViewerPageName} from "main/Main/Development/Viewer/viewerHelpers";
import ViewerChildCard from "../Viewer/ViewerChildCard";
import ValueListFieldsViewer from "./TypeSpecificFields/ValueListFieldsViewer";
import HierTreeFieldsViewer from "./TypeSpecificFields/HierTreeFieldsViewer";
import RangeFieldsViewer from "./TypeSpecificFields/RangeFieldsViewer";
import DateValueFieldsViewer from "./TypeSpecificFields/DateValueFieldsViewer";
import TokenInputFieldsViewer from "./TypeSpecificFields/TokenInputFieldsViewer";
import {FolderItemTypes} from "../../../FolderContent/FolderItemTypes";

/**
 * @callback onOkClick
 */
/**
 * Компонент для просмотра экземпляра фильтра
 * @param props - свойства компонента
 * @param {Number} props.filterInstanceId - ID просматриваемого экземпляра фильтра
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки "ОК"
 * @return {JSX.Element}
 * @constructor
 */
export default function FilterInstanceViewer(props) {

    const {enqueueSnackbar} = useSnackbar();

    const [data, setData] = useState({});

    let loadFunc = dataHub.filterInstanceController.get;
    let loadFuncFilterTemplate = dataHub.filterTemplateController.get;
    let loadParams = [props.filterInstanceId];

    function handleFilterInstanceDataLoaded(loadedData) {
        setData({
            ...data,
            ...loadedData,
        });
    }

    function handleFilterTemplateDataLoaded(loadedData) {
        setData({
            ...data,
            filterTemplate: loadedData,
        });
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении данных возникла ошибка: ${message}`,
            {variant: "error"});
    }

    // build component

    const children = [];

    const detailsData = [
        {label: "Название", value: data.name},
        {label: "Код", value: data.code},
        {label: "Описание", value: data.description},
    ];

    children.push(...createViewerTextFields(detailsData));

    children.push(
        data.filterTemplate ? (
            <ViewerChildCard
                id={data.filterTemplate.id}
                itemType={FolderItemTypes.filterTemplate}
                name={data.filterTemplate.name}
            />
        ) : ""
    );

    const filterTemplateType = data.filterTemplate ? data.filterTemplate.type.name : "";
    let fieldsComponent = "";

    if(filterTemplateType === 'VALUE_LIST') {

        fieldsComponent = <ValueListFieldsViewer
            filterInstanceData={data}
        />;
    } else if(filterTemplateType === 'HIERARCHY'
        || filterTemplateType === 'HIERARCHY_M2M') {

        fieldsComponent = <HierTreeFieldsViewer
            filterTemplateType={filterTemplateType}
            filterInstanceData={data}
        />
    } else if(filterTemplateType === 'DATE_RANGE'
        || filterTemplateType === 'RANGE') {

        fieldsComponent = <RangeFieldsViewer
            filterTemplateType={filterTemplateType}
            filterInstanceData={data}
        />;
    } else if(filterTemplateType === 'DATE_VALUE') {

        fieldsComponent = <DateValueFieldsViewer
            filterInstanceData={data}
        />;
    } else if( filterTemplateType === 'TOKEN_INPUT') {

        fieldsComponent = <TokenInputFieldsViewer
            filterInstanceData={data}
        />;
    }
    children.push(fieldsComponent);

    // edit check
    let userInfo = dataHub.localCache.getUserInfo();

    let isAdmin = userInfo.isAdmin
    let isDeveloper = userInfo.isDeveloper
    let authority = data.authority

    let hasRWRight = isAdmin || (isDeveloper && authority === "WRITE");

    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={handleFilterInstanceDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
            <DataLoader
                loadFunc={loadFuncFilterTemplate}
                loadParams={[data.templateId]}
                onDataLoaded={handleFilterTemplateDataLoaded}
                onDataLoadFailed={handleDataLoadFailed}
            >
                <ViewerPage
                    pageName={createViewerPageName(FolderItemTypes.filterInstance, data.name)}
                    id={data.id}
                    itemType={FolderItemTypes.filterInstance}
                    onOkClick={props.onOkClick}
                    readOnly={!hasRWRight}
                >
                    {children}
                </ViewerPage>
            </DataLoader>
        </DataLoader>
    );
}
