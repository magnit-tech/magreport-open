import React from 'react';
import clsx from 'clsx';

// material-ui
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

// local
import FilterWrapperViewer from './FilterWrapperViewer';

// styles
import {FilterGroupCSS, Accordion, AccordionSummary, AccordionDetails} from '../filters/FiltersCSS'
import {getAllGroupChildren} from "utils/reportFiltersFunctions";

/**
 * Просмотр группы фильтров
 * @param {Object} props - свойства компонента
 * @param {Object} props.groupData - данные группы фильтров
 * @param {Object} props.lastFilterValues - объект со значениями фильтров из последнего запуска
 */
export default function FilterGroupViewer(props) {
    const classes = FilterGroupCSS();

    const allChildren = getAllGroupChildren(props.groupData);

    const andOrTypeClass = props.groupData.operationType === "AND" ? classes.andType : classes.orType;

    const groupName = props.groupData.name;

    return (
        <Accordion defaultExpanded elevation={0}>
            <AccordionSummary expandIcon={<ExpandMoreIcon/>}>
                <Typography style={{fontWeight: 500}}>
                    {groupName}
                </Typography>
            </AccordionSummary>
            <AccordionDetails>
                <div className={classes.allChildrenBox}> {
                    allChildren.map((v) => {
                        if (v._elementType === "group") {
                            return <div key={v.id} className={clsx(classes.andOrType, andOrTypeClass)}>
                                <FilterGroupViewer
                                    groupData={v}
                                    lastFilterValues={props.lastFilterValues}
                                />
                            </div>
                        } else {
                            return <div key={v.id} className={clsx(classes.andOrType, andOrTypeClass)}>
                                <FilterWrapperViewer
                                    filterData={v}
                                    lastFilterValue={props.lastFilterValues.getFilterValue(v.id)}
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