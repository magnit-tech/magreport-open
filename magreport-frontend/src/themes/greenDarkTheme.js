import { createTheme, ThemeProvider } from '@material-ui/core/styles';

const greenDarkTheme = createTheme({
    palette: {
        primary: 
        {
            main: '#1b5e20'
        },
        secondary:
        {
            main: '#e65100'
        },
        background: {
            default: '#1e1e1e',
            paper: '#424242', //'#3f3f46',
            sidebar: '#252526',
           //sidebarList: '#ffffff'
        },
        type: 'dark'
    }
  });

export default greenDarkTheme