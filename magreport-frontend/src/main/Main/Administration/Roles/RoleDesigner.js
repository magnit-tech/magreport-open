import React from 'react';
import {useState} from 'react';
import { connect } from 'react-redux';
import { useSnackbar } from 'notistack';

// components
import { CircularProgress, Tooltip } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import CreateIcon from '@material-ui/icons/Create';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
// local
import DesignerPage from '../../Development/Designer/DesignerPage';
import PageTabs from 'main/PageTabs/PageTabs';
import DesignerTextField from '../../Development/Designer/DesignerTextField';
import UserList from '../Users/UserList';
import DomainGroupList from '../DomainGroups/DomainGroupList';
import {FolderItemTypes} from '../../../../main/FolderContent/FolderItemTypes';
import AsyncAutocomplete from '../../../../main/AsyncAutocomplete/AsyncAutocomplete';
import PermittedFoldersList from './PermittedFoldersList';
import DesignerFolderBrowser from '../../Development/Designer/DesignerFolderBrowser';

// dataHub
import dataHub from 'ajax/DataHub';
import DataLoader from 'main/DataLoader/DataLoader';

// styles 
import { RolesCSS } from "./RolesCSS";

// actions
import { actionUsersLoaded, actionUsersLoadFailed } from 'redux/actions/admin/actionUsers';
import { actionRoleSelectFolderType } from 'redux/actions/admin/actionRoles';
/**
 * 
 * @param {*} props.mode : 'edit', 'create' - режим редактирования или создания нового объекта
 * @param {*} props.datasourceId : id объекта при редактировании (имеет значение только при mode == 'edit')
 * @param {*} props.folderId : id папки в которой размещается объект при создании (имеет значение только при mode == 'create')
 * @param {*} props.onExit : callback при выходе
 */
