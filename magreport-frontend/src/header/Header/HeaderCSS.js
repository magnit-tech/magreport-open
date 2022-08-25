import { makeStyles } from '@material-ui/core/styles';
import StyleConsts from '../../StyleConsts'; 
import CatFingerPng from '../../images/catFinger.png'

export const HeaderCSS = makeStyles(theme => ({
    appBar: {
        backgroundColor: theme.palette.primary.dark,
        height: StyleConsts.headerHeight,
        overflowX: 'hidden',
        transform: 'translateZ(0)',
        whiteSpace: 'nowrap'
    },
    iconIndent: {
        paddingLeft: '12px'
    },
    logoText: {
        flexGrow: 1,
        textAlign: 'left',
        color: theme.palette.primary.contrastText,
        fontWeight: theme.typography.fontWeightMedium,
        fontSize: '1rem',
        margin: '10px'
    },
    iconButton: {
        color: theme.palette.primary.contrastText
    },
    userNameClass: {
        margin: theme.spacing(0,1)
    },
    catFinger : {
        backgroundImage : 'url(' + CatFingerPng + ')',
        backgroundSize: 'contain',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        //marginTop : '48px',
        height: '140px',
        width: '140px'
    }
  }));