import React, {useState} from "react";
import {useSnackbar} from "notistack";
import {CopyToClipboard} from 'react-copy-to-clipboard';

// dataHub
import dataHub from "ajax/DataHub";

// components

import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import Checkbox from '@material-ui/core/Checkbox';
import IconButton from '@material-ui/core/IconButton';
import FileCopyIcon from '@material-ui/icons/FileCopy';
import Tooltip from '@material-ui/core/Tooltip';
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";

// local components
import DataLoader from "main/DataLoader/DataLoader";
import PageTabs from "main/PageTabs/PageTabs";
import ViewerTextField from "main/Main/Development/Viewer/ViewerTextField";
import ViewerChildCard from "main/Main/Development/Viewer/ViewerChildCard";
import ViewerPage from "main/Main/Development/Viewer/ViewerPage";
import DesignerMultipleSelectField from "main/Main/Development/Designer/DesignerMultipleSelectField";
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";
import ReportStarterViewer from 'main/Report/viewer/ReportStarterViewer';
import {ScheduleTaskTypeMap, ScheduleStatusMap} from '../../../FolderContent/JobFilters/JobStatuses';
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";

// functions
import {createViewerPageName} from "main/Main/Development/Viewer/viewerHelpers";

/**
 * @callback onExit
 */

/**
 * Компонент создания и редактирования отчетов на расписании
 * @param {Object} props - параметры компонента
 * @param {Number} props.reportId - идентификатор отчета на расписании
 * @param {Number} props.scheduleId - идентификатор расписания
 * @param {onExit} props.onExit - callback, вызываемый при закрытии формы
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки ОК
 * @return {JSX.Element}
 * @constructor
 */
