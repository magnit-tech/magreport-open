import React, { useState } from 'react'
import { connect } from 'react-redux';
import { useSnackbar } from 'notistack';
import Button from '@material-ui/core/Button';

// components
import Grid from '@material-ui/core/Grid';
import DescriptionIcon from '@material-ui/icons/Description';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import DeleteIcon from '@material-ui/icons/Delete';
import StarIcon from '@material-ui/icons/Star';
import StarOutlineIcon from '@material-ui/icons/StarOutline';
import IconButton from '@material-ui/core/IconButton';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import Collapse from '@material-ui/core/Collapse';
import PublishIcon from '@material-ui/icons/Publish';
import CloseIcon from '@material-ui/icons/Close';
import { Tooltip } from '@material-ui/core';
import AttachFileIcon from '@material-ui/icons/AttachFile';
import InputAdornment from '@material-ui/core/InputAdornment';
import Typography from '@material-ui/core/Typography';




// local
import DesignerTextField from './../Designer/DesignerTextField'
// import DataLoader from 'main/DataLoader/DataLoader';

// actions 
import {actionAdd, actionDelete, actionLoaded, actionLoadedFailed, actionSetDefault} from 'redux/actions/developer/actionReportTemplates'
import {showAlertDialog, hideAlertDialog} from 'redux/actions/actionsAlertDialog';
// import dataHub from 'ajax/DataHub';

const EXCEL_XLSM_FILE = 'application/vnd.ms-excel.sheet.macroEnabled.12'

/**
 * @callback actionAdd
 * @param {String} name
 * @param {String} description
 * @param {Object} selectedFile
 * @param {Number} reportId
 */

/**
 * @callback actionSetDefault
 * @param {Number} id
 * @param {Number} reportId
 */

/**
 * @callback actionDelete
 * @param {Number} id
 */

/**
 * @callback hideAlertDialog
 */

/**
 * @callback showAlertDialog
 */
/**
 * Компонент добавления шаблонов отчета
 * @param {Object} props - свойства компонента
 * @param {Number} props.reportId - id отчета
 * @param {Array} props.reportTemplates - шаблоны отчета
 * @param {actionAdd} props.actionAdd - action, вызываемый при добавлении шаблона
 * @param {actionSetDefault} props.actionSetDefault - action, вызываемый при добавлении шаблона
 * @param {actionDelete} props.actionDelete - action, вызываемый при удалении шаблона
 * @param {hideAlertDialog} props.hideAlertDialog - action, вызываемый для скрытия диалога подтверждения удаления
 * @param {showAlertDialog} props.showAlertDialog - action, вызываемый для открытия диалога подтверждения удаления
 * @return {JSX.Element}
 * @constructor
 */
function ReportTemplates(props) {
    const { enqueueSnackbar } = useSnackbar();
    const [openCollapse, setOpenCollapse] = useState(false)
    const [newItem, setNewItem] = useState({})

    function handleAdd(){
        props.actionAdd(newItem.name, newItem.description, newItem.selectedFile, props.reportId)
        setNewItem({})
        setOpenCollapse(false)        
    }

    function handleSetDefault(id, isDefault) {
        if (!isDefault){
            props.actionSetDefault(id, props.reportId)
        }
    }

    function handleChange(key, v){
        if (key === "file") {
            if (v.target.files[0].type === EXCEL_XLSM_FILE){
                setNewItem({...newItem, [key]: v.target.files[0].name, selectedFile: v.target.files[0]})
            }
            else {
                enqueueSnackbar(`Файл шаблона должен быть файл Excel с поддержкой макросов`, {variant : "error"});
            }
        }
        else {
            setNewItem({...newItem, [key]: v})
        }
    }

    function handleDeleteAnswer(id, answer){
        if (answer) props.actionDelete(id)
        props.hideAlertDialog()
    }

    return (
        <>
            <div>
                <Button 
                    aria-label="add" 
                    startIcon={<AddCircleIcon color="primary"/>}
                    onClick={() => setOpenCollapse(true)}
                >
                    <Typography color="primary"> Добавить шаблон </Typography>
                </Button>
                <Collapse in={openCollapse} unmountOnExit>
                    <Grid 
                        container 
                        spacing={2} 
                        direction="row"
                        justifyContent="center"
                        alignItems="center"
                    >
                        <Grid item xs={3}>
                            <DesignerTextField 
                                label="Название шаблона"
                                value={newItem.name}
                                fullWidth
                                onChange={value => handleChange("name", value)}
                            />
                        </Grid>
                        <Grid item xs={4}>
                            <DesignerTextField 
                                label="Описание шаблона"
                                value={newItem.description}
                                fullWidth
                                onChange={value => handleChange("description", value)}
                            />
                        </Grid>
                        <Grid item xs={4}>
                            <DesignerTextField 
                                label="Файл"
                                value={newItem.file}
                                fullWidth
                                disabled
                                inputProps={{ endAdornment: 
                                    <InputAdornment position="start">
                                        <Tooltip title="Выбрать файл">
                                            {/* span не убирать, иначе сыпятся Warnings если disabled=true */}
                                            <span>
                                                <IconButton 
                                                    aria-label="Select"
                                                    component="label"
                                                >
                                                    <AttachFileIcon />
                                                    <input
                                                        type="file"
                                                        hidden
                                                        onChange={value => handleChange("file", value)}
                                                    />
                                                </IconButton>
                                            </span>
                                        </Tooltip>
                                    </InputAdornment>
                                }}
                            />
                        </Grid>
                        <Grid item xs={1} >
                            <span style={{display: 'flex', flexWrap: 'nowrap'}}>
                            <Tooltip title="Загрузить">
                                                <span>
                                                    <IconButton 
                                                        aria-label="add"
                                                        disabled={!newItem.name || !newItem.description || !newItem.selectedFile}
                                                        onClick={handleAdd}
                                                    >
                                                        <PublishIcon/>
                                                    </IconButton>
                                                </span>
                            </Tooltip>
                            <Tooltip title="Скрыть">
                                <IconButton 
                                    aria-label="close"
                                    onClick={() => setOpenCollapse(false)}
                                >
                                    <CloseIcon />
                                </IconButton>
                            </Tooltip>
                            </span>
                        </Grid>
                    </Grid>
                </Collapse>
            </div>
            <List>
                {
                    props.reportTemplates.map(i => 
                        <ListItem
                            key={i.excelTemplateId}
                        >
                            <ListItemIcon>
                                <DescriptionIcon />
                            </ListItemIcon>
                            <ListItemText
                                primary={i.name}
                                secondary={i.description}
                            />
                            <IconButton onClick={() => handleSetDefault(i.excelTemplateId, i.default)}>
                                {i.default ? <StarIcon color="secondary" /> : <StarOutlineIcon/>}
                            </IconButton>
                            <IconButton 
                                aria-label="delete"
                                disabled={i.default || i.excelTemplateId === 1}
                                onClick={() => props.showAlertDialog("Вы действительно хотите удалить шаблон фильтра?", null, null, answer => handleDeleteAnswer(i.excelTemplateId, answer))}
                            >
                                <DeleteIcon />
                            </IconButton>
                            
                        </ListItem>
                    )
                }
            </List>
        </>
    )
}

const mapStateToProps = state => {
    return {
        reportTemplates: state.reportTemplates.data,
    }
}

const mapDispatchToProps = {
    actionAdd, 
    actionDelete, 
    actionLoaded, 
    actionLoadedFailed, 
    actionSetDefault,
    showAlertDialog, 
    hideAlertDialog,
}

export default connect(mapStateToProps, mapDispatchToProps)(ReportTemplates)