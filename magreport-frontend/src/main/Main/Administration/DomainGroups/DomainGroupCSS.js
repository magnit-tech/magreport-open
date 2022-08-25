import {alpha, makeStyles} from '@material-ui/core/styles';
import StyleConsts from '../../../../StyleConsts.js';

export const DomainGroupCSS = makeStyles(theme => ({
    titlebar: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.primary.contrastText,
    },
    grow: {
        flexGrow: 1,
    },
    title: {
        flexGrow: 1,
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'block',
        },
    },
    domainGroupList: {
      //margin: theme.spacing(1),
      //height: `calc(100vh - ${StyleConsts.headerHeight} - 30px - 174px - ${StyleConsts.breadHeight})`,
      minWidth: StyleConsts.paperRoleUserWidth,
      padding: 0,
      display: 'flex',
      flexDirection: 'column',
      flex: 1,
      minHeight: '48px'
    },
    domainGroupListBox: {
      //overflow: 'auto',
      //height: `calc(100vh - ${StyleConsts.headerHeight} * 3 - 32px - 124px - ${StyleConsts.breadHeight})`
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      display: 'flex',
      overflow: 'auto',
      position: 'absolute',
      flexDirection: 'column'
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
            width: theme.spacing(0), //96px,
            '&:focus': {
              width: 200,
            },
          },
    },
}));
