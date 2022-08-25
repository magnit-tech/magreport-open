import React, { useState, useRef, useEffect } from 'react';

// material-ui
import { Dialog, DialogContent, DialogActions, Button} from '@material-ui/core';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import Breadcrumbs from '@material-ui/core/Breadcrumbs';
import Link from '@material-ui/core/Link';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import WarningIcon from '@material-ui/icons/Warning';
// local
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';
import {DesignerCSS} from './DesignerCSS';

// dataHub
import dataHub from 'ajax/DataHub';

import {folderItemTypesName, dataHubItemController} from 'main/FolderContent/FolderItemTypes';

/**
 * 
 * @param {*} props.itemType - тип выбираемого объекта
 * @param {*} props.defaultFolderId - папка по-умолчанию
 * @param {*} props.fullScreen - на весь экран
 * @param {*} props.onChange - function(itemId, item, folderId)
 * @param {*} props.onCLose - function()
 * @param {*} props.onSubmit - function() Вызываетс при нажатии на кнопку "Выбрать"
 */
export default function DesignerFolderBrowser(props){
    
    const classes = DesignerCSS();
    const [folderData, setFolderData] = useState(null);
    const reload = useRef({needReload: false});

    let itemName = folderItemTypesName(props.itemType);
    const title = props.onSubmit ? "Перейдите в целевой каталог" : "Выберите " + itemName ;
    const folderPath = useRef([{id: null, name: itemName[0].toUpperCase() + itemName.slice(1) }]) ;
    const loadFunc = dataHubItemController(props.itemType).getFolder;
    const [loadParams, setLoadParams] = useState([props.defaultFolderId === undefined ? null : props.defaultFolderId]);
    
    const sortParams = props.sortParams;

    useEffect(() => {
        setLoadParams([props.defaultFolderId === undefined ? null : props.defaultFolderId])
        folderPath.current = [{id: null, name: itemName[0].toUpperCase() + itemName.slice(1) }]
    }, [itemName]) // eslint-disable-line


    function handleSortData(data){

        let childFolders = data.childFolders,
            reports = data.reports;

        if (sortParams && (childFolders || reports)) {
            if (childFolders && childFolders.length > 0) {
                childFolders.sort((a, b) => {
                    if (a[sortParams.key] < b[sortParams.key]) {
                        return sortParams.direction === 'ascending' ? -1 : 1;
                    }
                    if (a[sortParams.key] > b[sortParams.key]) {
                        return sortParams.direction === 'ascending' ? 1 : -1;
                    }
                        return 0;
                });
            }

            if (reports && reports.length > 0) {
                reports.sort((a, b) => {
                    if (a[sortParams.key] < b[sortParams.key]) {
                        return sortParams.direction === 'ascending' ? -1 : 1;
                    }
                    if (a[sortParams.key] > b[sortParams.key]) {
                        return sortParams.direction === 'ascending' ? 1 : -1;
                    }
                        return 0;
                });
            }

            setFolderData({...data, childFolders, reports})
        } else {
            setFolderData(data);
        }
    }

    function handleFolderClick(folderId, folderName){
        folderPath.current.push({id: folderId, name: folderName});
        setLoadParams([folderId]);
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
    }

    let breadcrumbs = [];

    folderPath.current.forEach((item, index, array) =>{
        let len = array.length;
        breadcrumbs.push(
            <Link 
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
        <Dialog
            PaperProps={{ classes: {root: classes.dialogPaper}}}
           // className={classes.dialogPaper}
            open = {props.dialogOpen}
            onClose = {props.onClose}
            fullWidth
            fullScreen={props.fullScreen}
            maxWidth="xl"
        >
            <Toolbar position="fixed" elevation={5} className={classes.filterTitle}  variant="dense">
                <Typography variant="h6" /*className={classes.paramText}*/>{title} </Typography>
            </Toolbar>

            <DialogContent  style={{display: 'flex'}}>
                <DataLoader
                    loadFunc = {loadFunc}
                    loadParams = {loadParams}
                    reload = {reload.current}
                    onDataLoaded = {(data) => {handleSortData(data)}}
                    onDataLoadFailed = {(message) => {}}
                >
                    { folderPath.current.length > 1 &&
                        <div className={classes.path}>
                            <Tooltip title = {'Вверх'}>
                                <IconButton
                                    className={classes.upBtn}
                                    size="small"
                                    onClick={handleParentFolderClick}
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
                        data = {folderData}
                        showAddFolder = {false}
                        showAddItem = {false}
                        showFolderControls = {false}
                        showItemControls = {false}
                        onFolderClick = {(folderId, folderName) => {handleFolderClick(folderId, folderName)}}
                        onItemClick = {props.onSubmit ? ()=>{} : (itemId) => {handleItemClick(itemId)}}
                    />
                </DataLoader>
            </DialogContent>

            <DialogActions className={classes.indent}>
                {props.onSubmit &&
                    <div style={{display: 'flex'}}> 
                        {folderData?.authority !== 'WRITE' &&
                        <div style={{display: 'flex'}}> 
                            <WarningIcon className={classes.warning}/>
                            <Typography className={classes.warningText}> Нет прав на запись в папку</Typography>
                
                        </div>
                        }
                        <Button onClick={() => {props.onSubmit(folderData.id)}}
                            disabled = {folderData?.authority === 'WRITE' ? false: true}
                            type="submit"
                            variant="contained"
                            color="primary"
                            size="small"autoFocus
                        >
                            Выбрать
                        </Button>
                    </div>
                }
                <Button onClick={props.onClose}                     
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"autoFocus
                >
                        Отменить
                </Button>
            </DialogActions>
        </Dialog>
    )
};