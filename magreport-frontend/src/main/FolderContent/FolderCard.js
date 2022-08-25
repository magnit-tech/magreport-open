import React from 'react';
// material-ui
//import { ThemeProvider } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardActionArea from '@material-ui/core/CardActionArea'
import Typography from '@material-ui/core/Typography';
import FolderIcon from '@material-ui/icons/Folder';
import IconButton from '@material-ui/core/IconButton';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
// local
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
//styles
import { FolderCardCSS } from './FolderContentCSS'

/**
 * @callback onClick
 */

/**
 * @callback onEditClick
 */

/**
 * @callback onGrantsClick
 */

/**
 * @callback onDeleteClick
 */

/**
 * Компонент-карточка папки
 * @param {Object} props - свойства компонента
 * @param {Object} props.data - данные папки для компонента
 * @param {String} props.itemsType - тип объекта из FolderItemTypes
 * @param {onClick} props.onClick - callback, вызываемый при нажатии на карточку
 * @param {onEditClick} props.onEditClick - callback, вызываемый при нажатии пункта меню "Редактировать"
 * @param {onGrantsClick} props.onGrantsClick - callback, вызываемый при нажатии пункта меню "Выдать права"
 * @param {onDeleteClick} props.onDeleteClick - callback, вызываемый при нажатии пункта меню "Удалить"
 * @param {boolean} [props.isSearched=false] - если true, то в папке можно выполнять поиск
 * @param {boolean} [props.canCreateFolder=false] - если true, то в папке можно создавать подпапки
 * @param {boolean} [props.isAdmin] - если true, то текущи пользователь - администратор
 * @param {onContextMenu} props.onContextMenu - callback, действие при вызове контекстного меню
 * @return {JSX.Element}
 * @constructor
 */
function FolderCard(props){

    const data = props.data;
    const [anchorEl, setAnchorEl] = React.useState(null);

    const isEditable = props.itemsType !== FolderItemTypes.filterTemplate;

    function handleClickCardActionArea(){
        props.onClick();
    }

    function handleMenuClick(event){
        setAnchorEl(event.currentTarget);
    };
    
    function handleMenuClose(){
        setAnchorEl(null);
    };

    function handleEditFolderClick(){
        setAnchorEl(null);
        props.onEditClick()
    }

    function handleShowRoleWindow(){
        setAnchorEl(null);
        props.onGrantsClick()
    }

    function handleDeleteFolderClick(answer){
        setAnchorEl(null);
        props.onDeleteClick()
    }

    const classes = FolderCardCSS();
    return(
        <Card className={classes.card} 
            elevation ={5}
            onContextMenu={(event) => {props.onContextMenu(event, 'folder', props.data.id)}}
        >
            <CardActionArea onClick={handleClickCardActionArea}>                
                <div className={classes.detail}>
                    <FolderIcon fontSize="large" className={classes.folderIcon}/>
                    <CardContent className={classes.content}>
                        <Typography color="textPrimary" variant="subtitle2"  align="left">
                            {data.name}
                        </Typography>                
                        <Typography color="textSecondary" variant="caption" align= "left">
                            {data.description} (id: {data.id})
                        </Typography>
                        { props.isSearched &&
                            <Typography color="textSecondary" variant="caption" align= "left" style= {{display: 'flex'}}>
                                {props.data.path && `Путь: / ${props.data.path.map(p => p.name).join(' / ')}` }
                            </Typography>
                        }         
                    </CardContent>                      
                </div>   
            </CardActionArea>
            { props.canCreateFolder && props.itemsType !== FolderItemTypes.roles ? 
            <CardActions disableSpacing>
                <IconButton aria-label="Дополнительно" onClick={handleMenuClick}>
                    <MoreVertIcon />
                </IconButton>
                <Menu
                    id="folderMenu"
                    anchorEl={anchorEl}
                    keepMounted
                    open={Boolean(anchorEl)}
                    onClose={handleMenuClose}
                >
                    { isEditable ? <MenuItem onClick={handleEditFolderClick}>Изменить</MenuItem> : null }
                    { props.isAdmin ? <MenuItem onClick={handleShowRoleWindow}>Выдать права</MenuItem> : null }
                    { isEditable ? <MenuItem onClick={handleDeleteFolderClick}>Удалить</MenuItem> : null }
                </Menu>
            </CardActions>: null}
        </Card>
    );                    
}

function areEqual(prevProps, nextProps){
    return true
}

export default React.memo(FolderCard, areEqual)