import { 
    REPORT_TEMPLATES_LOADED,
    REPORT_TEMPLATES_ADDED,
    REPORT_TEMPLATES_DELETED,
    REPORT_TEMPLATES_SET_DEFAULT_SUCCESS,
} from '../../reduxTypes'

const initialState = {
    data: []
}

export const reportTemplatesReducer = (state = initialState, action) => {
    
    switch (action.type){
        case REPORT_TEMPLATES_LOADED:
            return {...state, data: action.data}

        case REPORT_TEMPLATES_ADDED:
            return {...state, data: [...state.data, action.data]}

        case REPORT_TEMPLATES_DELETED:
            return {...state, data: state.data.filter(i => i.excelTemplateId !== action.id)}

        case REPORT_TEMPLATES_SET_DEFAULT_SUCCESS:
            {
                const arr = [...state.data]
                let index = arr.findIndex(i => i.default === true)
                if (index > -1) arr[index] = {...arr[index], default: false}
                index = arr.findIndex(i => i.excelTemplateId === action.id)
                if (index > -1) arr[index] = {...arr[index], default: true}
                
                return {...state, data: arr}
            }
        
        default:
            return state
    }
}
