import React from 'react';
//import { ThemeProvider } from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import Filter9PlusIcon from '@material-ui/icons/Filter9Plus';
import InputAdornment from '@material-ui/core/InputAdornment';

// styles 
import { ElementsOnPageSelectCSS } from './JobFilters/JobFiltersCSS'

/**
  * Компонент выпадающего списка количества заданий на странице
  * 
  * @param {props} elementsOnPage - выбранное значение количества заданий на странице для отображения в списке, если выбрано
  * @param {props} onChange - функция, возвращающая выбранное значение
  * @returns {Component} React component
  */
export default function ElementsOnPageSelect (props){
    
    const classes = ElementsOnPageSelectCSS();    
    let arrayCounts = [10,20,50];

    return (
        <Select className={classes.selectStyle}
            variant="outlined"
            value={props.elementsOnPage}
            onChange={event => props.onChange(event.target.value)}
            startAdornment = {
                <InputAdornment position="start">
                <Filter9PlusIcon />
                </InputAdornment>
            }
        >
            { arrayCounts.map((value, index) => <MenuItem key={index} value={value}>{value}</MenuItem>) } 
        </Select>
    )
}