import React, {useState} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';

import { connect } from 'react-redux';
import { appLogin } from '../../redux/actions/actionsLogin';
import { hideAlert } from '../../redux/actions/actionsAlert';
import { LoginFormCSS } from './LoginFormCSS'
import dataHub from 'ajax/DataHub'
import CircularProgress from '@material-ui/core/CircularProgress';
import IconButton from '@material-ui/core/IconButton';
import OutlinedInput from '@material-ui/core/OutlinedInput';
import InputLabel from '@material-ui/core/InputLabel';
import InputAdornment from '@material-ui/core/InputAdornment';
import FormControl from '@material-ui/core/FormControl';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';

//local
import StyleConsts from 'StyleConsts.js';

function LoginForm(props){
    const classes = LoginFormCSS();

    const { loader } = props;

    const [form, setForm] = useState({login: '', password: ''});
    const [showPassword, setShowPassword] = useState(false);

    
      const handleClickShowPassword = () => {
        setShowPassword(!showPassword );
      };
    
      const handleMouseDownPassword = (event) => {
        event.preventDefault();
      };



    
    function handleChange(e){
        props.hideAlert();
        setForm( {...form, [e.target.name]: e.target.value}, );
    }

    function handleSubmit(e){
        e.preventDefault();
        props.appLogin(form.login, form.password, dataHub);
        if (!localStorage.getItem('drawerWidth')) {
            localStorage.setItem('drawerWidth', StyleConsts.drawerWidth);
        }
    }

    return(
        <div className={classes.paper}>
            <Avatar className={classes.avatar}>
                <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
                Авторизация
            </Typography>
            <form className={classes.form} noValidate>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="login"
                    label="Логин"
                    name="login"
                    autoFocus
                    value={form.login}
                    onChange={handleChange}
                    error={props.alertData.data.open}
                />
                <FormControl variant="outlined" fullWidth style={{margin: '16px 0px 8px'}}>
                    <InputLabel htmlFor="password" required error={props.alertData.data.open}>Пароль</InputLabel>
                    <OutlinedInput
                        id="password"
                        name = "password"
                        type={showPassword ? 'text' : 'password'}
                        value={form.password}
                        required
                        onChange={handleChange}
                       // autoComplete="current-password"
                        error={props.alertData.data.open}
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    style={{marginLeft: '-8px'}}
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowPassword}
                                    onMouseDown={handleMouseDownPassword}
                                    edge="end"
                                >
                                    {showPassword ? <Visibility/> : <VisibilityOff/>}
                                </IconButton>
                            </InputAdornment>
                        }
                        labelWidth={68}
                    />
                </FormControl>

                {/*
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="password"
                    label="Пароль"
                    type="password"
                    name="password"
                    value={form.password}
                    autoComplete="current-password"
                    onChange={handleChange}
                    error={props.alertData.data.open}
                    /> */
                }
                <Button                          
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    disabled={loader}
                    onClick={handleSubmit}> Войти
                </Button>
                { loader ? <div className={classes.circularProgress}><CircularProgress /></div> : null}
                {
                /*
                //TODO: Сделать загрузку ссылки и текста с бэкенда
                <Grid container>
                    <Grid item xs className={classes.failLogginLink}>
                        <Link variant="body2" href="mailto:sopr_magreport@magnit.ru?subject=Магрепорт: ошибка при входе">
                            Ошибка при входе? 
                        </Link>
                    </Grid>
                </Grid>
                */
                }
            </form>
        </div>
     );
}

const mapStateToProps = state => {
    return {
        loader: state.loader.loader,
        alertData: state.alert
    }
}

const mapDispatchToProps = {
    appLogin,
    hideAlert
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);
