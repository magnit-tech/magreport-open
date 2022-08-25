import React from 'react';
import {useState} from 'react';
import { connect } from 'react-redux';

// material-ui
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Paper from '@material-ui/core/Paper';
import Collapse from '@material-ui/core/Collapse';

//actions
import { foldersTreeToggle } from '../../../redux/actions/sidebar/actionFolderTree';
import actionSetSidebarItem from "../../../redux/actions/sidebar/actionSetSidebarItem";

// local
import FolderTree from './FolderTree/FolderTree';
import SidebarSubMenu from './SidebarSubMenu'

// css
import { SidebarCSS } from './SidebarCSS';

/**
 * 
 * @param {*} props.sidebarItem - объект пункта меню верхнего уровня в SidebarItems
 */
function SidebarTopLevelMenu(props){

    const classes = SidebarCSS();

    const[menuExpanded, setMenuExpanded] = useState(false);

    function handleClick(){
        if(!props.sidebarItem.subItems){
            props.actionSetSidebarItem(props.sidebarItem);
        }
    }

    function handleSubitemClick(item){
        props.actionSetSidebarItem(item);
    }

    function handleExpand(){
        if(props.sidebarItem.folderTree){
            foldersTreeToggle(props.sidebarItem.key, []);
        }
        if(props.sidebarItem.folderTree || props.sidebarItem.subItems){
            setMenuExpanded(!menuExpanded);
        }
        handleClick();
    }

    return (
        <React.Fragment>       
			<List className={classes.listClassMain}>
				<Paper key={props.sidebarItem.key} className={classes.paperRoot} elevation={5}>
					<ListItem 
						className={classes.listItem + ' ' + (props.currentSidebarItemKey === props.sidebarItem.key ? classes.folderListItemActive : null)} 
						onClick={(props.drawerOpen ? handleExpand : handleClick)} button key={props.sidebarItem.key}
					>
						{props.sidebarItem.icon && <ListItemIcon className={classes.listIconClass} >{props.sidebarItem.icon}</ListItemIcon>}
						<ListItemText primary={props.drawerOpen ? props.sidebarItem.text : ''} />
					</ListItem>
				</Paper>
			</List>
            <div>
				<Collapse in={props.drawerOpen && menuExpanded} timeout="auto" unmountOnExit>
					{props.sidebarItem.folderTree &&
						<FolderTree entity={props.sidebarItem} menuExpanded={menuExpanded} folderItemType={props.sidebarItem.folderItemType}/>
					}
					{props.sidebarItem.subItems && 
						<List>
							{
							Object.values(props.sidebarItem.subItems).map( item => (
								<SidebarSubMenu 
									key={item.key} 
									drawerOpen={props.drawerOpen} 
                                    item={item}
                                    focus={props.currentSidebarItemKey === item.key}
									onClick={handleSubitemClick}/>
							))}
						</List>
					}
				
				</Collapse>
			</div>
        </React.Fragment>
    );
}

const mapStateToProps = state => (
    {
        drawerOpen : state.drawer.open,
        currentSidebarItemKey : state.currentSidebarItemKey
    }
)

const mapDispatchToProps = {
    foldersTreeToggle,
    actionSetSidebarItem
}


export default connect(mapStateToProps, mapDispatchToProps)(SidebarTopLevelMenu);
