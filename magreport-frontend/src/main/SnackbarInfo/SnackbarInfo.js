import React from 'react';
import MuiAlert from '@material-ui/lab/Alert';
import Snackbar from '@material-ui/core/Snackbar';
import { connect } from 'react-redux';
import { hideSnackbar } from '../../redux/actions/actionSnackbar'

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

/**
  * Компонент вывода уведомлений внизу страницы
  * 
  * @param data - Объект со свойствами isOpen - признак показа уведомления, text - текст уведомления, color - цвет уведомления (доступные варианты: success, error, warning, info)  * @returns {Component} React component
  */
function SnackbarInfo (props) {
    
    const { data } = props;

    function handleCloseSnackbar(){
        props.hideSnackbar(data.text, data.color);
    }

    return (
        <Snackbar open={data.isOpen} autoHideDuration={3000} onClose={handleCloseSnackbar}>
            <Alert onClose={handleCloseSnackbar} color={data.color}>
                { data.text }
            </Alert>
        </Snackbar>
    )
}

const mapStateToProps = state => {
    return {
        data: state.snackbar,
    }
}

const mapDispatchToProps = {
    hideSnackbar,
}

export default connect(mapStateToProps, mapDispatchToProps)(SnackbarInfo);