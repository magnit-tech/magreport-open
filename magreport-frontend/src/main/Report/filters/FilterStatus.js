import React from 'react';

// components
import Tooltip from '@material-ui/core/Tooltip';
import CircularProgress from '@material-ui/core/CircularProgress';
import DoneIcon from '@material-ui/icons/Done';
import ErrorOutlineIcon from '@material-ui/icons/ErrorOutline';

export default function FilterStatus({status}){

    return (
        <Tooltip 
            title={
                status === 'waiting' ? "Идет проверка значений" :
                status === 'success' ? "Введены корректные значения" :
                status === 'error'   ? "Введены некорректные значения" : ""
            } 
            placement="top"
        >
            {
                status === 'waiting' ? <CircularProgress size={20} /> :
                status === 'success' ? <DoneIcon fontSize='small' style={{color: "green"}} /> :
                status === 'error'   ? <ErrorOutlineIcon fontSize='small' style={{color: "red"}} /> : <></>
            }
        </Tooltip>
    )
}