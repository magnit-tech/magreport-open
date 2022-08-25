import React, {useState} from 'react';

// components 
import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import DeleteIcon from '@material-ui/icons/Delete';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import Typography from '@material-ui/core/Typography';
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import ArrowDownwardIcon from '@material-ui/icons/ArrowDownward';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ExpandLessIcon from '@material-ui/icons/ExpandLess';
import Paper from '@material-ui/core/Paper';
import ButtonGroup from '@material-ui/core/ButtonGroup';
import WarningIcon from '@material-ui/icons/Warning';
import Tooltip from '@material-ui/core/Tooltip';
import ReportIcon from '@material-ui/icons/Report';
import ReportOffIcon from '@material-ui/icons/ReportOff';

//local
import DesignerFolderBrowser from '../../Designer/DesignerFolderBrowser';
import DesignerSelectField from '../../Designer/DesignerSelectField';
import DesignerTextField from '../../Designer/DesignerTextField';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import ReportFiltersItem from './ReportFiltersItem'
import DragAndDrop from 'main/DragAndDrop/DragAndDrop'

// utils
import ReorderList from 'utils/ReorderList'

// styles 
import { ReportDevFiltersCSS } from '../../Designer/DesignerCSS'

/**
 * @callback onAddFilter
 * @param {Number} id
 * @param {Object} item
 * @param {Array} reportFields
 */
/**
 * @callback onAddChildGroup
 * @param {Number} id
 * @param {Number} reportId
 */
/**
 * @callback onChangeFiltersGroup
 * @param {Number} id
 * @param {Number} index
 * @param {String} type
 * @param {Object} value
 */
/**
 * @callback onChangeFilterField
 * @param {Number} id
 * @param {Number} startIndex
 * @param {Object} filterItem
 */
/**
 * @callback onChangeOrdinal
 * @param {Number} id
 * @param {Array} newElements
 */
/**
 * @callback onDelete
 * @param {Number} id
 * @param {Number} index
 * @param {String} type
 */
/**
 * @callback onMoveBefore
 * @param {Number} beforeId
 * @param {Number} entityId
 */
/**
 * @callback onMoveInto
 * @param {Number} targetId
 * @param {Number} entityId
 */
/**
 * Компонент редактирования группы фильтров в отчете
 * @param {Object} props - свойства компонента
 * @param {Number} props.level - уровень для вложенных групп
 * @param {Number} props.reportId - id отчёта
 * @param {Array} props.reportFields - массив полей отчёта
 * @param {Object} props.childGroupInfo - объект с информацией о группе фильтров
 * @param {Object} props.childGroupsMap - Map со всеми группами фильтров
 * @param {onAddFilter} props.onAddFilter - callback при добавлении фильтра
 * @param {onAddChildGroup} props.onAddChildGroup - callback при добавлении группы фильтров
 * @param {onDelete} props.onDelete - callback при удалении
 * @param {onChangeFiltersGroup} props.onChangeFiltersGroup - callback при изменении группы фильтров
 * @param {onChangeFilterField} props.onChangeFilterField - callback при изменении свзяи полей фильтра с полями отчета
 * @param {onChangeOrdinal} props.onChangeOrdinal - callback при изменении порядка фильтров
 * @param {onMoveBefore} props.onMoveBefore - callback при перетаскивании элемента перед другим элементом
 * @param {onMoveInto} props.onMoveInto - callback при перетаскивании элемента внутрь другого элемента
 */
