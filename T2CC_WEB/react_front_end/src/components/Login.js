import React, { useState, useContext } from 'react';
import { login } from '../service/Userservice';
import { AuthContext } from "../providers/Auth";
import { Link, useHistory } from "react-router-dom";
import { withRouter, Redirect } from "react-router"

const Login = () => {

    let history = useHistory();
    const currentUser = useContext(AuthContext);

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    

    const onChangeHandler = (event) => {
        const {name, value} = event.currentTarget;

        if(name === 'email') {
            setEmail(value);
        }
        else if(name === 'password'){
            setPassword(value);
        }
    };

    const onSubmit = (event) => {

        event.preventDefault()
        const user = {
            email: email,
            password: password
        }

        login(user)
            .then(() =>{
                history.push("/");
            })
            .catch(e => {
                setError(e.message);
            })
        
    };

    if(currentUser) {
        return <Redirect to="/" />;
    } 

        return (
            <div className="container">
                <div className="row">
                <div className="col-md-6 mt-5 mx-auto">
                    <form noValidate onSubmit={(event) => onSubmit(event)}>
                    <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                    <p>{error}</p>
                    <div className="form-group">
                        <label htmlFor="email">Email address</label>
                        <input
                        type="email"
                        className="form-control"
                        name="email"
                        placeholder="Enter email"
                        onChange={(event) => onChangeHandler(event)}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                        type="password"
                        className="form-control"
                        name="password"
                        placeholder="Password"
                        onChange={(event) => onChangeHandler(event)}
                        />
                    </div>
                    <button type="submit" className="btn btn-lg btn-primary btn-block">
                        Sign in
                    </button>
                    <Link to="/register">Register</Link>
                    </form>
                </div>
                </div>
            </div>
        );
}

export default withRouter(Login);