import StyleConsts from './StyleConsts.js';
import { makeStyles } from '@material-ui/core/styles';

export const ReportDataCSS = makeStyles(theme => ({
   root: {
        textAlign:'justify',     
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        flex: 1
    }, 
    relativeDiv: {
        display: 'flex', 
        flex: 1, 
        position: 'relative',
         flexDirection: 'column'
    },
    flexDiv: {
        display: 'flex', 
        flex: 1, 
        flexDirection: 'column'
    },
    container: {
        position: 'absolute',
        top: 0,
        bottom: 0
        //maxHeight: `calc(100vh - ${StyleConsts.headerHeight} - ${StyleConsts.breadHeight} - ${StyleConsts.reportPaginationHeight} - 12px)`,      
    },
    repExec: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        marginTop: '30vh'

    },
    repExecFailed: {
        display: 'flex',
        flexDirection: 'column',
        margin: '16px'

    },
    tableHead: {
        background: theme.palette.primary.light,
        color: theme.palette.primary.contrastText,
    },
    pagination: {
        flexShrink: 0,
        marginLeft: theme.spacing(2),
        width:'500px',
    },
    pag: {
        overflow: 'hidden',
    },
    tablerow: {
        "&:hover": {
            backgroundColor: theme.palette.action.hover
        }        
    },
    buttonPaper: {
        display: 'flex',
        marginTop: '8px',
        borderBottomLeftRadius: 0,
        borderBottomRightRadius: 0,
        height: StyleConsts.reportPaginationHeight,
        width: '100%'
    },
    input: {
        marginLeft: theme.spacing(1),
        flex: 1,
    },
    iconButton: {
        marginLeft: theme.spacing(1.5),
    },
    divider: {
        height: 28,
        margin: 4,
    },
    pageNumber: {
        width: '60px',
        height: '29.6px',
        backgroundColor: theme.palette.action.hover,
        marginLeft: theme.spacing(0.5),
        fontSize: '0.9rem'
    },
    progress: {
        marginTop: '20px'
    },
    text: { 
        marginTop: '30vh',
        textAlign: 'center'
    },
    mdiIcon: {
        path: {
            fill: theme.palette.secondary.main
        }
    }
    
}));

export const ReportStarterCSS = makeStyles(theme => ({
    filterRoot: {
        overflow: 'auto',
        padding: "12px 16px",
        //maxHeight: `calc(100vh  - 60px -  ${StyleConsts.headerHeight} - ${StyleConsts.breadHeight})`,
        minWidth: '510px'
    },
    reportStarterRelative: {
        position: 'relative',
        display: 'flex', 
        flex:1, 
        flexDirection: 'column'
    },
    reportStarterAbsolute: {
        position: 'absolute',  
        top: 0, 
        bottom: 0, 
        right: 0, 
        left: 0, 
        display: 'flex', 
        flex:1, 
        flexDirection: 'column'
    },
    buttonContainer: {
        display: 'flex',
        paddingLeft:  theme.spacing(1),
        minWidth: '350px'
    },
    filterButton: {
        margin: theme.spacing(1),
        width: 100
    },    
}));