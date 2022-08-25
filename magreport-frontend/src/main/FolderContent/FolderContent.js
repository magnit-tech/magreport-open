import React, { useState } from 'react';

// dataHub
import dataHub from 'ajax/DataHub';

// local
import {FolderItemTypes} from './FolderItemTypes';
import FolderCard from './FolderCard';
import ItemCard from './ItemCard';
import AddButton from './AddButton';
import ItemWindow from './ItemWindow';
import FilterPanel from './FilterPanel';
import ElementsOnPageSelect from './ElementsOnPageSelect';
import RefreshButton from "./RefreshButton";
import SearchField from './SearchField';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';

// material-ui
//import { makeStyles} from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import { Grid } from '@material-ui/core';
import Pagination from '@material-ui/lab/Pagination';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';

//import CopyMoveFolderBrowser from './CopyMoveFolderBrowser';
import DesignerFolderBrowser from 'main/Main/Development/Designer/DesignerFolderBrowser';

// styles
import { FolderContentCSS } from './FolderContentCSS';
import SortModalWindow from './ModalWindows/SortModalWindow';

/**
 * @callback callback
 */
/**
 * @callback itemCallback
 * @param {Number} itemId
 */
/**
 * @callback reportCallback
 * @param {Number} jobIndex
 * @param {Number} jobId
 */
/**
 * @callback onFolderClick
 * @param {Number} folderId
 */
/**
 * @callback onReportRunClick
 * @param {Number} reportId
 * @param {Number} jobId
 */
/**
 * @callback onAddToFavorites
 * @param {Number} folderId
 * @param {Number} reportId
 */
/**
 * Компонент с содержимым папки
 * @param {Object}           props - свойства объекта
 * @param {Object}           props.data - объект data ответа от сервиса get-folder
 * @param {string}           props.itemsType - тип элементов папки
 * @param {boolean}          props.showItemControls - показывать ли элементы редактирования и удаления элемента (по умолчанию true)
 * @param {boolean}          props.showFolderControls - показывать ли элементы управления каталогом (по умолчанию true)
 * @param {boolean}          props.pagination - показывать ли постраничную навигацию
 * @param {boolean}          props.searchParams - параметры поиска
 * @param {onFolderClick}    props.onFolderClick - function(folderId) - действие при нажатии на папку
 * @param {itemCallback}     props.onItemClick - function(itemId) - действие при нажатии на объект
 * @param {callback}         props.onAddFolder - function() - действие при нажатии на кнопку "Добавить папку"
 * @param {callback}         props.onAddItemClick - function() - действие при нажатии на папку "Добавить объект"
 * @param {itemCallback}     props.onViewItemClick - действие при нажатии на кнопку просмотра элемента
 * @param {itemCallback}     props.onEditItemClick - function(itemId) - действие при нажатии на кнопку редактирования элемента
 * @param {itemCallback}     props.onDeleteItemClick - function(itemId) - действие при нажатии на кнопку удаления элемента
 * @param {onReportRunClick} props.onReportRunClick - function(reportId, jobId) - действие при нажатии на кнопку запуска отчёта по данному заданию
 * @param {reportCallback}   props.onJobCancelClick - function(jobIndex, jobId) - действие при нажатии на кнопку отмены задания
 * @param {onScheduleTaskSwitchClick} props.onScheduleTaskSwitchClick - function(taskIndex, taskId) - действие при нажатии на кнопку изменения статуса отчета на расписании
 * @param {reportCallback}   props.onSearchClick - действие при нажатии на кнопку поиска
 * @param {reportCallback}   props.onSortClick - действие при сортировке
 * @param {onAddToFavorites} props.onAddToFavorites - function(folderId, reportId) - действие при добавлении в избранное
 * @param {onAddToFavorites} props.onRefresh - function() - обновить отображение содержимого каталога
 * @param {Boolean}          props.showDialog - показать диалоговое окно
 * @param {Boolean}          props.contextAllowed - разрешено показывать контекстное меню (сортировка)
 * @param {Boolean}          props.copyAndMoveAllowed - разрешено показывать контекстное меню для каталогов и объектов
 * @param {Boolean}          props.notAllowedForItems - запрешено показывать контекстное меню для объектов
 */

