import React, { useEffect } from 'react';
import { connect } from 'react-redux';

// components
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import FolderIcon from '@material-ui/icons/Folder';
import { Collapse } from '@material-ui/core';
import LinearProgress from '@material-ui/core/LinearProgress';
import ReportProblemIcon from '@material-ui/icons/ReportProblem';

// local
import DragAndDrop from "main/DragAndDrop/DragAndDrop";

// actions
import { actionFolderClick, actionChangeParentFolderTree } from 'redux/actions/menuViews/folderActions';    
import { foldersLoading, foldersTreeToggle, foldersSetInit } from 'redux/actions/sidebar/actionFolderTree';
import actionSetSidebarItem from 'redux/actions/sidebar/actionSetSidebarItem';

// styles
import { FolderTreeCSS } from './FolderTreeCSS'

// dataHub
import dataHub from 'ajax/DataHub';

function FolderTree(props){

    const classes = FolderTreeCSS();

    const entity = props.entity;

    const foldersTreeEntity = props.foldersTreeEntity[props.entity.key]

    const isAdmin = dataHub.localCache.getUserInfo().isAdmin

    const ROOT_FOLDER_ID = null;

    useEffect(() => {
        // вызывается при размонтировании компонента, нужно чтобы если в дереве нет данных при следующем развороте заново началась загрузка данных с бэка
        return () => {
            props.foldersSetInit(entity.key)
        }
    }, []) // eslint-disable-line

    if (foldersTreeEntity.status === 'init' && props.menuExpanded){
        props.foldersLoading(ROOT_FOLDER_ID, entity.key, []);
    }

    function handleFolderIconClick(folderId, foldersPath, expanded) {
        if (expanded !== undefined){
            props.foldersTreeToggle(folderId, entity.key, foldersPath);
        }
        else {
            props.foldersLoading(folderId, entity.key, foldersPath);
        }
    }

    function handleFolderClick(folderId){
        props.actionSetSidebarItem(entity)
        props.actionFolderClick(props.folderItemType, folderId)
    }

    function handleDropBefore(e, parentFolderId, folderId) {
        const parentFolderData = dataHub.localCache.getFolderData(props.folderItemType, parentFolderId)

        let inRoot = false
        if (parentFolderData.parentId === null){
            inRoot = true
        }
        props.actionChangeParentFolderTree(props.folderItemType, parseInt(folderId, 10), parentFolderId, inRoot)
    }

    function handleDropInto(e, parentFolderId, folderId) {
        props.actionChangeParentFolderTree(props.folderItemType, parseInt(folderId, 10), parentFolderId, false)
    }

    function isParentFolder(idIncoming, parentFolders) {

        let isFound = true

        for (let i in parentFolders){
            if (parentFolders[i].id === idIncoming){
                isFound = false
                break
            }
        }
        
        return isFound
    }

    /**
     * Отрисовываем иерархию папок отчетов
     * 
     * @param {object[]} data             - массив childFolders корневой папки с отчетами
     * @param {object[]} parentFolders    - для первого запуска передаем []. Для рекурсивных запусков передаем массив родительских элементов
     *                                      (параметры такие же, как у navigationPath)
     * @returns {object}                  - иерархия JSX-тегов
     */
    function getFoldersTree(data, folderId, parentFolders) {
        let folderTree = [];
        const isArray = Array.isArray(data)

        data.forEach(x => {
            if (isArray){
                x = foldersTreeEntity.folders.get(x)
            }
            if (x) {
                if (x.parentId === folderId){
                    let foldersPath = parentFolders.slice();
                    foldersPath.push(x.id);
    
                    folderTree.push(
                        <div key={folderTree.length}>
                            <div style = {{marginLeft: 32 * parentFolders.length}}>
                            <DragAndDrop 
                                key={`${x.parentId}_${x.id}_${x.childFolders.join('')}`}
                                draggable={isAdmin} 
                                dropInto={isAdmin} 
                                dropBefore={isAdmin} 
                                dropVirtual={true} 
                                id={x.id} 
                                groupId={100} 
                                droppableGroups={["100"]} 
                                onDropBefore={handleDropBefore} 
                                onDropInto={handleDropInto}
                                isDropAllowedExt={(e, id, idIncoming) => isParentFolder(idIncoming,parentFolders)}
                            >
                                <ListItem className={classes.listItemSmall} onClick={()=>{ handleFolderClick(x.id) }} button key={x.id}>
                                    <ListItemIcon
                                        className={classes.listExpandClass} 
                                        onClick={ (e) => { e.stopPropagation(); handleFolderIconClick(x.id, foldersPath, x.expanded)}}
                                    >
                                        { (x.expanded) 
                                        ? <ArrowDropDownIcon/> 
                                        : <ArrowRightIcon/> 
                                        }
                                    </ListItemIcon>
                                    <ListItemIcon  size = "small" className={classes.folderIcon}><FolderIcon/>  </ListItemIcon>
                                    <ListItemText primary={x.name} />
                                    <ListItemText>
                                        {(foldersTreeEntity.status === 'startLoading' &&  foldersTreeEntity.folderId === x.id) || x.childMove ? 
                                        <div className={classes.loaderCenter}>
                                            <LinearProgress />
                                        </div> :
                                        null
                                        }
                                    </ListItemText>
                                </ListItem>
                            </DragAndDrop>
                            </div>
                            { x.childFolders && x.childFolders.length > 0
                                ?   <Collapse in={x.expanded} timeout="auto" unmountOnExit>
                                        <List className={classes.listClass}>
                                            { getFoldersTree(x.childFolders, x.id, foldersPath) }
                                        </List>
                                    </Collapse>
                                : ''
                            }
                        </div>
                    );
                }
            }
        })
        return folderTree;
    }

    if (foldersTreeEntity.status === 'loaded' || foldersTreeEntity.status === 'startLoading'){
        const f = foldersTreeEntity.folders
        if (f && f.size > 0 && f.get("root").childFolders.length > 0) {
            return (
                <List className={classes.listClass}>
                    <Collapse in={props.drawerOpen && props.menuExpanded} timeout="auto" unmountOnExit>
                        <div className={classes.divReportChild}>
                            <List className={classes.listClass}>
                                {getFoldersTree(foldersTreeEntity.folders, 'root', [])}
                            </List>
                        </div>
                    </Collapse>
                </List>
            );
        }
        else {
            return  <div className={classes.noFoldersBlock}>
                        <ReportProblemIcon className={classes.noFoldersBlockItem}/>Каталогов нет
                    </div>
            }
    }
    else {
        return null;
    }
    
};


const mapStateToProps = state => {
    return {
        foldersTreeEntity: state.folderTree,
        drawerOpen: state.drawer.open
    }
}

const mapDispatchToProps = {
    actionFolderClick, 
    actionChangeParentFolderTree,
    foldersLoading, 
    foldersTreeToggle, 
    foldersSetInit,
    actionSetSidebarItem,
}

export default connect(mapStateToProps, mapDispatchToProps)(FolderTree);