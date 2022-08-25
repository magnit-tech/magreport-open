import React, {useState} from "react";


import DataLoader from "main/DataLoader/DataLoader";
import dataHub from "../../../../ajax/DataHub";
import {useSnackbar} from "notistack";
import {ViewerCSS} from "../../Development/Viewer/ViewerCSS";
import ViewerPage from "../../Development/Viewer/ViewerPage";
import {FolderItemTypes} from "../../../FolderContent/FolderItemTypes";
import {createViewerTextFields} from "../../Development/Viewer/viewerHelpers";


/**
 * @callback onOkClick
 */
/**
 * Компонент просмотра расписаний
 * @param {Object} props - параметры компонента
 * @param {Number} props.serverMailTemplateId- идентификатор шаблона
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки ОК
 * @param {onEditClick} props.onEditClick - callback, вызываемый при нажатии кнопки Редактировать
 * @return {JSX.Element}
 * @constructor
 */
export default function ServerMailTemplateView(props) {

    const {enqueueSnackbar} = useSnackbar();
    const [data, setData] = useState({});
    const classes = ViewerCSS();


    function actionLoaded(loadData) {
        setData(loadData)
    }

    function actionFailedLoaded(message) {
        enqueueSnackbar(`При загрузке данных произошла ошибка: ${message}`, {variant: "error"});
    }


    const fieldsData = [
        {label: "Код шаблона", value: data.code},
        {label: "Название", value: data.name},
        {label: "Описание", value: data.description},
        {label: "Тема письма", value: data.subject},
        {label: "Тело письма", value: data.body},

    ];

    // edit check
    let userInfo = dataHub.localCache.getUserInfo();

    let isAdmin = userInfo.isAdmin
    let isDeveloper = userInfo.isDeveloper
    let authority = data.authority

    let hasRWRight = isAdmin || (isDeveloper && authority === "WRITE");

    return (
        <DataLoader
            loadFunc={dataHub.serverMailTemplateController.getMailTemplate}
            loadParams={[props.serverMailTemplateId]}
            reload={false}
            onDataLoaded={(data) => {
                actionLoaded(data)
            }}
            onDataLoadFailed={(message) => {
                actionFailedLoaded(message)
            }}
        >

            <ViewerPage
                id={data.id}
                itemType={FolderItemTypes.systemMailTemplates}
                disabledPadding={true}
                onOkClick={props.onOkClick}
                actionViewerEditItem={props.onEditClick}
                pageName={`Просмотр шаблона письма: ${data.name}`}
                readOnly={!hasRWRight}
            >
                <div className={classes.viewerTabPage}>
                    {createViewerTextFields(fieldsData)}
                </div>
            </ViewerPage>
        </DataLoader>
    )

}


