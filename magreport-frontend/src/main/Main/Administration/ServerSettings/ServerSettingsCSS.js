import { makeStyles } from '@material-ui/core/styles';

export const ServerSettingsCSS = makeStyles((theme) => ({
    paper: {
        margin: '10px',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(15),
        color: theme.palette.text.secondary,
    },
    icon: {
        verticalAlign: 'bottom',
        height: 20,
        width: 20,
    },
    details: {
        alignItems: 'center',
    },
    column: {
       // flexBasis: '80%',
        display: 'flex'
    },
    columnButtons: {
       // flexBasis: '20%',
       display: 'flex',
       alignItems: 'center',
       marginLeft: '8px'
    },
    helper: {
        borderLeft: `2px solid ${theme.palette.divider}`,
        padding: theme.spacing(1, 2),
    },
    link: {
        color: theme.palette.primary.main,
        textDecoration: 'none',
        '&:hover': {
            textDecoration: 'underline',
        },
    },
    settingsCircular: {
        margin: '12px'
    }
}));

export const HistorySettingsDialogCSS = makeStyles((theme) => ({
    table: {
        minWidth: 650,
    },
}))