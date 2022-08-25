import React, {useState} from "react";
import FormatBoldIcon from '@material-ui/icons/FormatBold';
import FormatItalicIcon from '@material-ui/icons/FormatItalic';
import FormatUnderlinedIcon from '@material-ui/icons/FormatUnderlined';
import FormatStrikethroughIcon from '@material-ui/icons/FormatStrikethrough';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import FormatTextdirectionLToRIcon from '@material-ui/icons/FormatTextdirectionLToR';
import {ToggleButton, ToggleButtonGroup} from "@material-ui/lab";


/**
 * Компонент просмотра расписаний
 * @param {Object} props - параметры компонента
 * @param {onClick} props.onClick - callback, вызываемый при нажатии кнопки
 * @return {JSX.Element}
 * @constructor
 */

export default function StyleTextPanel(props) {

    const [states, setStates] = useState([])
    const [alignment, setAlignment] = React.useState();

    const handleAlignment = (_event, newAlignment) => {
        setAlignment(newAlignment);
    };


    function click(value, ind) {

        if (!states[ind]) {
            states[ind] = true
            props.onClick(value)
        } else {
            states[ind] = false
            props.onClick(null)
        }

        setStates(states)
    }

    return <ToggleButtonGroup
                size="small"
                exclusive
                value={alignment}
                onChange={handleAlignment}>

                <ToggleButton
                    id="0"
                    key="0"
                    value="b"
                    onClick={() => click("b", 0)}
                > <FormatBoldIcon/>
                </ToggleButton>

                <ToggleButton
                    id="1"
                    key="1"
                    value="i"
                    onClick={() => click("i", 1)}
                > <FormatItalicIcon/>
                </ToggleButton>

                <ToggleButton
                    id="2"
                    key="2"
                    value="u"
                    onClick={() => click("u", 2)}
                > <FormatUnderlinedIcon/>
                </ToggleButton>

                <ToggleButton
                    id="3"
                    key="3"
                    value="strike"
                    onClick={() => click("strike", 3)}
                > <FormatStrikethroughIcon/>
                </ToggleButton>

                <ToggleButton
                    id="4"
                    key="4"
                    value="upperCase"
                    onClick={() => click("sup", 4)}
                >A<KeyboardArrowUpIcon/>
                </ToggleButton>

                <ToggleButton
                    id="5"
                    key="5"
                    value="lowCase"
                    onClick={() => click("sub", 5)}
                >A<KeyboardArrowDownIcon/>
                </ToggleButton>

                <ToggleButton
                    id="6"
                    key="6"
                    value="p"
                    onClick={() => click("p", 6)}
                ><FormatTextdirectionLToRIcon/>
                </ToggleButton>

            </ToggleButtonGroup>

}