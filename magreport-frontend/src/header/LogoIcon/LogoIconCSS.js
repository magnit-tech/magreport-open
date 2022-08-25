import { makeStyles } from '@material-ui/core/styles';
import LogoIconPng from '../icons/magreport-logo-white.png';

export const LogoIconCSS = makeStyles(theme => ({
    logoIcon : {
        backgroundImage : 'url(' + LogoIconPng + ')',
        backgroundSize: 'contain',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        margin : 5,
        height: 32,
        width: 32
    }
}));