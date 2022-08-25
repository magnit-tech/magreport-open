import React from 'react';
import Divider from "@material-ui/core/Divider";
import ViewerTextField from "./ViewerTextField";
import {ViewerCSS} from './ViewerCSS';

/**
 * Сопоставление двух полей ViewerTextField
 * @param {Object} props - свойства компонента
 * @param {String} props.leftLabel - заголовок (метка) левого поля
 * @param {String} props.rightLabel - заголовок (метка) правого поля
 * @param {*} props.leftValue - значение левого поля
 * @param {*} props.rightValue - значение правого поля
 * @returns {JSX.Element}
 * @constructor
 */
export default function ViewerFieldMapping(props){

    const classes = ViewerCSS();

    return (
        <div className={classes.displayFlex}>
            <ViewerTextField
                label = {props.leftLabel}
                value = {props.leftValue}
                displayBlock
                />
            <Divider orientation="vertical" flexItem  className={classes.displayFlex} variant="middle" />
            <ViewerTextField
                label = {props.rightLabel}
                value = {props.rightValue}
                displayBlock
                />
        </div>
    )
}
