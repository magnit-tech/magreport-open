import React, {useState} from 'react';

// components 
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ExpandLessIcon from '@material-ui/icons/ExpandLess';
import Paper from '@material-ui/core/Paper';
import Tooltip from '@material-ui/core/Tooltip';
import ReportIcon from '@material-ui/icons/Report';
import ReportOffIcon from '@material-ui/icons/ReportOff';

//local
import ViewerTextField from 'main/Main/Development/Viewer/ViewerTextField';
import ReportFiltersItemViewer from './ReportFiltersItemViewer'

// styles 
import {ReportDevFiltersCSS} from 'main/Main/Development/Designer/DesignerCSS'
import {ViewerCSS} from "main/Main/Development/Viewer/ViewerCSS";


function getElements(childGroupInfo) {
    const childGroupElements = (childGroupInfo.childGroups || [])
        .map((childGroup, index) =>
            ({...childGroup, __elmtype: 'childGroups', startIndex: index, expandedCollapse: false}));
    const filterElements = (childGroupInfo.filters || [])
        .map((filter, index) =>
            ({...filter, __elmtype: 'filters', startIndex: index, expandedCollapse: true}));

    return (childGroupElements.concat(filterElements))
        .sort((element1, element2) =>
            element1.ordinal > element2.ordinal);
}

/**
 * Компонент просмотра группы фильтров в отчете
 * @param {Object} props - свойства компонента
 * @param {Number} props.level - уровень для вложенных групп
 * @param {Number} props.reportId - id отчёта
 * @param {Array} props.reportFields - массив полей отчёта
 * @param {Object} props.childGroupInfo - объект с информацией о группе фильтров
 */
export default function GroupFiltersViewer(props) {
    const {level, reportId, childGroupInfo, reportFields} = props

    const classes = ReportDevFiltersCSS();

    const viewerClasses = ViewerCSS();

    const [elements, setElements] = useState(getElements(childGroupInfo));

    function getOperationTypeName(operationType) {
        switch (operationType) {
            case "AND":
                return "И";
            case "OR":
                return "ИЛИ";
            default:
                return "?";
        }
    }

    function toggleExpandedCollapse(index) {
        setElements(elements.map((element, elementIndex) => {
                if (elementIndex === index) {
                    return {
                        ...element,
                        expandedCollapse: !element.expandedCollapse
                    }
                } else {
                    return element;
                }
            }
        ));
    }


    let filterList = []
    elements.forEach((element, index) => {
        if (element.__elmtype === 'childGroups') {
            filterList.push(
                <Paper elevation={3} key={`${element.ordinal}_${element.id}`} className={classes.groupFilter}>
                    <div className={classes.devRepGroupFilterHeader}>
                        <Typography className={classes.filterTypeInTitle}>
                            {getOperationTypeName(element.operationType)}
                        </Typography>
                        <div>
                            <Tooltip title={!!element.mandatory ? "Обязательный" : "Не обязательный"}>
                                <div className={viewerClasses.icon}>
                                    {!!element.mandatory ? <ReportIcon color="secondary"/> : <ReportOffIcon color="primary"/>}
                                </div>
                            </Tooltip>
                        </div>
                        <Typography variant="h5" className={classes.filterTitle}>
                            {element.name || 'Группа фильтров'}
                        </Typography>

                        <IconButton
                            aria-label="expand"
                            className={classes.btn}
                            onClick={() => toggleExpandedCollapse(index)}
                        >
                            {element.expandedCollapse ? <ExpandLessIcon fontSize='small'/> :
                                <ExpandMoreIcon fontSize='small'/>}
                        </IconButton>

                    </div>
                    <Collapse
                        className={classes.devRepGroupFilterClps}
                        key={element.ordinal}
                        in={element.expandedCollapse}
                    >
                        <div className={classes.nameAndType}>
                            <div className={classes.groupFilterType}>
                                <ViewerTextField
                                    label="Тип операции"
                                    value={getOperationTypeName(element.operationType)}
                                />
                            </div>
                            <div className={classes.groupFilterName}>
                                <ViewerTextField
                                    label="Название группы фильтров"
                                    value={element.name}
                                />
                            </div>
                            <div className={classes.groupFilterCode}>
                                <ViewerTextField
                                    label="Код группы"
                                    value={element.code}
                                />
                            </div>
                        </div>
                        {level + 1 > 0 &&
                        <div className={classes.devRepGroupFilterDesc}>
                            <ViewerTextField
                                label="Описание группы фильтров"
                                value={element.description}
                            />
                        </div>
                        }
                        <div className={viewerClasses.groupFilters}>
                            <GroupFiltersViewer
                                level={level + 1}
                                reportId={reportId}
                                reportFields={reportFields}
                                childGroupInfo={element}
                                childGroupsMap={props.childGroupsMap}
                            />
                        </div>
                    </Collapse>
                </Paper>
            )
        } else {
            filterList.push(
                <div style={{minHeight: '1px'}}>
                    <div key={`${element.ordinal}_${element.id}`} className={classes.itemFilter}>
                        <ReportFiltersItemViewer
                            filterItem={element}
                            reportFields={reportFields}
                        />
                    </div>
                </div>

        );
        }
    });

    return (
        <div style={{display: 'grid'}}>
            {filterList}
        </div>
    );
}
