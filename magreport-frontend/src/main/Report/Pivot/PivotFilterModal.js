import React, { useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import {PivotCSS} from './PivotCSS';


import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Paper from '@material-ui/core/Paper';
import Draggable from 'react-draggable';

// local
import PivotGroupFilters from './PivotGroupFilters';

function PaperComponent(props) {
    return (
      <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
        <Paper {...props}/>
      </Draggable>
    );
  }

/**
 * 
 * @param {*} props.filterType
 * @param {*} props.filterValues
 * @param {*} props.open - признак открытого диалогового окна
 * @param {*} props.style - объект стиля
 * @param {*} props.jobId - id задания
 * @param {*} props.pivotConfiguration - объект конфигурации
 * @param {*} props.filterFieldIndex - индекс поля для которго настраивается фильтр в списке props.pivotConfiguration.fieldsLists.filterFields
 * @param {*} props.onClose - колбэк по закрытию без сохранения
 * @param {*} props.onOK - колбэк по закрытию с сохранением
 * 
 * 
 * @returns 
 */
export default function PivotFilterModal(props){
    const classes = PivotCSS();

    const [filterValues, setFilterValues] = useState({});

    const pivotTableFilters = {
        operationType: 'AND',
        invertResult: false,
        childGroups: [],
        filters: props.pivotConfiguration.fieldsLists.filterFields.map((v) => ({
            fieldId : v.fieldId,
            filterType: v.filter.filterType,
            invertResult: v.filter.invertResult,
            values:  v.filter.values 
        })) 
    }

    const fieldId = props.filterFieldIndex ? props.pivotConfiguration.fieldsLists.filterFields[props.filterFieldIndex].id : 0;

    function handleChangeFilterValues(value){
        setFilterValues(value);
        console.log('handleChangeFilterValues');
        console.log(value)
    }

    function handleOk(value){
        props.onOK(value)
    }

    function handleClose(){
        props.onClose();
    }

    return (
        <Dialog
            open={props.open}
            onClose={props.onClose}
            PaperComponent={PaperComponent}
            aria-labelledby="draggable-dialog-title"
            maxWidth = {false}
        >
            <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title" >
                <Typography variant="subtitle2">
                    {props.field?.fieldName}
                </Typography>
            </DialogTitle>
            <DialogContent>
                <PivotGroupFilters
                    filterValues = {props.filterValues}
                    filterType={props.filterType}
                    jobId={props.jobId}
                    fieldId={fieldId}
                    pivotTableFilters={pivotTableFilters}
                    onChangeFilterValues={handleChangeFilterValues}
                />
                <div className={classes.btnsArea}>
                    <Button color="primary" size="small" variant="outlined" onClick = {()=>handleOk(filterValues)}> OK </Button> 
                    <Button color="primary" size="small" variant="outlined" onClick = {()=>handleClose()} className={classes.cancelBtn}> Отмена </Button>
                </div>
            </DialogContent>
        </Dialog>

    )
}