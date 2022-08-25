import React from 'react';

// material-ui
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';

// styles
import { FolderContentCSS } from './FolderContentCSS'

export default function AddButton(props){
    const classes = FolderContentCSS();

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleAddButtonClick = event => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    function handleAddFolder(){
        props.onAddFolder(true)
        setAnchorEl(null);
    }

    function handleAddItemClick(){
        setAnchorEl(null);
        props.onAddItemClick();
    }

    return (
        <div className={classes.addButtonRoot}>
            <Menu
                id="folderMenu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
            >
                {props.showCreateFolder && <MenuItem onClick={handleAddFolder}>Добавить каталог</MenuItem>}
                {props.showCreateItem && <MenuItem onClick={handleAddItemClick}>Добавить {props.itemName}</MenuItem>}
            </Menu>
            <Fab      
                size = "medium"
                color="primary" 
                aria-label="add"
                onClick={handleAddButtonClick}
            >
                <AddIcon />
            </Fab>
        </div>
    );
}