import React, {useState} from "react";
import {useSnackbar} from "notistack";
import {CopyToClipboard} from 'react-copy-to-clipboard';

// dataHub
import dataHub from "ajax/DataHub";

// components
import {CircularProgress} from "@material-ui/core";
import DesignerFolderItemPicker from 'main/Main/Development/Designer/DesignerFolderItemPicker';

import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import Checkbox from '@material-ui/core/Checkbox';
import IconButton from '@material-ui/core/IconButton';
import FileCopyIcon from '@material-ui/icons/FileCopy';
import Tooltip from '@material-ui/core/Tooltip';

// date utils
import 'date-fns';
import RuLocalizedUtils from 'utils/RuLocalizedUtils'
import ruLocale from "date-fns/locale/ru";
import {MuiPickersUtilsProvider, KeyboardDatePicker,} from '@material-ui/pickers';

// local components
import {randomWordCode} from 'utils/randomWordCode';
import DataLoader from "main/DataLoader/DataLoader";
import PageTabs from "main/PageTabs/PageTabs";
import DesignerPage from "main/Main/Development/Designer/DesignerPage";
import DesignerTextField from "main/Main/Development/Designer/DesignerTextField";
import DesignerMultipleSelectField from "main/Main/Development/Designer/DesignerMultipleSelectField";
import ScheduleTasksExcellTemplatesSelect from "./ScheduleTasksExcellTemplatesSelect";
import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";
import ReportStarter from '../../../Report/ReportStarter';
import {ScheduleTaskTypeMap, ScheduleStatusMap} from '../../../FolderContent/JobFilters/JobStatuses';
import DesignerTextFieldWithSeparator from 'main/Main/Development/Designer/DesignerTextFieldWithSeparator';
//styles
import {DesignerCSS} from '../../Development/Designer/DesignerCSS';
/**
 * @callback onExit
 */

/**
 * Компонент создания и редактирования отчетов на расписании
 * @param {Object} props - параметры компонента
 * @param {String} props.mode - "create" - создание; "edit" - редактирование
 * @param {String} props.status - статус задания. Если CHANGED, то переход на вкладку "Фильтры обязателен"
 * @param {Number} props.reportId - идентификатор отчета на расписании
 * @param {onExit} props.onExit - callback, вызываемый при закрытии формы
 * @return {JSX.Element}
 * @constructor
 */
