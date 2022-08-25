import React, {useState, useEffect} from 'react';

// componenets
import Button from '@material-ui/core/Button';
import BlockIcon from '@material-ui/icons/Block';
import LockIcon from '@material-ui/icons/Lock';
import LockOpenIcon from '@material-ui/icons/LockOpen';
import Checkbox from '@material-ui/core/Checkbox';
import Hidden from '@material-ui/core/Hidden';
import IconButton from '@material-ui/core/IconButton';
import clsx from 'clsx';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import { 
    Card,
    List,
    Toolbar,
    Typography,
    InputBase
} from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';
import Tooltip from '@material-ui/core/Tooltip';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import Filter9PlusIcon from '@material-ui/icons/Filter9Plus';

// local
import UserCard from './UserCard'

// styles 
import { UsersCSS } from "./UsersCSS";

function UserList(props){
    const classes = UsersCSS();

    const [userPage, setUserPage] = useState(0)
    const [viewAll, setViewAll] = useState(false)

    const [selectedUser, setSelectedUser] = useState(props.selectedUser !== -1 ? props.selectedUser : -1);
    const [checkedAllUsers, setCheckedAllUsers] = React.useState(false);
    const [userFilterValue, setUserFilterValue] = useState("");
    const [needUserScroll, setNeedUserScroll] = useState(Boolean(props.needUserScroll));

    function filterUsers(){
        let filteredList = []
        const filterStr = userFilterValue.toLowerCase()
        
        for (let i in props.items) {
            if (props.items[i].name.toLowerCase().indexOf(filterStr) > -1) {
                filteredList.push(props.items[i])
            }
        }
        
        const countUsers = filteredList.length
        if (!viewAll){
            filteredList = filteredList.splice(userPage*50, 50)
        }
        return {filteredList, countUsers}
    }

    function getCheckedUserNames(){
        let users = []
        for (let u of props.items) {
            if (u.blockUserCheck) users.push(u.name)
        }
        return users
    }

    function handleCheckedAll(){
        props.onAllUsersChecked(!checkedAllUsers)
        setCheckedAllUsers(!checkedAllUsers);
    }

    function handleFilterUser (e) {
        setUserPage(0)
        setUserFilterValue(e.target.value);
    }

    function handleSelectUser(id, index) {
        setNeedUserScroll(false);
        setSelectedUser(id);
        
        if (props.onSelectUser) props.onSelectUser(id)
    }

    const listItems=[]
    const {filteredList, countUsers} = filterUsers()
 
    filteredList.forEach((i, index) => 
        listItems.push(
            <UserCard
                id = {i.id}
                key={i.id} 
                index={index}
                itemsType={props.itemsType}
                userDesc={i} 
                roleId={props.roleId}
                isSelected={selectedUser===i.id}
                onSelectedUser={handleSelectUser}
                enableRoleReload={props.enableRoleReload}
                showDeleteButton={props.showDeleteButton}
                showCheckbox={props.showCheckbox ? props.showCheckbox : false}
            />
        )
    )

    useEffect(() => {
        let userIndex = props.items.findIndex(item => item.id === props.selectedUser);

        if (userIndex !==-1) {setUserPage(Math.ceil((userIndex/50)-1))};

        var scrolledElement = document.getElementById(props.selectedUser);

        if (props.selectedUser !== -1 && props.from === "UserDesigner" && scrolledElement){
          scrolledElement.scrollIntoView({block: "start", behavior: "smooth"});
        }
        setNeedUserScroll(false);
    }, [needUserScroll]); // eslint-disable-line

    return (
        <Card elevation={3} className={classes.userListCard} /*className={clsx(classes.userList, {[classes.userListRole]: props.from!=="UserDesigner"})}*/>
            <Toolbar position="fixed"
                    className={classes.titlebar}
                    variant="dense">
                <Typography className={classes.title} variant="h6">Пользователи</Typography>
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
                        onChange={handleFilterUser}
                        value={userFilterValue}
                    />
                </div>
            </Toolbar>
            { props.showControlButtons ?
                <span className={classes.spanBtn}>
                    <div>
                    <Checkbox
                        onChange={handleCheckedAll}
                        checked={checkedAllUsers}
                        color="primary"
                    />
                    </div>
                    <Hidden only={['xs','sm','md']}>
                        <Button className={classes.usersBtn}
                            color="primary"
                            startIcon={<LockIcon color="secondary"/>}
                            onClick={() => props.onManageUsers("DISABLED", getCheckedUserNames())}
                        >
                            <Typography noWrap className={classes.btnText}>Lock</Typography>
                        </Button>
                        <Button className={classes.usersBtn} 
                            color="primary"
                            startIcon={<LockOpenIcon/>}
                            onClick={() => props.onManageUsers("ACTIVE", getCheckedUserNames())}
                        >
                            <Typography noWrap className={classes.btnText}>Unlock</Typography>
                        </Button>
                        <Button className={classes.usersBtn} 
                            color="primary"
                            startIcon={<BlockIcon color="secondary"/>}
                            onClick={() => props.onManageUsers("LOGGOFF", getCheckedUserNames())}
                        >
                            <Typography noWrap className={classes.btnText}>Logoff</Typography>
                        </Button>
                        <Button className={classes.usersBtn} 
                            color="primary"
                            startIcon={<ExitToAppIcon color="secondary"/>}
                            onClick={() => props.onManageUsers("LOGGOFFALL", [])}
                        >
                            <Typography noWrap className={classes.btnText}>Logoff All</Typography>
                        </Button>
                    </Hidden>
                    <Hidden only={['lg','xl']}>
                        <Tooltip title="Заблокировать">
                            <IconButton
                                color="secondary"
                                onClick={() => props.onManageUsers("DISABLED", getCheckedUserNames())}
                            >
                                <LockIcon />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Разблокировать">
                            <IconButton
                                color="primary"
                                onClick={() => props.onManageUsers("ACTIVE", getCheckedUserNames())}
                            >
                                <LockOpenIcon />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Завершить сессию">
                            <IconButton
                                color="secondary"
                                onClick={() => props.onManageUsers("LOGGOFF", getCheckedUserNames())}
                            >
                                <BlockIcon />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Завершить сессии всех пользователей">
                            <IconButton
                                color="secondary"
                                onClick={() => props.onManageUsers("LOGGOFFALL", [])}
                            >
                                <ExitToAppIcon />
                            </IconButton>
                        </Tooltip>
                    </Hidden>
                </span>
                : ""
            }
            <div className={classes.userListRelative}>
                <List data-testid="users_list" dense className={clsx(classes.userListBox, {[classes.userListBoxRole]: props.from!=="UserDesigner"})}>
                    {listItems}
                </List>
            </div>
            <div className={classes.bottomButtons}>
                <Tooltip title="Предыдущая страница">
                    <span>
                        <IconButton 
                            size='medium'
                            aria-label="Предыдущие" 
                            onClick={() => setUserPage(userPage-1)} 
                            disabled={userPage === 0 || viewAll}
                        >
                            <ArrowBackIosIcon />
                        </IconButton>
                    </span>
                </Tooltip>
                <Tooltip title="Следующая страница">
                    <span>
                        <IconButton 
                            size='medium'
                            aria-label="Следующие" 
                            onClick={() => setUserPage(userPage+1)} 
                            disabled={userPage ===  parseInt(countUsers % 50 === 0 ? countUsers / 50 -1 : countUsers / 50) || viewAll }
                        >
                            <ArrowForwardIosIcon />
                        </IconButton>
                    </span>
                </Tooltip>
                <Tooltip title={viewAll ? "Постранично" : "Показать всех"}>
                    <span>
                        <IconButton 
                            size='medium'
                            aria-label={viewAll ? "Постранично" : "Показать всех"} 
                            color={viewAll ? "secondary" : "default"}
                            onClick={() => setViewAll(!viewAll)}
                        >
                            <Filter9PlusIcon />
                        </IconButton>
                    </span>
                </Tooltip>
            </div>
        </Card>     
    )
}

export default UserList