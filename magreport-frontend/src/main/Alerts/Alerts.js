import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Slide from '@material-ui/core/Slide';
import { connect } from 'react-redux';


const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});


/**
  * Компонент вывода уведомлений в виде окна
  * 
  * @param open - признак открытия окна
  * @param title - заголовок окна
  * @param text - текст сообщения
  * @param buttons - массив кнопок. Например, [{'text':'OK','onClick':handleDialogClose}]
  * @param callback - ссылка на функцию, которая отработает при закрытии окна
  * @returns {Component} React component
  */
function Alerts(props){

    const { data } = props;

    let elems = [];
    /* генерируем код кнопок */
    elems = data.buttons.map( (elem,i) => { return ( 
                                                <Button 
                                                    key = {"keyAlarm"+i} 
                                                    onClick={() => { elem['onClick']()}} 
                                                    color="primary"
                                                >
                                                    {elem['text']}
                                                </Button>
                                           )});    

    return(
        <Dialog
            open={data.open}
            TransitionComponent={Transition}
            keepMounted
            onClose={() => data.callback()}
            aria-labelledby="alert-dialog-slide-title"
            aria-describedby="alert-dialog-slide-description"
        >
            <DialogTitle id="alert-dialog-slide-title">{data.title}</DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-slide-description">
                    {data.text}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                {elems}
            </DialogActions>
        </Dialog>
    );
}

const mapStateToProps = state => {
    return {
        data: state.alert.data,
    }
}

export default connect(mapStateToProps, null)(Alerts);
