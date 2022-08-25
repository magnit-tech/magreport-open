import { makeStyles } from '@material-ui/core/styles';

export const DatasourceMenuCSS = makeStyles((theme) => ({
    dsdViewerTable: {
        border: '1px solid',
        borderColor: theme.palette.divider
    },
    tableCellHead: {
        backgroundColor: theme.palette.drawerColor
    },
    paper: {
        margin: '10px',
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
    },
    details: {
        alignItems: 'center',
    },
}));