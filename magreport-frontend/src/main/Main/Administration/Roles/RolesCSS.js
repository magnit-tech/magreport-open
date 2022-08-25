import { alpha, makeStyles } from '@material-ui/core/styles';
import StyleConsts from '../../../../StyleConsts.js';

export const RolesCSS = makeStyles(theme => ({
    rel : {
        position: 'relative',
        display: 'flex',
        flex: 1
    },
    abs: {
        display: 'flex', 
        flexDirection: 'column', 
        flex: 1, width: '100%', 
        position: 'absolute', 
        inset: 0, 
        overflow: 'auto'
    },
    roleList: {
        minWidth: StyleConsts.paperRoleUserWidth,
        display: 'flex',
        flex: 1,
        flexDirection: 'column'

    },
    roleListBox: {
       top: 0,
       left: 0,
       right: 0,
       bottom: 0,
       display: 'flex',
       overflow: 'auto',
       position: 'absolute',
       flexDirection: 'column'
    },
    roleListFlex: {
        display: 'flex', 
        flex: 1, 
        flexDirection: 'column'
    },
    roleListFlexRelative: {
        display: 'flex', 
        flex: 1, 
        flexDirection: 'column',
        position: 'relative'
    },
    titlebar: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.primary.contrastText,
    },
    title: {
        flexGrow: 1,
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'block',
        },
    },
    userAddPanel: {
        display: 'flex',
        margin: '16px 40px 16px 16px'
    },
    userListPaper: {
        margin: '0px 16px 16px',
        display: 'flex',
        flex: 1
    },
    roleAutocompleteDiv: {
        flexGrow: 1,
        marginRight: theme.spacing(2)
    },
    /*roleAutocomplete: {
        display: 'flex',
        flexGrow: 1
    },*/
    addButton: {
        width: theme.spacing(20),
        height: theme.spacing(7),
        justifyContent: 'center'
    },
    addButtonRW: {
        justifyContent: 'center',
        height: theme.spacing(7),
    },
    addButtonsRW: {
        display: 'flex',
        justifyContent: 'space-between',
        width: '176px'
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
    permittedFoldersListRel: {
        position: 'relative', 
        display: 'flex', 
        flex: 1
    },
    permittedFoldersListAbs: {
        position: 'absolute', 
        inset: 0, 
        overflow: 'auto', 
        display: 'flex', 
        flex: 1, 
        flexDirection: 'column'
    }
}));