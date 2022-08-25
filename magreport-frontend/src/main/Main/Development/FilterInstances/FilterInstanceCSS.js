import { makeStyles } from '@material-ui/core/styles';

export const HierTreeFieldsCSS = makeStyles((theme) => ({
    levelFields: {
        border : "1px solid",
        borderColor: theme.palette.divider,
        margin : "5px 0px 5px 0px",
        padding: "16px",
       // borderRadius: 3,
        width:'100%',
    },
    htfCenterBtn: {
        display: 'flex',
        justifyContent: 'center'
    }
}));
