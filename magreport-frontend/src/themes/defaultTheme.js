import { createTheme } from '@material-ui/core/styles';
import { orange } from '@material-ui/core/colors';

const defaultTheme = createTheme({
    palette: {
        active: '#8C989D', //активный лист дерева 
        folderIcon: orange[200],
        folderIconLight: orange[100],
        breadcrumbsLabel: '#303f9f',
        drawerColor: '#e1e5f2',
     /*   primary: 
        {
            main: '#ff6d00'
        },
    /*    secondary:
        {
            main: '#e65100'
        },
        background: {
            default: '#1e1e1e',
            paper: '#424242', //'#3f3f46',
            sidebar: '#252526',
           //sidebarList: '#ffffff'
        }, */
        type: 'light'
    }  ,
    typography: {
        letterSpacing: '0.08333em',
        fontFamily: [
          'Roboto',
          'Helvetica',
          'Arial',
          'sans-serif',
          '"Segoe UI"' ,
          '-apple-system',
          'BlinkMacSystemFont',
          '"Apple Color Emoji"',
          '"Segoe UI Emoji"',
          '"Segoe UI Symbol"'
        ].join(',')  ,       
    },
     /*,
        body1: {
        /*fontFamily: ['Roboto', 'Helvetica', 'Arial', '"sans-serif"'],
        fontWeight: 400,
        fontSize: '1rem',
        lineHeight: 1.5,
        letterSpacing: '0.00938em'
        }
        /*
        htmlFontSize: 16,
        fontSize: 14,
        fontWeightLight: 300,
        fontWeightRegular: 400,
        fontWeightMedium: 500,
        fontWeightBold: 700,
        letterSpacing: '0.08333em' 
    } ,
      button: {
    //  fontFamily: ['Pattaya','Roboto', 'Helvetica', 'Arial', 'sans-serif'].join(','),
      fontWeight: 1000,
      fontSize: '0.875rem',
      lineHeight: 1.75,
      letterSpacing: '0.02857em',
      textTransform: 'uppercase'
      } */
  });

export default defaultTheme