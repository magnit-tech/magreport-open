import React, {useState} from "react";
import {useSnackbar} from "notistack";

import dataHub from "ajax/DataHub";

import DataLoader from "main/DataLoader/DataLoader";

import Paper from "@material-ui/core/Paper";
import PageTabs from "main/PageTabs/PageTabs";

import {FolderItemTypes} from "main/FolderContent/FolderItemTypes";
import ViewerPage from "main/Main/Development/Viewer/ViewerPage";
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";
import UserList from "main/Main/Administration/Users/UserList";
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';

import DomainGroupList from "main/Main/Administration/DomainGroups/DomainGroupList";
import PermittedFoldersList from './PermittedFoldersList';
import {
    createViewerTextFields,
    createViewerPageName
} from "main/Main/Development/Viewer/viewerHelpers";


/**
 * @callback onOkClick
 */
/**
 * Компонент просмотра роли
 * @param {Object} props - параметры компонента
 * @param {Number} props.roleId - ID роли
 * @param {onOkClick} props.onOkClick - callback вызываемый при нажатии кнопки ОК
 * @constructor
 */
export default function RoleViewer(props) {

    const classes = ViewerCSS();

    const {enqueueSnackbar} = useSnackbar();

    const [data, setData] = useState({});

    const [users, setUsers] = useState([]);

    const [domainGroups, setDomainGroups] = useState([]);

    const [permittedFolders, setPermittedFolders] = useState({loadedData: {}, namesList: []});
    const [selectedPermittedFolder, setSelectedPermittedFolder] = useState('');

    let loadFunc = dataHub.roleController.get;
    let loadParams = [props.roleId];

    function handleDataLoaded(loadedData) {
        setData(loadedData);
    }

    function handleUsersDataLoaded(loadedData) {
        setUsers(loadedData.users);
    }

    function handleDomainGroupsDataLoaded(loadedData) {
        setDomainGroups(loadedData.domainGroups);
    }

    function handleDataLoadFailed(message) {
        enqueueSnackbar(`При получении роли возникла ошибка: ${message}`,
            {variant: "error"});
    }

    function handlePermittedFoldersLoaded(loadedData){
        let namesList = Object.keys(loadedData);
        let fullNamesList = [{id: 'all', value: 'Все'}];
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

            if (loadedData[name].length > 0) {fullNamesList.push({id: name, value: n})};
        }

        setPermittedFolders({...permittedFolders, 
            loadedData: loadedData, 
            namesList:  fullNamesList
        })
        setSelectedPermittedFolder('all');
    }

    const handleChangeSelectedPermittedFolder = (event) => {
        setSelectedPermittedFolder(event.target.value);
    };

    // edit check
    let userInfo = dataHub.localCache.getUserInfo();

    let isAdmin = userInfo.isAdmin
    let isDeveloper = userInfo.isDeveloper
    let authority = data.authority

    let hasRWRight = isAdmin || (isDeveloper && authority === "WRITE");


    
    // build component

    // settings tab
    const settingsTabData = [
        {label: "Название", value: data.name},
        {label: "Описание", value: data.description},
    ];

    const settingsTab = {
        tablabel: "Настройки",
        tabcontent: (
            <div className={classes.viewerTabPage}>
                {createViewerTextFields(settingsTabData)}
            </div>
        )
    };

    // users tab
    const usersTab = {
        tablabel: "Пользователи",
        tabcontent: (
            <div className={classes.userListTabPage}>
                <Paper elevation={3} className={classes.userListPaper}>
                    <DataLoader
                        loadFunc={dataHub.roleController.getUsers}
                        loadParams={loadParams}
                        onDataLoaded={handleUsersDataLoaded}
                        onDataLoadFailed={handleDataLoadFailed}
                    >
                        <UserList
                            itemsType={FolderItemTypes.roles}
                            items={users}
                            showDeleteButton={false}
                            roleId={props.roleId}
                        />
                    </DataLoader>
                </Paper>
            </div>
        )
    };

    // domain groups tab
    const domainGroupsTab = {
        tablabel: "Доменные группы",
        tabcontent: (
            <div className={classes.userListTabPage}>
                <Paper elevation={3} className={classes.userListPaper}>
                    <DataLoader
                        loadFunc={dataHub.roleController.getDomainGroups}
                        loadParams={loadParams}
                        onDataLoaded={handleDomainGroupsDataLoaded}
                        onDataLoadFailed={handleDataLoadFailed}
                    >
                        <DomainGroupList
                            itemsType={FolderItemTypes.roles}
                            items={domainGroups}
                            showDeleteButton={false}
                            roleId={props.roleId}
                        />
                    </DataLoader>
                </Paper>
            </div>
        )
    };

    const permittedFoldersTab = props.roleTypeId === 2 ? {
        tablabel:"Права",
        tabcontent:
            <div style={{display: 'flex', flex: 1, flexDirection: 'column'}}>
                <div className={classes.userAddPanel}>
                    <div className={classes.permittedSelect}>
                        <TextField
                            fullWidth
                            id="permitted-folders-names-list"
                            select
                            label="Тип объекта"
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
                </div>
                <Paper elevation={3} className={classes.permittedListPaper}>
                    <DataLoader
                        loadFunc = {dataHub.roleController.getPertmittedFolders}
                        loadParams = {[props.roleId]}
                        reload = {/*reload*/ false}
                        onDataLoaded = {handlePermittedFoldersLoaded}
                        onDataLoadFailed = {(message) => enqueueSnackbar(`При получении данных возникла ошибка: ${message}`, {variant: "error"})}
                    >
                        <PermittedFoldersList
                            selectedItem={selectedPermittedFolder}
                            items={permittedFolders}
                            editable = {false}
                        />
                    </DataLoader>
                </Paper>

            </div>
        } : {};

    // component
    return (
        <DataLoader
            loadFunc={loadFunc}
            loadParams={loadParams}
            onDataLoaded={handleDataLoaded}
            onDataLoadFailed={handleDataLoadFailed}
        >
            <ViewerPage
                id={data.id}
                name={data.name}
                itemType={FolderItemTypes.roles}
                disabledPadding={true}
                onOkClick={props.onOkClick}
                readOnly={!hasRWRight}
            >
                <PageTabs
                    pageName={createViewerPageName(FolderItemTypes.roles, data.name)}
                    tabsdata={[
                        settingsTab,
                        usersTab,
                        domainGroupsTab,
                        permittedFoldersTab,
                    ]}
                />
            </ViewerPage>
        </DataLoader>
    );
}
