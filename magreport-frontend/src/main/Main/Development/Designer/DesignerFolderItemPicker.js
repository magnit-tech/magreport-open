import React, { useState, useRef } from 'react';
import { connect } from 'react-redux';
import clsx from 'clsx'

// material-ui
import { TextField, Dialog, DialogContent, DialogActions, Button} from '@material-ui/core';
import FolderOpenIcon from '@material-ui/icons/FolderOpen';
import InputAdornment from '@material-ui/core/InputAdornment';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import Link from '@material-ui/core/Link';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';

// dataHub
import dataHub from 'ajax/DataHub';

// local
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';
import {folderItemTypeName, dataHubItemController, FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

// actions 
import {actionFolderLoaded, actionFolderLoadFailed, actionFolderClick, actionSearchClick} from 'redux/actions/menuViews/folderActions';


// styles
import {DesignerCSS} from './DesignerCSS';

/**
 * @callback itemPickerOnChange
 * @param {number} itemId
 * @param {Object} item
 * @param {number} folderId
 */

/**
 * @callback actionFolderClick
 * @param {string} itemType
 * @param {number} folderId
 * @param {boolean} [isFolderItemPicker=false]
 */

/**
 * @callback actionFolderLoaded
 * @param {string} itemType
 * @param {*} data
 * @param {boolean} [isFolderItemPicker=false]
 */

/**
 * @callback actionFolderLoadFailed
 * @param {string} itemType
 * @param {string} message
 */

/**
 * @callback actionSearchClick
 * @param {string} itemType
 * @param {number} currentFolderId
 * @param {*} searchParams
 */

/**
 * Компонент выбора связанного объекта
 * @param {Object} props - свойства компонента
 * @param {Object} props.state - состояние из соответствующего MenuView
 * @param {string} props.label - название поля
 * @param {string} props.value - отображаемое название
 * @param {string} props.itemType - тип выбираемого объекта из FolderItemTypes
 * @param {number} props.defaultFolderId - папка по-умолчанию
 * @param {boolean} [props.error=false] - признак ошибки задания поля
 * @param {boolean} [props.disabled=false] - признак недоступности поля
 * @param {itemPickerOnChange} props.onChange - function(itemId, item, folderId)
 * @param {actionFolderClick} props.actionFolderClick - callback, вызываемый при нажатии на карточку папки
 * @param {actionFolderLoaded} props.actionFolderLoaded - callback, вызываемый при успешной загрузке содержимого папки
 * @param {actionFolderLoadFailed} props.actionFolderLoadFailed - callback, вызываемый при ошибке загрузки содержимого папки
 * @param {actionSearchClick} props.actionSearchClick - callback, вызываемый при нажатии на кнопку поиска
 */
function DesignerFolderItemPicker(props){

    const classes = DesignerCSS();

    const [dialogOpen, setDialogOpen] = useState(false);
    const [loadParams, setLoadParams] = useState(null);
    const reload = useRef({needReload: true});

    let itemName = folderItemTypeName(props.itemType);
    const title = "Выберите " + itemName;
    const folderPath = useRef([{id: null, name:  itemName[0].toUpperCase() + itemName.slice(1) }]) ;

    const loadFunc = dataHubItemController(props.itemType).getFolder;

    function handleFieldClick(){
        reload.current = {needReload: true};
        setDialogOpen(true);
    }

    function handleClose(){
        setDialogOpen(false);
    }

    function handleFolderClick(folderId, folderName){
        folderPath.current.push({id: folderId, name: folderName});
        props.actionFolderClick(props.itemType, folderId, true);
        setLoadParams(folderId)
    }

    function handleParentFolderClick(){
        if(folderPath.current.length > 1){
            folderPath.current.pop();
            let folder = folderPath.current.pop();
            handleFolderClick(folder.id, folder.name);
        }
    }

    function handleBreadcrumbClick(index, length, id, name){
        folderPath.current.splice(index, length-index)
        handleFolderClick(id, name);
    }

    function handleItemClick(itemId){
        let item = dataHub.localCache.getItemData(props.itemType, itemId);
        props.onChange(itemId, item, folderPath.current[folderPath.current.length - 1]);
        handleClose();
    }

    let breadcrumbs = [];

    folderPath.current.forEach((item, index, array) =>{
        let len = array.length;
        breadcrumbs.push(
            <Link 
                key = {index}
                style = {{cursor: 'pointer'}}
                id = {index}
                color = {len - 1  === index ? "textPrimary" : "inherit"}
                onClick={() => handleBreadcrumbClick( index, len, item.id, item.name)}
            >
                {item.name}
            </Link>
        )

    })

    return (
        <div>
            <TextField
                className = {clsx(classes.field, {[classes.displayBlock] : props.displayBlock})}
                style={{minWidth: props.minWidth}}
                placeholder = {props.label}
                label = {props.label}
                value = {props.value ? props.value : ''}
                variant = "outlined"
                InputProps = {{
                    readOnly : true,
                    disabled: false,
                    endAdornment:
                        <InputAdornment position='end' >
                            <IconButton
                                disabled = { props.disabled}
                                aria-label='openFolders'
                                onClick = {handleFieldClick}
                            >
                                <FolderOpenIcon />
                            </IconButton>
                        </InputAdornment>
                                    
                }}
                InputLabelProps={{ 
                    shrink: props.value !== null || dialogOpen,
                }}
                fullWidth = {props.fullWidth}
                error = {props.error}
                disabled={props.disabled}
               // onFocus={(e) => e.target.select()}
            />

            <Dialog
                PaperProps={{ classes: {root: classes.dialogPaper}}}
                open = {dialogOpen}
                onClose = {handleClose}
                fullWidth
                maxWidth="xl"
            >
                <Toolbar position="fixed" elevation={5} className={classes.filterTitle}  variant="dense">
                    <Typography variant="h6" /*className={classes.paramText}*/>{title} </Typography>
                </Toolbar>

                <DialogContent style={{display: 'flex'}}>
                    <DataLoader
                        loadFunc = {loadFunc}
                        loadParams = {[loadParams] /*[props.state.currentFolderId]*/}
                        reload = {reload.current}
                        onDataLoaded = {data => props.actionFolderLoaded(props.itemType, data, true, true)}
                        onDataLoadFailed = {message => props.actionFolderLoadFailed(props.itemType, message)}
                    >
                        { folderPath.current.length > 1 &&
                            <div className={classes.path}>
                                <Tooltip title = {'Вверх'}>
                                <IconButton
                                    className={classes.upBtn}
                                    size="small"
                                    onClick={ handleParentFolderClick}
                                >
                                    <ArrowUpwardIcon color="primary"/>
                                </IconButton>
                            </Tooltip>

                            <Breadcrumbs aria-label="breadcrumb">
                                {breadcrumbs}
                            </Breadcrumbs>
                            </div>
                        }
                        <FolderContent
                            itemsType = {props.itemType}
                            showItemControls={false}
                            data = {props.state.filteredFolderData ? props.state.filteredFolderData : props.state.currentFolderData}
                            searchParams = {props.state.searchParams || {}}
                            onFolderClick = {(folderId, folderName) => handleFolderClick(folderId, folderName)}
                            onItemClick = {itemId => handleItemClick(itemId)}
                            onSearchClick ={searchParams => props.actionSearchClick(props.itemType, props.state.currentFolderId, searchParams)}
                        />
                    </DataLoader>
                </DialogContent>

                <DialogActions className={classes.indent}>
                    <Button onClick={handleClose}                     
                        type="submit"
                        variant="contained"
                        color="primary"
                        size="small"
                        autoFocus
                    >
                        Отменить
                    </Button>
                </DialogActions>
            </Dialog>

        </div>
    )

}

const mapStateToProps = (state, props) => {
    const {itemType} = props
    let s = null
    if (itemType === FolderItemTypes.filterTemplate) s = state.filtersMenuView
    if (itemType === FolderItemTypes.filterInstance) s = state.filterInstancesMenuView
    if (itemType === FolderItemTypes.report) s = state.reportsMenuView
    if (itemType === FolderItemTypes.reportsDev) s = state.reportsDevMenuView
    if (itemType === FolderItemTypes.dataset) s = state.datasetsMenuView
    if (itemType === FolderItemTypes.datasource) s = state.datasourcesMenuView
    if (itemType === FolderItemTypes.roles) s = state.rolesMenuView
    if (itemType === FolderItemTypes.securityFilters) s = state.securityFiltersMenuView
    return {
        state: s
    }
}

const mapDispatchToProps = {
    actionFolderLoaded,
    actionFolderLoadFailed,
    actionFolderClick,
    actionSearchClick,
}

export default connect(mapStateToProps, mapDispatchToProps)(DesignerFolderItemPicker);
