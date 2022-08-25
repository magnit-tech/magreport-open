import {makeStyles, alpha} from '@material-ui/core/styles';


export const DesignerCSS = makeStyles(theme => ({
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
    },
    dialogPaper: {
        backgroundColor: theme.palette.background.default,
        minHeight: "40vh",
        height: '90vh'
    },
    filterTitle: {
        backgroundColor: theme.palette.primary.light,
        color: theme.palette.primary.contrastText,
    },
    indent: {
        margin: 'auto 16px 16px'
    },
    root: {
        margin: "8px 16px"
    },
    designerPage: {
        marginTop: "0px",
        display: 'flex',
        flexDirection: 'column',
        flex: 1
    },
    designerPageChildren : {
        position: 'relative',
        display: 'flex',
        flex: 1,
        flexDirection: 'column'
    },
    designerPageAbsolute:{
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
    designerPageWOPadding:{
        padding: '0px 0px'
    },
    field : {
        margin: "8px 0px"
    },
    displayBlock : {
        display : "flex"
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
    pageTabs: {
        margin: "8px 0px 0px",
        display: 'flex',
        flex: 1,
        flexDirection: 'column',
        overflow: 'hidden'
    },
    tabTitle: {
        borderTopLeftRadius: "4px",
        borderTopRightRadius: "4px",
        backgroundColor: theme.palette.primary
    },
    pageTitle: {
        marginTop: '8px',
        borderTopLeftRadius: "4px",
        borderTopRightRadius: "4px",
        backgroundColor: theme.palette.primary
    },
    textField: {
        //width: '100%',
        display: "flex",
        margin: "8px 0px",
        '& .MuiInputBase-root': {
            display: 'flex',
            alignItems: 'start'
        }
    },
    topInputAdornment: {
        marginTop: '8px'
    },
    path: {
        display: 'flex', 
        alignItems: 'center'
    },
    upBtn: {
        marginRight: '8px'
    },
    warning: {
        color: theme.palette.warning.main
    },
    warningText: {
        margin: '0px 16px',
        color: theme.palette.info.dark
    }
}));

export const DesignerMultipleSelectFieldCSS = makeStyles((theme) => ({
    root: {
     /*   '& > * + *': {
            marginTop: theme.spacing(3),
        },
        marginBottom: theme.spacing(1), */
        margin: '8px 0px'
    },
}));

export const PublishReportDesignerCSS = makeStyles((theme) => ({
    container: {
        display:'flex', 
        justifyContent:'center'
    },
    headerColor: {
        color:'#CCC'
    }
}));

export const ReportFiltersItemCSS = makeStyles((theme) => ({
    root: {
        minWidth: 275,
        width: '100%',
        "&:hover": {
            cursor: 'move'
        },
    },
    devRepFilterItemNameBlock: {
        minWidth: '300px',
        flexBasis: '400px',
        flexGrow: 1,
        margin: '8px 16px'
    },
    btn: {
        margin: theme.spacing(0, 0.5),
    },
    devRepFilterHeader: {
        padding: '0px 8px',
        display: 'flex',
        backgroundColor: alpha(theme.palette.primary.light, 0.1),
        border: '1px solid',
        borderColor: theme.palette.divider,
        minHeight: 56,
        alignItems: 'center'
    },
    repFieldFilterFieldBlock: {
        display: 'flex',
        flexDirection: 'column'
    },
    repFieldFilterField: {
        display: 'flex',
    },
    devRepFiltersClps: {
        padding: '8px 16px'
    },
    divCompare: {
        display: 'flex'
    },
    nameAndCode: {
        display: 'flex'
    },
    devRepFiltersName: {
        display: 'flex',
        flex: 1
    },
    devRepFiltersCode: {
      //  width: '108px',
        marginLeft: '16px'
    }
}));

export const DatasetDesignerCSS = makeStyles(theme => ({
    tableContainer:{
        position: 'absolute', 
        inset: 0, 
        display: 'flex', 
        overflow: 'auto'
    },
    table: {
        height: 'fit-content'
    },
    tableCellHead: {
        backgroundColor: theme.palette.drawerColor
    },
    updateBtn: {
        display: 'flex',
        padding: theme.spacing(0.5,2)
    },
    root: {
        width: '800px'
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    expandfields: {
        transform: 'rotate(360deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    invalid: {
        backgroundColor :  theme.palette.error.main,
    },
    invalidRow: {
        backgroundColor : theme.palette.error.main,
    },
    verticalScroll: {
        display: 'flex',
        flex: 1,
        position: 'relative'
    }
}));

export const ReportDevFiltersCSS = makeStyles((theme) => ({
    devRepGroupFiltersRoot: {
        '&:not(:first-child):before' :{
            content: '"ИЛИ"',
            //backgroundColor: theme.palette.primary.main,
            color: theme.palette.primary.main, //contrastText,
            fontSize: "1.5em",
            fontWeight: 600,
            margin: "0.3em 0.2em 0 0",
            padding: "0 0.2em 0.1em 0.2em",
            borderRadius: "0.1em",
         //   textShadow: '2px 2px 3px #2c2c2c',
           // textShadow: "-1px -1px #000, 0 1px 0 #444",
        }
    },
    listClass: {
        padding: 0
    },
    groupFilter: {
        width: '100%',
        margin: "4px auto",
        borderLeft: '5px solid DarkGray',
        borderRadius:'5px',
        minWidth: '700px',
        display: 'grid',
        "&:hover": {
            cursor: 'move'
        },
    }, 
    filterTypeInTitle: {
        color: theme.palette.primary.main, //contrastText,
        fontSize: "1.2em",
        fontWeight: 600,
        margin: "0px 20px",
        borderRadius: "0.1em",
    },
    filterTitle: {
        minWidth: '300px',
        flexBasis: '400px',
        flexGrow: 1,
        marginLeft: '8px'
    },
    devRepGroupFilterHeader: {
        display: 'flex',
        padding: '0px 8px',
        backgroundColor: alpha(theme.palette.primary.light, 0.1),
        minHeight: 56,
        alignItems: 'center',


        border: '1px solid',
        borderColor: theme.palette.divider,
        boxShadow: 'none',
    },
    nameAndType: {
        display: 'flex',
        margin: '8px 0px'
    },
    groupFilterType: {
        display: 'flex',
        alignItems: 'center',
        width: '110px',
        //margin: '0px 8px'
    },
    groupFilterName: {
        display: 'flex',
        alignItems: 'center',
        flexBasis: '400px',
        flexGrow: 1,
        marginLeft:'16px',
        minWidth: '400px'
    },
    groupFilterCode: {
        marginLeft:'16px',
        //width: '100px'
    },
    groupFilterNameBtns: {
        display: 'flex',
    },
    btn: {
        margin: theme.spacing(0, 0.5),
    },
    devRepGroupFilterDesc: {
        margin: theme.spacing(1, 0)
    },
    devRepGroupFilterClps: {
        //padding: theme.spacing(2),
        backgroundColor: theme.palette.background.paper,
        borderRadius: '0px 4px 4px 4px',
        boxShadow: theme.shadows[5],
        padding: '0px 16px'
    },
    orType: {
        '&:not(:first-child):before' :{
            content: '"ИЛИ"',
            //backgroundColor: theme.palette.primary.main,
            color: theme.palette.primary.main, //contrastText,
            fontSize: "1.5em",
            fontWeight: 600,
            margin: "0.3em 0.2em 0 0",
            padding: "0 0.2em 0.1em 0.2em",
            borderRadius: "0.1em",
         //   textShadow: '2px 2px 3px #2c2c2c',
           // textShadow: "-1px -1px #000, 0 1px 0 #444",
        }

    },
    andType: {         
        '&:not(:first-child):before' :{
            content: '"И"',
            width: "32px",
            color: theme.palette.primary.main, 
            fontSize: "1.5em",
            fontWeight: 600,
            margin: "0.3em 0.2em 0 0",
            padding: "0 0.2em 0.1em 0.2em",
            borderRadius: "0.1em"
        } 
    },
    itemFilter: {
        width: '100%',
        borderLeft: '5px solid LightSkyBlue',
        borderRadius:'5px',
        margin: '4px auto',
        //paddingLeft:'5px',
       // textAlign:'center'
    }, 
    justifyCenter: {
        display: 'flex', 
        justifyContent:'center',
        margin: '16px'
    },
    grpFilters: {
        marginTop: '16px'
    }
}));

export const ReportFieldItemCSS = makeStyles((theme) => ({
        devRepFilterItemNameBlock: {
        minWidth: '300px',
        flexBasis: '400px',
        flexGrow: 1,
        //marginLeft: '16px'
    },
    repFieldAccordion: {
        width: '100%',
        minWidth: '700px',
        "&:hover": {
            cursor: 'move'
        }
    },
    repFieldDesc: {
        width: '100%',
        margin: '8px 0px',
       // height: '88px',
        display: 'flex',
        //alignItems: 'center',
    },
    visibility: {
        margin: '8px'
    },
    repField: {
        minWidth: '352px',
        flexGrow: 1,
        flexShrink: 4,
        marginRight: '16px'
      //  margin: '8px 8px 0px 0px'
    } ,
    repFieldSel: {
        minWidth: '200px',
        flexBasis: '400px',
        flexShrink: 1
    },
    repFieldDet: {
        height: '88px',
        minWidth: '400px',
        margin: '-8px 64px 8px'
    },
    repFieldDetInvalid: {
        margin: '-8px 128px 8px 64px'
    }

}));