export default function GroupFilters(props){
    const {level, reportId, childGroupInfo, reportFields, childGroupsMap} = props

    const classes = ReportDevFiltersCSS();

    const [showBrouserFilters, setShowBrouserFilters] = useState(false)

    const operationTypeList = [
        {id: "AND", name: "И"},
        {id: "OR", name: "ИЛИ"},
    ]

    const handleChange = (itemId, item, folderId) => {
        props.onAddFilter(childGroupInfo.id, item, reportFields)
        setShowBrouserFilters(false)
    }

    function handleDelete(index, type, event){
        event.stopPropagation()
        props.onDelete(childGroupInfo.id, index, type)
    }

    function handleChangeOrdinal(index, direction, event){
        event.stopPropagation()
        const newElements = ReorderList(elements, index , direction === "up" ? index-1 : index+1)
        props.onChangeOrdinal(childGroupInfo.id, newElements)
    }

    function getElements(){
        let filtersElements = []
        let cntIter = childGroupInfo.childGroups.length + childGroupInfo.filters.length
        let cg = 0
        let f = 0
        let i=0
        
        while ((childGroupInfo.childGroups.length || childGroupInfo.filters.length) && i<cntIter ){
            let cgElement = childGroupsMap.get(childGroupInfo.childGroups[cg])
            let fElement = childGroupInfo.filters[f]
            if (cgElement ||fElement){
                let cgOrdinal = cgElement ? cgElement.ordinal : 1000000
                let fOrdinal = fElement ? fElement.ordinal : 1000000
                if (cgOrdinal < fOrdinal){
                    filtersElements.push({...cgElement, __elmtype:'childGroups', startIndex:cg})
                    cg++
                }
                else {
                    filtersElements.push({...fElement, __elmtype: 'filters', startIndex:f})
                    f++
                }
            }
            i++
        }
        return filtersElements
    }

    let elements = getElements()
    let filterList = []
    elements.forEach((element, index) => {
        if (element.__elmtype === 'childGroups'){
            filterList.push(
                <DragAndDrop 
                    style={{display: 'grid'}}
                    key={`${element.id}_${element.__elmtype}`}
                    draggable={true} 
                    dropInto={true} 
                    dropBefore={true} 
                    dropVirtual={true} 
                    id={`${element.id}_${element.__elmtype}`}
                    groupId={200} 
                    droppableGroups={["200"]} 
                    onDropBefore={(e, beforeId, entityId) => props.onMoveBefore(beforeId, parseInt(entityId))}
                    onDropInto={(e, targetId, entityId) => props.onMoveInto(targetId, parseInt(entityId))}
                    isDropAllowedExt={(e, id, idIncoming) => {return true}}
                >
                <Paper elevation= {3} key={`${element.ordinal}_${element.id}`} className={classes.groupFilter} >
                    <div className={classes.devRepGroupFilterHeader}>
                        <Typography className={classes.filterTypeInTitle}>
                            {element.operationType === "OR" ? "ИЛИ" : "И"}
                        </Typography>
                        <div>
                            <Tooltip title = {!!element.mandatory ? "Обязательный" : "Не обязательный"}>
                                <IconButton 
                                    className={classes.btn} 
                                    onClick={() => props.onChangeFiltersGroup(element.id, element.startIndex, 'mandatory', !element.mandatory)}
                                >
                                    {!!element.mandatory ? <ReportIcon color="secondary"/> : <ReportOffIcon color="primary"/> }
                                </IconButton>
                            </Tooltip>
                        </div>
                        <Typography variant="h5" className={classes.filterTitle}>
                            {element.name ? element.name : 'Группа фильтров'}
                        </Typography>
                        
                        {element.haveErrors && 
                        <Tooltip 
                            title={
                                <React.Fragment>
                                    {Object.values(element.errors).map((e, index) => 
                                        <Typography key={index} color="inherit" style={{fontSize:'1em'}}>{e}</Typography>
                                    )}
                                </React.Fragment>
                            } 
                            placement="top"
                        >
                            <WarningIcon fontSize='small' color="secondary"/>
                        </Tooltip>
                        }
                        <IconButton
                            aria-label="delete" 
                            color="primary" 
                            className={classes.btn} 
                            onClick={event=>handleDelete(element.startIndex, 'childGroups', event)}
                        >
                            <DeleteIcon fontSize='small' />
                        </IconButton>

                        <IconButton 
                            aria-label="up" 
                            color="primary" 
                            disabled={index === 0}
                            className={classes.btn} 
                            onClick={event=>handleChangeOrdinal(index, 'up', event)}
                        >
                            <ArrowUpwardIcon fontSize='small' />
                        </IconButton>
                        
                        <IconButton 
                            aria-label="down" 
                            color="primary" 
                            disabled={index === elements.length-1}
                            className={classes.btn} 
                            onClick={event=>handleChangeOrdinal(index, 'down', event)}
                        >
                            <ArrowDownwardIcon fontSize='small' />
                        </IconButton>

                        <IconButton
                            aria-label = "expand"
                            className={classes.btn}
                            onClick={()=>props.onChangeFiltersGroup(element.id, element.startIndex,'expandedCollapse', !element.expandedCollapse)}
                        >
                            {element.expandedCollapse ? <ExpandLessIcon fontSize='small'/> : <ExpandMoreIcon fontSize='small'/>}
                        </IconButton>
                            
                    </div>
                    <Collapse 
                        className={classes.devRepGroupFilterClps}
                        key={element.ordinal}
                        in={element.expandedCollapse}
                    >
                        <div className={classes.nameAndType}>
                            <div className={classes.groupFilterType}>
                                <DesignerSelectField
                                    label = "Тип операции"
                                    fullWidth
                                    displayBlock
                                    size="small"
                                    data = {operationTypeList}
                                    value = {element.operationType}
                                    onChange = {value => props.onChangeFiltersGroup(element.id, element.startIndex,'operationType', value)}
                                />
                            </div>
                            <div className={classes.groupFilterName}>
                                <DesignerTextField
                                    label = "Название группы фильтров"
                                    value = {element.name}
                                    fullWidth
                                    size="small"
                                    onChange = {value => props.onChangeFiltersGroup(element.id, element.startIndex,'name', value)}
                                    error = {element.errors ? !!element.errors.groupName : false}
                                /> 
                            </div>
                            <div className={classes.groupFilterCode}>
                                <DesignerTextField
                                    label = "Код группы"
                                    value = {element.code}
                                    fullWidth
                                    size="small"
                                    onChange = {value => props.onChangeFiltersGroup(element.id, element.startIndex,'code', value)}
                                    error = {element.errors ? !!element.errors.groupCode : false}
                                /> 
                            </div>                            
                        </div>
                        {level+1>0 && 
                            <div className={classes.devRepGroupFilterDesc}>
                                <DesignerTextField
                                    label = "Описание группы фильтров"
                                    value = {element.description}
                                    displayBlock
                                    multiline
                                    fullWidth
                                    size="small"
                                    onChange = {value => props.onChangeFiltersGroup(element.id, element.startIndex,'description', value)}
                                    error = {element.errors ? !!element.errors.groupDescription : false}
                                />  
                            </div>
                        }
                        <div className={classes.grpFilters}>
                            <GroupFilters 
                                level={level+1}
                                reportId={reportId}
                                reportFields={reportFields}
                                childGroupInfo={element}
                                childGroupsMap={props.childGroupsMap}
                                onAddFilter={props.onAddFilter}
                                onAddChildGroup={props.onAddChildGroup}
                                onDelete={props.onDelete}
                                onChangeFiltersGroup={props.onChangeFiltersGroup}
                                onChangeFilterField={props.onChangeFilterField}
                                onChangeOrdinal={props.onChangeOrdinal}
                                onMoveBefore={props.onMoveBefore}
                                onMoveInto={props.onMoveInto}
                                datasetType={props.datasetType}
                            />
                        </div>
                    </Collapse>
                </Paper>
                </DragAndDrop>
            )
        }
        else {
            filterList.push(
                <DragAndDrop 
                    key={`${element.id}_${element.__elmtype}`}
                    draggable={true} 
                    dropInto={true} 
                    dropBefore={true} 
                    dropVirtual={true} 
                    id={`${element.id}_${element.__elmtype}`}
                    groupId={200} 
                    droppableGroups={["200"]} 
                    onDropBefore={(e, beforeId, entityId) => props.onMoveBefore(beforeId, parseInt(entityId))}
                    onDropInto={(e, targetId, entityId) => props.onMoveInto(targetId, parseInt(entityId))}
                    isDropAllowedExt={(e, id, idIncoming) => {return true}}
                >
                    <ReportFiltersItem
                        key={`${element.ordinal}_${element.id}`}
                        className={classes.itemFilter}
                        index={index}
                        disabled={index === elements.length-1}
                        filterItem={element}
                        reportFields={reportFields}
                        datasetType={props.datasetType}
                        onChange={filterItem => props.onChangeFilterField(childGroupInfo.id, element.startIndex, filterItem)}
                        onDeleteItem={event=>handleDelete(element.startIndex, 'filters', event)}
                        onChangeOrdinal={handleChangeOrdinal}
                    />
                </DragAndDrop>
            )
        }
    })
    
    return (
        <div style={{display: 'grid'}}>
            {filterList}
            <div className={classes.justifyCenter}>
                <ButtonGroup color="primary" aria-label="Add buttons"> 
                    <Button
                        color="primary"
                        className={classes.button}
                        startIcon={<AddIcon />}
                        onClick={() => props.onAddChildGroup(childGroupInfo.id, reportId)}
                    >
                        Добавить группу фильтров
                    </Button>
                    <Button
                        color="primary"
                        size="large"
                        className={classes.button}
                        startIcon={<AddIcon />}
                        onClick={() => setShowBrouserFilters(true)}
                >
                    Добавить фильтр
                    </Button>
                </ButtonGroup>
            </div>
            <DesignerFolderBrowser 
                dialogOpen={showBrouserFilters}
                itemType={FolderItemTypes.filterInstance}
                onChange={handleChange}
                onClose={() => setShowBrouserFilters(false)}
            />
        </div>
    )
};