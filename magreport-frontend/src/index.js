import React from 'react';
import ReactDOM from 'react-dom';
//import 'fontsource-roboto';
import "fontsource-roboto/300.css"; // All styles included.
import "fontsource-roboto/400.css"; // All styles included.
import "fontsource-roboto/500.css";// All styles included.
//import './index.css';
import App from './App';
import { Provider } from 'react-redux'
import * as serviceWorker from './serviceWorker';
import store from 'redux/store';

const app = (
    <Provider store={store}>
        <App />
    </Provider>
)

ReactDOM.render( app, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
