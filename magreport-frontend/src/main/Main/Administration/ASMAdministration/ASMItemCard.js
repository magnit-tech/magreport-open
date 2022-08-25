import React from "react";
import {useSnackbar} from "notistack";

import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import CardContent from "@material-ui/core/CardContent";
import CardActions from '@material-ui/core/CardActions';
import Typography from "@material-ui/core/Typography";
import Tooltip from "@material-ui/core/Tooltip";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import Pageview from "@material-ui/icons/Pageview";
import EditIcon from "@material-ui/icons/Edit";
import DeleteIcon from "@material-ui/icons/Delete";
import IconButton from "@material-ui/core/IconButton";

// local
import { ASMCSS as useStyles} from "./ASMCSS";
import dataHub from "ajax/DataHub";
import {actionAsmDeleted, actionAsmViewItemClick, actionAsmEditItemClick} from "redux/actions/admin/actionAsm";
import {hideAlertDialog, showAlertDialog} from "redux/actions/actionsAlertDialog";
import {connect} from "react-redux";

/**
 * @callback actionAsmDeleted
 * @param {Object} data
 */

/**
 * @callback actionAsmEditItemClick
 * @param {Number} id
 */

/**
 * @callback actionAsmViewItemClick
 * @param {Number} id
 */

/**
 * @callback showAlertDialogCallback
 * @param {Boolean} isOk
 */

/**
 * @callback showAlertDialog
 * @param {String} title
 * @param {String} entityType
 * @param {String} entity
 * @param {showAlertDialogCallback} callback
 */

/**
 * @callback hideAlertDialog
 */

/**
 * @callback onClick
 */

/**
 *
 * @param {Object} props - component properties
 * @param {Number} props.id - ID объекта ASM
 * @param {String} props.name - имя объекта ASM
 * @param {String} props.username - имя создателя объекта ASM
 * @param {String} props.description - описание объекта ASM
 * @param {Number} props.created - Unix-timestamp времени создания объекта ASM
 * @param {Number} props.modified - Unix-timestamp времени изменения объекта ASM
 * @param {Boolean} props.isSelected - элемент выбран/не выбран
 * @param {actionAsmDeleted} props.actionAsmDeleted - action успешного удаления ASM
 * @param {actionAsmViewItemClick} props.actionAsmViewItemClick - action, открывает объект ASM для просмотра
 * @param {actionAsmEditItemClick} props.actionAsmEditItemClick - action открывает дизайнер ASM для редактирования объекта
 * @param {showAlertDialog} props.showAlertDialog - показать диалоговое окно
 * @param {hideAlertDialog} props.hideAlertDialog - скрыть диалоговое окно
 * @param {onClick} props.onClick - выполняется при нажатии на карточку
 * @returns {JSX.Element}
 * @constructor
 */
function ASMItemCard(props) {
    const {enqueueSnackbar} = useSnackbar();
    const classes = useStyles();

    const id = props.id
    const name = props.name;
    const username = props.username;
    const description = props.description;
    //TODO: reformat created and modified timestamps
    const created = new Date(props.created).toLocaleDateString("ru");
    const modified = new Date(props.modified).toLocaleDateString("ru");
    const isSelected = props.isSelected;

    //event handlers
    function handleViewButtonClick() {
        props.actionAsmViewItemClick(id);
    }

    function handleEditButtonClick() {
        props.actionAsmEditItemClick(id);
    }

    function handleDeleteButtonClick() {
        props.showAlertDialog("Удалить?", null, null, handleDelete);
    }

    function handleDelete(isOk) {
        if(isOk) {
            dataHub.asmController.delete(id, (magResponse) => {
                if(magResponse.ok) {
                    enqueueSnackbar(`ASM "${name} успешно удалён`, {variant: "success"});
                    props.actionAsmDeleted(magResponse.data)
                } else {
                    enqueueSnackbar(`При удалении ASM "${name}" произошла ошибка: ${magResponse.data}`,
                        {variant: "error"});
                }
            });
        }
        props.hideAlertDialog();
    }

    let cardClasses = classes.card;

    if(isSelected){
        cardClasses += " " + classes.cardSelected;
    }

    return (
            <Card className={cardClasses} elevation={5} onClick={props.onClick}>
                <CardHeader
                    classes={{root: classes.cardHead, content: classes.cardHeadContent}}
                    titleTypographyProps={{variant: "subtitle2", align: "left"}}
                    subheaderTypographyProps={{variant: "caption", align: "left"}}
                    title={name}
                    subheader={description}
                />
                <CardContent>
                    <Table>
                        <TableBody>
                            <TableRow>
                                <TableCell padding="none">
                                    <Typography variant="caption">Пользователь: {username}</Typography>
                                </TableCell>
                                <TableCell  align="right" padding="none">
                                    <Typography variant="caption">Создан: {created}</Typography>
                                </TableCell>
                            </TableRow>
                            <TableRow>

                            </TableRow>
                            <TableRow>
                                <TableCell padding="none">
                                    <Typography variant="caption">id: {id}</Typography>
                                </TableCell>
                                <TableCell  align="right" padding="none">
                                    <Typography variant="caption">Изменен: {modified}</Typography>
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </CardContent>
                <CardActions className={classes.cardAction}>
                    <span>
                            <Tooltip title="Просмотр">
                                <IconButton fontSize="small" color="primary" onClick={handleViewButtonClick}>
                                    <Pageview />
                                </IconButton>
                            </Tooltip>
                            <Tooltip title="Редактировать">
                                <IconButton fontSize="small" color="primary" onClick={handleEditButtonClick}>
                                    <EditIcon/>
                                </IconButton>
                            </Tooltip>
                            <Tooltip title="Удалить">
                                <IconButton fontSize="small" color="primary" onClick={handleDeleteButtonClick}>
                                    <DeleteIcon/>
                                </IconButton>
                            </Tooltip>
                    </span>
                </CardActions>
            </Card>
        );
}

const mapStateToProps = (state) => {
    return {

    }
};

const mapDispatchToProps = {
    actionAsmDeleted,
    actionAsmViewItemClick,
    actionAsmEditItemClick,
    showAlertDialog,
    hideAlertDialog
};

export default connect(mapStateToProps, mapDispatchToProps)(ASMItemCard);