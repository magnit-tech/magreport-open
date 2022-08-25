import { makeStyles, alpha } from '@material-ui/core/styles';

export const FolderTreeCSS = makeStyles(theme => ({
    listClass: {
        padding: 0,
    },
    listExpandClass: {
        minWidth: '24px'
    },
    folderIcon: {
        color: alpha(theme.palette.primary.light, 0.75),
        minWidth: '32px'
    },
    listItemSmall: {
        height: '1.5em',
        display: 'flex',
        paddingLeft: '4px',
        "&:hover": {
            borderRadius: theme.shape.borderRadius
        },
    },
    divReportChild: {
        padding: theme.spacing(1,0),
        display: 'flex',
        minHeight: 0 ,
        margin: theme.spacing(0, 0.25),
    },
    loaderCenter: {
        float: 'right',
        width: '10px',
    },
    noFoldersBlock: {
        display:"flex", 
        marginLeft: "30px", 
        fontSize:"0.8em"
    },
    noFoldersBlockItem: {
        marginRight:"5px", 
        fontSize:"1em", 
        marginTop:"3px"
    }
    }));