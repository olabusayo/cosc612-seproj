import React, { Component } from 'react'
import { BrowserRouter as Router, Route } from 'react-router-dom'
import UserProvider from "./providers/UserProvider";

import Navbar from './components/Navbar'
import Landing from './components/Landing'
import Login from './components/Login'
import Register from './components/Register'
import Profile from './components/Profile'

class App extends Component {
  render() {
    return (
      <UserProvider> 
        <Router>
          <div className="App">
            <Navbar />
            <Route exact path="/" component={Landing} />
            <div className="container">
              <Route exact path="/register" component={Register} />
              <Route exact path="/login" component={Login} />
              <Route exact path="/profile" component={Profile} />
            </div>
          </div>
        </Router>
      </UserProvider>
    )
  }
}

export default App