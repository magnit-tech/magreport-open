import React, { useState } from 'react';
import { connect } from 'react-redux';
import { useSnackbar } from 'notistack';

// components
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import InputBase from '@material-ui/core/InputBase';
import SearchIcon from '@material-ui/icons/Search';
import AddIcon from '@material-ui/icons/Add';

// local
import DataLoader from '../../DataLoader/DataLoader';
import Rolelist from '../../Main/Administration/Roles/RoleList'
import AsyncAutocomplete from 'main/AsyncAutocomplete/AsyncAutocomplete';

import {dataHubRightsController} from 'main/FolderContent/FolderItemTypes';

// styles
import { RoleUserListWindowCSS } from './ModalWindowsCSS'
import { UsersCSS } from "main/Main/Administration/Users/UsersCSS";

// actions
import { actionRolesLoaded, actionRolesLoadFailed, actionRoleAdd, actionRoleDelete, actionFilterRoles } from 'redux/actions/admin/actionRoles'

function GrantsModalWindow(props){
    const classes = RoleUserListWindowCSS()
    const roleClasses = UsersCSS()

    const { enqueueSnackbar } = useSnackbar()

    const [addedRole, setAddedRole] = useState(null)
    const [resetAutocomplete, setResetAutocomplete] = useState(false)

    let controller = dataHubRightsController(props.itemsType)
    let loadFunc = controller.getPermissions

    const handleFilter = str => {
        props.actionFilterRoles(str.trim().toLowerCase())
    }

    const handleSaveRoles = () => {
        let roles = []
        for (let r of props.items){
            let item = {...r}
            delete item.role
            roles.push(item)
        }
        controller.setPermissions(props.folderId, roles, handleSaveResponse)
    }

    const handleSaveResponse = magrepResponse => {
        if (magrepResponse.ok){
            enqueueSnackbar(`Права доступа установлены`, {variant : "success"})
            props.onClose()
        }
        else {
            enqueueSnackbar(`Не удалось установить новые роли для каталога. Ошибка: ${magrepResponse.data}`, {variant : "error"})
        }
    }

    function handleAddRole(){
        if (props.items.findIndex(r => r.roleId === addedRole.roleId) === -1){
            props.actionRoleAdd(addedRole)
            setResetAutocomplete(!resetAutocomplete)
        }
        else {
            enqueueSnackbar('Роль уже есть в списке', {variant : "error"})
            setResetAutocomplete(!resetAutocomplete)
        }
    }

    function handleAutocomplete(role) {
        if(role) {
            setAddedRole({...role, roleId: role.id, permissions: ["READ"]})
        }
        return false
    }

    const asyncElem =                     
        <div className={roleClasses.roleHideSelect}>
            <div className={roleClasses.roleAutocompleteDiv}>
                <AsyncAutocomplete
                    typeOfEntity = {"role"}
                   // filterOfEntity = {(item) => item.typeId === 2 /*FOLDER_ROLES*/ || item.typeId === 0 /*SYSTEM*/ }
                    onChange={role => handleAutocomplete(role)}
                    resetAutocomplete={resetAutocomplete}
                /> 
            </div>
            <Button color="primary"
                className={roleClasses.addButton}
                variant="outlined"
                disabled = {false}
                onClick={handleAddRole}
            >
                <AddIcon/>Добавить
            </Button>
        </div>

    return (
        <Dialog
            maxWidth='md'
            open={props.isOpen}
            onClose={props.onClose}
            aria-labelledby="form-dialog-title"
            PaperProps={{ classes: {root: classes.root }}}
        >
            <Toolbar position="fixed" className={classes.filterTitle}  variant="dense">
                <Typography variant="h6" className={classes.title}>Роли</Typography>

                <div className={classes.search}>
                    <div className={classes.searchIcon}>
                        <SearchIcon />
                    </div>
                    <InputBase
                        placeholder="Поиск…"
                        classes={{
                            root: classes.inputRoot,
                            input: classes.inputInput,
                        }}
                        onChange={event => handleFilter(event.target.value)}
                        value={props.filteredStr}
                    />
                </div>
            </Toolbar>
            <DialogContent style={{display: 'flex'}}>
                {
                    <DataLoader
                        loadFunc = {loadFunc}
                        loadParams = {[props.folderId]}
                        onDataLoaded = {data => props.actionRolesLoaded(data)}
                        onDataLoadFailed = {data => props.actionRolesLoadFailed(data)}
                    >
                        <Rolelist 
                            itemsType={props.itemsType}
                            items={props.filteredItems ? props.filteredItems : props.items}
                            showDeleteButton={true}
                            hideSearh={true}
                            showCheckboxRW={true}
                            onDelete={props.actionRoleDelete}
                            topElems = {asyncElem}
                        />
                    </DataLoader>
                }
            </DialogContent>
            <DialogActions className={classes.indent}> 
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={handleSaveRoles}>Сохранить
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"
                    onClick={props.onClose}>Отменить
                </Button>
            </DialogActions>
        </Dialog>

    )
}

const mapStateToProps = state => {
    return {
        items: state.roles.data,
        filteredItems: state.roles.filteredData,
        filteredStr: state.roles.filteredStr
    }
}

const mapDispatchToProps = {
    actionRolesLoaded,
    actionRolesLoadFailed,
    actionRoleAdd,
    actionRoleDelete,
    actionFilterRoles
}

export default connect(mapStateToProps, mapDispatchToProps)(GrantsModalWindow);