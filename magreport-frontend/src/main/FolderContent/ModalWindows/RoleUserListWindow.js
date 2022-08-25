import React, { useState } from 'react';
import { connect } from 'react-redux';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import Toolbar from '@material-ui/core/Toolbar';
import DialogTitle from '@material-ui/core/DialogTitle';
import Typography from '@material-ui/core/Typography';
// dataHub
import dataHub from 'ajax/DataHub';
// components
import DataLoader from '../../DataLoader/DataLoader';
import Userlist from '../../Main/Administration/Users/UserList'
import UserAutocomplete from '../../Main/Administration/Users/UserAutocomplete'
// styles
import { RoleUserListWindowCSS } from './ModalWindowsCSS'

// actions
import { actionUsersLoaded, actionUsersLoadFailed, actionUserAdd } from 'redux/actions/admin/actionUsers'

function RoleUserListWindow(props){

    const classes = RoleUserListWindowCSS();

    let loadFunc = dataHub.roleController.getUsers;
    let reload = {needReload : false};
    let roleId = props.id

    const [selectedUser, setSelectedUser] = useState(null)

    const handleUserToAdd = user => {
        setSelectedUser([user.id])
    }

    const handleAddUser = () => {
        props.actionUserAdd(roleId, props.itemsType, selectedUser)
    }
    
    return (
        <div>
            <Dialog open={props.isOpen} onClose={props.onClose} aria-labelledby="form-dialog-title" PaperProps={{ classes: {root: classes.root }}}>
            <Toolbar position="fixed" elevation={5} className={classes.filterTitle}  variant="dense">
                <DialogTitle id="form-add-role">
                   <Typography className={classes.paramText}>Пользователи</Typography>
                </DialogTitle>
            </Toolbar>
                <DialogActions className={classes.indent}> 
                    <UserAutocomplete 
                        onChange={handleUserToAdd}
                    />
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        size="small"
                        onClick={handleAddUser}>Добавить
                    </Button>
                </DialogActions>
                <DialogContent>
                    <DataLoader
                        loadFunc = {loadFunc}
                        loadParams = {[roleId]}
                        reload = {reload}
                        onDataLoaded = {data => {props.actionUsersLoaded(data, props.itemsType)}}
                        onDataLoadFailed = {data => props.actionUsersLoadFailed(data, props.itemsType)}
                    >
                        <Userlist 
                            itemsType={props.itemsType}
                            items={props.items.data}
                            roleId={roleId}
                        />
                    </DataLoader>
                </DialogContent>
                <DialogActions className={classes.indent}> 
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        size="small"
                        onClick={props.onClose}>Отменить
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    )
}

const mapStateToProps = state => {
    return {
        items: state.users
    }
}

const mapDispatchToProps = {
    actionUsersLoaded, 
    actionUsersLoadFailed,
    actionUserAdd
}

export default connect(mapStateToProps, mapDispatchToProps)(RoleUserListWindow);