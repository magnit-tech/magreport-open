import React, {useState} from 'react';

// components
import DeleteIcon from '@material-ui/icons/Delete';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import VisibilityIcon from '@material-ui/icons/Visibility';
import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';
import Collapse from '@material-ui/core/Collapse';
import Paper from '@material-ui/core/Paper';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ExpandLessIcon from '@material-ui/icons/ExpandLess';
import clsx from 'clsx';

//local
import ViewerTextField from 'main/Main/Development/Viewer/ViewerTextField';

//styles
import {ReportFieldItemCSS} from '../Designer/DesignerCSS';
import {ViewerCSS} from "../Viewer/ViewerCSS";

/**
 * Компонент просмотра поля отчета
 * @param {Object} props - свойства компонента
 * @param {Number} props.key - ключ поля для React
 * @param {Number} props.index - порядковый номер поля в отчете
 * @param {String} props.name - имя поля
 * @param {String} props.description - описание поля
 * @param {Boolean} props.open - если true, то отображаются дополнительные свойства поля
 * @param {Boolean} props.visible - является ли поле отображаемым в отчете
 * @param {Number} props.dataSetFieldId - ID поля в наборе данных
 * @param {Boolean} props.valid - является ли поле валидным
 * @param {Array} props.dataSetFields - общий список полей набора данных
 * @return {JSX.Element}
 * @constructor
 */
export default function ReportFieldItem(props) {
    const classes = ReportFieldItemCSS();

    const viewerClasses = ViewerCSS();

    const [open, setOpen] = useState(props.open);

    const dataSetFieldIdToName = Object.fromEntries(
        (props.dataSetFields || [])
            .map(field => [field.id, field.name]))

    function getDataSetFieldName(id) {
        return dataSetFieldIdToName[id];
    }

    return (
        <Paper className={viewerClasses.reportFieldAccordion} elevation={3}>
            <div className={classes.repFieldDesc}>
                <Tooltip title="Выводимое">
                    <div className={classes.visibility}>
                        <IconButton disabled>
                            {props.visible ? <VisibilityIcon color="primary"/> : <VisibilityOffIcon color="secondary"/>}
                        </IconButton>
                    </div>
                </Tooltip>
                <div className={classes.repField}>
                    <ViewerTextField
                        label="Название"
                        value={props.name}
                    />
                </div>
                <div className={classes.repFieldSel}>
                    <ViewerTextField
                        label="Поле набора данных"
                        value={getDataSetFieldName(props.dataSetFieldId)}
                    />
                </div>
                {!props.valid &&
                <div className={classes.visibility}>
                    <IconButton
                        aria-label="delete"
                        onClick={() => props.onDropField(props.index)}>
                        <DeleteIcon color="secondary"/>
                    </IconButton>
                </div>
                }
                <div className={classes.visibility}>
                    <IconButton onClick={() => setOpen(!open)}>
                        {open ? <ExpandLessIcon/> : <ExpandMoreIcon/>}
                    </IconButton>
                </div>
            </div>
            {open &&
            <Collapse in={open} timeout="auto" unmountOnExit
                      className={clsx(classes.repFieldDet, {[classes.repFieldDetInvalid]: !props.valid})}>
                <ViewerTextField
                    label="Описание"
                    value={props.description}
                />
            </Collapse>
            }
        </Paper>
    )
}
