import { APP_LOGGED_IN, APPLOGOUT, APPLOGINFAILRIGHTS, APPLOGINFAIL } from '../reduxTypes'

const initialState = {
    loginStatus: APPLOGOUT,
    userName: ''
}
    
export const loginReducer = (state = initialState, action) => {
    switch (action.type){
        case APP_LOGGED_IN: 
            return {...state, loginStatus: APP_LOGGED_IN, userName: action.userName}
        case APPLOGOUT: 
            return {...state, loginStatus: APPLOGOUT, userName: ''}
        case APPLOGINFAILRIGHTS: 
            return {...state, loginStatus: APPLOGINFAILRIGHTS, userName: ''}
        case APPLOGINFAIL: 
            return {...state, loginStatus: APPLOGINFAIL, userName: ''}
        default: 
            return state
    }
    
    
}