import { makeStyles} from '@material-ui/core/styles';
import LoginCatPng from '../../images/LoginCat.png';


export const LoginPageCSS = makeStyles(theme => ({
    main: {
        padding: theme.spacing(0),
        margin: theme.spacing(0)
    },
    welcomeText: {
        textAlign: 'center',
    },
    loginBox: {
        paddingTop: '32px'
    },
    loginCat : {
        backgroundImage : 'url(' + LoginCatPng + ')',
        backgroundSize: 'contain',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        marginTop : '48px',
        height: '400px'
    },
    errorPaper: {
        padding: '16px', 
        marginTop: '8px', 
        marginRight: '4px',
        border: '2px solid', 
        borderColor: theme.palette.error.dark,
        display: 'flex'
    }

    
}));