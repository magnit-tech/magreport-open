import { makeStyles, createTheme } from '@material-ui/core/styles';

export const AutoRefreshSelectCSS = makeStyles(theme => ({
    selectStyle: {
        height: '34px',
        width: '56px',
        textAlign: 'center',
    },
    iconStyle: {
        width: '46px',
    },
    text: {
        width:300
    }
}));

export const AutoRefreshSelectTheme = createTheme({
    overrides: {
        MuiInput: {
            underline: {
                backgroundColor: 'white',
                borderBottom: 0,
                '&:hover:not($disabled):before': {//its when you hover and input is not foucused 
                    borderBottom: 0
                },
                '&:after': {//when input is focused, Its just for example. Its better to set this one using primary color
                    borderBottom: 0
                },
                '&:before': {// when input is not touched
                    borderBottom: 0
                },
                '&:focus': {
                    backgroundColor: 'white'
                }
            },
        },
    },
});
//!!!
export const JobStatusSelectCSS = makeStyles(theme => ({
    formControl: {
        margin: theme.spacing(0.5),
        minWidth: "420px",
        width: "100%"
    }
}));
//!!!
export const JobUsernameSelectCSS = makeStyles(theme => ({
    formControl: {
        margin: theme.spacing(0.5),
        minWidth: '216px'
    }
}));
//!!!
export const ElementsOnPageSelectCSS = makeStyles(theme => ({
    selectStyle: {
        marginLeft: '3px',
        height: '32px',
        minWidth: '96px',
        textAlign: 'center',
        flexDirection: 'row',
        backgroundColor: theme.palette.background.default,
        color: theme.palette.text.primary
    }
}));