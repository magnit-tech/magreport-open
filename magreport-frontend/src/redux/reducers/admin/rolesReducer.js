import { 
    ROLE_LIST_LOADED, ROLE_LIST_LOAD_FAILED, ROLE_CHANGE_WRITE_RIGHTS, ROLE_ADD, ROLE_DELETE, ROLE_FILTER, ROLE_SELECTED, FOLDER_CONTENT_FOLDER_CLICK, ROLE_SELECTED_FOLDER_TYPE 
} from 'redux/reduxTypes'

const initialState = {
    selectedRole: null,
    data:[]
}

export const rolesReducer = (state = initialState, action) => {
    let arr = []
    let rights = []
    let filteredArr = []
    let newState
    switch (action.type){
        case ROLE_LIST_LOADED:
            for (let r of action.data){
                r.roleId = r.role.id
                r.id = r.role.id
                r.name = r.role.name
            }
            action.data.sort((a,b) => {
                if (a.role.name < b.role.name){
                    return -1
                }
                else if (a.role.name > b.role.name){
                    return 1
                }
                return 0
            })
            return {
                ...state, data: [...action.data]
            }

        case ROLE_LIST_LOAD_FAILED:
            return {
                ...state, data: action.error
            }

        case ROLE_CHANGE_WRITE_RIGHTS:
            arr = [...state.data]
            rights = ["READ"]
            if (action.value){
                rights.push("WRITE")
            }

            if (state.filteredData){
                filteredArr = [...state.filteredData]
                let roleId = filteredArr[action.index].roleId
                let changedRole = {...filteredArr[action.index], permissions: rights}
                filteredArr.splice(action.index, 1, changedRole)
                let index = arr.findIndex(item => item.roleId === roleId)
                arr.splice(index, 1, changedRole)
                newState = {...state, data: arr, filteredData: filteredArr}
            }
            else {
                arr.splice(action.index, 1, {...arr[action.index], permissions: rights})
                newState = {...state, data: arr}
            }
            
            return newState

        case ROLE_ADD:
            arr = [...state.data]
            arr.push(action.role)
            newState = {...state, data: arr}
            if (state.filteredData && action.role.role.name.trim().toLowerCase().includes(state.filteredStr)){
                filteredArr = [...state.filteredData]
                filteredArr.push(action.role)
                newState.filteredData = filteredArr
            }
            return newState

        case ROLE_DELETE:
            arr = [...state.data]
            if (state.filteredData){
                filteredArr = [...state.filteredData]
                let roleId = filteredArr[action.index].roleId
                filteredArr.splice(action.index, 1)
                arr = arr.filter(item => item.roleId !== roleId)
                newState = {...state, data: arr, filteredData: filteredArr}
            }
            else{
                arr = arr.filter(item => item.roleId !== action.roleId)
                newState = {...state, data: arr}
            }
            return newState

        case ROLE_FILTER:
            newState = {...state}
            if (action.filterStr){
                arr = [...state.data].filter(item => item.role.name.trim().toLowerCase().includes(action.filterStr))
                newState.filteredData = arr
                newState.filteredStr = action.filterStr
            }
            else {
                delete newState.filteredData
                delete newState.filteredStr
            }
            
            return newState
        
        case ROLE_SELECTED:
            return {...state, selectedRole: action.roleId}

        case FOLDER_CONTENT_FOLDER_CLICK:
            return {...state, selectedRole: null}
        case ROLE_SELECTED_FOLDER_TYPE:
            return {...state, selectedFolderType: action.folderType}
        default:
            return state
    }
}