function RoleDesigner(props){

    const classes = RolesCSS();
    const { enqueueSnackbar } = useSnackbar();

    const [pageName, setPagename] = useState(props.mode === 'create' ? "Создание роли" : "Редактирование роли");

    const [uploading, setUploading] = useState(false);
    const [reload, setReload] = useState({ needReload: false });
    const [selectedUserToAdd, setSelectedUserToAdd] = useState("");
    const [selectedDomainGroupToAdd, setSelectedDomainGroupToAdd] = useState("");
    const [folderBrowserOpen, setFolderBrowserOpen] = useState(false);
    const [readWrite, setReadWrite] = useState(['READ']);

    const [data, setData] = useState({
        roleName : '',
        roleDescription : ''
    });


    const [domainGroupsData, setDomainGroupsData] = useState({
        domainGroups: []
    });

    const [permittedFolders, setPermittedFolders] = useState({loadedData: {}, namesList: []});
    const [selectedPermittedFolder, setSelectedPermittedFolder] = useState(props.selectedFolderType ?? 'userReportFolders');

    const fieldLabels = {
        roleName : "Название",
        roleDescription : "Описание"
    }

    const [errorField, setErrorField] = useState({});

    let loadFunc;
    let loadParams = [];
    

    if(props.mode === 'edit'){
        loadFunc = dataHub.roleController.get;
        loadParams = [props.roleId];
    }

    /*
        Data loading
    */

    function enableRoleReload(){
        setReload({needReload: true})
    }    

    function handleDataLoaded(loadedData){
        setData({
            ...data,
            roleName : loadedData.name,
            roleDescription : loadedData.description,
        });

        setPagename("Редактирование роли: " + loadedData.name);
    }

    function handleDataLoadFailed(message){
        
    }

    function handleDomainGroupsDataLoaded(loadedData){
        setDomainGroupsData({
            ...domainGroupsData,
            domainGroups : loadedData.domainGroups,
        });
    }

    function handleDomainGroupsDataLoadFailed(message){
    }

    function handlePermittedFoldersLoaded(loadedData){
        let namesList = Object.keys(loadedData);
        let fullNamesList = []; //[{id: 'all', value: 'Все'}];
        let n = ''; 
        
        for (let name of namesList){
            
            n = name.toLowerCase().includes('filtertemplate') ?  'Шаблоны фильтров':
                name.toLowerCase().includes('exceltemplate') ? 'Шаблоны Excel': 
                name.toLowerCase().includes('filterinstance') ? 'Экземпляры фильтров':
                name.toLowerCase().includes('securityfilter') ? 'Фильтры безопасности':
                name.toLowerCase().includes('datasource') ? 'Источники данных':
                name.toLowerCase().includes('dataset') ? 'Наборы данных':
                name.toLowerCase().includes('devreport') ? 'Разработка отчётов':
                name.toLowerCase().includes('userreport') ? 'Отчёты':
                name;

            if (name !==n) {fullNamesList.push({id: name, value: n})};
        }

        setPermittedFolders({...permittedFolders, 
            loadedData: loadedData, 
            namesList:  fullNamesList
        })
      //  setSelectedPermittedFolder('userReportFolders');
    }

    const handleChangeSelectedPermittedFolder = (event) => {
        setSelectedPermittedFolder(event.target.value);
        props.actionRoleSelectFolderType(event.target.value)
    };

    function handleChangeName(newName){
        setData({
            ...data,
            roleName : newName
        });
        setErrorField({
            ...errorField,
            roleName : false
        });
    }

    function handleChangeDescription(newDescription){
        setData({
            ...data,
            roleDescription : newDescription
        });
        setErrorField({
            ...errorField,
            roleDescription : false
        });        
    }

    function handleTabChange(tabId){
    }
    
    /*
        Save and cancel
    */

    function handleSave(){
        
        let errors = {};
        let errorExists = false;
        
        // Проверка корректности заполнения полей
        Object.entries(data)
            .filter( ([fieldName, fieldValue]) => 
                ( fieldName !== "roleId" && (fieldValue === null || (typeof fieldValue === "string" && fieldValue.trim() === "") ) ) )
            .reverse()
            .forEach( ([fieldName, fieldValue]) => 
                {
                    errors[fieldName] = true;
                    enqueueSnackbar("Недопустимо пустое значение в поле " + fieldLabels[fieldName], {variant : "error"});
                    errorExists = true;
                } );
        
        if(errorExists){
            setErrorField(errors);
        }
        else{
            if(props.mode === 'create'){
                dataHub.roleController.add(
                    props.roleId, 
                    props.roleTypeId,
                    data.roleName, 
                    data.roleDescription,
                    handleAddEditAnswer);
            }
            else{
                dataHub.roleController.edit(
                    props.roleId,
                    props.roleTypeId,
                    data.roleName, 
                    data.roleDescription,
                    handleAddEditAnswer);
            }
            setUploading(true);
        }
    }

    function handleCancel(){
        props.onExit();
    }

    function handleOnChangeAddUserText(value){
        setSelectedUserToAdd(value);
    }

    function handleOnChangeAddDomainGroupText(value){
        setSelectedDomainGroupToAdd(value);
    }

    function handleAddUserToRole(){
        for (let u of props.users.data) {
            if (u.id === selectedUserToAdd.id) {
                enqueueSnackbar("Пользователю уже назначена эта роль!", {variant : "error"});
                return;
            }
        }
        dataHub.roleController.addUsers(props.roleId, [selectedUserToAdd.id], handleAddUserToRoleResponse)
    }

    function handleAddUserToRoleResponse(magrepResponse){
        if (magrepResponse.ok) {
            enqueueSnackbar("Пользователь добавлен!", {variant : "success"});
            enableRoleReload()
        }
        else {
            enqueueSnackbar("Не удалось добавить пользователя", {variant : "error"});
        }
    }

    function handleAddDomainGroupToRole(){
        for (let u of domainGroupsData.domainGroups) {
            if (u === selectedDomainGroupToAdd.name) {
                enqueueSnackbar("Доменной группе уже назначена эта роль!", {variant : "error"});
                return;
            }
        }
        dataHub.roleController.addDomainGroups(props.roleId, [selectedDomainGroupToAdd.name], handleAddDomainGroupToRoleResponse)
    }

    function handleAddDomainGroupToRoleResponse(magrepResponse){
        if (magrepResponse.ok) {
            enqueueSnackbar("Доменная группа добавлена!", {variant : "success"});
            enableRoleReload()
        }
        else {
            enqueueSnackbar("Не удалось добавить доменную группу", {variant : "error"});
        }
    }

    function handleDeleteDomainGroupFromRoleResponse (magrepResponse) {
        enableRoleReload()
    }

    function handleAddEditAnswer(magrepResponse){
        setUploading(false);
        if(magrepResponse.ok){
            props.onExit();
        }
        else{
            let actionWord = props.mode === 'create' ? "создании" : "обновлении";
            enqueueSnackbar("Ошибка при " + actionWord + " объекта: " + magrepResponse.data, {variant : "error"});
        }
    }

    function handleAddFolderRead(){
        setFolderBrowserOpen(true);
        setReadWrite(['READ']);
    }

    function handleAddFolderWrite(){
        setFolderBrowserOpen(true);
        setReadWrite(['READ', 'WRITE']);
    }

    function handleCloseFolderBrowser(){
        setFolderBrowserOpen(false)
    }

    let SelectedPermittedFolderType = selectedPermittedFolder.includes("filterTemplateFolders") ? 'FILTER_TEMPLATE' :
        selectedPermittedFolder.includes("excelTemplateFolders") ? 'EXCEL_TEMPLATE' : 
        selectedPermittedFolder.includes("filterInstanceFolders") ? 'FILTER_INSTANCE' :
        selectedPermittedFolder.includes("securityFilterFolders") ? 'SECURITY_FILTER' :
        selectedPermittedFolder.includes("datasourceFolders") ? 'DATASOURCE' :
        selectedPermittedFolder.includes("datasetFolders") ? 'DATASET' :
        selectedPermittedFolder.includes("devReportFolders") ? 'REPORT_FOLDER' :
        selectedPermittedFolder.includes("userReportFolders") ? 'PUBLISHED_REPORT' : 'PUBLISHED_REPORT';

    /* Adding a folder role */
    function handleSubmit(folderId, roleType){
        dataHub.roleController.addPermission(folderId, roleType, props.roleId, SelectedPermittedFolderType, handleAddRolePermissionAnswer);
    }
    function handleAddRolePermissionAnswer(magrepResponse){
        if (magrepResponse.ok) {
            handleCloseFolderBrowser();
            enableRoleReload();
            enqueueSnackbar("Роль для папки добавлена!", {variant : "success"});
        }
        else {
            enqueueSnackbar("Не удалось добавить роль для папки", {variant : "error"});
        }
    }

    /* Deleting a folder role */
    function handleDeleteRolePermission(folderId){
        dataHub.roleController.deletePermission(folderId, props.roleId, SelectedPermittedFolderType, handleDeleteRolePermissionAnswer);
    }
    function handleDeleteRolePermissionAnswer(magrepResponse){
        if (magrepResponse.ok) {
            enableRoleReload();
            enqueueSnackbar("Роль с папки удалена!", {variant : "success"});
        }
        else {
            enqueueSnackbar("Не удалось удалить роль с папки", {variant : "error"});
        }
    }

    let tabs = []    

    tabs.push({tablabel:"Настройки",
        tabcontent:
        <DesignerPage 
            onSaveClick={handleSave}
            onCancelClick={handleCancel}
        >
            <DesignerTextField
                label = {fieldLabels.roleName}
                value = {data.roleName}
                onChange = {handleChangeName}
                displayBlock
                fullWidth
                error = {errorField.roleName}
                disabled = {props.folderName === 'SYSTEM'}
            />
            <DesignerTextField
                label = {fieldLabels.roleDescription}
                value = {data.roleDescription}
                onChange = {handleChangeDescription}
                displayBlock
                fullWidth
                error = {errorField.roleDescription}
            />         
        </DesignerPage>   
    });

    tabs.push({tablabel:"Пользователи",
        tabdisabled: props.roleId === null || props.roleId === undefined ? true : false,
        tabcontent:
        <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}>
            <div className={classes.userAddPanel}>
                <div className={classes.roleAutocompleteDiv}>
                    <AsyncAutocomplete 
                        disabled = {false}
                        typeOfEntity = {"user"}
                       // filterOfEntity = {(item) => item.status !== "ARCHIVE" /*"SECURITY_FILTER"*/}
                        onChange={handleOnChangeAddUserText}
                    />
                </div>
                <Button 
                    className={classes.addButton}
                    color="primary"
                    variant="outlined"
                    disabled = {false}
                    onClick={handleAddUserToRole}
                >
                    <AddIcon/>Добавить
                </Button>
            </div>
            <Paper elevation={3} className={classes.userListPaper}>
                <DataLoader
                    loadFunc = {dataHub.roleController.getUsers}
                    loadParams = {[props.roleId]}
                    reload = {reload}
                    onDataLoaded = {data => props.actionUsersLoaded(data, FolderItemTypes.roles)}
                    onDataLoadFailed = {props.actionUsersLoadFailed}
                >
                    <UserList
                        itemsType={FolderItemTypes.roles}
                        items={props.users.data}
                        showDeleteButton={true}
                        roleId={props.roleId}
                    />
                </DataLoader>
            </Paper>
        </div>
    });

    tabs.push({tablabel:"Доменные группы",
        tabdisabled: props.roleId === null || props.roleId === undefined ? true : false,
        tabcontent:
        <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}>
            <div className={classes.userAddPanel}>
                <div className={classes.roleAutocompleteDiv}>
                <AsyncAutocomplete 
                    disabled = {false}
                    typeOfEntity = {"domainGroup"}
                    onChange={handleOnChangeAddDomainGroupText}
                /> 
                </div>
                <Button
                    className={classes.addButton}
                    color="primary"
                    variant="outlined"
                    disabled = {false}
                    onClick={handleAddDomainGroupToRole}
                >
                    <AddIcon/>Добавить
                </Button>
            </div>
            <Paper elevation={3} className={classes.userListPaper}>
                <DataLoader
                    loadFunc = {dataHub.roleController.getDomainGroups}
                    loadParams = {[props.roleId]}
                    reload = {reload}
                    onDataLoaded = {handleDomainGroupsDataLoaded}
                    onDataLoadFailed = {handleDomainGroupsDataLoadFailed}
                >
                    <DomainGroupList
                        itemsType={FolderItemTypes.roles}
                        items={domainGroupsData.domainGroups}
                        showDeleteButton={true}
                        roleId={props.roleId}
                        onDeleteDomainGroupFromRoleResponse={handleDeleteDomainGroupFromRoleResponse}
                    />
                </DataLoader>
            </Paper>
        </div>
    });

    let FolderItemType = selectedPermittedFolder.toLowerCase().includes('filtertemplate') ?  FolderItemTypes.filterTemplate:
        selectedPermittedFolder.toLowerCase().includes('exceltemplate') ? FolderItemTypes.excelTemplates: 
        selectedPermittedFolder.toLowerCase().includes('filterinstance') ? FolderItemTypes.filterInstance:
        selectedPermittedFolder.toLowerCase().includes('securityfilter') ? FolderItemTypes.securityFilters:
        selectedPermittedFolder.toLowerCase().includes('datasource') ? FolderItemTypes.datasource:
        selectedPermittedFolder.toLowerCase().includes('dataset') ? FolderItemTypes.dataset:
        selectedPermittedFolder.toLowerCase().includes('devreport') ? FolderItemTypes.reportsDev:
        selectedPermittedFolder.toLowerCase().includes('userreport') ? FolderItemTypes.report:
        FolderItemTypes.report;

    if (props.roleTypeId === 2 /*FOLDER_ROLES*/){
    tabs.push({tablabel:"Права",
        tabdisabled: props.roleId === null || props.roleId === undefined ? true : false,
        tabcontent:
        <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}>
            <div className={classes.userAddPanel}>
                <div className={classes.roleAutocompleteDiv}>
                    <TextField
                        fullWidth
                        id="permitted-folders-names-list"
                        select
                        label="Объекты"
                        value={selectedPermittedFolder}
                        onChange={handleChangeSelectedPermittedFolder}
                        //helperText="Please select your currency"
                        variant="outlined"
                    >
                        {permittedFolders.namesList.map((item, index) => (
                            <MenuItem key={item.id} value={item.id}>
                                {item.value}
                            </MenuItem>
                         ))}
                    </TextField> 
                    
                </div>
                { FolderItemType !== FolderItemTypes.excelTemplates &&
                    <div className={classes.addButtonsRW}>
                        <Tooltip title={'Выдать права на чтение'}>
                            <Button
                                className={classes.addButtonRW}
                                color="primary"
                                variant="outlined"
                                disabled = {false}
                                onClick={handleAddFolderRead}
                            >
                                <MenuBookIcon/>
                                <AddIcon/>
                            </Button>
                        </Tooltip>
                        <Tooltip title={'Выдать права на запись'}>
                            <Button
                                className={classes.addButtonRW}
                                color="primary"
                                variant="outlined"
                                disabled = {false}
                                onClick={handleAddFolderWrite}
                            >
                                <CreateIcon/>
                                <AddIcon/>
                            </Button>
                        </Tooltip>
                    </div>
                }
            <div>
            {uploading ? <div className={classes.container}><CircularProgress/></div> :
            <div className={classes.container}>
                { FolderItemType !== FolderItemTypes.excelTemplates &&
                    <DesignerFolderBrowser 
                        dialogOpen={folderBrowserOpen}
                        itemType={FolderItemType}
                        defaultFolderId = {null}
                        onSubmit={(folderId) => handleSubmit(folderId, readWrite)}
                        //  onChange={handleChange}
                        onClose={handleCloseFolderBrowser}
                        sortParams = {props.sortParams}
                    />
                }
            </div>
            }
        </div>
            </div>
            <Paper elevation={3} className={classes.userListPaper}>
                <DataLoader
                    loadFunc = {dataHub.roleController.getPertmittedFolders}
                    loadParams = {[props.roleId]}
                    reload = {reload}
                    onDataLoaded = {handlePermittedFoldersLoaded}
                    onDataLoadFailed = {(message) => enqueueSnackbar(`При получении данных возникла ошибка: ${message}`, {variant: "error"})}
                >
                    <PermittedFoldersList
                        //itemsType={FolderItemTypes.roles}
                        selectedItem={selectedPermittedFolder}
                        items={permittedFolders}
                        editable = {true}
                        onChange={(folderId, roleType) => handleSubmit(folderId, roleType)}
                        onDelete={(folderId) => handleDeleteRolePermission(folderId)}
                        
                       // showDeleteButton={true}
                      //  roleId={props.roleId}
                      //  onDeleteDomainGroupFromRoleResponse={handleDeleteDomainGroupFromRoleResponse}
                    />
                </DataLoader>
            </Paper>

        </div>
    })} /*else if (props.roleTypeId === 3 /*SECURITY_FILTER) {

    }*/;

    return(
        <DataLoader
            loadFunc = {loadFunc}
            loadParams = {loadParams}
            onDataLoaded = {handleDataLoaded}
            onDataLoadFailed = {handleDataLoadFailed}
        >
            {uploading ? <CircularProgress/> :
                <div className={classes.rel}> 
                    <div className={classes.abs}>
                        <PageTabs
                            tabsdata={tabs} 
                            onTabChange={handleTabChange}
                            pageName={pageName}
                        />
                    </div>
                </div>
            }
        </DataLoader>
    );
}

const mapStateToProps = state => {
    return {
        users: state.users,
        selectedFolderType: state.roles.selectedFolderType
    }
}

const mapDispatchToProps = {
    actionUsersLoaded, 
    actionUsersLoadFailed,
    actionRoleSelectFolderType
}

export default connect(mapStateToProps, mapDispatchToProps)(RoleDesigner);
