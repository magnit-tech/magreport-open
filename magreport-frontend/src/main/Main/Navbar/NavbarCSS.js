import { makeStyles /*, emphasize*/ } from '@material-ui/core/styles';

export const NavbarCSS = makeStyles(theme => ({
    navbarBlock: {
        padding: '4px 0px 4px 16px',
        backgroundColor:  theme.palette.background.default,
        position: 'sticky',
        top: '0',
        zIndex: 1
    },
    chip: {
        color: theme.palette.primary.main,
        marginTop: theme.spacing(0.5)
    },
    sprt: {
        margin: theme.spacing(0,1,0,0)
    },
    chipLabel: {
        color: theme.palette.breadcrumbsLabel
    }
}))