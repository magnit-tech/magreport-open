import { DESIGN_SETTINGS_SET_VALUE, APPLOGOUT, LIGHTTHEME, DARKTHEME } from "redux/reduxTypes";

export function actionThemeDesignSetValue(theme) {
    return {
        type: DESIGN_SETTINGS_SET_VALUE,
        theme: theme
    };
}

export const setLightTheme = () =>{
    return {
        type: LIGHTTHEME,
        darkTheme: false
    }
}

export const setDarkTheme = () =>{
    return {
        type: DARKTHEME,
        darkTheme: true
    }
}

export const appLogout = () => {
    const isDarkTheme = localStorage.getItem('isDarkTheme')==='true' ? true: false;
    return {type: APPLOGOUT, darkTheme: isDarkTheme}
}