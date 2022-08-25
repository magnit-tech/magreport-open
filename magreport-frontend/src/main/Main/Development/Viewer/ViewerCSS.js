import {makeStyles} from '@material-ui/core/styles';

export const ViewerCSS = makeStyles(theme => ({
    viewerPage: {
        marginTop: "0px",
        display: 'flex',
        flexDirection: 'column',
        flex: 1
    },
    viewerPageChildren : {
        position: 'relative',
        display: 'flex',
        flex: 1,
        flexDirection: 'column'
    },
    viewerPageAbsolute:{
        display: 'flex', 
        flexDirection: 'column', 
        flex: 1, width: '100%', 
        position: 'absolute', 
        top:0, 
        bottom: 0, 
        right: 0, 
        left: 0, 
        overflow: 'auto',
        padding: '8px 16px'
    },
    viewerPageWOPadding:{
        padding: '0px 0px'
    },
    viewerTabPage: {
        margin: "8px"
    },
    field : {
        margin: "8px 0px"
    },
    displayBlock : {
        display : "block"
    },
    displayFlex : {
        display: 'flex'
    },
    pageBtnPanel: {
        padding: '8px 0px',
        borderRadius: '0px',
        display: 'flex',
        justifyContent: 'flex-end',
        backgroundColor: theme.palette.drawerColor
    },
    pageBtn: {
        margin: "0px 16px 0px 0px"
    },
    pageBtnConnection: {
        width: "fit-content",
        margin: "8px 8px 8px 0"
    },
    pageTabs: {
        margin: "8px 0px 0px",
        display: 'flex',
        flex: 1,
        flexDirection: 'column'
    },
    viewerChildrenPanelTitleBar: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.primary.contrastText,
    },
    viewerChildrenPanelContainer: {
        paddingTop: "8px",
    },
    tableCellHead: {
        backgroundColor: theme.palette.drawerColor
    },
    invalidRow: {
        backgroundColor : theme.palette.error.main,
    },
    userListTabPage: {
        margin: "8px",
        display: "flex",
        flexDirection: "column",
        flex: 1
    },
    userListPaper: {
        display: "flex",
        flex: 1
    },
    permittedListPaper: {
        margin: '0px 8px 8px',
        display: 'flex',
        flex: 1
    },
    reportFieldAccordion: {
        width: '100%',
        minWidth: '700px',
    },
    groupFilters: {
        margin: '16px 0px'
    },
    icon: {
        display: 'flex',
        alignItems: 'center',
        margin: theme.spacing(0, 0.5),
        padding: '12px',
    },
    reportFilterItemRoot: {
        minWidth: 275,
        width: '100%',
    },
    pageTitle: {
        marginTop: '8px',
        borderTopLeftRadius: "4px",
        borderTopRightRadius: "4px",
        backgroundColor: theme.palette.primary
    },
    permittedSelect: {
        margin: '16px 8px 8px'
    }
}));
