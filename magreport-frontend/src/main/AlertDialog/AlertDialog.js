import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import ContactSupportIcon from '@material-ui/icons/ContactSupport';
import { AlertDialogCSS } from './AlertDialogCSS'
import { connect } from 'react-redux';


/**
  * Компонент вывода диалога с кнопками да/нет
  * 
  * @param {props} data - Объект с атрибутами: isOpen - признак показа уведомления, title - текст уведомления, entityType - тип сущности, entity - объект сущности
  * @param {props} onClose - возвращает массив с элементами: ответ (true|false), тип сущности и объект сущности.
  * @returns {Component} React component
  */
function AlertDialog(props) {

    const classes = AlertDialogCSS();  
    const { alertData } = props;

    function handleClose(answer){
        alertData.callback(answer, alertData.entityType, alertData.entity);
    }

    return (
        <div>
            <Dialog
                open={alertData.open}
                onClose={() => {handleClose(false)}}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
                PaperProps={{ classes: {root: classes.root }}}
            >
            <DialogTitle id="alert-dialog-title" className={classes.title}>
                <ContactSupportIcon fontSize="large" color="primary" style={{verticalAlign:'top'}}/>
                <span className={classes.paramText}>{alertData.title}</span>
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                </DialogContentText>
            </DialogContent>
            <DialogActions style={{margin: '17px', marginTop: '-37px', marginBottom: '16px'}}>
                <Button onClick={() => {handleClose(true)}}                     
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small">Да
                </Button>
                <Button onClick={() => {handleClose(false)}}                     
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"autoFocus>Нет
                </Button>
            </DialogActions>
            </Dialog>
        </div>
    );
}

const mapStateToProps = state => {
    return {
        alertData: state.alertDialog.data,
    }
}

export default connect(mapStateToProps, null)(AlertDialog);
