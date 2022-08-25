import React from 'react';
import { connect } from 'react-redux';
import LoginForm from '../LoginForm/LoginForm.js';
import LoginCat from './LoginCat.js';
import Alerts from '../../main/Alerts/Alerts'
import CssBaseline from '@material-ui/core/CssBaseline';
import Paper from '@material-ui/core/Paper';
import Hidden from '@material-ui/core/Hidden';
import Header from '../../header/Header/Header'
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import { LoginPageCSS } from './LoginPageCSS';
import ErrorIcon from '@material-ui/icons/Error';


function LoginPage(props){

    const classes = LoginPageCSS();

    return (
        <div>
            <Header version={props.version}/>
            <Grid container component="main" className={classes.main} >
                <CssBaseline/>
                <Grid item xs={false} sm={4} md={7} lg={9}>
                    <Hidden smDown>
                        <Box marginTop={4} className={classes.loginBox}>
                            <Typography component="h1" variant="h5" className={classes.welcomeText}>
                                Добро пожаловать на портал корпоративной отчетности МАГРЕПОРТ!
                            </Typography>
                                <LoginCat/>
                        </Box>
                    </Hidden>
                </Grid>
                <Grid item xs={12} sm={8} md={5} lg={3} >                 
                   <Box component={Paper} elevation={6} square> <LoginForm /></Box>
                        {/*Alerts используется, чтобы вызвать window.location.reload() по нажатию на кнопку
                        При ошибке логина/пароля лишние действия по закрытию окна не нужны*/
                        props.alertData.title ==='Информация' ? <Alerts /> :
                            <div>
                                {props.alertData.open &&
                                    <Paper elevation={5} className={classes.errorPaper}>
                                        <ErrorIcon color='error' fontSize='large' style= {{margin: '16px'}}/>
                                        <div style= {{margin: '0px 16px'}}> 
                                            <Typography variant='h6'>  {props.alertData.title}</Typography>
                                            <Typography color='textSecondary'>{props.alertData.text}</Typography>
                                        </div>
                                    </Paper>
                                }
                            </div>
                        }
                </Grid>
            </Grid>
        </div>
    )
}

const mapStateToProps = state => {
    return {
        alertData: state.alert.data,
    }
}
export default connect(mapStateToProps, null)(LoginPage);
