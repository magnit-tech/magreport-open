import React, {useState} from 'react';
import Button from "@material-ui/core/Button";
import {List, ListItem, ListItemSecondaryAction, ListItemText, MenuItem, TextField} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import dataHub from "../../../ajax/DataHub";
import {useSnackbar} from "notistack";
import DataLoader from "../../DataLoader/DataLoader";
import ArrowRightIcon from '@material-ui/icons/ArrowRight';
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from '@material-ui/icons/Delete';
import ClearIcon from '@material-ui/icons/Clear';
import DoubleArrowIcon from '@material-ui/icons/DoubleArrow';
import Dialog from "@material-ui/core/Dialog";
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import VisibilityIcon from '@material-ui/icons/Visibility';
import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';
import Tooltip from '@material-ui/core/Tooltip';

// styles
import { ChooserDestinationWindowCSS } from './ModalWindowsCSS'


/**
 * @callback onOkClick
 */
/**
 * Компонент выбора получателей письма
 * @param {Object} props - параметры компонента
 * @param {Boolean} props.open-признак открытого окна
 * @param {Array} props.destinations- список уже выбранных пользователей
 * @param {onOkClick} props.onOkClick - callback, вызываемый при нажатии кнопки Выбрать
 * @param {onExitClick} props.onExitClick - callback, вызываемый при нажатии кнопки Отмена
 * @return {JSX.Element}
 * @constructor
 */
