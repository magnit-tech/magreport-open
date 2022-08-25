import React, { useState } from 'react';
import { connect } from 'react-redux';
import { useSnackbar } from 'notistack';

// components
import Card from '@material-ui/core/Card';
import Grid from '@material-ui/core/Grid';
import AddIcon from '@material-ui/icons/Add';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Toolbar from '@material-ui/core/Toolbar';

// local
import DataLoader from 'main/DataLoader/DataLoader';
import FilterWrapper from 'main/Report/FilterWrapper';
import FilterWrapperViewer from 'main/Report/viewer/FilterWrapperViewer';
import Rolelist from 'main/Main/Administration/Roles/RoleList';
import DesignerPage from '../../Development/Designer/DesignerPage';
import AsyncAutocomplete from 'main/AsyncAutocomplete/AsyncAutocomplete';

// dataHub
import dataHub from 'ajax/DataHub';

// styles
import { GridCSS } from "./SecurityFilterCSS";
import { UsersCSS } from "../Users/UsersCSS";

// actions 
import { actionRolesLoaded, actionRolesLoadFailed, actionRoleAdd, actionRoleDelete, actionSelectRole } from 'redux/actions/admin/actionRoles'
import { actionRoleSettingsLoaded, actionRoleSettingsChanged, actionSetLastFilterValue, actionRoleSettingsCountChange } from 'redux/actions/admin/actionSecurityFilters'

/**
 * @callback onExit
 */

/**
 * Компонент для настройки ролей Фильтра безопасности
 * @param {Object} props - параметры компонента
 * @param {Number} props.securityFilterId - id фильтра безопасности для ролей и значений из БД
 * @param {Object} props.filterInstance - объект экземпляра фильтра
 * @param {onExit} props.onExit - callback при выходе
 * @param {string} props.mode - edit/view режим отображения: редактировать/просмотр
 */
