import React from "react";
import {TimePicker, MuiPickersUtilsProvider} from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import {DesignerCSS} from "./DesignerCSS";
import clsx from "clsx";


/**
 * @callback onChange
 * @param {Date} value
 */
/**
 * Поле с временем
 * @param {Object} props - свойства компонента
 * @param {String} [props.label="Время"] - название поля
 * @param {Date|String} props.value - значение типа Date, либо строка в формате HH:mm:ss
 * @param {onChange} props.onChange - callback, вызываемый при изменении значения
 * @param {Boolean} [props.fullWidth=false] - если true, то элемент примет ширину контейнера
 * @param {Boolean} [props.displayBlock=false] - если true, то добавляет стиль display: block
 * @param {String} [props.variant="outlined"] - вариант дизайна поля
 * @return {JSX.Element}
 * @constructor
 */
export default function DesignerTimeField(props) {

    const classes = DesignerCSS();

    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <TimePicker
                className = {clsx(classes.field, {[classes.displayBlock] : props.displayBlock})}
                inputVariant={props.variant ? props.variant : "outlined"}
                label={props.label || "Время"}
                value={props.value}
                onChange={props.onChange}
                fullWidth={props.fullWidth}
                ampm={false}
                format={"HH:mm:ss"}
                openTo="hours"
                views={["hours", "minutes", "seconds"]}
            />
        </MuiPickersUtilsProvider>
    );
}
