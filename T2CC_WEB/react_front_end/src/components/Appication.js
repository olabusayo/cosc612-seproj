import React, { useContext } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import { ConnectedRouter } from 'connected-react-router';

// Common management
import Header from './layouts/header';
import Sidebar from './layouts/sidebar';
import Breadcrumb from './layouts/breadcrumb';

// Auth management
import Register from './auth/register';
import Login from './auth/login';

// Dashboard management
import Dashboard from './dashboard';

// Class management
import AddClass from './classes/addclass';
import ClassList from './classes/classlist';

import { AuthContext } from "../providers/Auth";

// middleware configuration
import configureStore, { history } from '../configureStore';

const store = configureStore();


const Application = () => {

    const currentUser = useContext(AuthContext);

    if(!currentUser) {
        return (
            <Provider store={store}>
              <ConnectedRouter history={history}>
              <Switch>
                  <Route exact path="/" component={Login} />
                  <Route exact path="/register" component={Register} />
              </Switch>
              </ConnectedRouter>
            </Provider>
          )
    } else {

        return (
            <Provider store={store} forceRefresh={true}>    
                <ConnectedRouter history={history}>
                    <Header/>
                    <Sidebar/>
                    <Switch>
                        <div className="content-wrapper">
                            <section className="content">
                                <Breadcrumb />
                                <div className="container-fluid">
                                    <Route exact path="/" component={ClassList} />
                                    <Route exact path="/createclass" component={AddClass} />
                                </div>
                            </section>
                        </div>
                    </Switch>
                </ConnectedRouter>
            </Provider>
        );
    }
};

export default Application;