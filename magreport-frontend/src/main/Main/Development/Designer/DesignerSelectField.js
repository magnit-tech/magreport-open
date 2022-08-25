import React from 'react';
import {Select, MenuItem, FormControl, InputLabel } from "@material-ui/core";
//import FormControlLabel from '@material-ui/core/FormControlLabel';
import clsx from 'clsx'

import {DesignerCSS} from './DesignerCSS';

/**
 * 
 * @param {*} props.label - название поля
 * @param {*} props.onChange - function(id) - действие при изменении значения
 * @param {*} props.data - массив объектов {id, name}
 * @param {*} props.disabled - признак неактивности элемента
 */
export default function DesignerSelectField(props){

    const classes = DesignerCSS();

    return (
        <FormControl
            style = {{ minWidth: props.minWidth}}
            className = {clsx(classes.field, {[classes.displayBlock] : props.displayBlock})}
            variant ="outlined"
            fullWidth = {props.fullWidth}
            size={props.size}
        >
            <InputLabel variant ="outlined" id = "selectInputLabel">{props.label}</InputLabel>
            <Select
                labelId = "selectInputLabel"
                value = {props.value === 0 || props.value ? props.value : ''}
                id = "idSelectInputLabel"
                onChange = {(event) => {props.onChange(event.target.value)}}
                fullWidth = {props.fullWidth}
                error = {props.error}
                disabled = {props.disabled}
                label={props.label}
                defaultValue = {props.defaultValue}
                
            >
                {props.data.map((v) => (<MenuItem key={v.id} value={v.id}>{v.name}</MenuItem>))}
            </Select>
        </FormControl>
    )
}
