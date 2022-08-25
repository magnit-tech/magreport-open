import { makeStyles } from '@material-ui/core/styles';

export const AlertDialogCSS = makeStyles(theme => ({
    root: {
        overflow: 'hidden',
        margin: 'auto',
        minWidth: '250px',
        minHeight: '122px',
        maxWidth: '100%'
    },
    paramText: {
        color: theme.palette.text.primary,
        overflow: 'hidden',
        fontSize: 'medium',
        fontWeight: 'normal',
    },
    title:{
        whiteSpace: 'normal',
        marginTop: '20px'
    },
})); 