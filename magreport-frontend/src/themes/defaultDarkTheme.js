import { createTheme /*, ThemeProvider*/ } from '@material-ui/core/styles';
// import { light } from '@material-ui/core/styles/createPalette';
import { orange } from '@material-ui/core/colors';

const defaultDarkTheme = createTheme({
    palette: {
        primary: 
        {
            main: '#7377a9', //'#55588a', //'#464775', //'#7986cb'
        }, 
        secondary:
        {
            main: '#ff4081'
        },
        active: '#8C989D', //активный лист дерева
     /*   background: {
            default: '#1e1e1e',
            paper: '#424242', //'#3f3f46',
            sidebar: '#252526',
           //sidebarList: '#ffffff'
        }, */
        folderIcon: orange[100],
        breadcrumbsLabel: '#bcc2e5',
        drawerColor: '#434550',
        type: 'dark',
      //  header: '#464775'
       // filtersTone: 
    },
    typography: {
        htmlFontSize: 16,
        fontFamily: [
              //'Pattaya',
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
        fontSize: 14,   
    
            
    }
  });

export default defaultDarkTheme