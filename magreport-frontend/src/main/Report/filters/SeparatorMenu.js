import React from 'react';
import PropTypes from 'prop-types';

// components
import IconButton from '@material-ui/core/IconButton';
import DnsIcon from '@material-ui/icons/Dns';
import Tooltip from '@material-ui/core/Tooltip';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

// predefined values

export default function SeparatorMenu({separators, onChangeSeparators}){

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleButtonClick = event => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleChange = index => {
        const nesSeparators = separators.map((s, i) => {
            if (i === index) {
                s.checked = !s.checked
            }
            return s
        })
        onChangeSeparators(nesSeparators)
    }

    return (
        <>
            <Tooltip 
                title="Выбрать разделители" 
                placement="top"
                onClick={handleButtonClick}
            >
                <IconButton aria-label="Separators" size='small'>
                    <DnsIcon/>
                </IconButton>
            </Tooltip>
            <Menu
                id="predefinedMenu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
            >
                {
                    separators.map( (s, index) => 
                        <MenuItem key={index}>
                            <FormControlLabel
                                control={<Checkbox 
                                    checked={s.checked}
                                    onChange={event => handleChange(index)}
                                    />}
                                label={s.name}
                            />
                        </MenuItem>
                    )
                }
            </Menu>
        </>
    )
}

SeparatorMenu.propTypes = {
    separators: PropTypes.array.isRequired,
    onChangeSeparators: PropTypes.func.isRequired,
};