function SecurityFilterRoles(props){

    const { enqueueSnackbar } = useSnackbar();
    const classes = GridCSS();
    const topElemsClasses = UsersCSS();
    const [addedRole, setAddedRole] = useState(null)
    
    function handleDataLoaded(loadedData){
        const loadedDataExt = loadedData.roleSettings.map(r => {return {...r, roleId: r.role.id}})
        props.actionRolesLoaded({rolePermissions: loadedData.roleSettings})
        props.actionRoleSettingsLoaded(loadedDataExt)
        props.actionSelectRole(null)
    }

    function handleDataLoadFailed(){
        enqueueSnackbar("Не удалось загрузить информацию! Попробуйте позже...", {variant : "error"});
    }

    function handleSelectRole(roleId){
        props.actionSelectRole(roleId)
        props.actionSetLastFilterValue(roleId)
    }

    function handleSave(){
        let roleNames = []
        for (let r of props.roleSettings){
            if (r.tuples.length === 0){
                roleNames.push(r.role.name)
            }
        }
        if (roleNames.length > 0){
            enqueueSnackbar(`Не установлены значения фильтра для следующих ролей: ${roleNames.join(', ')}`, {variant : "error"});
        }
        else {
            let roleSet = []
            for (let r of props.roleSettings){
                let element = {...r}
                delete element.role
                roleSet.push(element)
            }
            dataHub.securityFilterController.setRoleSettings(
                props.securityFilterId,
                roleSet,
                handleSetRoles
            )
        }
    }

    function handleCancel(){
        props.actionSelectRole(null)
        props.onExit()
    }

    function handleSetRoles(magrepResponse){
        if (magrepResponse.ok){
            enqueueSnackbar("Значения фильтров безопасности установлены", {variant : "success"});
            props.actionSelectRole(null)
            props.onExit()
        }
        else {
            enqueueSnackbar(`Не удалось установить значения для фильтра безопасности: ${magrepResponse.data}`, {variant : "error"});
        }
    }

    function handleAddRole(){
        if(!addedRole) {
            return;
        }
        if (props.items.findIndex(r => r.roleId === addedRole.roleId) === -1){
            props.actionRoleAdd(addedRole)
            props.actionRoleSettingsCountChange("add", addedRole.roleId, addedRole)
        }
        else {
            enqueueSnackbar('Роль уже есть в списке', {variant : "error"})
        }
    }

    function handleDeleteRole(roleId){
        props.actionRoleDelete(roleId)
        props.actionRoleSettingsCountChange("delete", roleId, null)
    }

    function handleRoleAutocompleteChange(role) {
        if(role) {
            setAddedRole({...role, roleId: role.id})
        } else {
            setAddedRole(null);
        }
    }


    const topElem =                     
        <div className={topElemsClasses.roleHideSelect}>
            <div className={topElemsClasses.roleAutocompleteDiv}>
                <AsyncAutocomplete
                    disabled = {false}
                    typeOfEntity = {"role"}
                    filterOfEntity = {(item) => item.typeId === 1 /*"SECURITY_FILTER"*/}
                    onChange={handleRoleAutocompleteChange}
                /> 
            </div>
            <Button color="primary"
                className={topElemsClasses.addButton}
                variant="outlined"
                disabled = {false}
                onClick={handleAddRole}
            >
                <AddIcon/>Добавить
            </Button>
        </div>
    
    return (
        props.securityFilterId ? 
            <DataLoader
                loadFunc = {dataHub.securityFilterController.getRoleSettings}
                loadParams = {[props.securityFilterId]}
                onDataLoaded = {handleDataLoaded}
                onDataLoadFailed = {handleDataLoadFailed}
            >
                <DesignerPage 
                    onSaveClick={props.mode==='edit' ? handleSave : null}
                    onCancelClick={props.mode==='edit' ? handleCancel : null}
                    twoColumn = {true}

                >
                    <Grid container className={classes.securityFilterGrid}>
                        <Grid item xs={6} className={classes.securityFilterGridItemLeft}>
                            <Rolelist 
                                itemsType={props.itemsType}
                                items={props.items}
                                selectedRole={props.selectedRole}
                                showDeleteButton={props.mode==='edit' ? true: false}
                                hideSearh={false}
                                showCheckboxRW={false}
                                topElems = {props.mode==='edit' ? topElem : null}
                                onSelectRole={handleSelectRole}
                                onDelete={handleDeleteRole}
                                from="SecurityFilterRoles"
                            />
                        </Grid>
                        <Grid item xs={6} className={classes.securityFilterGridItemRight}>
                            <Card elevation = {3} className={classes.filterList}>
                                <Toolbar position="fixed"
                                    className={classes.titlebar}
                                    variant="dense"
                                >
                                    <Typography className={classes.title} variant="h6">{props.filterInstance.name}</Typography>
                                </Toolbar>
                                { // selectedRole может быть 0 - это роль Admin
                                props.selectedRole !== undefined && props.selectedRole !== null &&
                                <div style={{display: 'flex', flex: 1, flexDirection: 'column', position: 'relative'}}>
                                    <div className = {classes.filterListBox}>
                                        <div style={{padding: '8px'}}>
                                        {props.mode==='edit' ?
                                            <FilterWrapper
                                                key={props.selectedRole}
                                                filterData = {props.filterInstance}
                                                lastFilterValue = {props.lastFilterValue}
                                                toggleClearFilters = {false}
                                                onChangeFilterValue = {valueObj => props.actionRoleSettingsChanged(props.selectedRole, valueObj)}
                                            /> :
                                            <FilterWrapperViewer
                                                key={props.selectedRole}
                                                filterData = {props.filterInstance}
                                                lastFilterValue = {props.lastFilterValue}
                                                toggleClearFilters = {false}
                                                onChangeFilterValue = {valueObj => props.actionRoleSettingsChanged(props.selectedRole, valueObj)}
                                            />
                                        }
                                        </div>
                                    </div>
                                    </div>
                                }
                            </Card>
                            
                        </Grid>
                    </Grid>
                </DesignerPage>
            </DataLoader>
        : 
            <Typography 
                align="center"
                color="textSecondary"
                gutterBottom
            >
                Сначала заполните поля фильтра безопасности
            </Typography>
    )
}

const mapStateToProps = state => {
    return {
        selectedRole: state.roles.selectedRole,
        items: state.roles.data,
        lastFilterValue: state.securityFilters.lastFilterValue,
        roleSettings: state.securityFilters.data,
    }
}

const mapDispatchToProps = {
    actionRolesLoaded,
    actionRolesLoadFailed,
    actionRoleAdd,
    actionRoleDelete,
    actionSelectRole,
    actionRoleSettingsLoaded, 
    actionRoleSettingsChanged,
    actionSetLastFilterValue,
    actionRoleSettingsCountChange,
}

export default connect(mapStateToProps, mapDispatchToProps)(SecurityFilterRoles);