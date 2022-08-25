import React from 'react';

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
import DesignerSelectField from '../Designer/DesignerSelectField';
import DesignerTextField from '../Designer/DesignerTextField';

//styles
import { ReportFieldItemCSS } from '../Designer/DesignerCSS';

/**
 * @callback onFieldChange
 * @param {Number} index
 * @param {String} key
 * @param {String} newValue
 */
/**
 * @callback onDropField
 * @param {Number} index
 */
/**
 * Компонент редактирования поля отчета
 * @param {Object} props - свойства компонента
 * @param {Number} props.key - ключ поля для React
 * @param {Number} props.index - порядковый номер поля в отчете
 * @param {String} props.name - имя поля
 * @param {String} props.description - описание поля
 * @param {Boolean} props.open - если true, то отображаются дополнительные свойства поля
 * @param {Array} props.datasetFields - общий список полей набора данных
 * @param {Boolean} props.visible - является ли поле отображаемым в отчете
 * @param {Number} props.dataSetFieldId - ID поля в наборе данных
 * @param {Boolean} props.valid - является ли поле валидным
 * @param {Boolean} [props.readOnly=false] - если true, то отображает компонент только для чтения
 * @param {onFieldChange} props.onFieldChange - callback, вызываемый при изменении поля
 * @param {onDropField} props.onDropField - callback, вызываемый при удалении поля
 * @return {JSX.Element}
 * @constructor
 */
function ReportFieldItem(props){
    const classes = ReportFieldItemCSS();

    return (
        <Paper className={classes.repFieldAccordion} elevation={3}>
            <div className={classes.repFieldDesc}>
                <Tooltip title = "Выводимое">
                    <div className={classes.visibility}>
                            <IconButton  onClick={event => props.onFieldChange(props.index, 'visible', !props.visible)}>
                                {props.visible ? <VisibilityIcon color="primary"/> : <VisibilityOffIcon color="secondary"/>}
                            </IconButton>
                    </div>
                </Tooltip>
                <div className={classes.repField}>
                    <DesignerTextField   
                        label = "Название"
                        value = {props.name}
                        fullWidth
                        onChange = {value => props.onFieldChange(props.index, 'name', value)}
                        error={props.name.length === 0}
                    />
                </div>
                <div className={classes.repFieldSel}>
                    <DesignerSelectField
                        label = "Поле набора данных"
                        data = {props.datasetFields}
                        value = {props.dataSetFieldId}
                        fullWidth
                        onChange = {value => props.onFieldChange(props.index, 'dataSetFieldId', value)} 
                    />
                </div>
                {!props.valid && 
                    <div className={classes.visibility}>
                        <IconButton aria-label="delete" onClick={() => props.onDropField(props.index)}>
                            <DeleteIcon color="secondary" />
                        </IconButton>
                    </div>
                }
                <div className={classes.visibility}>
                    <IconButton onClick={event => props.onFieldChange(props.index, 'open', !props.open)}>
                        {props.open ? <ExpandLessIcon/>:<ExpandMoreIcon/>}
                    </IconButton>
                </div>
            </div>
            {props.open &&
            <Collapse in={props.open} timeout="auto" unmountOnExit className= {clsx(classes.repFieldDet,{[classes.repFieldDetInvalid] : !props.valid })}>
                <DesignerTextField
                    label = "Описание"
                    value = {props.description}
                    fullWidth
                    onChange = {value => props.onFieldChange(props.index, 'description', value)}
                    error={props.description.length === 0}
                />
            </Collapse>
            }
        </Paper>
    )
}

export default React.memo(ReportFieldItem)