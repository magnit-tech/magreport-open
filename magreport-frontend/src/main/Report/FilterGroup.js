import React from 'react';
import clsx from 'clsx';
//material-ui
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import WarningIcon from '@material-ui/icons/Warning';
import Tooltip from '@material-ui/core/Tooltip';

// styles
import { FilterGroupCSS, Accordion, AccordionSummary, AccordionDetails } from './filters/FiltersCSS'
import FilterWrapper from './FilterWrapper';

import {getAllGroupChildren} from "utils/reportFiltersFunctions";



/**
 * Группа фильтров
 * @param {*} props.groupData - данные группы фильтров
 * @param {*} props.lastFilterValues - объект FilterValues со значениями фильтров из последнего запуска
 * @param {*} props.onChangeFilterValue - function(filterValue) - callback для передачи значения изменившегося параметра фильтра, 
 *                                         filterValue - объект для передачи в сервис в массиве parameters
 */

/**
 * 
 * @param {*} props.operationType
 */

export default function FilterGroup(props){
    const classes = FilterGroupCSS();
    
    const allChildren = getAllGroupChildren(props.groupData);
    let andOrType = props.groupData.operationType === 'AND' ? classes.andType : classes.orType;

    const groupName = props.groupData.mandatory ? <span>{props.groupData.name}<i className={classes.mandatory}>*</i></span> : <span>{props.groupData.name}</span>
    let isInvalid = false
    if (props.groupData.mandatory) {
        const filterIds = props.mandatoryGroups.get(props.groupData.id)
        if (filterIds.size === 0) {
            isInvalid = true
        }
    }

    return (  
        <Accordion defaultExpanded elevation={0}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Typography style={{fontWeight: 500}}>
                    {groupName}
                </Typography>
                {   isInvalid && 
                    <Tooltip 
                        title={
                            <Typography color="inherit" style={{fontSize:'1em'}}>В группе должен быть заполнен хотя бы один фильтр</Typography>
                        } 
                        placement="top"
                    >
                        <WarningIcon fontSize='small' color="secondary"/>
                    </Tooltip>
                }
            </AccordionSummary>
            <AccordionDetails>
                <div className={classes.allChildrenBox}> {
                    allChildren.map( (v) => {
                        if(v._elementType === 'group') {
                            return <div key = {v.id} className = {clsx(classes.andOrType, andOrType)} >
                                <FilterGroup
                                    groupData = {v}
                                    mandatoryGroups = {props.mandatoryGroups}
                                    lastFilterValues = {props.lastFilterValues}
                                    onChangeFilterValue = {props.onChangeFilterValue}
                                    toggleClearFilters = {props.toggleClearFilters}
                                />
                            </div>
                        }
                        else {
                            return <div key = {v.id} className = {clsx(classes.andOrType, andOrType)}>        
                                <FilterWrapper
                                    filterData = {v}
                                    lastFilterValue = {props.lastFilterValues.getFilterValue(v.id)}
                                    onChangeFilterValue = {props.onChangeFilterValue}
                                    toggleClearFilters = {props.toggleClearFilters}
                                />
                            </div>
                        }                            
                    })
                }
                </div>
            </AccordionDetails>
        </Accordion>  
    )
}