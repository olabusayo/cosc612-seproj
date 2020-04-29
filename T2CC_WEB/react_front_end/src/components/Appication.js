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

import Subscription from './subscription/subscription';
import CreateMessage from './messages/createmsg';

import { AuthContext } from "../providers/Auth";

// middleware configuration
import configureStore, { history } from '../configureStore';

const store = configureStore();


const Application = () => {

    const currentUser = useContext(AuthContext);
    console.log(currentUser);

    const loginPage =  (
        <Provider store={store}>
          <ConnectedRouter history={history}>
          <Switch>
              <Route exact path="/" component={Login} />
              <Route exact path="/register" component={Register} />
          </Switch>
          </ConnectedRouter>
        </Provider>
      );

    const homePage = (
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
                                <Route exact path="/create-class" component={AddClass} />
                                <Route exact path="/subscription" component={Subscription} />
                                <Route exact path="/create-message" component={CreateMessage} />
                            </div>
                        </section>
                    </div>
                </Switch>
            </ConnectedRouter>
        </Provider>
    );

    if(!currentUser) {
        return loginPage;
    } 
    else {
        if(currentUser.emailVerified) {
            return homePage;
        } else {
            return loginPage;
        }
    }
};

export default Application;