import React from 'react';
import {useState} from 'react';

// material-ui
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Collapse from '@material-ui/core/Collapse';

// local
import FolderTree from './FolderTree/FolderTree';

// css
import { SidebarCSS } from './SidebarCSS';

/**
 * 
 * @param {*} props.sidebarItem - объект пункта меню верхнего уровня в SidebarItems
 */
function SidebarSubMenu(props){

    const classes = SidebarCSS();

    const[menuExpanded, setMenuExpanded] = useState(false);

    const handleClick = item => {
        if(props.focus || !menuExpanded){
            setMenuExpanded(!menuExpanded)
        }
        props.onClick(item)
    }

    return (
        props.item.text !== 'Дизайн' ?
        <div>
            <ListItem button 
                className={classes.listItemSmall}
                onClick={() => handleClick(props.item)}
            >
                {props.item.icon && <ListItemIcon className={classes.listIconClass} >{props.item.icon}</ListItemIcon>}
                <ListItemText className={classes.nestedMenuText} primary = {props.item.text} />
            </ListItem>
            <Collapse in={props.drawerOpen && menuExpanded} timeout="auto" unmountOnExit>
                <div> {props.item.folderTree && <FolderTree entity={props.item} menuExpanded={menuExpanded} folderItemType={props.item.folderItemType}/>}</div>
            </Collapse>
        </div> : null
        
    )
}

export default SidebarSubMenu;
