import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import PrivateRoute from "./PrivateRoute"

import Home from './Home'
import Login from './Login'
import Register from './Register'

const Application = () => {

    return (
        <Router>
            <div className="App">
            <Switch>
                <PrivateRoute exact path="/" component={Home} />
                <Route path="/login" component={Login} />
                <Route path="/Register" component={Register} />
            </Switch>
            </div>
        </Router> 
    );
    
};

export default Application;