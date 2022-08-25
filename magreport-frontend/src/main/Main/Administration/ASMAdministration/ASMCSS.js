import makeStyles from "@material-ui/core/styles/makeStyles";

export const ASMCSS = makeStyles(theme => ({
    asmList: {
        display: 'flex',
        flex: 1,
        flexDirection: 'column'
    },
    buttonPanel: {
        padding: theme.spacing(0.5,2, 1),
        overflow: 'hidden'
    },
    card: {
        display: 'flex',
        flexDirection: 'column',
        width: 450,
        cursor: 'pointer',
        margin: theme.spacing(1, 0, 1, 2),
        "&:hover": {
            backgroundColor: theme.palette.action.hover
        }
    },
    cardHead: {
        height: '100%',
        alignItems: 'start'
    },
    cardHeadContent: {
        alignSelf: 'start'
    },
    cardAction: {
        float: 'right', 
        padding: '2px', 
        backgroundColor: theme.palette.drawerColor,
        borderTop: `2px dashed ${theme.palette.common.white}`,
        boxShadow: `0 0 0 4px ${theme.palette.drawerColor}, 2px 1px 6px 4px ${theme.palette.drawerColor}`
    },
    cardSelected: {
        backgroundColor: theme.palette.action.selected
    },
    asmListFlexRelative: {
        display: 'flex',
        flex: '1',
        border: '1px solid',
        borderColor: theme.palette.divider,
        position: 'relative'
    },
    asmListGridFlexAbsolite: {
        position: 'absolute',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
        overflow: 'auto'
    },
    gridItem: {
        display: 'flex',
        alignItem: 'stretch'
    },
    progress: {
        margin: 'auto auto'
    },
}));