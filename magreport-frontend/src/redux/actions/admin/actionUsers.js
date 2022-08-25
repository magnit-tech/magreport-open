import {FolderItemTypes} from 'main/FolderContent/FolderItemTypes';
import { USERS_LIST_LOADED, 
    USERS_LIST_LOAD_FAILED,
    USERS_LIST_ADD,
    USERS_SELECTED,
    USERS_LIST_ADDED,
    USERS_LIST_ADD_FAILED,
    USERS_LIST_DELETE,
    USERS_LIST_DELETED,
    USERS_LIST_DELETE_FAILED,
    USERS_LIST_CHECKED,
    USERS_LIST_ALL_CHECKED,
    USERS_LIST_MANAGE_STARTED,
    USERS_LIST_MANAGE,
    USERS_LIST_MANAGE_FAILED,
    USERS_LIST_ROLE_DELETE,
    USERS_LIST_ROLE_DELETED,
    USERS_LIST_ROLE_DELETE_FAILED,
} from 'redux/reduxTypes'
import store from 'redux/store';
import dataHub from 'ajax/DataHub';

export const actionUsersLoaded = (data, itemsType) =>{

    let usersData = itemsType === FolderItemTypes.roles ? data.users : data

    usersData.sort((a, b)=> {
        const aIsAdmin = a.roles.findIndex(r => r.name === 'ADMIN') > -1 ? 0 : 1
        const bIsAdmin = b.roles.findIndex(r => r.name === 'ADMIN') > -1 ? 0 : 1

        if (`${aIsAdmin}_${a.name}` > `${bIsAdmin}_${b.name}`){
            return 1
        }
        if (`${aIsAdmin}_${a.name}` < `${bIsAdmin}_${b.name}`){
            return -1
        }
        return 0
    })


    return {
        type: USERS_LIST_LOADED,
        data: usersData
    }    
}

export const actionUsersLoadFailed = error =>{
    return {
        type: USERS_LIST_LOAD_FAILED,
        error
    }
}

export const actionUserChecked = userId => {
    return {
        type: USERS_LIST_CHECKED,
        userId
    }
}

export const actionAllUserChecked = operation => {
    return {
        type: USERS_LIST_ALL_CHECKED,
        operation
    }
}

export const actionUserAdd = (id, users) => {
    dataHub.roleController.addUsers(id, users, m => handleAddUser(m, users[0]))
    
    return {
        type: USERS_LIST_ADD,
    }
}

export const actionUserSelect = (id) => {
    return {
        type: USERS_SELECTED,
        id
    }
}

function handleAddUser(magrepResponse, userId){
    const type = magrepResponse.ok ? USERS_LIST_ADDED : USERS_LIST_ADD_FAILED;
    const data = magrepResponse.ok ? magrepResponse.data.users : magrepResponse.data 

    store.dispatch({
        type,
        data,
        userId,
    })
}

export const actionUserDelete = (id, users) => {
    dataHub.roleController.deleteUsers(id, users, m => handleDeleteUser(m, users))
    
    return {
        type: USERS_LIST_DELETE,
    }
}

function handleDeleteUser(magrepResponse, users){
    let type = magrepResponse.ok ? USERS_LIST_DELETED : USERS_LIST_DELETE_FAILED;
    let data = magrepResponse.ok ? magrepResponse.data.users : magrepResponse.data 

    store.dispatch({
        type,
        data,
        users,
    })
}

export const actionManageUsers = (operation, users) => {
    if (operation === "DISABLED" || operation === "ACTIVE"){
        dataHub.userController.setStatus(operation, users, m => handleManageUsersResponse(m, operation, users))
    }
    else {
        if (users.length > 0){
            dataHub.userController.logoff(users, m => handleManageUsersResponse(m, operation, users))
        }
        else {
            dataHub.userController.logoffAll(m => handleManageUsersResponse(m, operation, users))
        }
    }

    return {
        type: USERS_LIST_MANAGE_STARTED,
    }
}

function handleManageUsersResponse(magrepResponse, operation, users) {
    let type = magrepResponse.ok ? USERS_LIST_MANAGE : USERS_LIST_MANAGE_FAILED;
    let data = magrepResponse.data

    store.dispatch({
        type,
        operation,
        data,
        users,
    })
}

export const actionUserRoleDelete = (id, userId) => {
    dataHub.roleController.deleteUsers(id, [userId], m => handleUserRoleDelete(m, id, userId))
    
    return {
        type: USERS_LIST_ROLE_DELETE,
    }
}

function handleUserRoleDelete(magrepResponse, roleId, userId){
    let type = magrepResponse.ok ? USERS_LIST_ROLE_DELETED : USERS_LIST_ROLE_DELETE_FAILED;

    store.dispatch({
        type,
        roleId,
        userId,
    })
}