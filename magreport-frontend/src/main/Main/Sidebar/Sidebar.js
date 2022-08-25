import React, {useState, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import clsx from 'clsx';

// dataHub
import dataHub from 'ajax/DataHub';

// material-ui
import { Drawer} from '@material-ui/core';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import IconButton from '@material-ui/core/IconButton';

// actions
import { drawerToogle } from '../../../redux/actions/sidebar/actionDrawer';

// local
import SidebarItems from './SidebarItems';
import SidebarTopLevelMenu from './SidebarTopLevelMenu';

// css
import { SidebarCSS } from './SidebarCSS';

export const defaultDrawerWidth = localStorage.getItem('drawerWidth');
const minDrawerWidth = 230; // StyleConsts.drawerWidth;
const maxDrawerWidth = 1000;

function Sidebar(){

    const classes = SidebarCSS();
    const dispatch = useDispatch();
    const drawerOpen = useSelector(state => state.drawer.open);
    const currentUser = dataHub.localCache.getUserInfo();

    const defaultDraggerColor = "#f4f7f9";
    const [drawerWidth, setDrawerWidth] = useState(defaultDrawerWidth);
    const [draggerColor, setDraggerColor] = useState(defaultDraggerColor);

    const handleMouseDown = e => {
        document.addEventListener("mouseup", handleMouseUp, true);
        document.addEventListener("mousemove", handleMouseMove, true);
      };
    
      const handleMouseUp = () => {
        document.removeEventListener("mouseup", handleMouseUp, true);
        document.removeEventListener("mousemove", handleMouseMove, true);
        setDraggerColor(defaultDraggerColor);
      };
    
      const handleMouseMove = useCallback(e => {
        const newWidth = e.clientX - document.body.offsetLeft;
        if (newWidth > minDrawerWidth && newWidth < maxDrawerWidth) {
          setDrawerWidth(newWidth);
          localStorage.setItem('drawerWidth', newWidth);
        }

        if (newWidth < minDrawerWidth + 8)
        {
            setDraggerColor('red');
        }
        else {
            setDraggerColor(defaultDraggerColor);
        }
      }, []);

   
    function handleOpenClose(e){
        dispatch(drawerToogle());
    }

   let drawerOpenWidth = drawerOpen ? { maxWidth: '100vw', width: drawerWidth} : null;
   let drawerOpenWidthStyle ={style: drawerOpenWidth};
    return (
        <Drawer
            variant="permanent"
            className={clsx(classes.drawer, {
                        [classes.drawerOpen]: drawerOpen,
                        [classes.drawerClose]: !drawerOpen,
            })}
            classes={{
                paper: clsx({
                    [classes.drawerOpen]: drawerOpen,
                    [classes.drawerClose]: !drawerOpen,
                }),
            }}
            open={drawerOpen}
            PaperProps={drawerOpenWidthStyle}   
            style={drawerOpenWidth} 
        >
            { drawerOpen ? <div style={{backgroundColor: draggerColor}} onMouseDown={e => handleMouseDown(e)} className={classes.dragger}/> : null}
            <div className={classes.drawerContent}>
                <div className={classes.toolbar}>
                    <IconButton onClick={handleOpenClose}>
                        {drawerOpen ? <ChevronLeftIcon /> : <ChevronRightIcon /> }
                    </IconButton>
                </div>
            
                {Object.entries(SidebarItems).filter(([key, item]) => (
                    (!item.permission || (item.permission === 'DEVELOPER' && currentUser.isDeveloper) || 
                        currentUser.isAdmin)
                    )).map( ([key, item]) => (
                        <SidebarTopLevelMenu key={key} sidebarItem = {item}/>
                    )
                )}
            </div>
            
        </Drawer>
    );
}

export default Sidebar;
