import React from 'react';
import { Card,
    List,
    Toolbar,
    Typography,
    InputBase
} from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';

import { useSnackbar } from 'notistack';
import {useState} from 'react';

// local
import DomainGroupCard from './DomainGroupCard'
import dataHub from 'ajax/DataHub';
import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';

// styles 
import { DomainGroupCSS } from "./DomainGroupCSS";

function DomainGroupList(props){
    const classes = DomainGroupCSS();

    const [selectedDomainGroup, setSelectedDomainGroup] = useState(-1);
    const { enqueueSnackbar } = useSnackbar();
    const [domainGroupFilterValue, setDomainGroupFilterValue] = useState("");

    const listItems=[]

    function filterDomainGroups(){
        let filteredList = []
        for (let u of props.items) {
            if (u.indexOf(domainGroupFilterValue) >= 0) filteredList.push(u)
        }
        return filteredList
    }

    function handleFilterDomainGroup (e) {
        setDomainGroupFilterValue(e.target.value);
    }

    function handleDeleteDomainGroup (domainGroupName) {
        if (props.itemsType === FolderItemTypes.roles){
            dataHub.roleController.deleteDomainGroups(props.roleId, [domainGroupName], handleDeleteDomainGroupFromRoleResponse)
        }
        else {
            
        }
    }

    function handleDeleteDomainGroupFromRoleResponse (magrepResponse) {
        if (magrepResponse.ok) {
            enqueueSnackbar("Доменная группа удалена!", {variant : "success"});
        }
        else {
            enqueueSnackbar("Не удалось удалить доменную группу", {variant : "error"});
        }

        props.onDeleteDomainGroupFromRoleResponse (magrepResponse)
    }
    
    for (let i of filterDomainGroups()){
        listItems.push(
            <DomainGroupCard 
                key={i} 
                itemsType={props.itemsType}
                domainGroupDesc={i} 
                roleId={props.roleId}
                isSelected={selectedDomainGroup===i}
                setSelectedDomainGroup={setSelectedDomainGroup}
                enableRoleReload={props.enableRoleReload}
                onDeleteDomainGroup={handleDeleteDomainGroup}
                showDeleteButton={props.showDeleteButton}
            />
        )
    }

    return (
        <Card elevation={3} className={classes.domainGroupList}>
            <Toolbar position="fixed"
                className={classes.titlebar}
                variant="dense"
            >
                <Typography className={classes.title} variant="h6">Доменные группы</Typography>
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
                        onChange={handleFilterDomainGroup}
                        value={domainGroupFilterValue}
                    />
                </div>
            </Toolbar>
                <div style={{display: 'flex', flex: 1, flexDirection: 'column', position: 'relative'}}>
                    <List dense className={classes.domainGroupListBox}>
                        {listItems}
                    </List>
                </div>
        </Card>
    )
}

export default DomainGroupList