export default function FolderContent(props){
    const classes = FolderContentCSS();

    const [searchOpen, setSearchOpen] = useState(false);
    const [panelOpen, setPanelOpen] = useState(false);

    const [openWindow, setOpenWindow] = useState(false);
    const [windowType, setWindowType] = useState('');
    const [windowData, setWindowData] = useState(null);
    const [page, setPage] = useState(1);
    const [elementsOnPage, setElementsOnPage] = useState(10)
    const [countElement, setCountElement] = useState(100)
    const [filterValues, setFilterValues] = useState(props.filters || {
        periodStart: null,
        periodEnd: null, 
        selectedStatuses: ['RUNNING','SCHEDULED','COMPLETE','FAILED','CANCELING','CANCELED'],
    })
    const [contextPosition, setContextPosition] = useState({
        mouseX: null,
        mouseY: null,
    });

    const [contextMenu, setContextMenu] = useState([]);

    /* Выделение */
    const [selectedItems, setSelectedItems] = useState(new Set());
    const [selectedFolders, setSelectedFolders] = useState(new Set());

    /* Копирование и перемещение */
    
    const [showCopyMoveDialogue, setShowCopyMoveDialogue] = useState('none');
    const [showContextMenu, setShowContextMenu] = useState('');


    const handleContextClick = (event) => {
        
        if(props.contextAllowed){        
            event.preventDefault();
        
            setContextPosition({
                mouseX: event.clientX - 2,
                mouseY: event.clientY - 4,
            });

            setShowContextMenu('sort')
        }
    };

    const handleSortClick = (sortParams) => {
        if(sortParams) {
            props.onSortClick(sortParams)
            handleContextClose()
        }
    };

    const handleContextClickObject = (event, objectType, id) => {
        if (props.copyAndMoveAllowed) {
            
            if (objectType === 'item' && props.copyAndMoveAllowedForReport) {
                return false
            } else {
                event.stopPropagation();
            
                if(objectType === 'folder'){
    
                    if (hasRWRight){
                        let newSelectedFolders = new Set(selectedFolders);
                        newSelectedFolders.add(id);
                        setSelectedFolders(newSelectedFolders);
    
                        let contextItems = [];
    
                        if (!props.copyAndMoveAllowedForReport) {
                            contextItems.push(<MenuItem onClick={handleShowCopyDialogue} key="copy"> Копировать </MenuItem>);
                        }
    
                        if (hasRWRight){
                            contextItems.push(<MenuItem onClick={handleShowMoveDialogue} key="move"> Переместить </MenuItem>);
                        }
    
                        setContextMenu(contextItems);
    
                    } else {
                        return false
                    }
                }
                else {
                    let newSelectedItems = new Set(selectedItems);
                    newSelectedItems.add(id);
                    setSelectedItems(newSelectedItems);
    
                    let contextItems = [];
                    contextItems.push(<MenuItem onClick={handleShowCopyDialogue} key="copy"> Копировать </MenuItem>);
                    if (hasRWRight){
                        contextItems.push(<MenuItem onClick={handleShowMoveDialogue} key="move"> Переместить </MenuItem>);
                    }
                    setContextMenu(contextItems);
                }
    
                event.preventDefault();
            
                setContextPosition({
                    mouseX: event.clientX - 2,
                    mouseY: event.clientY - 4,
                });
    
                setShowContextMenu('copy&move')
            }
        }
    };

    const handleShowMoveDialogue = () => {
        handleContextClose(false);
        setShowCopyMoveDialogue('move');
    }

    const handleShowCopyDialogue = () => {
        handleContextClose(false);
        setShowCopyMoveDialogue('copy');
    }    
  
    const handleContextClose = (clearSelections) => {
        if(clearSelections){
            setSelectedFolders(new Set());
            setSelectedItems(new Set());
        }
        setContextPosition({
            mouseX: null,
            mouseY: null,
        });
        setShowContextMenu('')
    }

    const handleCopyMoveDialogueClose = () => {
        setShowCopyMoveDialogue('none');
        setSelectedFolders(new Set());
        setSelectedItems(new Set());
    }

    const handleCopyMoveSubmit = (folderId, action) => {

        let folders = Array.from(selectedFolders);
        let items = Array.from(selectedItems);

        const textForSnackbar = itemName.charAt(0).toUpperCase() + itemName.slice(1)

        if (folders.length > 0) {
            if(action === 'move') {
                props.onChangeParentFolder(props.itemsType, folders[0], folderId)
                handleCopyMoveDialogueClose()
            } else {
                props.onCopyFolder(props.itemsType, folderId, folders)
                handleCopyMoveDialogueClose()
            }
        } else {
            if(action === 'move') {
                props.onMoveFolderItem(props.itemsType, folderId, items, textForSnackbar)
                handleCopyMoveDialogueClose()
            } else {
                props.onCopyFolderItem(props.itemsType, folderId, items, textForSnackbar)
                handleCopyMoveDialogueClose()
            }
        }
    }

    const itemName =  props.itemsType === FolderItemTypes.report || props.itemsType === FolderItemTypes.reportsDev ? "отчёт"
                    : props.itemsType === FolderItemTypes.job || props.itemsType === FolderItemTypes.userJobs ? "задание"
                    : props.itemsType === FolderItemTypes.datasource ? "источник данных"
                    : props.itemsType === FolderItemTypes.dataset ? "набор данных"
                    : props.itemsType === FolderItemTypes.filterTemplate ? "шаблон фильтра"
                    : props.itemsType === FolderItemTypes.filterInstance ? "экземпляр фильтра"
                    : props.itemsType === FolderItemTypes.roles ? "роль"
                    : props.itemsType === FolderItemTypes.securityFilters ? "фильтр безопасности"
                    : props.itemsType === FolderItemTypes.schedules ? "расписание"
                    : props.itemsType === FolderItemTypes.scheduledReports ? "отчет на расписании"
                    : props.itemsType === FolderItemTypes.systemMailTemplates ? "шаблоны системных писем"
                    : props.itemsType === FolderItemTypes.scheduleTasks ? "отчет на расписании"
                    : props.itemsType === FolderItemTypes.theme ? "дизайн"
                    : "???";

    let gridFolders = [];
    let gridItems = [];

    let userInfo = dataHub.localCache.getUserInfo();

    let isAdmin = userInfo.isAdmin
    let isDeveloper = userInfo.isDeveloper
    let authority = props.data.authority

    let hasRWRight = isAdmin || (isDeveloper && authority === "WRITE");
    let canCreateFolder = hasRWRight;
    let canCreateItem = hasRWRight;

    let isSearched = props.searchParams === undefined ? false:
        props.searchParams.searchString && props.searchParams.isRecursive ? true:  false;

    /*
        Формируем карточки папок
    */
    //debugger
    let cntPages = 0
    let cntItems = 0
    let restItems = 0
    let folders = []
    if(props.data.childFolders){
        cntItems = props.data.childFolders.length
        folders = props.data.childFolders
        if (props.pagination && folders.length > 0){
            const startIndex = (page-1)*elementsOnPage
            const endIndex = page*elementsOnPage
            if (startIndex>=folders.length){
                restItems = elementsOnPage
                folders = []
            }
            else if (endIndex>=folders.length){
                restItems = endIndex-folders.length
                folders = folders.slice(startIndex, folders.length)
            }
            else {
                folders = folders.slice(startIndex, endIndex)
            }
        }
        else {
            if (countElement >= folders.length){
                restItems = countElement - folders.length
                folders = folders.slice(0, folders.length)
            }
            else {
                folders = folders.slice(0, countElement)
            }
        }
        for(let f of folders){
            let hasRWRightChild = isAdmin || (f.authority === "WRITE" && isDeveloper);

            gridFolders.push(
                <Grid item key={f.id} className = {classes.gridItem}>
                    <FolderCard
                        data = {f}
                        isSearched = {isSearched}
                        isAdmin = {isAdmin && !(props.showFolderControls === false)}
                        canCreateFolder = {hasRWRightChild}
                        itemsType = {props.itemsType}
                        onGrantsClick={() => {handleGrantstFolderWindow(f.id)}}
                        onClick = {() => {props.onFolderClick(f.id, f.name)}}
                        onEditClick = {() => {handleEditFolderWindow(f.id, f.name, f.description)}}
                        onDeleteClick = {() => {props.onDeleteFolderClick(f.id)}}
                        onContextMenu={handleContextClickObject} 
                    />
                </Grid>
            );
        }
    }

    /*
        Формируем карточки объектов
    */

    let items = props.itemsType === FolderItemTypes.report ? props.data.reports :
                props.itemsType === FolderItemTypes.favorites ? props.data.reports :
                props.itemsType === FolderItemTypes.job || props.itemsType === FolderItemTypes.userJobs ? props.data.jobs :
                props.itemsType === FolderItemTypes.datasource ? props.data.dataSources :
                props.itemsType === FolderItemTypes.dataset ? props.data.dataSets :
                props.itemsType === FolderItemTypes.filterTemplate ? props.data.filterTemplates :
                props.itemsType === FolderItemTypes.filterInstance ? props.data.filterInstances :
                props.itemsType === FolderItemTypes.reportsDev ? props.data.reports :
                props.itemsType === FolderItemTypes.roles ? props.data.roles :
                props.itemsType === FolderItemTypes.securityFilters ? props.data.securityFilters :
                props.itemsType === FolderItemTypes.schedules ? props.data.schedules :
                props.itemsType === FolderItemTypes.systemMailTemplates ? props.data.systemMailTemplates:
                props.itemsType === FolderItemTypes.scheduleTasks ? props.data.scheduleTasks :
                props.itemsType === FolderItemTypes.theme ? props.data.themes :
                [];


    if(items){
        cntItems += items.length
        if (props.pagination){
            if (folders.length && restItems === 0){
                items = []
            }
            else if (folders.length && restItems < elementsOnPage){
                items = items.slice(0, restItems)
            }
            else {
                let foldersLength = props.data.childFolders ? props.data.childFolders.length : 0
                let startIndex = (page-1)*elementsOnPage - foldersLength
                let endIndex = page*elementsOnPage - foldersLength
                items = items.slice(startIndex, endIndex)
            }
        }
        else {
            items = items.slice(0, restItems > countElement ? countElement : restItems)
        }

        items.forEach( (i, index) => {
            let keyParentId = -1
            let folderId = props.data.id
            if (i.path && i.path.length){
                keyParentId = i.path[i.path.length-1].id
                folderId = keyParentId
            }
            gridItems.push(
                <Grid item
                    className = {classes.gridItem}
                    key={`${keyParentId}_${i.id}`}
                    onClick = {() => {props.onItemClick(i.id)}}
                >
                    <ItemCard
                        itemType = {props.itemsType}
                        data = {i}
                        roleType={{isAdmin: isAdmin, isDeveloper: isDeveloper}}
                        isSearched = {isSearched}
                        editButton = {hasRWRight && !(props.showItemControls === false)}
                        deleteButton = {hasRWRight && !(props.showItemControls === false) && !(props.itemsType === FolderItemTypes.roles && props.data.name === 'SYSTEM')}
                        onViewButtonClick={() => props.onViewItemClick(i.id)}
                        onEditButtonClick = {() => {props.onEditItemClick(i.id)}}
                        onDeleteButtonClick = {() => {props.onDeleteItemClick(i.id)}}
                        onReportRunClick = {() => {props.onReportRunClick(i.report.id, i.id)}}
                        onScheduleTaskRunClick = {() => {props.onScheduleTaskRunClick(i.id)}}
                        onScheduleTaskSwitchClick = {() => {props.onScheduleTaskSwitchClick(index, i.id)}}
                        onJobCancelClick = {() => props.onJobCancelClick(index, i.id)}
                        onAddDeleteFavorites = {() => props.onAddDeleteFavorites(index, folderId, i.id, i.favorite)}
                        onLinkPathClick = {props.onFolderClick}
                        onDependenciesClick = {() => props.onDependenciesClick(i.id)}
                        showDialog = {props.showDialog}
                        onContextMenu={handleContextClickObject}
                        
                    />
                </Grid>

            );
        })
    }

    cntPages = cntItems%elementsOnPage === 0 ? Math.floor(cntItems/elementsOnPage) : Math.floor(cntItems/elementsOnPage+1)

    function handleAddFolder(){
        setWindowData(null)
        setWindowType('folder')
        setOpenWindow(true);
    }

    function handleEditFolderWindow(id, name, description){
        setWindowData({
            id, 
            name,
            description
        })
        setWindowType('folder')
        setOpenWindow(true);
    }

    function handleAddItemClick(){
        props.onAddItemClick();
    }

    function handleCloseFolderWindow(){
        setOpenWindow(false);
    }

    function handleSaveFolderWindow(itemsType, id, name, desc, ...args){
        setOpenWindow(false);
        if (itemsType === 'folder'){
            if (id){
                props.onEditFolderClick(id, name, desc);
            }
            else {
                props.onAddFolder(name, desc);
            }
        }
        else{
            if (id){
                props.onEditItemClick(id, props.data.id, name, desc, ...args);
            }
            else {
                props.onAddItemClick(props.data.id, name, desc, ...args);
            }
        }
    }

    function handleGrantstFolderWindow(id){
        setWindowData({
            folderId: id,
            itemsType:props.itemsType
        })
        setWindowType('grants')
        setOpenWindow(true);
    }

    function handlePageNumberChange(e, numPage){
        setPage(numPage);
    }

    function handleFilterChange(key,value){
        let newFilterValues = {...filterValues, [key]: value, isCleared: false}
        if (key === 'updatePeriod'){
            newFilterValues.periodStart = new Date(Date.now()-value*3600*1000)
            newFilterValues.periodEnd = new Date()
        }
        setFilterValues(newFilterValues)
    }

    function handleFilterClick(isCleared){
        let filters = filterValues
        if (isCleared){
            filters = {
                periodStart: null,
                periodEnd: null,
                selectedStatuses: ['RUNNING','SCHEDULED','COMPLETE','FAILED','CANCELING','CANCELED'],
                isCleared: true
            }
            setFilterValues(filters)
        }
        setPage(1)
        props.onFilterClick(filters)
    }

    function handleScroll(e){
        if (e.target.offsetHeight + e.target.scrollTop === e.target.scrollHeight) {
            setCountElement(prev => prev + 100)
        }
    }

    return(
        <div className={classes.relative}>

            {/*props.searchParams &&
				<SearchField 
					searchParams={props.searchParams}
                    onSearchClick={props.onSearchClick}
                    searchOpen = {searchOpen}
                    setSearchOpen = {()=> setSearchOpen(!searchOpen)}
				/>*/
			}

            <Menu
                keepMounted
                open={showContextMenu === 'copy&move'}
                onClose={handleContextClose}
                anchorReference="anchorPosition"
                anchorPosition={
                contextPosition.mouseY !== null && contextPosition.mouseX !== null
                    ? { top: contextPosition.mouseY, left: contextPosition.mouseX }
                    : undefined
                }
            >   
                {contextMenu}
            </Menu>

            {props.sortParams && 
                <SortModalWindow
                    keepMounted
                    open={showContextMenu === 'sort'}
                    onClose={handleContextClose}
                    anchorReference="anchorPosition"
                    anchorPosition={
                    contextPosition.mouseY !== null && contextPosition.mouseX !== null
                        ? { top: contextPosition.mouseY, left: contextPosition.mouseX }
                        : undefined
                    }
                    sortParams = {props.sortParams}
                    onSortClick = {sortParams => handleSortClick(sortParams)}
                />
            }

            {props.searchParams && !searchOpen &&

                <Paper elevation={3} className={classes.openSearchBtn}>
                    <IconButton
                        size="small"
                        aria-label="searchBtn"
                        onClick= {()=> setSearchOpen(!searchOpen)}
                    >
                        <SearchIcon color={props.searchParams && props.searchParams.searchString ? "secondary" : "inherit"}/>
                    </IconButton>
                </Paper>
            }
       
            { (props.itemsType === FolderItemTypes.job || props.itemsType === FolderItemTypes.userJobs) &&
                <FilterPanel
                    itemsType={props.itemsType}
                    filters={filterValues}
                    onFilterChange={handleFilterChange}
                    onFilterClick={handleFilterClick}
                    panelOpen={panelOpen}
                    setPanelOpen={()=> setPanelOpen(!panelOpen)}
                />
            }

            { props.pagination && cntPages > 0 &&
                <div elevation={0} className={classes.divPagination}>
                    <Pagination
                        count={cntPages} 
                        variant="outlined" 
                        shape="rounded" 
                        page={page} 
                        onChange={handlePageNumberChange}
                    /> 
                    <ElementsOnPageSelect 
                        elementsOnPage={elementsOnPage}
                        onChange={value => {setPage(1); setElementsOnPage(value)}}
                    />
                </div>
            }

            <div style={{display: 'block'}}>
            <ItemWindow 
                isOpen={openWindow}
                type={windowType}
                data={windowData}
                onClose={handleCloseFolderWindow}
                onSave={handleSaveFolderWindow}
            />
            </div>
            <div className={classes.gridContentRelative}>
                <div className={classes.gridContentAbsolute} 
                    onScroll={handleScroll} 
                    onContextMenu={handleContextClick}
                >
                    {props.searchParams &&
				        <SearchField 
					        searchParams={props.searchParams}
                            onSearchClick={props.onSearchClick}
                            searchOpen = {searchOpen}
                            setSearchOpen = {()=> setSearchOpen(!searchOpen)}
                            searchWithoutRecursive = {props.searchWithoutRecursive}
				        />
			        }
					<Grid container>
						{gridFolders}
					</Grid>
					<Grid container>
						{gridItems}
					</Grid>
				</div>
            </div>
            {
                (props.itemsType === FolderItemTypes.report || props.itemsType === FolderItemTypes.dataset || 
                props.itemsType === FolderItemTypes.datasource || props.itemsType === FolderItemTypes.filterTemplate ||
                props.itemsType === FolderItemTypes.filterInstance || (props.itemsType === FolderItemTypes.roles && props.data.name !== 'SYSTEM') ||
                props.itemsType === FolderItemTypes.reportsDev || props.itemsType === FolderItemTypes.securityFilters ||
                props.itemsType === FolderItemTypes.schedules||props.itemsType === FolderItemTypes.scheduleTasks||
                props.itemsType === FolderItemTypes.theme) &&
                (canCreateFolder || canCreateItem) && 
                (props.showAddFolder  || props.showAddItem) && 
                (props.itemsType !== FolderItemTypes.roles || props.data.id !== null) &&
                
                <AddButton
                    showCreateFolder = {canCreateFolder && props.showAddFolder}
                    showCreateItem = {canCreateItem && props.showAddItem && props.data.id !== null}
                    itemName = {itemName}
                    onAddFolder = {handleAddFolder}
                    onAddItemClick = {handleAddItemClick}
                />
            }
            {   (props.itemsType === FolderItemTypes.userJobs || props.itemsType === FolderItemTypes.job) && <RefreshButton onRefreshClick={props.onRefreshClick} />}

            {
                <DesignerFolderBrowser
                    itemType = {props.itemsType}
                    dialogOpen = {showCopyMoveDialogue !== 'none'}
                    onSubmit = {(folderId) => {handleCopyMoveSubmit(folderId, showCopyMoveDialogue)}}
                    onClose = {handleCopyMoveDialogueClose}
                    sortParams = {props.sortParams}
                />
            }
            
        </div>
    );
}