export default function ScheduleTasksDesigner(props) {
    const classes = DesignerCSS();
 
    const {enqueueSnackbar} = useSnackbar();

    const [uploading, setUploading] = useState(false);
    const [errorField, setErrorField] = useState({});

    let maxDate = new Date();
    maxDate.setMonth(maxDate.getMonth() + 6);

    const [data, setData] = useState({
        code: null,
        name: null,
        description: null,
        reportId: null,
        report: {},
        excelTemplateId: null,
        schedules: [],
        expirationDate: maxDate,
        taskTypeId: '0',
        destinationEmails: [],
        destinationRoles: [],
        destinationUsers: [],
        reportTitleMail: null,
        reportBodyMail: null,
        errorTitleMail: null,
        errorBodyMail: null,
        errEmails: [],
        errRoles: [],
        errUsers: [],
        users: [],
        roles: [],
        reportJobFilter: [],
        sendEmptyReport: true,
        maxFailedStarts: 0
    });

    const FieldNames = new Map([      
		["name"             , "Наименование"    ],
		["description"      , "Описание"        ],
		["reportId"         , "Отчёт"           ],
		["excelTemplateId"  , "Шаблон Excel"    ],
		["schedules"        , "Расписания"      ],
		["taskTypeId"       , "Тип рассылки"    ],
        ["destinationEmails", "E-mail"          ],
        ["destinationUsers" , "Пользователи"    ],
        ["destinationRoles" , "Роли"            ],
        ["errEmails"        , "E-mail"          ],
        ["errRoles"         , "Роли"            ],
        ["errUsers"         , "Пользователи"    ],
		["expirationDate"   , "Дата завершения" ],
        ["reportBodyMail"   , "Тело письма"     ],
        ["reportTitleMail"  , "Тема письма"     ],
        ["errorTitleMail"   , "Тема письма"     ],
        ["errorBodyMail"    , "Тело письма"     ],
		["code"             , "Код"             ],
        ["reportJobFilter"  , "Фильтры к отчёту"],
        ["sendEmptyReport"  , "Отправлять пустой отчёт"],
        ["maxFailedStarts " , "Количество падений"]
    ]); 

    const [id, setId] = useState(props.scheduleId);
    const [scheduledReport, setScheduledReport] = useState([]);
    const [schedulesList, setSchedulesList] = useState([]);
    const [usersList, setUsersList] = useState([]);
    const [rolesList, setRolesList] = useState([]);
    const [taskTypeId, setTaskTypeId] = useState('0');
    const [isManual, setIsManual] = useState(false);
    const [runLink, setRunLink] = useState('');
    const [emailStatus, setEmailStatus] = useState('success');
    const [emailErrorStatus, setEmailErrorStatus] = useState('success');
    const [disableSave, setDisableSave] = useState(true);
    const [hideFilterTab, setHideFilterTab] = useState(false);
    const pageName = props.mode === "create" ? "Добавление отчёта на расписание" : "Редактирование отчёта на расписании";
    
    function handleSchedulesLoaded(data){
        setSchedulesList(data.map((v) => ({id: v.id, name: v.name, type: v.type})));
    }

    function handleRolesLoaded(data){
        setRolesList(data.map((v) => ({id: v.id, name: v.name})))
    }

    function handleUsersLoaded(data){
        setUsersList(data.map((v) => ({id: v.id, name: v.name, email: v.email})))
    }

    function handleRunLinkLoaded(data){
        setRunLink(data)  //.map((v) => ({id: v.id, name: v.name, email: v.email})))
    }

    let loadFunc;
    let loadParams = [];
    let loadFuncSchedules = dataHub.scheduleController.getAll;
    let loadFuncUsers = dataHub.userController.users;
    let loadFuncRoles = dataHub.roleController.getAll;
    let loadFuncRunLink = dataHub.scheduleController.taskGetManualLink;

    if (props.mode === "edit") {
        loadFunc = dataHub.scheduleController.taskGet;
        loadParams = [id];
    }

    function hasErrors() {

        let errors = {};
        let errorExists = false;
        
        Object.entries(data)
            .filter( ([fieldName, fieldValue]) => 
                (   
                   (    fieldName === "name" || 
                        fieldName === "description" ||
                        fieldName === "reportId" ||
                        fieldName === "excelTemplateId" ||
                        fieldName === "schedules" ||
                        fieldName === "taskTypeId" ||
                        (((fieldName === "destinationEmails" && taskTypeId === '0')|| fieldName === "destinationUsers" || fieldName === "destinationRoles")
                            && (data.destinationEmails.length === 0 || taskTypeId !== '0')
                            && data.destinationUsers.length === 0 
                            && data.destinationRoles.length  === 0
                        ) ||
                        fieldName === "expirationDate" ||
                        (fieldName === "reportBodyMail" && taskTypeId === '0') ||
                        //fieldName === "reportJobFilter" ||
                        (fieldName === "code" && data.schedules.find(item  => schedulesList.filter(item=>item.type === 'MANUAL').map(item=>item.id).find(item1=>item1===item)))
                    ) &&
                    (   fieldValue === null || 
                        (typeof fieldValue === "string" && fieldValue.trim() === "") ||
                        (typeof fieldValue === "object" && fieldValue.length === 0)
                    ) 
                ))
            .reverse()
            .forEach( ([fieldName, fieldValue]) => 
                {
                    errors[fieldName] = true;
                    enqueueSnackbar("Недопустимо пустое значение в поле " + FieldNames.get(fieldName), {variant : "error"});
                    errorExists = true;
                } );

            if(errorExists){
                setErrorField(errors);
            }
            if (emailStatus !== 'success'){
                errorExists = true;
                errors["destinationEmails"] = true;
                setErrorField(errors);
                enqueueSnackbar("На вкладке Рассылки есть некорректные E-mail", {variant : "error"});
            }
            if (emailErrorStatus !== 'success'){
                errorExists = true;
                errors["errEmails"] = true;
                setErrorField(errors);
                enqueueSnackbar("На вкладке Падение расссылки есть некорректные E-mail ", {variant : "error"});
                
    
            }
            return errorExists
    }

    function handleChange(key, value){
        console.log('handleChange');
        console.log(key);
        console.log(value);

        setData({...data, [key]: value});
        if (key === 'destinationRoles' || key === 'destinationUsers' || key === 'destinationEmails') {
            setErrorField({...errorField, destinationRoles: false, destinationUsers: false, destinationEmails: false});
        }
        else {
            setErrorField({...errorField, [key]: false});
        }
    }

    function handleChangeSchedules(key, value) {
        if (Boolean(value.find(item  => schedulesList.filter(item=>item.type === 'MANUAL'
                    ).map(item=>item.id).find(item1=>item1===item)))) {
            if (isManual){
                setData({...data, [key]: value});
                setErrorField({...errorField, [key]: false});
            }
            else {
                setIsManual(true);
                setData({...data, [key]: value, code: randomWordCode(8)})
                setErrorField({...errorField, [key]: false, code: false});
                dataHub.scheduleController.taskGetManualLink(
                magResponse => handleGetManualLink(magResponse)
                );
            }
        } else {
            setIsManual(false);
            setData({...data, [key]: value, code: null});
            setErrorField({...errorField, [key]: false, code: false});
        }
    }

    function handleGetManualLink(magRepResponse){
        if (magRepResponse.ok){
            setRunLink(magRepResponse.data)
        }
        else {
            enqueueSnackbar(`Неудалось получить ссылку для запуска отчета по требованию: ${magRepResponse.data}`,
                {variant: "error"});
        }
    }

    function handleChangeTaskTypeId(value){
        setTaskTypeId(value);
        setErrorField({...errorField, 'taskTypeId': false});
    }

    
    function mandatoryFilters(data){
        let mg = mandatoryGroups(data)
        
        if (mg) {
            return true
        } else {
            if (data.childGroups){
                for (let cg of data.childGroups){
                    let mf =  mandatoryFilters(cg);
                    if (mf) {
                        return true
                    }
                }
            }
            if (data.filters){
                for (let f of data.filters){
                    if (f.mandatory){
                        return true
                    }
                }
            }
        }
        return false
    }

    function mandatoryGroups(group){
        if (group.mandatory) {
            return true
            
        } else {
            return false
        }
    }

    function handleChangeReport (reportId, reportData, folderId) {
        if(reportId !== data.reportId){
            let mf = mandatoryFilters(reportData.filterGroup);
            setScheduledReport(reportData);
            setData({...data, reportId: reportId});
            setErrorField({...errorField, 'reportId': false});
            let childGroups = reportData.filterGroup.childGroups === null ? [] : reportData.filterGroup.childGroups;
            let filters     = reportData.filterGroup.filters     === null ? [] : reportData.filterGroup.filters;

            if (childGroups.length === 0  && filters.length === 0){
                //Нет фильтров, раздизейблить кнопку Сохранить, убрать вкладку Фильтры
                setDisableSave(false);
                setHideFilterTab(true);
            }
            else { if ( mf)  { 
                //Есть обязательные фильтры, задизейблить кнопку сохранения
                    setDisableSave(true)
                } else {
                    setDisableSave(false)
                }
                setHideFilterTab(false);
            };
        }
    }

    function handleChangeEmails(key, value, status, source){
        handleChange(key, value);
        if (source === 'destinationEmails'){
            setEmailStatus(status);
        } else {
            setEmailErrorStatus(status);
        }
    }

    function onSaveScheduleTaskFilterData(filtersData){
        if (filtersData.length > 0) {
            setData({...data, "reportJobFilter": filtersData});
        }
        setErrorField({...errorField, reportJobFilter: false});
        setDisableSave(false);
    }

    function handleSave() { 
        let destEmails =  taskTypeId === '0' ? data.destinationEmails.map(item=>({emailValue: item, typeId: 0})): [];
        let destUsers = data.destinationUsers.map(item=>({userName: item, typeId: 0}));
        let destRoles = data.destinationRoles.map(item=>({roleId: item.id, name: item.name, typeId: 0}));
        let dataToSave = {
            code: data.schedules.find(item  => schedulesList.filter(item=>item.type === 'MANUAL').map(item=>item.id).find(item1=>item1===item)) ? data.code : null,
            description: data.description,
            destinationEmails:  destEmails.concat( data.errEmails.map(item=>({emailValue: item, typeId: 1}))),
            destinationUsers:  destUsers.concat( data.errUsers.map(item=>({userName: item, typeId: 1}))),
            destinationRoles : destRoles.concat( data.errRoles.map(item=>({roleId: item.id, name: item.name, typeId: 1}))),
            expirationDate: data.expirationDate,
            reportTitleMail:   data.reportTitleMail?.trim() === "" ? null : data.reportTitleMail,
            reportBodyMail: taskTypeId === '0' ? data.reportBodyMail : null,
            errorTitleMail: data.errorTitleMail,
            errorBodyMail: data.errorBodyMail,
            name: data.name,
            excelTemplateId: data.excelTemplateId,
            reportId: data.reportId,
            reportJobFilter: data.reportJobFilter,
            schedules: data.schedules.map(item=> ({id: item})),
            taskTypeId: parseInt(taskTypeId),
            sendEmptyReport: data.sendEmptyReport,
            maxFailedStarts: data.maxFailedStarts
        }; 

        if(hasErrors()) {
            //enqueueSnackbar(`Форма содержит ошибки`, {variant: "error"});
        } else if (props.mode === "create") {
            dataHub.scheduleController.taskAdd(
                dataToSave,
                magResponse => handleAddedEdited(magResponse)
            );
            setUploading(true);
        } else {
            dataHub.scheduleController.taskEdit(
                {id: props.scheduleId, ...dataToSave },
                magRepResponse => handleAddedEdited(magRepResponse)
            );
            setUploading(true);
        }
    }

    function handleAddedEdited(magRepResponse) {
        if (magRepResponse.ok) {
            props.onExit();
            enqueueSnackbar("Отчет на расписании успешно сохранен", {variant : "success"});
        } else {
            setUploading(false);
            const actionWord = props.mode === "create" ? "создании" : "обновлении";
            enqueueSnackbar(`При ${actionWord} возникла ошибка: ${magRepResponse.data}`,
                {variant: "error"});
        }
    }

    function handleCancel() {
        props.onExit();
    }

    function handleDataLoaded(loadedData) {

        let tTask = ScheduleTaskTypeMap.get(loadedData.typeTask);

        let childGroups = loadedData.report.filterGroup.childGroups === null ? [] : loadedData.report.filterGroup.childGroups;
        let filters     = loadedData.report.filterGroup.filters     === null ? [] : loadedData.report.filterGroup.filters;

        setId(loadedData.id);
        setScheduledReport({id: loadedData.report.id, name: loadedData.report.name});
        setTaskTypeId(tTask);
        setIsManual(loadedData.code ? true : false);
        //setDestinationTypeId(tDest);
        setData({
            code: loadedData.code,
            description: loadedData.description,
            destinationEmails: loadedData.destinationEmails.filter(item =>item.type === 'REPORT').map(item=>item.value),
            destinationUsers: loadedData.destinationUsers.filter(item=>item.type === 'REPORT').map(item=>item.userName),
            destinationRoles: loadedData.destinationRoles.filter(item=>item.type === 'REPORT').map( item=>({id: item.roleId, name: item.name})),
            errEmails: loadedData.destinationEmails.filter(item=>item.type === 'ERROR').map(item=>item.value),
            errUsers: loadedData.destinationUsers.filter(item=>item.type === 'ERROR').map(item=>item.userName),
            errRoles: loadedData.destinationRoles.filter(item=>item.type === 'ERROR').map( item=>({id: item.roleId, name: item.name})),
            expirationDate: loadedData.expirationDate,
            reportTitleMail: loadedData.reportTitleMail,
            reportBodyMail: loadedData.reportBodyMail,
            errorTitleMail: loadedData.errorTitleMail,
            errorBodyMail: loadedData.errorBodyMail,
            name: loadedData.name,
            excelTemplateId: loadedData.excelTemplate.excelTemplateId,
            reportId: loadedData.report.id,
            reportJobFilter: loadedData.reportJobFilters,
           // reportJobFilterLast: 
            schedules:  loadedData.schedules.map(item=>item.id),
            taskTypeId: parseInt(tTask),
            sendEmptyReport: loadedData.sendEmptyReport,
            maxFailedStarts: loadedData.maxFailedStarts ,
            failedStart: loadedData.failedStart
        });
        if  (childGroups.length === 0  && filters.length === 0) {
            setDisableSave(false);
            setHideFilterTab(true);
        }
        else if (props.status === 'CHANGED') { setDisableSave(true)}
             else {setDisableSave(false)}
        
    };

    function handleCheckFilters(isCorrect){ 
        setDisableSave(!isCorrect);
    }

    // building component
    const tabs = [];

    // general
    tabs.push({
        tablabel: "Общие",
        tabcontent: uploading ? <CircularProgress/> :
            <DesignerPage
                disableSave = {disableSave}
                onSaveClick={handleSave}
                onCancelClick={handleCancel}
            >
                <DesignerFolderItemPicker
                //minWidth = {StyleConsts.designerTextFieldMinWidth}
                    label = "Отчёт"
                    value = {scheduledReport.name}
                    itemType = {FolderItemTypes.reportsDev}
                    onChange = {handleChangeReport}
                    displayBlock
                    fullWidth
                    error = {errorField.reportId}
                />
                <DesignerTextField
                    label="Название"
                    value={data.name}
                    onChange={data => {handleChange('name', data)}}
                    displayBlock
                    fullWidth
                    error = {errorField.name}
                />
                <DesignerTextField
                    label="Описание"
                    value={data.description}
                    onChange={data => {handleChange('description', data)}}
                    displayBlock
                    fullWidth
                    error = {errorField.description}
                />
                {data.reportId &&
                    <ScheduleTasksExcellTemplatesSelect
                        //minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = "Шаблон Excel"
                        reportId = {data.reportId}
                        value = {data.excelTemplateId}
                        onChange = {handleChange}
                        displayBlock
                        fullWidth
                        error = {errorField.excelTemplateId}
                    />
                }
            
            </DesignerPage>
    })

    tabs.push({
        tablabel: "Расписания",
        tabdisabled: !(data.reportId && data.name && data.description) ,
        tabcontent: uploading ? <CircularProgress/> :
            <DesignerPage
                disableSave = {disableSave}
                onSaveClick={handleSave}
                onCancelClick={handleCancel}
            >
                <DesignerMultipleSelectField
                   // minWidth = {StyleConsts.designerTextFieldMinWidth}
                    label = "Расписания"
                    value = {schedulesList.filter(item =>  data.schedules.indexOf(item.id)>=0 )}
                    data = {schedulesList}
                    onChange = {data => {handleChangeSchedules('schedules', data)}}
                    displayBlock
                    fullWidth
                    error = {errorField.schedules}
               />
                { isManual &&
                    <DesignerTextField
                        label="Код"
                        value={data.code}
                        onChange={data => {handleChange('code', data)}}
                        displayBlock
                        fullWidth
                        error = {errorField.code}
                    />
                }
                { isManual &&
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
                       // onChange={data => {handleChange('code', data)}}
                        displayBlock
                        fullWidth
                       // error = {errorField.code}
                    />
                }

               <MuiPickersUtilsProvider utils={RuLocalizedUtils} locale={ruLocale}>
                    <KeyboardDatePicker
                        style={{margin: '8px 0px'}}
                        autoOk
                        views={["year", "month", "date"]}
                        openTo="date"
                        variant="inline"
                        inputVariant="outlined"
                        format="dd.MM.yyyy"
                        id="expirationDate"
                        maxDate = {maxDate}
                      //  defaultValue = {maxDate}
                        label="Дата завершения"
                        value={data.expirationDate}
                        onChange={(data) => handleChange( "expirationDate", data)}
                        KeyboardButtonProps={{
                            'aria-label': 'expirationDate',
                        }}
                        error = {errorField.expirationDate}
                    />
                </MuiPickersUtilsProvider>
                
            </DesignerPage>
    })

    tabs.push({
        tablabel: "Рассылка",
        tabdisabled: !(data.reportId && data.name && data.description) ,
        tabcontent: uploading ? <CircularProgress/> :
            <DesignerPage
                disableSave = {disableSave}
                onSaveClick={handleSave}
                onCancelClick={handleCancel}
            >
                <FormControl component="fieldset" style={{margin: '8px 0px'}}>
                    <FormLabel component="legend">Тип рассылки:</FormLabel>
                        <RadioGroup row aria-label="taskType" name="taskType" 
                            value={taskTypeId} 
                            defaultValue = {taskTypeId}
                            onChange={(event) => handleChangeTaskTypeId( event.target.value)}
                        >
                            <FormControlLabel value= '0' control={<Radio />} label="E-mail" />
                            <FormControlLabel value= '1' control={<Radio />} label="Задания" />
                        </RadioGroup>
                </FormControl>
                
                {taskTypeId === '0' &&
                    <DesignerTextFieldWithSeparator
                        label = "Адреса"
                        source = "destinationEmails"
                        value = {data.destinationEmails}
                        onChange = {handleChangeEmails}
                        mandatory = {taskTypeId === '0'}
                        displayblock 
                        fullWidth
                        multiline
                        error = {errorField.destinationEmails}
                    />
                }    

                <DesignerMultipleSelectField
                        // minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = "Пользователи"
                        value = {usersList.filter(item => data.destinationUsers.indexOf(item.name)>=0 )}
                        data = {usersList}
                        needName = {true}
                        onChange = {data => {handleChange('destinationUsers', data)}}
                        displayBlock
                        fullWidth
                        error = {errorField.destinationUsers}
                    />
                <DesignerMultipleSelectField
                        // minWidth = {StyleConsts.designerTextFieldMinWidth}
                        label = "Роли"
                        value = {rolesList.filter(item => data.destinationRoles.map(item => item.id).indexOf(item.id)>=0 )}
                        data = {rolesList}
                        needIdName = {true}
                        onChange = {data => {handleChange('destinationRoles', data )}}
                        displayBlock
                        fullWidth
                        error = {errorField.destinationRoles}
                />
                {taskTypeId === '0' &&
                    <DesignerTextField
                        label="Тема письма"
                        value={data.reportTitleMail}
                        onChange={data => {handleChange('reportTitleMail', data)}}
                        multiline
                        displayBlock
                        fullWidth
                        error = {errorField.reportTitleMail}
                    />
                } 
                {taskTypeId === '0' &&
                    <DesignerTextField
                        label="Тело письма"
                        value={data.reportBodyMail}
                        onChange={data => {handleChange('reportBodyMail', data)}}
                        multiline
                        displayBlock
                        fullWidth
                        error = {errorField.reportBodyMail}
                    />
                }
                {taskTypeId === '0' &&
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={data.sendEmptyReport}
                                onChange={event => {handleChange('sendEmptyReport', event.target.checked)}}
                                name="sendEmptyReport"
                                color="primary"
                            />
                        }
                        label="Отправлять пустой отчёт?"
                    />
                }
            </DesignerPage>
        })
        tabs.push({
            tablabel: "Падение рассылки",
            tabdisabled: !(data.reportId && data.name && data.description) ,
            tabcontent: uploading ? <CircularProgress/> :
                <DesignerPage
                    disableSave = {disableSave}
                    onSaveClick={handleSave}
                    onCancelClick={handleCancel}
                >
                    <DesignerTextFieldWithSeparator
                            label = "Адреса"
                            source = "errEmails"
                            value = {data.errEmails}
                            onChange = {handleChangeEmails}
                            displayblock 
                            fullWidth
                            multiline
                            error = {errorField.errEmails}
                    />  
                    <DesignerMultipleSelectField
                        label = "Пользователи"
                        value = {usersList.filter(item => data.errUsers.indexOf(item.name)>=0 )}
                        data = {usersList}
                        needName = {true}
                        onChange = {data => {handleChange('errUsers', data)}}
                        displayBlock
                        fullWidth
                        error = {errorField.errUsers}
                    />
                    <DesignerMultipleSelectField
                        label = "Роли"
                        value = {rolesList.filter(item => data.errRoles.map(item => item.id).indexOf(item.id)>=0 )}
                        data = {rolesList}
                        needIdName = {true}
                        onChange = {data => {handleChange('errRoles', data )}}
                        displayBlock
                        fullWidth
                        error = {errorField.errRoles}
                />
                  <DesignerTextField
                            label="Тема письма"
                            value={data.errorTitleMail}
                            onChange={data => {handleChange('errorTitleMail', data)}}
                            multiline
                            displayBlock
                            fullWidth
                            error = {errorField.errorTitleMail}
                    />
                    <DesignerTextField
                            label="Тело письма"
                            value={data.errorBodyMail}
                            onChange={data => {handleChange('errorBodyMail', data)}}
                            multiline
                            displayBlock
                            fullWidth
                            error = {errorField.errorBodyMail}
                    />
                    <div style={{display: 'flex'}}> 
                        <DesignerTextField
                            label="Количество падений"
                            helperText = {`после которых задание изменит статус на ${ScheduleStatusMap.get('FAILED')}`}
                            value={data.maxFailedStarts}
                            minWidth = {`calc(50% - 16px)`}
                            onChange={data => {handleChange('maxFailedStarts', data)}}
                            type='number'
                            error = {errorField.maxFailedStarts}
                        /> 
                        <DesignerTextField
                            margin ={'8px 0px 0px 16px'}
                            minWidth = {'50%'}
                            label="Количество фактических падений"
                            value={data.failedStart}
                            onChange={data => {handleChange('failedStart', data)}}
                            disabled
                            type='number'
                            error = {errorField.failedStart}
                        />
                    </div>
    
                </DesignerPage>
            })
    if (!hideFilterTab){
        tabs.push({
            tablabel: "Фильтры",
            tabdisabled: !(data.reportId && data.name && data.description) ,
            tabcontent: uploading ? <CircularProgress/> :
                <DesignerPage
                    //onSaveClick={handleSave}
                    //onCancelClick={handleCancel}
                > 
                    <ReportStarter
                        woStartButton = {true}
                        reportId = {data.reportId}
                        onDataLoadFunction={dataHub.reportController.getScheduleReport}
                        scheduleTaskId={id}
                        parameters = {data.reportJobFilter}
                        onCancel = {handleCancel}
                        onSave = {handleSave}
                        onSaveScheduleTaskFilterData = {onSaveScheduleTaskFilterData}
                        checkFilters = {handleCheckFilters}
                    />
                </DesignerPage>
        })
    }

    return <DataLoader
        loadFunc={loadFunc}
        loadParams={loadParams}
        onDataLoaded={handleDataLoaded}
        onDataLoadFailed={message => enqueueSnackbar(`Не удалось получить информацию об отчёте на расписании: ${message}`, {variant : "error"})}
    >
        <DataLoader
            loadFunc = {loadFuncSchedules}
            loadParams={[]}
            onDataLoaded = {handleSchedulesLoaded}
            onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить информацию о расписаниях: ${message}`, {variant : "error"})}
        >
            <DataLoader
                loadFunc = {loadFuncRoles}
                loadParams={[]}
                onDataLoaded = {handleRolesLoaded}
                onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить информацию о ролях: ${message}`, {variant : "error"})}
            >
                <DataLoader
                    loadFunc = {loadFuncUsers}
                    loadParams={[]}
                    onDataLoaded = {handleUsersLoaded}
                    onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить информацию о пользователях: ${message}`, {variant : "error"})}
                >
                    <DataLoader
                        loadFunc = {loadFuncRunLink}
                        loadParams={[]}
                        onDataLoaded = {handleRunLinkLoaded}
                        onDataLoadFailed = {message => enqueueSnackbar(`Не удалось получить ссылку для запуска отчета по требованию: ${message}`, {variant : "error"})}
                    >
                        <div className={classes.rel}> 
                            <div className={classes.abs}>
                                <PageTabs
                                    tabsdata={tabs}
                                    pageName={pageName}
                                />
                            </div>
                        </div>
                    </DataLoader>
                </DataLoader>
            </DataLoader>
       </DataLoader>
    </DataLoader>
}
