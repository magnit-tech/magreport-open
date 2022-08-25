import {alpha, makeStyles} from '@material-ui/core/styles';
import StyleConsts from '../../../../StyleConsts.js';

export const UsersCSS = makeStyles(theme => ({
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
    userDesignerGrid: {
        minWidth: `calc(${StyleConsts.paperRoleUserWidth} * 2 + 16px)`,
        display: 'flex',
        flex: 1,
        padding: '0px 8px 8px'
    },
    userDesignerGridItem: {
        display: 'flex',
        margin: '8px'
    },
    userListCard: {
        minWidth: StyleConsts.paperRoleUserWidth,
        display: 'flex',
        flex: 1,
        flexDirection: 'column'
    },
    userListRelative: {
        display: 'flex', 
        flex: 1, 
        flexDirection: 'column', 
        position: 'relative',
        borderColor: theme.palette.divider,
        borderBottom: `1px solid ${theme.palette.divider}`,
    },
    userListBox: {
        overflow: 'auto',
        display: 'flex',
        flexDirection: 'column',
        position: 'absolute',
        top: 0,
        bottom: 0,
        left: 0,
        right: 0
    },
    bottomButtons:{
        margin: '4px 16px'
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
    roleAutocompleteDiv: {
        flexGrow: 1,
        marginRight: theme.spacing(1)
    },
    roleHideSelect: {
        display: 'flex',
        margin: theme.spacing(1,3,0,1),
        justifyContent: 'end'
    },
    userIcon: {
        marginRight: '18px'
    },
    filterTitle: {
        backgroundColor: theme.palette.primary.light,
        overflow: 'hidden',
        height: StyleConsts.headerHeight
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
          width: theme.spacing(12), //96px,
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
    roleList: {
        padding: 0,
        flexGrow: 1
    },
    addButton: {
        width: theme.spacing(20),
        height: theme.spacing(7),
        justifyContent: 'center'
    },
    spanBtn: {
        display: 'flex',
        minWidth: '400px',
        alignItems: 'center',
        paddingLeft: theme.spacing(2),
       height: '64px'
    },
    usersBtn:{
        margin: theme.spacing(0, 1),
    },
    btnText:{
        fontSize: 'unset',
        fontWeight: 'unset'
    }
}));