function ChooserDestinationWindow(props) {
    const {enqueueSnackbar} = useSnackbar();
    const classes = ChooserDestinationWindowCSS();

    const [selectType, setSelectType] = useState("all");
    const [visibility, setVisibility] = React.useState(true);
    const [left, setLeft] = React.useState([]);
    const [right, setRight] = React.useState([]);
    const [listRole, setListRole] = React.useState([]);
    const [selectRole, setSelectRole] = React.useState("");
    const [search, setSearch] = React.useState("");


    const updateType = (e) => {
        setSelectType(e.target.value)

        if (e.target.value === "role") {
            dataHub.roleController.getAll(
                response =>
                    setListRole(response.data.filter(item => (item.name.indexOf("SF_HYPER_WHS") === -1 && item.name.indexOf("SF_AMS_WHS") === -1))));
            setLeft([]);
        }

        if(e.target.value === "all"){
            setSelectRole("");
            dataHub.userController.getActualUsers(
                response => {
                    setLeft(response.data.filter(item => item.email !== null && item.email !== "").map(item =>( {...item, invisible: item.name.indexOf(search) === -1 ? true : false, exist: right.find(i => i.name === item.name) ? true : false})));
                }
            )
        }
    }

    function renderListRole(list) {
        let result = []
        list.forEach(role => result.push(<MenuItem key={role.id} value={role.id}> {role.name} </MenuItem>))
        return result;
    }

    function setSelectedRole(role) {
        
        setSelectRole(role);
        dataHub.userController.getUserByRole(role,
            response => {
                setLeft(response.data.filter(item => item.email !== null && item.email !== "").map(item =>( {...item, invisible: item.name.indexOf(search) === -1 ? true : false, exist: right.find(i => i.name === item.name) ? true : false})));
            })

    }

    function handleLoadFailed(message) {
        enqueueSnackbar(`При загрузке данных произошла ошибка: ${message}`, {variant: "error"});
    }

    function updateListChoose(data) {
        let arr = data.filter(item => item.email !== null && item.email !== "");
        setSearch("");
        setSelectRole("");
        setSelectType("all");
        setRight(props.destinations);
        setLeft(arr.map(item =>( {...item, invisible: false, exist: props.destinations.find(i => i.name === item.name) ? true : false})));  
    }

    function addItem(item) {
        if (right.indexOf(item) === -1) {
            setRight(right.concat(item));
            setLeft(left.map(i =>  ({...i, exist: i.name.indexOf(item.name) === -1 ? i.exist : true})));
        }
    }

    function addAllItems() {
        let distinctItems = []
        left.forEach(item => {
            if (!right.find(i=> i.name === item.name)) {
                distinctItems.push(item)
            }
        })
        setRight(right.concat(distinctItems));
        setLeft(left.map(i =>({...i, exist: true})));
    }

    function deleteItem(item) {
        setRight(right.filter(i => i !== item));       
        setLeft(left.map(i =>  ({...i, exist: i.name.indexOf(item.name) === -1 ? i.exist : false})));
    }

    function deleteAllItems() {
        setRight([]);
        setLeft(left.map(i =>({...i, exist: false})));
    }

    function handleSearch(value) {
        setSearch(value);
        setLeft(left.map(i => ({...i, invisible: i.name.indexOf(value) === -1 ? true : false})));
    }

    function renderRow(items, column) {
        let result = []

        if (column === "left") {
            items.forEach(item => result.push(leftItem(item)))
        }
        if (column === "right") {
            items.forEach(item => result.push(rightItem(item)))
        }

        return result;
    }

    function leftItem(item) {
        return (<ListItem
            disabled = {item.exist ? true: false}
            dense
            disableGutters
            key={item.id}>
            <ListItemText primary={item.name} secondary={item.email}/>
            <ListItemSecondaryAction>
                <IconButton
                    disabled = {item.exist ? true: false}
                    edge="end"
                    aria-label="add"
                    value={item.id}
                    onClick={() => {
                        addItem(item)
                    }}>
                    <ArrowRightIcon/>
                </IconButton>
            </ListItemSecondaryAction>
        </ListItem>)
    }

    function rightItem(item) {
        return (<ListItem
            dense
            disableGutters
            key={item.id}>
            <ListItemText primary={item.name} secondary={item.email}/>
            <ListItemSecondaryAction>
                <IconButton
                    edge="end"
                    aria-label="add"
                    value={item.id}
                    onClick={() => {
                        deleteItem(item)
                    }}>
                    <ClearIcon/>
                </IconButton>
            </ListItemSecondaryAction>
        </ListItem>)
    }

    return (
        <Dialog open={props.open} PaperProps={{ classes: {root: classes.root }}}>
            <DataLoader
                loadFunc={dataHub.userController.getActualUsers}
                loadParams={[]}
                onDataLoaded={(data) => {
                    updateListChoose(data)
                }}
                onDataLoadFailed={handleLoadFailed}>
                <DialogActions className={classes.topDialogAction}>
                    <Grid container> 
                        <Grid item xs={12}>
                            <TextField id="select"  fullWidth label="Пользователи" value={selectType} onChange={updateType} select>
                                <MenuItem value="all">Все пользователи</MenuItem>
                                <MenuItem value="role">Роль</MenuItem>
                                <MenuItem value="ldap" disabled={true}> Доменная группа</MenuItem>
                            </TextField>
                        </Grid>
                        <Grid item xs={12}>
                            {selectType === "role" ?
                                <TextField fullWidth id="selectRole" value={selectRole} label="Список ролей"
                                    onChange={value => setSelectedRole(value.target.value)} select>
                                    {renderListRole(listRole)}
                                </TextField> : <div></div>
                            }
                        </Grid>
                        <Grid item xs={12}>
                            <TextField 
                                value={search}
                                style={{marginBottom: '8px'}} 
                                fullWidth 
                                label="Поиск" 
                                onChange={value => handleSearch(value.target.value)}
                            />
                        </Grid>
                        <Grid item xs={6}  className={classes.leftBtnPanel}>
                        <Tooltip title = "Показывать выбранные"> 
                                <Button
                                    color='primary' 
                                    onClick={() => setVisibility(!visibility)}
                                    endIcon={visibility ? <VisibilityIcon color="primary"/> : <VisibilityOffIcon color="secondary"/>}
                                > 
                                    {visibility ? 'Скрыть' : 'Показать все'}
                                </Button>
                            </Tooltip>
                            <Button
                                color="primary"
                                style={{marginRight: '8px'}}
                                onClick={addAllItems}
                                endIcon={<DoubleArrowIcon color='primary'/>}>Добавить все 
                            </Button>
                            
                            
                        </Grid>
                        <Grid item xs={6}  className={classes.rightBtnPanel}>
                            <Button
                                color="primary"
                                onClick={deleteAllItems}
                                endIcon={<DeleteIcon color='primary'/>}>Удалить все
                            </Button>                
                        </Grid>
                        
                    </Grid>
                </DialogActions>
                <DialogContent className={classes.flx}>  
                    <Grid 
                        container 
                        spacing={1}
                        justifyContent="center"
                        wrap="nowrap" 
                        className={classes.leftRightPanel}
                    >
                        <Grid item key={3} xs={6} className={classes.flex8px}>                        
                            <div className={classes.flexRelative}>
                                <List className={classes.flexAbsolute}>
                                    {renderRow(left.filter(i=> i.invisible === false && (visibility || i.exist === false)), "left")}
                                </List>
                            </div>                       
                        </Grid>
                        <Grid item key={4} xs={6} className={classes.flex8px}>
                            <div className={classes.flexRelative}>
                                <List className={classes.flexAbsolute}>
                                    {renderRow(right, "right")}
                                </List>
                            </div>
                        </Grid>
            
                    </Grid>               
            </DialogContent>
            <DialogActions className={classes.indent}>
                <Button
                    type="submit"
                    variant="contained"
                    size="small"
                    color="primary"
                    onClick={() => {props.onOkClick(right)}}>Сохранить
                </Button>
                <Button
                    type="submit"
                    variant="contained"
                    size="small"
                    color="primary"
                    onClick={props.onExitClick}>Отменить
                </Button>
            </DialogActions>
            </DataLoader>
        </Dialog>
    )
}

export default ChooserDestinationWindow