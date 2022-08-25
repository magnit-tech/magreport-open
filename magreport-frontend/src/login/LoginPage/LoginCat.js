import React from 'react';
import { LoginPageCSS } from './LoginPageCSS'

function LoginCat(){
    const classes = LoginPageCSS();
    return (
        <div className={classes.loginCat}></div>
    )
}

export default LoginCat;