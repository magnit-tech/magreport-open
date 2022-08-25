import React from 'react';
import {useState} from 'react';
import { connect } from 'react-redux';
import { useSnackbar } from 'notistack';

// components
import Grid from '@material-ui/core/Grid';
import AddIcon from '@material-ui/icons/Add';
import Button from '@material-ui/core/Button';

// dataHub
import dataHub from 'ajax/DataHub';

// local
import DataLoader from '../../../DataLoader/DataLoader';
import Userlist from './UserList'
import Rolelist from '../Roles/RoleList'
import AsyncAutocomplete from '../../../../main/AsyncAutocomplete/AsyncAutocomplete';

// actions
import { actionUsersLoaded, actionUsersLoadFailed, actionManageUsers, actionAllUserChecked, actionUserAdd, actionUserRoleDelete, actionUserSelect } from 'redux/actions/admin/actionUsers'
import { showAlertDialog, hideAlertDialog } from 'redux/actions/actionsAlertDialog'

// styles 
import { UsersCSS } from "./UsersCSS";


function UserDesigner(props){

    const { enqueueSnackbar } = useSnackbar();
    const [selectedUser, setSelectedUser] = useState(props.items?.selectedUser?.id||-1);
    const [selectedRoleToAdd, setSelectedRoleToAdd] = useState("");
    const classes = UsersCSS();



    function handleSelectUser (id) {
        if (selectedUser !== id) { // если нажали на уже выбранного пользователя, ничего делать не нужно
            setSelectedUser(id);
            props.actionUserSelect(id)
        }
    }

    function getSelectedUserRoles(id) {

        for (let u of props.items.data) {
            if (u.id === id) return u.roles
        }

        return []
    }

    function handleOnChangeAddRoleText(value){
        setSelectedRoleToAdd(value);
    }

    function handleAddRoleToUser(){
        let roles = getSelectedUserRoles(selectedUser)
        for (let u of roles) {
            if (u.name === selectedRoleToAdd.name) {
                enqueueSnackbar("Пользователю уже назначена эта роль!", {variant : "error"});
                return;
            }
        }
        props.actionUserAdd(selectedRoleToAdd.id, [selectedUser])
    }

    function handleDeleteRoleFromUser(id) {
        props.actionUserRoleDelete(id, selectedUser)
    }

    function handleManageUsers(operation, users){
        if (operation === 'LOGGOFFALL'){
            props.showAlertDialog("Вы действительно хотите завершить сессии всех пользователей?", null, null, answer => handlelogoffAll(answer, operation, users))
        }
        else {
            props.actionManageUsers(operation, users)
        }
    }

    function handlelogoffAll(answer, operation, users){
        if (answer){
            props.actionManageUsers(operation, users)
        }
        props.hideAlertDialog()
    }

    let asyncElem =                     
    <div className={classes.roleHideSelect}>
        <div className={classes.roleAutocompleteDiv}>
            <AsyncAutocomplete
                disabled = {(selectedUser !== -1) ? false : true}
                typeOfEntity = {"role"}
                onChange={handleOnChangeAddRoleText}
            /> 
        </div>
        <Button color="primary"
            className={classes.addButton}
            variant="outlined"
            disabled = {!selectedRoleToAdd}
            onClick={handleAddRoleToUser}
        >
            <AddIcon/>Добавить
        </Button>
    </div>

    return(   
        <DataLoader
            loadFunc = {dataHub.userController.users}
            loadParams = {[]}
            onDataLoaded = {props.actionUsersLoaded}
            onDataLoadFailed = {props.actionUsersLoadFailed}
        >
            <div className={classes.rel}>
                <div className={classes.abs}>
                    <Grid
                        className={classes.userDesignerGrid} 
                        container 
                        justifyContent="center"
                        wrap="nowrap"
                    >
                        <Grid key={1} xs={6} item className={classes.userDesignerGridItem}>
                            <Userlist 
                                items={props.items.data}
                                from="UserDesigner"
                                showDeleteButton={false}
                                showControlButtons={true}
                                showCheckbox={true}
                                onSelectUser={handleSelectUser}
                                selectedUser={selectedUser}
                                onManageUsers={handleManageUsers}
                                onAllUsersChecked={props.actionAllUserChecked}
                                needUserScroll = {true}
                            />
                        </Grid>
                        <Grid key={2} xs={6} item className={classes.userDesignerGridItem}>
                            <Rolelist
                                items = {getSelectedUserRoles(selectedUser)}
                                showViewButton = {true}
                                showEditButton = {true}
                                showDeleteButton = {true}
                                hideSearh={false}
                                onDelete = {handleDeleteRoleFromUser}
                                parentsKey = {selectedUser}
                                topElems = {asyncElem}
                            />
                        </Grid>            
                    </Grid>
                </div>
            </div>
        </DataLoader>
    )
}

const mapStateToProps = state => {
    return {
        items: state.users,
    }
}

const mapDispatchToProps = {
    actionUsersLoaded, 
    actionUsersLoadFailed,
    actionManageUsers,
    actionAllUserChecked,
    actionUserAdd,
    actionUserRoleDelete,
    showAlertDialog, 
    hideAlertDialog,
    actionUserSelect
}

export default connect(mapStateToProps, mapDispatchToProps)(UserDesigner);
