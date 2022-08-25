import React from 'react';
// material-ui
import {Table, TableBody, TableRow, TableCell } from '@material-ui/core';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Typography from '@material-ui/core/Typography';
import InfoIcon from '@material-ui/icons/Info';
import EditIcon from '@material-ui/icons/Edit';
import Pageview from '@material-ui/icons/Pageview';
import DeleteIcon from '@material-ui/icons/Delete';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import CircularProgress from '@material-ui/core/CircularProgress';
import StarsIcon from '@material-ui/icons/Stars';
import Link from '@material-ui/core/Link';
import ViewComfyIcon from '@material-ui/icons/ViewComfy';
import SettingsEthernetIcon from '@material-ui/icons/SettingsEthernet';
import SwapHorizontalCircleIcon from '@material-ui/icons/SwapHorizontalCircle';

// local
import { JobStatusMap, ScheduleStatusMap } from './JobFilters/JobStatuses';

// styles
import { ItemCardCSS } from './FolderContentCSS'
import { FolderItemTypes } from './FolderItemTypes';

/**
 * @callback eventHandler
 */
/**
 * Карточка объекта в главном меню
 * @param {*} props.itemType - тип элементы в соответствии с FolderItemTypes
 * @param {Object} props.data - данные элемента (как они возвращаются в массиве элементов от сервиса get-folder)
 * @param {Object} props.roleType -  {isAdmin: boolean, isDeveloper: boolean} - признак admin, developer
 * @param {Boolean} props.editButton - показывать ли кнопку редактирования элемента
 * @param {Boolean} props.deleteButton - показывать ли кнопку удаления элемента
 * @param {eventHandler} props.onViewButtonClick - действие при нажатии кнопки просмотра элемента
 * @param {eventHandler} props.onEditButtonClick - действие при нажатии кнопки редактирования элемента
 * @param {eventHandler} props.onDeleteButtonClick - действие при нажатии кнопки удаления элемента
 * @param {eventHandler} props.onJobCancelClick - действие при наатии кнопки отмены задания
 * @param {eventHandler} props.onClick - действие при нажатии на элементе
 * @param {eventHandler} props.onReportRunClick - действие при нажатии на кнопке рестарта отчета
 * @param {eventHandler} props.onAddDeleteFavorites - действие при нажатии на кнопку добавления/ удаления в избранное
 * @param {eventHandler} props.onLinkPathClick - действие при нажатии на ссылку, содержащую путь к отчету
 * @param {eventHandler} props.onDependenciesClick - действие при нажатии кнопку получения зависимостей
 * @param {eventHandler} props.showDialog - действие при нажатии на кнопку получения SQL-запроса
 * @param {eventHandler} props.onContextMenu - действие при вызове контекстного меню
 * @param {eventHandler} props.onScheduleTaskSwitchClick - действие при нажатии на кнопку "Изменить статус" для отчетов на расписании
 */

