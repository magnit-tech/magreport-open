import { makeStyles } from '@material-ui/core/styles';
//import StyleConsts from 'StyleConsts.js';
import { withStyles } from '@material-ui/core/styles';
import MuiAccordion from '@material-ui/core/Accordion';
import MuiAccordionSummary from '@material-ui/core/AccordionSummary';
import MuiAccordionDetails from '@material-ui/core/AccordionDetails';

export const SingleValueUnboundedCSS = makeStyles(theme => ({
    textField: {
        display: "flex",
        margin: "8px 0px",
      /*  '& .MuiInputBase-root': {
            display: 'flex',
            alignItems: 'start'
        }*/
    },
    topInputAdornment: {
        marginTop: '8px'
    }
}));

export const ValueListCSS = makeStyles(theme => ({
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
    }
}));

export const HierTreeCSS = makeStyles(theme => ({
    root: {
        width: '100%',
        //boxShadow: StyleConsts.treeBoxShadow,
        border: '1px solid',
        borderColor: theme.palette.divider,
        borderRadius: 3,
        '&:hover': {
            borderColor: theme.palette.text.primary
        }
    },
    treeDiv:{
        margin: '8px 0px'
    },
    treeItem:{
        borderRadius: theme.shape.borderRadius,
        paddingRight: theme.spacing(2),
        width: 'auto'
    },
    sortIcon: {
        width: 18,
        height: 18,
        verticalAlign: 'middle',
        paddingBottom: '2px'
    },
    listText: {
        verticalAlign: 'middle'
    },
    treeItemLabel: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center'
    },
    /*
    notCheckedIcon: {
        backgroundColor: theme.palette.primary.main
        borderRadius: 3,
        width: 18,
        height: 18,
        boxShadow: StyleConsts.treeBoxShadow,
        backgroundColor: '#f5f8fa',
        backgroundImage: 'linear-gradient(180deg,hsla(0,0%,100%,.8),hsla(0,0%,100%,0))',
        '$root.Mui-focusVisible &': {
            outline: '2px auto rgba(19,124,189,.6)',
            outlineOffset: 2,
        },
        'input:hover ~ &': {
            backgroundColor: '#ebf1f5',
        } 
    },
    partialCheckedIcon: {
        color: theme.palette.secondary.light,
        borderRadius: 3,
        width: 18,
        height: 18,
        boxShadow: StyleConsts.treeBoxShadow,
        backgroundColor: theme.palette.primary.light,
        backgroundImage: 'linear-gradient(180deg,hsla(0,0%,100%,.8),hsla(0,0%,100%,0))',
        '$root.Mui-focusVisible &': {
            outline: '2px auto rgba(19,124,189,.6)',
            outlineOffset: 2,
        },
        'input:hover ~ &': {
            backgroundColor: '#80bfff',
        } 
    },      
    checkedIcon: {
        borderRadius: 3,
        backgroundColor: theme.palette.primary.main,
        '&:before': {
            display: 'block',
            width: 18,
            height: 18,
            backgroundImage:
                "url(\"data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3E%3Cpath" +
                " fill-rule='evenodd' clip-rule='evenodd' d='M12 5c-.28 0-.53.11-.71.29L7 9.59l-2.29-2.3a1.003 " +
                "1.003 0 00-1.42 1.42l3 3c.18.18.43.29.71.29s.53-.11.71-.29l5-5A1.003 1.003 0 0012 5z' fill='%23fff'/%3E%3C/svg%3E\")",
            content: '""',
        },
        'input:hover ~ &': {
            backgroundColor: theme.palette.primary.light
        },
    },
    */
    svgClass:{
        fontSize: '24px'
    },
    labelTreeItem: {
        display: "flex", 
        justifyContent: "space-between",
    }
}));

export const DatePickersCSS = makeStyles(theme => ({
    root: {
        display: 'flex',
        margin: '8px 0px',
        alignItems: 'baseline'
    },
    rangeRoot: {
        display: 'flex', 
        flexDirection: 'column'
    },
    cDiv: {
        alignItems: 'center', 
        display: 'flex'
    },
    fieldsDiv: {
        display: 'flex',
        alignItems: 'baseline'
    },
    status: {
        display: "flex",
        alignItems: "center",
        marginLeft: "16px"
    }
}));

export const DateValueCSS = makeStyles(theme => ({
    root: {
        display: 'flex',
        justifyContent: 'space-between',
        margin: '8px 0px'
    }
}));

export const FilterGroupCSS = makeStyles(theme => ({
    allChildrenBox : {
        width: "100%",
    },
    andOrType: {
        width: "100%",
        margin: "8px 0px"
    },
    orType: {
        '&:not(:first-child):before' :{
            content: '"ИЛИ"',
            color: theme.palette.primary.main,
            fontSize: "1.5em",
            fontWeight: 600,
            margin: "0.3em 0.2em 0 0",
            padding: "0 0.2em 0.1em 0.2em",
            borderRadius: "0.1em",
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
    mandatory: {
        color:"red", 
        marginLeft:"5px"
    },
}));

export const Accordion = withStyles(theme => ({
    root: {
        border: '1px solid',
        borderColor: theme.palette.divider,
        boxShadow: theme.shadows[5],
       '&:not(:last-child)': {
            borderBottom: 0,
        },
       '&:before': {
            display: 'none',
        },
       '&$expanded': {
            margin: 'auto',
        },
    },
    expanded: {},
  }))(MuiAccordion);
  
export  const AccordionSummary = withStyles(theme => ({
    root: {
        backgroundColor: theme.palette.drawerColor,
       // borderBottom: '1px solid', // rgba(0, 0, 0, .125)',
        borderBottom: `2px dashed ${theme.palette.primary.contrastText}`,
        boxShadow: `0 0 0 4px ${theme.palette.drawerColor}`, //, ${theme.shadows[5]}`/*2px 1px 6px 4px ${theme.palette.drawerColor}`*/,
        borderRadius: '2px 2px 0px 0px',
        margin: '2px 3px',
        minHeight: '48px',
        '&$expanded': {
            minHeight: '48px',
        },  
    },
    content: {
        margin: '8px 0px',
        '&$expanded': {
            margin: '8px 0',
        },
    },
    expanded: {},
  }))(MuiAccordionSummary);

export  const AccordionDetails = withStyles((theme) => ({
    root: {
        padding: theme.spacing(2),
        backgroundColor: theme.palette.background.paper,
        borderRadius: '0px 4px 4px 4px',
      //  boxShadow: theme.shadows[5],
    },
}))(MuiAccordionDetails);


export const AllFiltersCSS = makeStyles(theme => ({
    mandatory: {
        color:"red", 
        marginLeft:"5px"
    },
    divAutocomplete: {
        margin: '8px 0px'
    }
}))


