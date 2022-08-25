import React from 'react';
import {useState} from 'react';
import RoleCard from './RoleCard'
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import { 
    Card,
    List,
    Toolbar,
    Typography,
    InputBase
} from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';

// styles 
import { RolesCSS } from "./RolesCSS";

function RoleList(props){
    const classes = RolesCSS();

    const [parentsKeySelected, setParentsKeySelected] = useState(-1);
    const [roleFilterValue, setRoleFilterValue] = useState("");

    function handleFilterRole (e) {
        setRoleFilterValue(e.target.value);
    }

    function filterRoles(){
        let filteredList = []
        for (let u of props.items) {
            if (u.name.toLowerCase().indexOf(roleFilterValue.toLowerCase()) >= 0) filteredList.push(u)
        }
        return filteredList
    }

    function handleSelectRole(id) {
        setParentsKeySelected(parentsKey)
        if (props.onSelectRole) props.onSelectRole(id)
    }

    function handleDelete(id) {
        if (props.onDelete) props.onDelete(id)
    }
    
    let parentsKey = props.parentsKey ? props.parentsKey : -1 // если одна роль есть у нескольких пользователей, при переключении между ними не нужно ее подсвечиывать

    const listItems=[]
    filterRoles().forEach((i, index) => {
        listItems.push(
            <RoleCard 
                key={i.id + `_${index}_${props.parentsKey}`}
                index={index}
                data={i}
                isSelected={(props.selectedRole===i.id) && (parentsKey===parentsKeySelected)}
                itemsType={FolderItemTypes.roles}
                showCheckboxRW={props.showCheckboxRW}
                showDeleteButton={props.showDeleteButton}
                showEditButton = {props.showEditButton}
                showViewButton = {props.showViewButton}
                onChangeSelectedRole={handleSelectRole}
                onDelete={handleDelete}
            />
        )
    })

    return (
        <div className={classes.roleListFlex}>
            {!props.hideSearh ?
                <Card elevation={3} className={classes.roleList}>
                    <Toolbar position="fixed"
                        className={classes.titlebar}
                        variant="dense"
                    >
                        <Typography className={classes.title} variant="h6">Роли</Typography>
                        <div className={classes.search}>
                            <div className={classes.searchIcon}>
                                <SearchIcon />
                            </div>
                            <InputBase
                                placeholder="Поиск…"
                                classes={{
                                    root: classes.inputRoot,
                                    input: classes.inputInput,
                                }}
                                onChange={handleFilterRole}
                                value={roleFilterValue}
                            />
                        </div>
                    </Toolbar>
                    {props.topElems ? props.topElems : ""}
                    <div className={classes.roleListFlexRelative}>
                        <List  data-testid="roles_list" dense className={classes.roleListBox}>
                            {listItems}
                        </List>
                    </div>
                </Card>
                :
                <div  className={classes.roleListFlex}>
                    {props.topElems ? props.topElems : ""}
                    <div className={classes.roleListFlexRelative}>
                        <List  data-testid="roles_list" dense className={classes.roleListBox}>
                            {listItems}
                        </List>
                    </div>
                </div>
            }
        </div>
    )
}

export default RoleList