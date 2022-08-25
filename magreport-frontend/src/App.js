import React from 'react';
import LoginPage from './login/LoginPage/LoginPage';
import Main from './main/Main/Main'
import dataHub from 'ajax/DataHub'
import { connect } from 'react-redux';
import { APP_LOGGED_IN } from './redux/reduxTypes'
import { appLogout } from './redux/actions/admin/actionThemeDesign'
import { showAlert, hideAlert } from './redux/actions/actionsAlert'
import { ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from "@material-ui/core/CssBaseline";
import { SnackbarProvider } from 'notistack';

function App(props) {

    const appVersion = process.env.REACT_APP_VERSION ? process.env.REACT_APP_VERSION : "???";

    const {loginStatus} = props;

    function unautorizedHandler(){
        props.appLogout();
        
        function callback(){
            props.hideAlert()
            window.location.reload();
        }
        const buttons = [{'text':'OK','onClick': callback}]
        props.showAlert("Информация", "Вам необходимо перелогиниться в системе!", buttons, callback)
    }

    dataHub.setUnautorizedHandler(unautorizedHandler);

    return (
        <ThemeProvider theme={props.theme}>
            <SnackbarProvider maxSnack={10}>
                <CssBaseline/>
                <div>
                    {loginStatus === APP_LOGGED_IN ?
                        <Main version={appVersion}/> :
                        <LoginPage version={appVersion}/>
                    }
                </div>
            </SnackbarProvider>
        </ThemeProvider>
    );

}

const mapStateToProps = state => {
    return {
        loginStatus: state.login.loginStatus,
        theme: state.themesMenuView.theme
    }
}

const mapDispatchToProps = {
    appLogout,
    showAlert,
    hideAlert
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
