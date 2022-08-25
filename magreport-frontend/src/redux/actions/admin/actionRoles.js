import {ROLE_LIST_LOADED, 
        ROLE_LIST_LOAD_FAILED, 
        ROLE_CHANGE_WRITE_RIGHTS, 
        ROLE_ADD,
        ROLE_DELETE,
        ROLE_FILTER,
        ROLE_SELECTED,
        ROLE_SELECTED_FOLDER_TYPE
} from 'redux/reduxTypes'

export const actionRolesLoaded = data =>{
    
    return {
        type: ROLE_LIST_LOADED,
        data: data.rolePermissions
    }    
}

export const actionRolesLoadFailed = error =>{
    return {
        type: ROLE_LIST_LOAD_FAILED,
        error
    }
}

export const actionRolesChangeWriteRights = (index, value) => {
    return {
        type: ROLE_CHANGE_WRITE_RIGHTS,
        index,
        value,
    }
}

export const actionRoleAdd = role => {
    return {
        type: ROLE_ADD,
        role,
    }
}

export const actionRoleDelete = roleId => {
    return {
        type: ROLE_DELETE,
        roleId,
    }
}

export const actionFilterRoles = filterStr => {
    return {
        type: ROLE_FILTER,
        filterStr
    }
}

export const actionSelectRole = roleId => {
    return {
        type: ROLE_SELECTED,
        roleId
    }
}

export const actionRoleSelectFolderType = folderType => {
    return {
        type: ROLE_SELECTED_FOLDER_TYPE,
        folderType
    }
}