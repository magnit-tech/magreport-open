import { makeStyles } from '@material-ui/core/styles';
import StyleConsts from '../../../../StyleConsts.js';

export const GridCSS = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    securityFilterGrid: {
        display: 'flex',
        flex: 1
    },
    securityFilterGridItemLeft:{
        display: 'flex',
        padding: '8px 8px 8px 0px'
    },
    securityFilterGridItemRight:{
        display: 'flex',
        padding: '8px 0px 8px 8px'
    },
    paper: {
       // padding: theme.spacing(2),
    },
    titlebar: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.primary.contrastText,
    },
    filterCard: {
        margin: theme.spacing(1,1,0,1),
        height: `calc(100vh - ${StyleConsts.headerHeight} - 22px - ${StyleConsts.breadHeight}*5)`,
        minWidth: StyleConsts.paperRoleUserWidth,
        padding: 0,
        flexGrow: 1,
        minHeight: '48px'

    },
    filterList: {
        //height: `calc(100vh - ${StyleConsts.headerHeight} * 3 - 32px - ${StyleConsts.breadHeight} - 104px)`,
        display: 'flex',
        flex: 1,
        flexDirection: 'column',
        overflow: 'auto',
       // margin: '8px'
    },
    filterListBox: {
        top: 0,
       left: 0,
       right: 0,
       bottom: 0,
       display: 'flex',
       overflow: 'auto',
       position: 'absolute',
       flexDirection: 'column'
    },
    sfGrid: {
        margin: '-8px'
    }
})); 

export const DatasetItemCSS = makeStyles((theme) => ({
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
    divAccordionContent: {
        width: '100%',
        display: 'flex',
        flexDirection: 'column'
    },
    accordionTitle: {
        display: 'flex',
        flexGrow: 1,
        alignItems: 'center'
    },
    divCompare: {
        display: 'flex'
    },
    divider: {
        padding: '0px 8px'
    },
    progress: {
        margin: 'auto auto'
    },
})); 
