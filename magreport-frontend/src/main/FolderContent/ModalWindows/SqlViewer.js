import React from 'react';
import { connect } from 'react-redux';
import {FolderItemTypes} from  '../FolderItemTypes';
//redux



import {hideSqlDialog} from 'redux/actions/jobs/actionJobs';

// components
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

/**
 * 
 * @param {*} props.id - id задания
 * @param {*} props.open - признак открытия окна
 * @param {*} props.sqlQuery - Текст запроса
 * @param {*} props.onCLose - функция при закрытии окна
 */
function SqlViewer(props){

    const handleClose = event => {
        event.stopPropagation()
        props.hideSqlDialog(props.itemsType)
    }

    return (
        <div>
        {(props.itemsType === FolderItemTypes.job || props.itemsType === FolderItemTypes.userJobs) &&

        
        <Dialog
            open={props.open}
            onClose={handleClose}
            aria-labelledby="SQL-dialog-title"
            aria-describedby="SQL-dialog-description"
        >
            <DialogTitle id="SQL-dialog-title">{props.titleName}</DialogTitle>
            <DialogContent>
                <DialogContentText id="SQL-dialog-description">
                    {props.data.sqlQuery}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button style={{margin: '16px'}}
                    color="primary"
                    autoFocus
                    type="submit"
                    variant="contained"
                    size="small"
                    onClick={handleClose}
                >
                    Закрыть
                </Button>
            </DialogActions>
        </Dialog>
        }
    </div>
    )
}

const mapStateToProps = state => {
    return {
        open : state.jobSql.open,
        itemsType: state.jobSql.itemsType,
        data: state.jobSql.data,
        titleName: state.jobSql.titleName
        
    }
}

const mapDispatchToProps = {
    hideSqlDialog
}

export default connect(mapStateToProps, mapDispatchToProps)(SqlViewer);