import React, {useState} from 'react';

// components
import Paper from '@material-ui/core/Paper';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import Tooltip from '@material-ui/core/Tooltip';
import ReportIcon from '@material-ui/icons/Report';
import ReportOffIcon from '@material-ui/icons/ReportOff';
import VisibilityIcon from '@material-ui/icons/Visibility';
import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ExpandLessIcon from '@material-ui/icons/ExpandLess';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

// local
import ViewerTextField from 'main/Main/Development/Viewer/ViewerTextField';

// styles
import {ReportFiltersItemCSS} from 'main/Main/Development/Designer/DesignerCSS';
import {ViewerCSS} from "../../Viewer/ViewerCSS";

/**
 * Компонент просмотра фильтра отчета
 * @param {Object} props - свойства компонента
 * @param {Object} props.filterItem - объект фильтра
 * @param {Array} props.reportFields - поля отчёта
 */
export default function ReportFiltersItemViewer({filterItem, reportFields}) {

    const classes = ReportFiltersItemCSS()

    const viewerClasses = ViewerCSS();

    const [expanded, setExpanded] = useState(true);

    return (
        <Paper className={classes.root} elevation={3}>
            <div className={classes.devRepFilterHeader}>
                <Tooltip title={!!filterItem.hidden ? "Скрытый" : "Видимый"}>
                    <div className={viewerClasses.icon}>
                        {!!filterItem.hidden ? <VisibilityOffIcon color="secondary"/> :
                            <VisibilityIcon color="primary"/>}
                    </div>

                </Tooltip>
                <Tooltip title={!!filterItem.mandatory ? "Обязательный" : "Не обязательный"}>
                    <div className={viewerClasses.icon}>
                        {!!filterItem.mandatory ? <ReportIcon color="secondary"/> : <ReportOffIcon color="primary"/>}
                    </div>
                </Tooltip>
                <div className={classes.devRepFilterItemNameBlock}>
                    <Typography variant='h5'> {filterItem.name || "Название не указано" || " "} </Typography>
                    <Typography varian='body' color='textSecondary'> {filterItem.description}</Typography>
                </div>

                <IconButton className={classes.btn}
                            onClick={() => setExpanded(!expanded)}>
                    {expanded ? <ExpandLessIcon fontSize='small'/> : <ExpandMoreIcon fontSize='small'/>}
                </IconButton>
            </div>

            <Collapse in={expanded} timeout="auto" unmountOnExit
                      className={classes.devRepFiltersClps}>
                <div className={classes.nameAndCode}>
                    <div className={classes.devRepFiltersName}>
                        <ViewerTextField
                            label="Название фильтра в отчете"
                            value={filterItem.name}
                        />
                    </div>
                    <div className={classes.devRepFiltersCode}>
                        <ViewerTextField
                            label="Код фильтра"
                            value={filterItem.code}
                        />
                    </div>

                </div>
                <div>
                    <Typography variant="body2" color="textSecondary" component="p" gutterBottom>
                        Сопоставление поля фильтра с полями отчёта:
                    </Typography>
                    <div className={classes.repFieldFilterFieldBlock}>
                        {
                            filterItem.fields
                                .filter(field => (field.type === 'ID_FIELD' && !(filterItem.type === 'DATE_RANGE' && field.level > 1)))
                                .map((field, itemIndex) =>
                                    <div key={field.filterInstanceFieldId} className={classes.repFieldFilterField}>
                                        <ViewerTextField
                                            value={field.name}
                                        />

                                        <Divider orientation="vertical" flexItem className={classes.divCompare}
                                                 variant="middle"/>

                                        <ViewerTextField
                                            key={itemIndex}
                                            data={reportFields}
                                            value={filterItem.fields[itemIndex] ? filterItem.fields[itemIndex].reportFieldId : null}
                                        />
                                    </div>
                                )}
                    </div>
                </div>
            </Collapse>
        </Paper>
    )
}
