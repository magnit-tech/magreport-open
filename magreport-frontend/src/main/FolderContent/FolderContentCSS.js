import { withStyles, makeStyles, alpha } from '@material-ui/core/styles';
import Slider from '@material-ui/core/Slider';
import StyleConsts from '../../StyleConsts';

export const FolderContentCSS = makeStyles(theme => ({
    relative: {
        position: 'relative',
        display: 'flex',
        flexDirection: 'column',
        flex: 1,
        width: '100%'
    },
    gridContentRelative: {
        position: 'relative',
        display: 'flex',
        flexDirection: 'column',
        flex: '1',
        border: '1px solid',
        borderColor: theme.palette.divider,
       // borderRadius: '4px',
        marginTop:'4px',
       // minWidth: '550px',
    }, 
    gridContentAbsolute: {
        position: 'absolute', 
        top: 0, 
        bottom: 0, 
        display: 'flex', 
        flexDirection: 'column', 
        flex: '1', 
        width: '100%',
        overflow: 'auto'
    },
    gridItem: {
        display: 'flex',
        alignItem: 'stretch'
    },
    addButtonRoot: {
        position: 'fixed',
        right: '20px',
        bottom: '20px',
        margin: '20px'
    },
    filterButton: {
        position: 'fixed',
        right: '20px',
        top: '36px',
        margin: '20px',
        zIndex: 2,
        paddingTop: '25px'
    },
    refreshButton: {
        position: 'fixed',
        right: '20px',
        bottom: '20px',
        margin: '20px',
        zIndex: 100
    },
    drawerStyles: {
        width: "auto",
        minWidth: "508px",
        display: 'flex',
        margin: "4px 8px",
        padding: "2px 8px",
        border: `1px solid ${theme.palette.divider}`,
        borderRadius: theme.shape.borderRadius,
        backgroundColor: theme.palette.background.firstLevel
    }, 
    gridFilter: {
        display: "flex",
        flexWrap: "wrap",
        width: "calc(100% - 60px)"
    },
    divTime: {
        display: "flex",
        flexDirection: "column"
    }, 
    itemStatusFilter: {
        minWidth: "428px",
        maxWidth: "calc(100% - 450px)"
    },
    dtmStyle: {
        margin: '4px',
        width: 206
    },
    divPagination: {
        display: 'flex',
        alignItems: 'center',
        margin: '8px 16px 2px 13px',
        backgroundColor:  theme.palette.background.workspace,
    },
    datesFilter: {
        display: 'flex'
    },
    openSearchBtn:  {
        height: '36px',
        borderRadius: '0px 0px 24px 24px',
        padding: '3px',
        position: 'fixed',
        right: '30px',
        top: StyleConsts.headerHeight ,
        zIndex: 100
  },
  
}));


export const ItemCardCSS = makeStyles(theme => ({
    card: {
        //zIndex:1,
        display: 'flex',
        flexDirection: 'column',
        width: 450,
        cursor: 'pointer',
        borderRadius: '8px 8px 8px 8px',
        margin: theme.spacing(1, 0, 1, 2),
        "&:hover": {
                backgroundColor: theme.palette.action.focus,
                zIndex: 1
            }, 
    },
    successIcon:{
        color: theme.palette.success.main
    },
    errorIcon: {
        color: theme.palette.error.main
    },
    primaryIcon: {
        color: theme.palette.primary.main
    },
    cardHead: {
        height: '100%',
        alignItems: 'start',
        padding: '16px 16px 0px',
        color: theme.palette.success.main
    },
    cardHeadContent: {
        alignSelf: 'start',
        hyphens: 'auto'
    },
    cardAction: {
        float: 'right', 
        padding: '2px', 
        backgroundColor: theme.palette.drawerColor,
        borderTop: `2px dashed ${theme.palette.common.white}`,
        boxShadow: `0 0 0 4px ${theme.palette.drawerColor}, 2px 1px 6px 4px ${theme.palette.drawerColor}`
    },
    invalid: {
        backgroundColor : alpha(theme.palette.error.light, 0.15),
        "&:hover": {
            backgroundColor: alpha(theme.palette.error.light, 0.35)
        }
    },
    subHead: {
        display: 'flex',
        justifyContent: 'space-between'
    },
    cancel: {
        display: 'inline',
        color: theme.palette.error.light,
        "&:hover": {
            color: theme.palette.error.dark
        },
    }, 
    favoriteItem: {
        color: "orange",
    }
}));

export const FolderCardCSS = makeStyles(theme => ({
    card: {
        width: 450,
        cursor: 'pointer',
        display: 'flex',
        margin: theme.spacing(1, 0, 1, 2),
        backgroundColor: theme.palette.background.firstLevel,
        "&:hover": {
            backgroundColor: theme.palette.action.focus
        }
    },
    detail: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center'
    },
    folderIcon: {
        color:theme.palette.folderIcon, 
        marginLeft: theme.spacing(1.5)
    }
}));

export const TimeSlider = withStyles({
    root: {
      height: "26px",
      marginBottom: 0,
      padding: "4px 0px"
    },
    thumb: {
      height: 24,
      width: 24,
      backgroundColor: '#fff',
      marginTop: -8,
      marginLeft: -12,
      '&:focus, &:hover, &$active': {
        boxShadow: 'inherit',
      },
    },
    active: {},
    valueLabel: {
      left: 'calc(-50% + 4px)',
     
    },
    markLabel: {
        top: "16px"
    },
    track: {
      height: 8,
      borderRadius: 4,
    },
    rail: {
      height: 8,
      borderRadius: 4,
    },
    mark: {
        height: 6
      }
  })(Slider);

export const SearchFieldCSS = makeStyles((theme) => ({
    searchDiv: {
        position: 'sticky', 
        top: 0, 
        zIndex: 100, 
        backgroundColor: theme.palette.background.default
    },
    searchRoot: {
        padding: "2px 4px 2px 8px",
        margin: "5px 16px 2px",
        minWidth: "450px",
        backgroundColor: theme.palette.background.firstLevel
    },
    input: {
        marginLeft: 16
    },
    searchEndIcons: {  
        height: 'auto',
    },
    divider: {
        width: 1,
        height: 28,
        margin: 4,
    },
    searchControl: {
        verticalAlign: "middle",
        width: "100%"
    },
    inputRoot: {
        color: 'inherit'
    },
    inputInput: {
        padding: theme.spacing(1),
/*
    transition: theme.transitions.create('width'),
    width: 100,
    [theme.breakpoints.up('sm')]: {
     // width: '100%' ,//theme.spacing(6), //96px,
        '&:focus': {
            width: 500,
        },
    },
    [theme.breakpoints.down('md')]: {
        width: theme.spacing(0), //96px,
        '&:focus': {
            width: 100,
        },
        },*/
    },
}));
