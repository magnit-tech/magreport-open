import React from "react";
import {render} from "@testing-library/react";
import {ThemeProvider} from "@material-ui/core";
import {SnackbarProvider} from "notistack";
import {Provider} from "react-redux";
import userEvent from "@testing-library/user-event";

import defaultTheme from '../themes/defaultTheme.js';
import store from "../redux/store";


const Wrapper = ({children}) => {
    return (
        <Provider store={store}>
            <ThemeProvider theme={defaultTheme}>
                <SnackbarProvider maxSnack={10}>
                    {children}
                </SnackbarProvider>
            </ThemeProvider>
        </Provider>
    );
}

const customRender = (ui, options) => render(ui, {wrapper: Wrapper, ...options});

export * from "@testing-library/react";
export {userEvent};
export {customRender as render};