function ItemCard(props){

    //let [taskStatus, setTaskStatus] = useState(props.data.status);

    const classes = ItemCardCSS();

    let isInvalid = (props.data.isValid !==undefined && !props.data.isValid) ||
    (props.itemType === FolderItemTypes.scheduleTasks && 
        (props.data.status === 'FAILED' || props.data.status === 'EXPIRED' || props.data.status === 'CHANGED')
    )

    let isSuccess = (props.itemType === FolderItemTypes.scheduleTasks && 
        (props.data.status === 'SCHEDULED' || props.data.status === 'RUNNING' || props.data.status === 'COMPLETE')
    )

    let invalidSuccessDefault = isSuccess ? classes.successIcon : 
    isInvalid ? classes.errorIcon : classes.primaryIcon;

   // const [sqlViewerOpen, setSqlViewerOpen] = React.useState(false)
    const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric' };
    
    const data = props.data;
    const userNameLabel = props.itemType === FolderItemTypes.report ? "Автор"
                        : props.itemType === FolderItemTypes.reportsDev  ? "Разработчик"
                        : "Пользователь";
    let modifiedDateTime = props.data.modifiedDateTime;
    let createdDateTime = props.data.createdDateTime;
    let expirationDate = props.data.expirationDate;
    let username = props.data.userName
    const cardName = props.itemType === FolderItemTypes.userJobs || props.itemType === FolderItemTypes.job ? data.report.name : data.name
    let cardDescription = props.itemType === FolderItemTypes.userJobs || props.itemType === FolderItemTypes.job ? data.report.description : data.description
    let subheader = cardDescription;
    let recipients = ''

    if (props.data.destinationEmails || props.data.destinationUsers || props.data.destinationRoles) {
        recipients = props.data.destinationEmails.map(item=>item.value).concat(props.data.destinationUsers.map(item=>item.userName)).concat(props.data.destinationRoles.map(item=>item.name)).join('; ');
    }

    if ((props.itemType === FolderItemTypes.report || props.itemType === FolderItemTypes.reportsDev || props.itemType === FolderItemTypes.favorites) && props.data.userPublisher){
        modifiedDateTime = props.data.modified;
        createdDateTime = props.data.created;
        username = props.data.userPublisher.name
    }
    else if (props.itemType === FolderItemTypes.job || props.itemType === FolderItemTypes.userJobs){
        createdDateTime = props.data.created;
        modifiedDateTime = props.data.modified;
        username = props.data.user.name
        subheader = 
            <span className={classes.subHead}>{JobStatusMap.get(props.data.status)}
                { ["RUNNING", "SCHEDULED"].indexOf(props.data.status) > -1 ? 
                    props.data.waitCancelResponse ? 
                    <CircularProgress /> :
                    <Typography className={classes.cancel} variant="subtitle1" onClick={handleJobCancelClick}> Отменить </Typography> : ""
                }
                <span>id={props.data.id}</span>
            </span>
    }
    else if (props.itemType === FolderItemTypes.scheduleTasks) {
        username = props.data.user.name;
        subheader = <span className={classes.subHead}>
            {ScheduleStatusMap.get(props.data.status)}
            <span>id={props.data.id}</span>
        </span>
    }
    else if (props.data.created || props.data.modified){
        createdDateTime = props.data.created;
        modifiedDateTime = props.data.modified;
    }

    if (props.itemType === FolderItemTypes.theme || props.itemType === FolderItemTypes.systemMailTemplates) {
        username = props.data.user.name;
    }
     
    if(props.itemType === FolderItemTypes.roles){
        username = 'SYSTEM'
    }

    function handleReqLinkClick(event){
        event.stopPropagation();

        if(data.requirementsLink){
            window.open(data.requirementsLink);
        }
    }

    function handleJobCancelClick(event){
        console.log('handleJobCancelClick');
        event.stopPropagation();
        props.onJobCancelClick()
    }

    function handleScheduleTaskSwitchClick(event){
        event.stopPropagation();
        props.onScheduleTaskSwitchClick();
    }

    function handleRestartClick(event){
        event.stopPropagation();
        props.onReportRunClick()
    }

    function handleScheduleTaskRunClick(event){
        event.stopPropagation();
        props.onScheduleTaskRunClick()
    }


    function handleViewClick(event){
        event.stopPropagation();
        props.onViewButtonClick();
    }

    function handleEditClick(event){
        event.stopPropagation();
        props.onEditButtonClick();
    }

    function handleDeleteClick(event){
        event.stopPropagation();
        props.onDeleteButtonClick();
    }

    function handleFavoritesClick(event){
        event.stopPropagation()
        props.onAddDeleteFavorites()
    }

    function handleLinkPathClick(event){
        event.stopPropagation()
        event.preventDefault()
        const folderId = props.data.path[props.data.path.length-1].id
        props.onLinkPathClick(folderId)
    }

    function handleDependenciesClick(event){
        event.stopPropagation()
        props.onDependenciesClick()
    }

    function handleShowSqlClick(event){
        event.stopPropagation()
        //setSqlViewerOpen(true)
        props.showDialog( props.itemType, `Просмотр SQL-запроса для задания  ${props.data.id}`, props.data.id)

    }

    function handleTruncate(str, wordsToCut) {
        let wordCounter = null;
        if (str) {
            wordCounter = str.match(/(\w+)/g).length;
        }
        if (wordCounter > wordsToCut) {
            return str.split(" ").splice(0, wordsToCut).join(" ").concat(' ...');
        }

        return str.split(" ").splice(0, wordsToCut).join(" ");
    }

    let actionBtns =[];

    if ( data.requirementsLink) {
        actionBtns.push(
            <Tooltip key={1} title="Реестр требований">
                <IconButton
                    fontSize="small"
                    onClick={handleReqLinkClick}
                >
                    <InfoIcon className = {invalidSuccessDefault}/>
                </IconButton>
            </Tooltip>
        )
    };

    if (props.itemType === FolderItemTypes.report || props.itemType === FolderItemTypes.favorites || props.itemType === FolderItemTypes.theme) {
        actionBtns.push(
            <Tooltip key={2} title={props.data.favorite ? "Удалить из избранного" : "Добавить в избранное"}>
                <IconButton
                    fontSize="small"
                    onClick={handleFavoritesClick}
                >
                    <StarsIcon
                        color = {props.data.favorite ? "action": "primary"}
                        className = {props.data.favorite ? classes.favoriteItem : invalidSuccessDefault}
                    />
                </IconButton>
            </Tooltip>
        );
    };

    if (props.itemType === FolderItemTypes.dataset || props.itemType === FolderItemTypes.datasource || props.itemType === FolderItemTypes.reportsDev || props.itemType === FolderItemTypes.filterInstance ) {
        actionBtns.push(
            <Tooltip key={3} title="Показать зависимости">
                <IconButton
                    fontSize="small"
                    onClick={handleDependenciesClick}
                >
                    <ViewComfyIcon className = {invalidSuccessDefault}/>
                </IconButton>
            </Tooltip>
        );
    };

    if (props.itemType === FolderItemTypes.reportsDev) {
        actionBtns.push(
            <Tooltip key={4} title="Просмотр">
                <IconButton
                    fontSize="small"
                    onClick={handleViewClick}
                >
                    <Pageview className = {invalidSuccessDefault} />
                </IconButton>
            </Tooltip>
        );
    }

    if (props.editButton && props.itemType !== FolderItemTypes.report) {
        actionBtns.push(
            <Tooltip key={5} title="Редактировать">
                <IconButton
                    fontSize="small"
                    onClick={handleEditClick}
                >
                    <EditIcon className = {invalidSuccessDefault}/>
                </IconButton>
            </Tooltip>
        );
    };

    if ((props.itemType === FolderItemTypes.job || props.itemType === FolderItemTypes.userJobs) 
        && (props.roleType.isAdmin || props.roleType.isDeveloper)) {
        actionBtns.push(
            <Tooltip key={7} title="Показать SQL запрос">
                <IconButton
                    fontSize="small"
                    onClick={handleShowSqlClick}
                >
                    <SettingsEthernetIcon className = {invalidSuccessDefault} />
                </IconButton>
            </Tooltip>
        )
    };

    if (props.itemType === FolderItemTypes.job || props.itemType === FolderItemTypes.userJobs) {
        if(props.data.canExecute) {
            actionBtns.push(
                <Tooltip  key={8} title="Запустить отчёт заново">
                    <IconButton
                        fontSize="small"
                        onClick={handleRestartClick}
                    >
                        <PlayArrowIcon className = {invalidSuccessDefault}/>
                    </IconButton>
                </Tooltip>
            )
        } else {
            actionBtns.push(
                <Tooltip  key={8} title="У Вас нет прав" disabled>
                    <span>
                        <IconButton
                            fontSize="small"
                            disabled 
                        >
                            <PlayArrowIcon/>
                        </IconButton>
                    </span>
                </Tooltip>
            )
        }
    };

    if (props.itemType === FolderItemTypes.scheduleTasks) {
        actionBtns.push(
            <Tooltip  key={9} title="Запустить отчёт">
                <IconButton
                    fontSize="small"
                    onClick={handleScheduleTaskRunClick}
                >
                    <PlayArrowIcon className = {invalidSuccessDefault}/>
                </IconButton>
            </Tooltip>
        )
    };

    if (props.itemType === FolderItemTypes.scheduleTasks && props.data.status !== 'CHANGED') {
        actionBtns.push(
            <Tooltip  key={10} title="Изменить статус">
                <IconButton
                    fontSize="small"
                    onClick=  {handleScheduleTaskSwitchClick}
                >
                    <SwapHorizontalCircleIcon className = {invalidSuccessDefault}/>
                </IconButton>
            </Tooltip>
        )
    };


    if (props.deleteButton) {
        actionBtns.push(
            <Tooltip key={6} title="Удалить">
                <IconButton
                    fontSize="small"
                    onClick={handleDeleteClick}
                >
                    <DeleteIcon className = {invalidSuccessDefault}/>
                </IconButton>
            </Tooltip>
        )
    };

    return(
        <Card className={[classes.card, isInvalid ? /*classes.invalid*/'' : ''].join(' ')} 
            elevation ={5}
            onContextMenu={(event) => {props.onContextMenu(event, 'item', props.data.id)}}
        >
            <CardHeader classes={{root: classes.cardHead, content: classes.cardHeadContent}}
                titleTypographyProps={{
                    variant: "subtitle2", 
                    align: "left", 
                    color: `${isSuccess? "inherit" : isInvalid ? "error":"textPrimary"}`
                }}
                subheaderTypographyProps={{
                    variant:"caption", 
                    lang: 'ru', 
                    align: 'justify', 
                    component: "div", 
                    color: `${isSuccess? "inherit" : isInvalid ? "error":"textPrimary"}`
                }}
                title={
                    //<div /*style={{wordBreak: 'break-all'}}*/>
                        //<span
                        /*lang='en' Из-за свойства hyphens: 'auto' в классе cardHeadContent переносится по слогам будут только английские заголовки. Т.к. не указала lang
                        Сделано для того, чтобы названия отчетов не переносились, а наборов данных, напр. V_WHS_ALC_ART_OPRT_INCOME_DAY переносились бы, если не помещаются в карточку.
                        */
                      //  >
                           // {
                                cardName
                           // }
                      //  </span>
                   // </div>
                }
                subheader={subheader}
            />

            <CardContent>
                <Table>
                    <TableBody>
                        {(props.itemType === FolderItemTypes.scheduleTasks) &&
                            <>
                                <TableRow>
                                    <TableCell colSpan = {2} align="left"  padding="none"><Typography variant="caption">Отчёт: {props.data.report.name}</Typography></TableCell>
                                </TableRow>

                                <TableRow>
                                    <TableCell colSpan = {2} align="left"  padding="none"><Typography variant="caption">Расписания: {props.data.schedules.map(item=>item.name).join('; ')}</Typography></TableCell>
                                </TableRow>

                                <TableRow>
                                    <TableCell colSpan = {2} align="left"  padding="none"><Typography variant="caption">Получатели: {handleTruncate(recipients, 2)}</Typography></TableCell>
                                </TableRow>
                            </>
                        }
                        
                        <TableRow>
                            <TableCell align="left"  padding="none">
                                <Typography variant="caption">{userNameLabel}: {username}</Typography>
                            </TableCell> 

                            {(props.itemType === FolderItemTypes.scheduleTasks) &&
                                <TableCell align="right" padding="none">
                                    <Typography variant="caption">Создан: {new Date(props.data.created).toLocaleString('ru', options)}</Typography>
                                </TableCell>
                            }

                            {createdDateTime &&
                                <TableCell align="right" padding="none">
                                    <Typography variant="caption">
                                        { props.itemType === FolderItemTypes.favorites ? 'Добавлен:' : (props.itemType === FolderItemTypes.job || props.itemType === FolderItemTypes.userJobs) ? 'Запущен:': 'Создан:'} {new Date(createdDateTime).toLocaleString('ru', options)}
                                    </Typography>
                                </TableCell>
                            }
                        </TableRow>

                        {(props.itemType === FolderItemTypes.scheduleTasks) &&
                            <TableRow>
                                <TableCell align="left" padding="none">
                                    <Typography variant="caption">Дата окончания: {new Date(expirationDate).toLocaleString('ru', options)}</Typography>
                                </TableCell>

                                <TableCell align="right"  padding="none">
                                    <Typography variant="caption">Изменён: {new Date(props.data.modified).toLocaleString('ru', options)}</Typography>
                                </TableCell>
                            </TableRow>
                        }

                        <TableRow>
                            {(props.itemType === FolderItemTypes.job || props.itemType === FolderItemTypes.userJobs)
                            ?   <TableCell align="left" padding="none"><Typography variant="caption">Кол-во строк: {props.data.rowCount}</Typography></TableCell>
                            :   (props.itemType !== FolderItemTypes.scheduleTasks) && <TableCell align="left" padding="none"><Typography variant="caption">id: {props.data.id}</Typography></TableCell>
                            }
                            { modifiedDateTime && 
                                <TableCell align="right"padding="none">
                                    <Typography variant="caption">
                                        {(props.itemType === FolderItemTypes.job || props.itemType === FolderItemTypes.userJobs) ? 'Завершен:': 'Изменён:'} {new Date(modifiedDateTime).toLocaleString('ru', options)}
                                    </Typography>
                                </TableCell>
                            }
                        </TableRow>
                        { (props.itemType === FolderItemTypes.favorites ||  props.isSearched ) &&
                        <TableRow>
                            <TableCell align="left" padding="none"><Typography variant="caption">Путь:</Typography></TableCell>
                            <TableCell align="right"padding="none">
                                <Typography variant="caption">
                                    <Link href="#" onClick={handleLinkPathClick} color="inherit">
                                        {`/ ${props.data.path.map(p => p.name).join(' / ')}`}
                                    </Link>                                
                                </Typography>
                            </TableCell>
                        </TableRow>
                        }
                        { props.itemType === FolderItemTypes.filterInstance &&
                        <TableRow>
                            <TableCell align="left" padding="none"><Typography variant="caption">Тип фильтра</Typography></TableCell>
                            <TableCell align="right"padding="none"><Typography variant="caption">{props.data.type.name}</Typography></TableCell>
                        </TableRow>
                        }
                        { props.itemType === FolderItemTypes.schedules &&
                        <TableRow>
                            <TableCell align="left" padding="none"><Typography variant="caption">Плановая дата старта</Typography></TableCell>
                            <TableCell align="right"padding="none">
                                <Typography variant="caption">{new Date(props.data.planStartDate).toLocaleString('ru', options)}</Typography>
                            </TableCell>
                        </TableRow>
                        }
                    </TableBody>
                </Table>
            </CardContent>
            {actionBtns.length !== 0 &&
                <CardActions className={classes.cardAction}>
                    <span>
                        {actionBtns}
                    </span>
                </CardActions>
            }

        </Card>
    )
}

// function areEqual(prevProps, nextProps){
//     if (prevProps.data && prevProps.data.favorite !== undefined && prevProps.data.favorite !== nextProps.data.favorite){
//         return false
//     }
//     return true
// }

// export default React.memo(ItemCard, areEqual)

export default ItemCard