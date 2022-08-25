import React from 'react';

// components
import IconButton from '@material-ui/core/IconButton';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Tooltip from '@material-ui/core/Tooltip';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';

// predefined values
import {PredefinedRanges, PredefinedValues} from './PredefinedValues'

export default function PredefinedMenu(props){

    const [anchorEl, setAnchorEl] = React.useState(null);
    let predefinedValuesArr
    if (props.filterType === 'DATE_VALUE'){
        predefinedValuesArr = Object.entries(PredefinedValues)
    }
    else {
        predefinedValuesArr = Object.entries(PredefinedRanges)
    }

    const handlePredefinedValueClick = fn => {
        if (props.filterType === 'DATE_VALUE'){
            props.onClickValue(fn())
        }
        else {
            props.onClickValue(fn())
        }
        setAnchorEl(null);
    }

    const handleButtonClick = event => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    return (
        <>
            <Menu
                id="predefinedMenu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
            >
                {
                    predefinedValuesArr.map(([key, obj]) => 
                        <MenuItem key={key} onClick={() => handlePredefinedValueClick(obj.fn)}>{obj.name}</MenuItem>
                    )
                }
                
            </Menu>
            <Tooltip 
                title="Предопределенные значения" 
                placement="top"
                onClick={handleButtonClick}
            >
                <IconButton aria-label="More">
                    <MoreVertIcon fontSize="inherit" />
                </IconButton>
            </Tooltip>
        </>
    )
}