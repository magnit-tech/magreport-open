import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import AccessibilityIcon from '@material-ui/icons/Accessibility';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import Checkbox from '@material-ui/core/Checkbox';
import LockIcon from '@material-ui/icons/Lock';
import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
import Tooltip from '@material-ui/core/Tooltip';

// local
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
// styles
import { connect } from 'react-redux';
import { UsersCSS } from "./UsersCSS";
//actions
import { showAlertDialog, hideAlertDialog } from 'redux/actions/actionsAlertDialog'
import { actionUserDelete, actionUserChecked } from 'redux/actions/admin/actionUsers'


function UserCard(props){
    const classes = UsersCSS();
    
    const handleClickDelete = event => {
        event.stopPropagation();
        const text='Исключить пользователя из роли?'
        props.showAlertDialog(text, null, null, handleDelete)
    }

    function handleDelete(answer){
        if (answer){
            props.actionUserDelete(props.roleId, [props.userDesc.id])
        }
        props.hideAlertDialog()
    }

    function handleClick(event){
        event.stopPropagation();
        props.onSelectedUser(props.userDesc.id)
    }

    function handleCheck(event){
        event.stopPropagation()
        props.actionUserChecked(props.userDesc.id)
    }
    
    return(
            
        <ListItem  
            id = {props.id}
            button 
            onClick={handleClick}
            selected={props.isSelected}
        >
            { props.showCheckbox ? 
                <Checkbox
                    checked={props.userDesc.blockUserCheck || false}
                    onChange={handleCheck}
                    color="primary"
                />
                : ""
            }
            <div>
            <ListItemIcon>
                <AccessibilityIcon className={classes.usersBtn}/>
            </ListItemIcon>
            </div>
            <ListItemText
                primary={props.userDesc.name}
                secondary={"e-mail: " + props.userDesc.email}
            />
            {
                props.userDesc.status === "DISABLED" && <ListItemIcon><LockIcon color="secondary"/></ListItemIcon>
            }
            {
                props.userDesc.roles.findIndex(r => r.name === 'ADMIN') > -1 && 
                <ListItemIcon>
                    <Tooltip title="Администратор">
                        <SupervisorAccountIcon color="secondary"/>
                    </Tooltip>
                </ListItemIcon>
            }
            {
                props.showDeleteButton && props.itemsType === FolderItemTypes.roles ?
                <ListItemIcon>
                    <IconButton 
                        aria-label="Удалить" 
                        color="primary" 
                        onClick={handleClickDelete}
                    >
                        <DeleteIcon />
                    </IconButton>
                </ListItemIcon>
                : ""
            }
                
        </ListItem>
    );
}

const mapDispatchToProps = {
    showAlertDialog, 
    hideAlertDialog,
    actionUserDelete,
    actionUserChecked,
}

export default connect(null, mapDispatchToProps)(UserCard);