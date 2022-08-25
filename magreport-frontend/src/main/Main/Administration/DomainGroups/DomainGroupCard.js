import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import AccessibilityIcon from '@material-ui/icons/Accessibility';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
// local
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
// styles
import { connect } from 'react-redux';
//actions
import { showAlertDialog, hideAlertDialog } from 'redux/actions/actionsAlertDialog'


function DomainGroupCard(props){
    
    const handleClickDelete = event => {
        event.stopPropagation();
        let text = 'Удалить доменную группу?'
        if (props.itemsType === FolderItemTypes.roles){
            text='Исключить доменную группу из роли?'
        }
        props.showAlertDialog(text, null, null, handleDelete)
    }

    function handleDelete(answer){
        if (answer){
            props.onDeleteDomainGroup(props.domainGroupDesc)
        }
        props.hideAlertDialog()
    }

    function handleClick(event){
        props.setSelectedDomainGroup(props.domainGroupDesc)
        event.stopPropagation();
    }

    return(
            <ListItem  
                button 
                onClick={handleClick}
                selected={props.isSelected}
            >
                <ListItemIcon>
                    <AccessibilityIcon/>
                </ListItemIcon>
                <ListItemText 
                    primary={props.domainGroupDesc}
                />
                {
                    props.showDeleteButton ?
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
    hideAlertDialog
}

export default connect(null, mapDispatchToProps)(DomainGroupCard);