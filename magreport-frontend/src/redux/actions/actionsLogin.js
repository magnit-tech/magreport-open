import { APP_LOGGED_IN,  APPLOGINFAIL, APPLOGINFAILRIGHTS } from '../reduxTypes'
import { showLoader, hideLoader } from './actionLoader'
import { showAlert, hideAlert } from './actionsAlert'

export const appLogin = (userName, passWord, dataHub) => {
    return async dispatch => {
        dispatch(showLoader())
        

        try {        
            dataHub.login(userName, passWord, handleLogin)

            function handleLogin (magrepResponse){
                if (magrepResponse.ok){
                    dataHub.userController.whoAmI(handleWhoAmI)                    
                }
                else {
                    switch(magrepResponse.data.status) {
                        case 401 : handleFail("Неверный логин / пароль");
                        break;
                        case 403 : handleFail("Учетная запись заблокирована");
                        break;
                        default : handleFail("Произошла неизвестная ошибка");
                    }
                }
            }
            
            function handleWhoAmI (magrepResponse){
                if (magrepResponse.ok){
                    dispatch(hideLoader())
                    dispatch({type: APP_LOGGED_IN, userName})
                }
                else {
                    handleFail("Не удалось получить информацию о пользователе")
                }
            }

            function handleFail(errorText){
                dispatch(hideLoader())
                const callback = () => {dispatch(hideAlert())}
                const buttons = [{'text':'OK','onClick': callback}]
                dispatch(showAlert("Ошибка", errorText, buttons, callback))
                dispatch(appLoginFail())
            }
            
        }
        catch (e) {
            console.error(e)
            
        }
    }
}

export const appLoginFail = () => {
    return {type: APPLOGINFAIL}
}

export const appLoginFailRights = () => {
    return {type: APPLOGINFAILRIGHTS}
}