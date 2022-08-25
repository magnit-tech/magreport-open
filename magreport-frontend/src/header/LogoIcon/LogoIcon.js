import React from 'react';
import { LogoIconCSS } from './LogoIconCSS'

function LogoIcon(){
    const classes = LogoIconCSS();
    return (
        <div className={classes.logoIcon}></div>
    )
}

export default LogoIcon;