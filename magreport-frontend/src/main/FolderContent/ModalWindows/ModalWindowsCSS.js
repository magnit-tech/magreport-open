import { makeStyles, alpha } from '@material-ui/core/styles';
//import StyleConsts from 'StyleConsts';

export const RoleWindowCSS = makeStyles(theme => ({
    root: {
        minWidth: '250px', 
        maxHeight: '284px',
    },
    paramText: {
        color: theme.palette.primary.contrastText,
        fontWeight: theme.typography.fontWeightMedium,
        overflow: 'hidden',
        whiteSpace: 'nowrap',
        marginLeft: '-16px',
        fontSize: 'large'
    },
    indent: {
        margin: 'auto 16px 16px'
    },
})); 

export const RoleUserListWindowCSS = makeStyles(theme => ({
    root: {
        minWidth: '400px', 
        height: '80vh',
        width: '100%' //'50vw'
    },
    filterTitle: {
        backgroundColor: theme.palette.primary.light,
        color: theme.palette.primary.contrastText,

    },
    title: {
        flexGrow: 1,
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'block',
        },
    },
    indent: {
        margin: 'auto 16px 16px'
    },
    search: {
        position: 'relative',
        borderRadius: theme.shape.borderRadius,
        color: theme.palette.primary.contrastText,
        backgroundColor: alpha(theme.palette.common.white, 0.15),
        '&:hover': {
          backgroundColor: alpha(theme.palette.common.white, 0.25),
        },
        marginLeft: 0,
        width: '100%',
        [theme.breakpoints.up('sm')]: {
          marginLeft: theme.spacing(1),
          width: 'auto',
        },
    },
    searchIcon: {
        width: theme.spacing(7),
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    inputRoot: {
        color: 'inherit',
    },
    inputInput: {
        padding: theme.spacing(1, 1, 1, 7),
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(12),
            '&:focus': {
                width: 200,
            },
        },
        [theme.breakpoints.down('md')]: {
            width: theme.spacing(0),
            '&:focus': {
                width: 200,
            },
        },
    },
    addRoleBtn: {
        width: "160px"
    },
    addRoleBtnDiv: {
        display: "flex",
        justifyContent: "end"
    }
})); 

export const ChooserDestinationWindowCSS = makeStyles(theme => ({
    root: {
        minWidth: '400px', 
        height: '90vh',
        width: '100%',
        maxWidth: '700px'
    },
    topDialogAction: {
        padding: '32px 32px 0px'
    },
    leftBtnPanel: {
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'space-between'
    },
    rightBtnPanel: {
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'end'
    },
    leftRightPanel: {
        display: 'flex', 
        flex: 1, 
        padding: '0px 8px 8px'
    },
    flex8px: {
        display: 'flex', 
        margin: '8px'
    },
    flexRelative: {
        display: 'flex', 
        flex: 1, 
        flexDirection: 'column',
         position: 'relative'
    },
    flexAbsolute: {
        overflow: 'auto', 
        display: 'flex', 
        flexDirection: 'column', 
        position: 'absolute',
        top: 0, 
        bottom: 0, 
        left: 0, 
        right: 0
    },
    flx: {
        display: 'flex', 
        flex: 1
    }
}));