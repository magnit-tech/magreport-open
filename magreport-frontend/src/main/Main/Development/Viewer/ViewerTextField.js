import React from 'react';
import { TextField } from "@material-ui/core";
import clsx from 'clsx'

import {ViewerCSS} from './ViewerCSS';

/**
 * Текстовое поле компонента ViewerPage
 * @param {Object} props - свойства компонента
 * @param {String} props.label - заголовок (метка) поля
 * @param {*} props.value - значение поля
 * @param {Boolean} [props.multiline=false] - отображение поля в несколько строк
 * @param {Boolean} [props.displayBlock=false] - если true - отображает как display:block
 * @returns {JSX.Element}
 * @constructor
 */
export default function ViewerTextField(props){

    const classes = ViewerCSS();

    let value = props.value === 0 || props.value ? props.value : ''
    let type = "text";
    let multiline = props.multiline;

    if(typeof value === "number") {
        type = "number";
    }

    return (
        <TextField
            className = {clsx({[classes.field]: !Boolean(props.woStyle), [classes.displayBlock] : props.displayBlock})}
            style={{minWidth: props.minWidth, margin: props.margin}}
            label = {props.label}
            value = {value}
            variant = {"outlined"}
            type = {type}
            fullWidth = {true}
            multiline = {multiline}
            disabled = {true}
        />
    )
}
