import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import promiseMiddleware from 'redux-promise-middleware';
import rootReducer from './reducers';
import App from './containers/app';

const store = createStore(
    rootReducer,
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
    applyMiddleware(promiseMiddleware()),
	);

ReactDOM.render(
	<Provider store={store}>
		<App />
	</Provider>
	, document.querySelector('.container'));