import React, { useState, useRef } from 'react';
import TextField from '@material-ui/core/TextField';

// local
import TransferList from './TransferList';
import {PivotCSS} from './PivotCSS';

function PivotFiltersItem(props){
    const classes = PivotCSS();

    const timer = useRef(0);
    const [from, setFrom] = useState(null);
    const [to, setTo] = useState(null);
    const [page, setPage] = useState(1);

    const [startRow, setStartRow] = useState(0);
    const [rowsNum, setRowsNum] = useState(10);
    const [params, setParams] = useState(buildParams(null, 0));

    function handleSearch(value){

        if (timer.current > 0){
            clearInterval(timer.current); // удаляем предыдущий таймер
        }

        timer.current = setTimeout(() => {           
            setParams(buildParams({   operationType: 'AND',
            invertResult: false,
            filters: [
            /*поиск значения текущего поля*/
                {   fieldId: props.fieldId , //props.field?.fieldId,
                    filterType: 'CONTAINS_CI',
                    invertResult: false,
                    values: [value]
                }
            ],
            childGroups: []
        }, 0)); 
        }, 1000);
  }

    function handleChangePage(value){
        let sr = (value - 1) * rowsNum;
        setPage(value);
        setStartRow(sr);
        setParams({...params, from: sr});
    }

    function handleRowsNumChange(value){
        let sr = 0;
        if (startRow !== 0)
        {
            sr = (Math.ceil(startRow/value) -1)  * value;
            console.log('handleRowsNumChange');
            console.log(sr);
            
            setPage(Math.ceil((sr+value)/value));
        }
        setRowsNum(value);
        setParams({...params, count: value, from: sr});
    }

    function buildParams(searchString, startRow) {
        let chGrs = [];
        if (searchString) {
            chGrs.push(searchString)
        }

        if (props.pivotTableFilters) {
            chGrs.push(props.pivotTableFilters);
        }

        return (
         {
             jobId: props.jobId,
             fieldId: props.fieldId,
             filterGroup: {
                 operationType: 'AND',
                 invertResult: false,
                 childGroups: chGrs,
                 filters: []
             } ,
             from: startRow,
             count: rowsNum 
         }
        )
    }

    function handleChangeBetweenValues(source, value){
        if (source === 'from') {
            setFrom(value)
        } else if (source === 'to'){
            setTo(value)
        }

        if (from !== null && to !== null) {
            props.onChangeValues([from, to])
        }
    }

    if (props.filterType === "EQUALS" || props.filterType === "NOT_EQUALS" || props.filterType ==="CONTAINS_CI" || props.filterType === "NOT_CONTAINS"){
        return (
            <TextField defaultValue = {props.filterValues[0]} size = "small" multiline className={classes.equalText} onChange={e=>props.onChangeValues([e.target.value])}/>
        )
    } else if (props.filterType ==="BETWEEN" || props.filterType === "NOT_BETWEEN") {
        return (
            <div> 
                <TextField defaultValue = {props.filterValues[0]} size = "small" multiline className={classes.equalText} onChange={e=>handleChangeBetweenValues('from',e.target.value)}/>
                <TextField defaultValue = {props.filterValues[1]} size = "small" multiline className={classes.equalText} onChange={e=>handleChangeBetweenValues('to'  ,e.target.value)}/>
            </div>
        )
    } else if (props.filterType === "IN_LIST" || props.filterType === "NOT_IN_LIST") {
        return (
            
            <TransferList
                /* left ={fieldValues} */
                filterValues = {props.filterValues}
                params={params}
                right={[]}
                cntPerPage={rowsNum}
                page={page}
                onSearch={handleSearch}
                onChange={props.onChangeValues}
                onPageChange={handleChangePage}
                onRowsNumChange={handleRowsNumChange}
            />
              
        )
    } else {
        return (
            <p>
                Неизвестный тип фильтра
            </p>
        )
    }
}

export default PivotFiltersItem