export default function ScheduleTasksViewer(props) {
    const classes = ViewerCSS();

    const {enqueueSnackbar} = useSnackbar();

    const [data, setData] = useState({});
    const [runLink, setRunLink] = useState('');

    let loadFuncRunLink = dataHub.scheduleController.taskGetManualLink;
    let loadFunc = dataHub.scheduleController.taskGet;
    let loadParams = [props.scheduleId];

    function handleDataLoaded(loadedData) {
        let childGroups = loadedData.report.filterGroup.childGroups === null ? [] : loadedData.report.filterGroup.childGroups;
        let filters     = loadedData.report.filterGroup.filters     === null ? [] : loadedData.report.filterGroup.filters;

        setData({
            id: loadedData.id,
            code: loadedData.code,
            description: loadedData.description,
            destinationEmails: loadedData.destinationEmails.filter(item =>item.type === 'REPORT').map(item=>item.value),
            destinationUsers: loadedData.destinationUsers.filter(item =>item.type === 'REPORT').map(item=>({id: item.id, name: item.userName})),
            destinationRoles: loadedData.destinationRoles.filter(item =>item.type === 'REPORT').map(item=>({id: item.id, name: item.name})),
            errEmails: loadedData.destinationEmails.filter(item=>item.type === 'ERROR').map(item=>item.value),
            errUsers: loadedData.destinationUsers.filter(item=>item.type === 'ERROR').map(item => ({id: item.id, name: item.userName})),
            errRoles: loadedData.destinationRoles.filter(item=>item.type === 'ERROR').map( item=>({id: item.id, name: item.name})),
            expirationDate: loadedData.expirationDate,
            reportTitleMail: loadedData.reportTitleMail,
            reportBodyMail: loadedData.reportBodyMail,
            errorTitleMail: loadedData.errorTitleMail,
            errorBodyMail: loadedData.errorBodyMail,
            name: loadedData.name,
            excelTemplateName: loadedData.excelTemplate.name,
            reportId: loadedData.report.id,
            reportName: loadedData.report.name,
            reportJobFilter: loadedData.reportJobFilters,
            schedules:  loadedData.schedules.map(item=>({id: item.id, name: item.name})),
            taskTypeId: ScheduleTaskTypeMap.get(loadedData.typeTask),
            sendEmptyReport: loadedData.sendEmptyReport,
            hasFilters: !(childGroups.length === 0 && filters.length === 0),
            maxFailedStarts: loadedData.maxFailedStarts ,
            failedStart: loadedData.failedStart                 
        })
    }

    function handleRunLinkoaded(loadedData) {
        setRunLink(loadedData)
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
                <ViewerChildCard
                    id={data.reportId}
                    itemType={FolderItemTypes.reportsDev}
                    name={data.reportName}
                />
                <ViewerTextField
                        label = "Шаблон Excel"
                        value = {data.excelTemplateName}
                />
            
            </div>
    })

    tabs.push({
        tablabel: "Расписания",
        tabdisabled: !(data.reportId && data.name && data.description) ,
        tabcontent: 
            <div className={classes.viewerTabPage}>
               <DesignerMultipleSelectField
                   // minWidth = {StyleConsts.designerTextFieldMinWidth}
                    label = "Расписания"
                    value = {data.schedules}
                    data = {data.schedules}
                    displayBlock
                    fullWidth
                    disabled
               />
                { Boolean(data.code) &&
                    <ViewerTextField
                        label="Код"
                        value={data.code}
                    />
                }
                { Boolean(data.code) &&
                    <DesignerTextField
                        label="Ссылка для запуска по коду"
                        disabled
                        inputProps={{endAdornment: (
                            <CopyToClipboard text={runLink + data.code}
                                //onCopy={() => this.setState({copied: true})}>
                            >
                                <Tooltip  placement="top" title = 'Копировать'>
                                <IconButton position="end">
                                    <FileCopyIcon />
                                </IconButton>
                                </Tooltip>
                            </CopyToClipboard>
                          )
                        }}

                        value={runLink + data.code}
                        displayBlock
                        fullWidth
                    />
                }
                <ViewerTextField
                    label="Дата завершения"
                    value={data.expirationDate}
                />
                
            </div>
    })
    tabs.push({
        tablabel: "Рассылка",
        tabdisabled: !(data.reportId && data.name && data.description) ,
        tabcontent: 
            <div className={classes.viewerTabPage}>
                <FormControl component="fieldset">
                    <FormLabel component="legend">Тип рассылки</FormLabel>
                        <RadioGroup row aria-label="taskType" name="taskType" 
                            value={data.taskTypeId} 
                            defaultValue = {data.taskTypeId}
                        >
                            <FormControlLabel value= '0' control={<Radio disabled/>} label="E-mail" />
                            <FormControlLabel value= '1' control={<Radio disabled/>} label="Задания" />
                        </RadioGroup>
                </FormControl>    
                {data.taskTypeId === '0' &&
                    <ViewerTextField
                        label="Адреса"
                        value={data.destinationEmails}
                        multiline
                    />
                }
                
                <DesignerMultipleSelectField
                    // minWidth = {StyleConsts.designerTextFieldMinWidth}
                    label = "Пользователи"
                    value = {data.destinationUsers}
                    data = {data.destinationUsers}
                    displayBlock
                    fullWidth
                    disabled
                />
                <DesignerMultipleSelectField
                    // minWidth = {StyleConsts.designerTextFieldMinWidth}
                    label = "Роли"
                    value = {data.destinationRoles}
                    data = {data.destinationRoles}
                    displayBlock
                    fullWidth
                    disabled
                />
                {data.taskTypeId === '0' &&
                    <ViewerTextField
                        label="Тема письма"
                        value={data.reportTitleMail}
                        multiline
                    />
                } 
                {data.taskTypeId === '0' &&
                    <ViewerTextField
                        label="Тело письма"
                        value={data.reportBodyMail}
                        multiline
                    />
                }
                {data.taskTypeId === '0' &&
                    <FormControlLabel
                        control={
                            <Checkbox
                                disabled 
                                checked={data.sendEmptyReport}
                                name="sendEmptyReport"
                                color="primary"
                            />
                        }
                        label="Отправлять пустой отчёт?"
                    />
                }
            </div>
    })

    tabs.push({
        tablabel: "Падение рассылки",
        tabdisabled: !(data.reportId && data.name && data.description) ,
        tabcontent: 
            <div className={classes.viewerTabPage}>
                <ViewerTextField
                    label="Адреса"
                    value={data.errEmails}
                    multiline
                />
                <DesignerMultipleSelectField
                    label = "Пользователи"
                    value = {data.errUsers}
                    data = {data.errUsers}
                    displayBlock
                    fullWidth
                    disabled
                />
                <DesignerMultipleSelectField
                    label = "Роли"
                    value = {data.errRoles}
                    data = {data.errRoles}
                    displayBlock
                    fullWidth
                    disabled
                /> 
                <ViewerTextField
                    label="Тема письма"
                    value={data.errorTitleMail}
                    multiline
                />
                <ViewerTextField
                    label="Тело письма"
                    value={data.errorBodyMail}
                    multiline
                />
                <div style={{display: 'flex'}}> 
                    <ViewerTextField
                        label="Количество падений"
                        helperText = {`после которых задание изменит статус на ${ScheduleStatusMap.get('FAILED')}`}
                        value={data.maxFailedStarts}
                        minWidth = {`calc(50% - 16px)`}
                    /> 
                    <ViewerTextField
                        margin ={'8px 0px 0px 16px'}
                        minWidth = {'50%'}
                        label="Количество фактических падений"
                        value={data.failedStart}
                    />
                </div>
            </div>
    })

   if (data.hasFilters){
        tabs.push({
            tablabel: "Фильтры",
            tabdisabled: !(data.reportId && data.name && data.description) ,
            tabcontent: 
                <ReportStarterViewer
                    reportId = {data.reportId}
                    scheduleTaskId = {data.id}
                    parameters = {data.reportJobFilter}
                    onDataLoadFunction={dataHub.reportController.getScheduleReport}
                    />
        })
    }
    return <DataLoader
        loadFunc={loadFunc}
        loadParams={loadParams}
        onDataLoaded={handleDataLoaded}
        onDataLoadFailed={message => enqueueSnackbar(`Не удалось получить информацию об отчёте на расписании: ${message}`, {variant : "error"})}
    >
        <DataLoader
            loadFunc={loadFuncRunLink}
            loadParams={[]}
            onDataLoaded={handleRunLinkoaded}
            onDataLoadFailed={message => enqueueSnackbar(`Не удалось получить ссылку для запуска отчета по требованию: ${message}`, {variant : "error"})}
        >
            <ViewerPage   
                itemType={FolderItemTypes.scheduleTasks}
                id={props.scheduleId}
                onOkClick={props.onOkClick}
                disabledPadding={true}
                readOnly={!hasRWRight}
            >
                <PageTabs
                    tabsdata={tabs}
                    pageName={createViewerPageName(FolderItemTypes.scheduleTasks, data.name)}
                />
            </ViewerPage>
        </DataLoader>
    </DataLoader>
}
