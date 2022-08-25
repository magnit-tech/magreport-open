import React from 'react'
import {connect} from "react-redux";
import clsx from 'clsx';

import Button from '@material-ui/core/Button';
import AppBar from '@material-ui/core/AppBar';
import Paper from '@material-ui/core/Paper';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';

import {actionViewerEditItem} from "redux/actions/actionViewer";

import {ViewerCSS} from './ViewerCSS';

/**
 * @callback onOkClick
 *
 */


/**
 * @callback actionViewerEditItem
 * @param {String} itemType
 * @param {Number} itemId
 * @return {{type: String, itemType: String, itemId: Number}}
 */

/**
 * Компонент для просмотра содержания объектов
 * @param {Object} props - параметры компонента
 * @param {String} [props.pageName=""] - опционально. Имя страницы
 * @param {Number} props.id - ID объекта
 * @param {String} props.itemType - тип объекта из FolderItemTypes
 * @param {Array} props.children - вложенные компоненты
 * @param {boolean} [props.disabledPadding=false] - опционально. true - отключает отступы внутрь контейнера
 * @param {boolean} [props.readOnly=false] - опционально. true - компонент только для чтения, скрывается кнопка "Редактировать"
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки "ОК"
 * @param {actionViewerEditItem} props.actionViewerEditItem - action, выполняемый при нажатии кнопки "Редактировать"
 * @returns {JSX.Element}
 * @constructor
 */
function ViewerPage(props) {

    const classes = ViewerCSS();

    function onEditClick() {
        props.actionViewerEditItem(props.itemType, props.id, props.name)
    }

    return (
        <div className={classes.viewerPage}>
            {props.pageName ?
                <AppBar position="static" className={classes.pageTitle} >
                    <Toolbar variant="dense" >
                        <Typography variant="h6">
                            {props.pageName}
                        </Typography>
                    </Toolbar>
                </AppBar>
                :
                ""
            }
            <div className={classes.viewerPageChildren}>
                <div
                    className={clsx(classes.viewerPageAbsolute, {[classes.viewerPageWOPadding]: props.disabledPadding})}>
                    <div className={classes.viewerPage}>
                        {props.children}
                    </div>
                </div>
            </div>
            <Paper elevation={3} className={classes.pageBtnPanel}>
                {
                    !props.readOnly ?
                        <Button
                            className={classes.pageBtn}
                            type="submit"
                            variant="contained"
                            size="small"
                            color="primary"
                            onClick={onEditClick}
                        >
                            Редактировать
                        </Button>
                        : ""
                }
                <Button
                    className={classes.pageBtn}
                    type="submit"
                    variant="contained"
                    size="small"
                    color="primary"
                    onClick={props.onOkClick}
                >
                    Отменить
                </Button>
            </Paper>
        </div>
    );
}

const mapActionsToProps = {
    actionViewerEditItem,
};

export default connect(null, mapActionsToProps)(ViewerPage);
