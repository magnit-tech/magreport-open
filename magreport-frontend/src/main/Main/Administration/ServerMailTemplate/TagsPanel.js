import React from "react";
import {ToggleButton} from "@material-ui/lab";


/**
 * Компонент просмотра расписаний
 * @param {Object} props - параметры компонента
 * @param {Array.<label: String, value: *, state: Boolean>} props.data -  массив тэгов
 * @param {onClick} props.onClick - callback, вызываемый при нажатии кнопки
 * @return {JSX.Element}
 * @constructor
 */

export default function TagsPanel(props){

    const result = [];

    function click(value,ind) {
        if(!props.data[ind].state) {
            props.data.forEach(
                tag => tag.state = false
            )
            props.data[ind].state = true
            props.onClick(value)
        }
        else {
            props.data[ind].state = false
            props.onClick(null)
        }

    }

    props.data.forEach((item,ind) =>
        result.push(
            <ToggleButton
                id={ind}
                key={ind}
                value={item.value}
                selected={item.state}
                onClick={() => click(item.value,ind)}
            >{item.label} </ToggleButton>)
    )

    return <div>{result}</div>;
}