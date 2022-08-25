import React from 'react';

// material-ui
import Fab from '@material-ui/core/Fab';
import RefreshIcon from '@material-ui/icons/Refresh';

// styles
import { FolderContentCSS } from './FolderContentCSS'

export default function RefreshButton(props){
    const classes = FolderContentCSS();

    return (
        <div className={classes.refreshButton}>
            <Fab      
                size="medium"
                color="primary" 
                aria-label="add"
                onClick={props.onRefreshClick}
            >
                <RefreshIcon />
            </Fab>
        </div>
    );
}