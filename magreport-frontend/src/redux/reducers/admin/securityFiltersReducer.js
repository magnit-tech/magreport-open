import { SF_ROLE_SETTINGS_LOADED, SF_ROLE_SETTINGS_CHNAGED, SF_SET_LAST_FILTER_VALUE, SF_ROLE_SETTINGS_COUNT_CHANGE } from 'redux/reduxTypes'

const initialState = {
    data: [], 
    lastFilterValue: null
}

export const securityFiltersReducer = (state = initialState, action) => {
    let arr = []
    let index=-1
    let lastFilterValue=null
    switch (action.type){
        case SF_ROLE_SETTINGS_LOADED:
            return {...state, data: action.data, lastFilterValue: null}

        case SF_ROLE_SETTINGS_CHNAGED:
            arr = [...state.data]
            index = arr.findIndex(r => r.roleId === action.roleId)        
            arr.splice(index, 1, {...arr[index], tuples: action.value.parameters ? action.value.parameters : []})
            return {...state, data: arr}

        case SF_SET_LAST_FILTER_VALUE:
            index = state.data.findIndex(r => r.roleId === action.roleId)
            if (index > -1){
                lastFilterValue = { parameters: state.data[index].tuples}
            }
            return {...state, lastFilterValue }
        
        case SF_ROLE_SETTINGS_COUNT_CHANGE:
            if (action.operation === "add"){
                arr = [...state.data]
                arr.push({
                    role: action.role,
                    roleId: action.roleId,
                    tuples: []
                })
            }
            else {
                arr = [...state.data].filter(r => r.roleId !== action.roleId)
            }
            return {...state, data: arr}

        default:
            return state
    }
}