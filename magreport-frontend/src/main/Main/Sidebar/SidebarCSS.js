import { makeStyles, alpha} from '@material-ui/core/styles';

export const SidebarCSS = makeStyles(theme => ({
    drawer: {
      display: 'flex',
      flexDirection: 'column',
      whiteSpace: 'nowrap',
      height: '100vh',
      fontSize: '1rem',
      backgroundColor: theme.palette.drawerColor
    },
    drawerOpen: {
        width: localStorage.getItem('drawerWidth') + 'px',
        transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
      }),
    },
    drawerClose: {
        transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      overflowX: 'hidden',
      width: theme.spacing(8),
    },
    drawerContent: {
      overflow: 'auto', 
      display: 'flex',
      flexDirection: 'column',
      whiteSpace: 'nowrap',
      height: '100vh',
      fontSize: '1rem',
      backgroundColor: theme.palette.drawerColor
    },
    drowerViewPanel: {
      
    },
    toolbar: {
      position: 'sticky',
      top: 0,
      left: 0,
      zIndex: 5,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'flex-end',
      padding: theme.spacing(0, 1),
      minHeight: '48px'
    },
    dragger: {
      width: "3px",
      cursor: "ew-resize",
      padding: "4px 0 0",
      borderTop: "1px solid #ddd",
      position: "absolute",
      top: 0,
      right: 0,
      bottom: 0,
      zIndex: 100,
      backgroundColor: "#f4f7f9"
    },
    nestedMenu: {
      paddingLeft: theme.spacing(4)
    },
    nestedMenuText: {
      fontSize: 8
    },
    listItemSmall: {
        height: '1.6em',
        paddingLeft: '16px',
        "&:hover": {
            borderRadius: theme.shape.borderRadius
          },
      },
    listIconClass: {
        minWidth: '40px'
    },
    folderListItem: {
        borderRadius: theme.shape.borderRadius
     },
     folderListItemActive: {
      backgroundColor: theme.palette.active,
      borderRadius: theme.shape.borderRadius,
      "&:hover": {
          backgroundColor: alpha(theme.palette.active, 0.45)
        }
    },      
     listItem: {
      height: '2.5em',
      display: 'flex',
      "&:hover": {
          borderRadius: theme.shape.borderRadius
        },
    },
    paperRoot: {
        position: 'sticky',
       // left: '40px',
        margin: theme.spacing(0.5, 0.5, 0.25, 0.5),
        borderRadius: theme.shape.borderRadius,
    },
    listClassMain: {
        padding: 0,
        position: 'sticky',
        left: 0
    },
  }));