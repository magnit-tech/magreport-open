import React from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { JobStatuses, JobStatusMap } from './JobStatuses.js';

// styles
import { JobStatusSelectCSS } from "./JobFiltersCSS";

/**
  * Компонент выпадающего списка для фильтрации по статусу задания
  * 
  * @param {props} selectedStatuses - массив статусов для отображения в списке, если выбрано
  * @param {props} onChange - функция, возвращающая массив выбранных значений
  * @returns {Component} React component
  */
export default function JobStatusSelect (props){
    const classes = JobStatusSelectCSS();

    return (
        <div>
       { <FormControl variant="filled" className={classes.formControl} size = 'small'>
            <InputLabel shrink id="jobStatusSelect">Статус</InputLabel>
            <Select
                size = 'small'
                labelId="jobStatusSelect"
                id="idJobStatusSelect"
                multiple
                value={props.selectedStatuses ? props.selectedStatuses : []}
                onChange={event => props.onChange(event.target.value)}
                children = { Object.entries(JobStatuses).map(item => <MenuItem key={item[0]} value={item[1]}>{JobStatusMap.get(item[1])}</MenuItem >) }
            >
                 
            </Select>
        </FormControl> }
        </div>
    )
    
}