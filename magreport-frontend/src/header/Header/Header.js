import React from 'react';
import Draggable from 'react-draggable';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import IconButton from '@material-ui/core/IconButton';
import LogoIcon from '../LogoIcon/LogoIcon';
import HelpIcon from '@material-ui/icons/Help';
import { HeaderCSS } from './HeaderCSS'
import { connect } from 'react-redux';
import { appLogout } from '../../redux/actions/admin/actionThemeDesign'
import Brightness5Icon from '@material-ui/icons/Brightness5';
import Brightness4Icon from '@material-ui/icons/Brightness4';
import Tooltip from '@material-ui/core/Tooltip';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Paper from '@material-ui/core/Paper';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';

import { setLightTheme, setDarkTheme } from '../../redux/actions/admin/actionThemeDesign';

function PaperComponent(props) {
    return (
      <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
        <Paper {...props} />
      </Draggable>
    );
  }

function Header(props){
    const classes = HeaderCSS();
    
    const { userName } = props.userName;

    const themeLightness  = props.themeLightness;
    const tooltipTitle = props.themeLightness ? 'Светлый фон': 'Тёмный фон';

    const [openAbout, setOpenAbout] = React.useState(false);

    const handleClickOpenAbout = () => {
        setOpenAbout(true);
        handleCloseMenu();
      };
    
      const handleCloseAbout = () => {
        setOpenAbout(false);
      };
    
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleOpenMenu = (event) => {
        setAnchorEl(event.currentTarget);
      };
    
    const handleCloseMenu = () => {
        setAnchorEl(null);
      };

    function handleClick(){
        props.appLogout();
    }

    function handleThemeClick(){
        localStorage.setItem('isDarkTheme', !themeLightness)
        if  (themeLightness ) {props.setLightTheme() }
        else { props.setDarkTheme () }
    }
    return(
        <AppBar position="static" className={classes.appBar}>
            <Menu
                id="simple-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleCloseMenu}
            >
                <MenuItem onClick={handleClickOpenAbout}>О программе</MenuItem>
            </Menu>
            <Dialog
                open={openAbout}
                onClose={handleCloseAbout}
                PaperComponent={PaperComponent}
                aria-labelledby="draggable-dialog-title"
            >
                <DialogTitle  style={{ cursor: 'move' }} id="draggable-dialog-title"> 
         
                </DialogTitle> 
                <DialogContent>
                    <div style={{display: 'flex', flexDirection: 'row'}}>
                        <div className={classes.catFinger}/>
                        <div style={{margin: '32px'}}>
                           <Typography variant='h6'> МАГРЕПОРТ </Typography>
                           <Typography variant='h6'> Версия: {props.version}</Typography>
                           </div>
                        </div>
                    <DialogContentText>
                        
                        
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button autoFocus variant= 'contained' onClick={handleCloseAbout} color="primary">
                        Понятно
                    </Button>
                </DialogActions>
            </Dialog>

            <Toolbar variant="dense" className={classes.iconIndent}>
                <LogoIcon/>
                <Typography  className={classes.logoText}>МАГРЕПОРТ</Typography>
                <Tooltip title = {'Справка'}>
                    <IconButton onClick={handleOpenMenu}>
                        <HelpIcon className={classes.iconButton} ></HelpIcon>
                    </IconButton>
                </Tooltip>
                <Tooltip title = {tooltipTitle}>
                    <IconButton onClick={handleThemeClick}>
                        {
                            themeLightness ? <Brightness5Icon className={classes.iconButton}/> : <Brightness4Icon className={classes.iconButton}/>                
                        }
                    </IconButton> 
                </Tooltip>         
                <Typography variant="overline" className={classes.userNameClass}>{userName}</Typography>
                { userName ?
                    <Tooltip title = 'Выйти'>
                        <IconButton 
                            className={classes.iconButton}
                            aria-label="logout" 
                            onClick={handleClick}
                        >
                            <ExitToAppIcon />
                        </IconButton>
                    </Tooltip>
                : ""}
            </Toolbar>
        </AppBar>
    );
}

const mapStateToProps = state => {
    return {
        userName: state.login,
        themeLightness: state.themesMenuView.darkTheme
    }
}

const mapDispatchToProps = {
    appLogout,
    setLightTheme,
    setDarkTheme
}

export default connect(mapStateToProps, mapDispatchToProps)(Header);