import { 
    USERS_LIST_LOADED, 
    USERS_LIST_LOAD_FAILED, 
    USERS_LIST_ADDED, 
    USERS_LIST_DELETED, 
    USERS_LIST_CHECKED, 
    USERS_LIST_MANAGE,
    USERS_LIST_ALL_CHECKED,
    USERS_LIST_ROLE_DELETED,
    USERS_SELECTED
} from 'redux/reduxTypes'

const initialState = {
    status: 'init',
    error: '',
    data:[]
}

export const usersReducer = (state = initialState, action) => {
    switch (action.type){
        case USERS_LIST_LOADED:
            return {
                ...state, data: action.data
            }
        case USERS_LIST_ADDED:
            {
                const userRoles = action.data.find(u => u.id === action.userId).roles
                const users = [...state.data]
                const index = users.findIndex(u => u.id === action.userId)
                users.splice(index, 1, {...users[index], roles: userRoles})

                return {
                    ...state, data: users
                }
            }
        case USERS_LIST_DELETED:
            {
                const users = [...action.data].filter(u => action.users.indexOf(u.id) < 0)

                return {
                    ...state, data: users
                }
            }
        case USERS_LIST_LOAD_FAILED:
            return {
                ...state, data: []
            }
        case USERS_LIST_ALL_CHECKED:
            {
                const users = [...state.data]
                users.forEach(u => {
                    if (action.operation){
                        let isAdmin = false
                        if (u.roles.filter(r => r.name === "ADMIN").length > 0){
                            isAdmin=true
                        }
                        if (!isAdmin){
                            u.blockUserCheck = true
                        }
                    }
                    else {
                        u.blockUserCheck = false
                    }
                })
                return {...state, data: users}
            }
        case USERS_LIST_CHECKED:
            {
                const users = [...state.data]
                const index = users.findIndex( u => u.id === action.userId)
                const user = {...state.data[index]}
                user.blockUserCheck=!user.blockUserCheck
                users.splice(index, 1, user)
                return {...state, data: users}
            }
        case USERS_LIST_MANAGE:
            {
                if (action.operation === "DISABLED" || action.operation === "ACTIVE"){
                    const users = [...state.data]
                    users.forEach(u => {
                        if (action.users.indexOf(u.name)>-1){
                            u.status = action.operation
                        }
                    })
                    return {...state, data: users}
                }
                else {
                    return state
                }
                
            }
        case USERS_LIST_ROLE_DELETED:
            {
                const users = [...state.data]
                const index = users.findIndex( u => u.id === action.userId)
                const user = {...state.data[index]}
                const roleIndex = user.roles.findIndex(r => r.id === action.roleId)
                user.roles.splice(roleIndex, 1)
                users.splice(index, 1, user)

                return {
                    ...state, data: users
                }
            }
        case USERS_SELECTED:
            return {...state, selectedUser: action}
        default:
            return state
    }
}