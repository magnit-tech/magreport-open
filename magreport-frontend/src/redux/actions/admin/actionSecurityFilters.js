import {SF_ROLE_SETTINGS_LOADED, 
    SF_ROLE_SETTINGS_CHNAGED, 
    SF_SET_LAST_FILTER_VALUE,
    SF_ROLE_SETTINGS_COUNT_CHANGE,
} from 'redux/reduxTypes'

export const actionRoleSettingsLoaded = data =>{
    
    return {
        type: SF_ROLE_SETTINGS_LOADED,
        data
    }    
}

export const actionRoleSettingsCountChange = (operation, roleId, role) =>{
    
    return {
        type: SF_ROLE_SETTINGS_COUNT_CHANGE,
        operation,
        roleId,
        role,
    }    
}

export const actionRoleSettingsChanged = (roleId, value) =>{
    
    return {
        type: SF_ROLE_SETTINGS_CHNAGED,
        roleId,
        value,
    }    
}

export const actionSetLastFilterValue = roleId =>{
    
    return {
        type: SF_SET_LAST_FILTER_VALUE,
        roleId,
    }    
}