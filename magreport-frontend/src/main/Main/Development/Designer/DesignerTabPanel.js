import React from 'react';
import Box from '@material-ui/core/Box';

export default function DesignerTabPanel(props) {
    const { children, value, index, ...other } = props;
    if (value === index) {
    return (
        <div style={{display: 'flex', flex: '1', flexDirection: 'column'}}
            component="div" 
            role="tabpanel"
           // hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && <Box p={0} style={{display: 'flex', flex: '1', flexDirection: 'column'}}>{children}</Box>}
        </div>
    )}
    else {return (<div/>)};
  }