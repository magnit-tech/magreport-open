import React, { useState, useRef } from 'react';

// material-ui
import { Dialog, DialogContent, DialogActions, Button} from '@material-ui/core';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';

// local
import DataLoader from 'main/DataLoader/DataLoader';
import FolderContent from 'main/FolderContent/FolderContent';
import {DesignerCSS} from 'main/Main/Development/Designer/DesignerCSS';

// dataHub
import dataHub from 'ajax/DataHub';

import {folderItemTypeName, dataHubItemController} from 'main/FolderContent/FolderItemTypes';

/**
 * 
 * @param {*} props.itemType - тип выбираемого объекта
 * @param {*} props.defaultFolderId - папка по-умолчанию
 * @param {*} props.fullScreen - на весь экран
 * @param {*} props.onSubmit - function(folderId) - по выбору папки с данным folderId
 * @param {*} props.onClose - function()
 */
export default function CopyMoveFolderBrowser(props){

    const classes = DesignerCSS();
    const [folderData, setFolderData] = useState(null);
    const reload = useRef({needReload: true});
    const folderPath = useRef([null]);

    const title = "Перейдите в целевой каталог";
    const loadFunc = dataHubItemController(props.itemType).getFolder;
    const [loadParams, setLoadParams] = useState([props.defaultFolderId === undefined ? null : props.defaultFolderId]);

    function handleFolderClick(folderId){
        folderPath.current.push(folderId);
        setLoadParams([folderId]);
    }

    function handleParentFolderClick(){
        if(folderPath.current.length > 1){
            folderPath.current.pop();
            let folderId = folderPath.current.pop();
            handleFolderClick(folderId);
        }
    }

    return (
        <Dialog
            PaperProps={{ classes: {root: classes.dialogPaper}}}
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
                    onDataLoaded = {(data) => {setFolderData(data)}}
                    onDataLoadFailed = {(message) => {}}
                >
                    { folderPath.current.length > 1 &&
                        <div>
                            <Button onClick={handleParentFolderClick}                     
                                type="submit"
                                variant="outlined"
                                color="primary"
                                size="small"autoFocus
                                startIcon={<ArrowUpwardIcon/>}
                            >
                                Вверх
                            </Button>
                        </div>
                    }

                    <FolderContent
                        itemsType = {props.itemType}
                        data = {folderData}
                        showAddFolder = {false}
                        showAddItem = {false}
                        showFolderControls = {false}
                        showItemControls = {false}
                        onFolderClick = {(folderId) => {handleFolderClick(folderId)}}
                        onItemClick = {() => {}}
                    />
                </DataLoader>
            </DialogContent>

            <DialogActions className={classes.indent}>
                <Button onClick={() => {props.onSubmit(folderData.id)}}
                    type="submit"
                    variant="contained"
                    color="primary"
                    size="small"autoFocus
                >
                        Выбрать
                </Button>
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