import React, {useEffect, useState} from "react";
import {useSnackbar} from "notistack";
import {connect} from "react-redux";

import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import CircularProgress from '@material-ui/core/CircularProgress';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import UpdateIcon from '@material-ui/icons/Update';
import Typography from '@material-ui/core/Typography';

// components
import ASMItemCard from "./ASMItemCard";
import AddButton from '../../../FolderContent/AddButton';

// local
import dataHub from "ajax/DataHub";
import {actionAsmAddItemClick, actionAmsRefresh} from "redux/actions/admin/actionAsm";
import {ASMCSS} from "./ASMCSS";

/**
 * @callback actionAsmAddItemClick
 */

/**
 * Список всех объектов ASM
 * @param {Object} props - component properties
 * @param {Array} props.data - начальное состояние компонента
 * @param {actionAsmAddItemClick} props.actionAsmAddItemClick - callback, вызываемый при нажатии кнопки "Добавить"
 * @returns {JSX.Element}
 * @constructor
 */
function ASMList(props) {
    const classes = ASMCSS();
    const {enqueueSnackbar} = useSnackbar();

    const [data, setData] = useState([]);
    const listItems = [];

    const [isSel, setIsSel] = useState(false);

    useEffect(() => {
        setData(props.data);
    }, [props.data]);

    function handleSelect(e) {
        const newData = data.map(item => ({...item, isSelected: e}));
        setData(newData);
        setIsSel(e);
    }

    function handleAddButtonClick() {
        props.actionAsmAddItemClick();
    }

    function handleRefreshButtonClick() {
        const idList = data.filter(item => item.isSelected).map(item => item.id);
        if(idList.length === 0) {
            enqueueSnackbar("Не выбран ни один ASM", {variant: "error"});
            return;
        }

        props.actionAmsRefresh("START")
        dataHub.asmController.refresh(idList, magResponse => {
            if(magResponse.ok) {
                enqueueSnackbar("ASM были успешно обновлены", {variant: "success"});
                handleSelect(false);
            } else {
                enqueueSnackbar(`При обновлении ASM возникла ошибка: ${magResponse.data}`, {variant: "error"});
            }
            props.actionAmsRefresh("FINISH")
        })
    }

    function handleItemCardClick(index) {
        const newData = data.slice();
        const item = data[index];
        newData[index] = {...item, isSelected: !item.isSelected};
        setData(newData);
    }

    data.forEach((item, index) => {
        listItems.push(
            <Grid item
                key={item.id} 
                className = {classes.gridItem}
            >
                <ASMItemCard
                    id={item.id}
                    name={item.name}
                    username={item.userName}
                    description={item.description}
                    created={item.created}
                    modified={item.modified}
                    isSelected={item.isSelected}
                    onClick={() => handleItemCardClick(index)}
                />
            </Grid>
        );
    })

    return (
        <div  className={classes.asmList}>
            <div className={classes.buttonPanel}>
                <FormControlLabel className="MuiButtonBase-root MuiButton-root MuiButton-text MuiButton-textPrimary" //"MuiButtonBase-root MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-textSizeLarge MuiButton-sizeLarge"
                    style={{marginRight: '8px', marginLeft: '0px'}}
                    control={
                        <Checkbox 
                            style={{padding: 0}}
                            className="MuiButton-startIcon MuiButton-iconSizeLarge"
                            checked={isSel}
                            onChange={(e)=>{handleSelect(e.target.checked)}}
                            name="checkedB"
                            color="primary"
                            size="small"
                        />
                    }
                    label={<Typography color="primary" noWrap>ВЫБРАТЬ ВСЁ</Typography>}
                />

                <Button
                    className={classes.usersBtn} 
                    color="primary"
                    startIcon={
                        props.refresh ? <CircularProgress size={20}/> : <UpdateIcon  color="secondary"/>
                    }
                    onClick={handleRefreshButtonClick}
                >
                    <Typography noWrap>Обновить ASM</Typography>
                </Button>
            </div>
            <div className={classes.asmListFlexRelative}>
                <div className={classes.asmListGridFlexAbsolite}>
                    <Grid container >
                        {listItems}
                    </Grid>
                </div>
            </div>
            <AddButton
                showCreateItem = {true}
                itemName = {"объект ASM"}
                onAddItemClick = {handleAddButtonClick}
            />

        </div>
    );
}

const mapStateToProps = state => {
    return {
        refresh: state.asmAdministrationMenuView.refresh
    }
};

const mapDispatchToProps = {
    actionAsmAddItemClick,
    actionAmsRefresh,
};

export default connect(mapStateToProps, mapDispatchToProps)(ASMList);
