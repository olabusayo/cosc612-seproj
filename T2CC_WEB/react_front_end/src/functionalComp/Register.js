import React, { useState } from 'react';
import { register } from '../service/Userservice';
import { useHistory } from "react-router-dom";
import { withRouter } from "react-router";
import { logout } from "../service/Userservice";

const Register = () => {
    const [fname, setFname] = useState('');
    const [lname, setLname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    let history = useHistory();

    const onSubmit = (event) => {
        event.preventDefault()
        const newUser = {
            fname: fname,
            lname: lname,
            email: email,
            password: password
        }

        register(newUser)
            .then((user) => {
                logout();
                history.push('/login');
            }).catch(error => {
                setError(error.message);
            });
    }

    const onChange = (event) => {
        const {name, value} = event.currentTarget;

        if(name === 'email') {
            setEmail(value);
        }
        else if(name === 'password'){
            setPassword(value);
        }
        else if(name === 'fname'){
            setFname(value);
        }
        else if(name === 'lname'){
            setLname(value);
        }  
    }


    return (
        <div className="container">
            <div className="row">
            <div className="col-md-6 mt-5 mx-auto">
                <form noValidate onSubmit={(event) => onSubmit(event)}>
                <h1 className="h3 mb-3 font-weight-normal">Register</h1>
                <p>{error}</p>
                <div className="form-group">
                    <label htmlFor="name">First name</label>
                    <input
                    type="text"
                    className="form-control"
                    name="fname"
                    placeholder="Enter your first name"
                    onChange={(event) => onChange(event)}
                    />

                </div>
                <div className="form-group">
                    <label htmlFor="name">Last name</label>
                    <input
                    type="text"
                    className="form-control"
                    name="lname"
                    placeholder="Enter your lastname name"
                    onChange={(event) => onChange(event)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email address</label>
                    <input
                    type="email"
                    className="form-control"
                    name="email"
                    placeholder="Enter email"
                    onChange={(event) => onChange(event)}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                    type="password"
                    className="form-control"
                    name="password"
                    placeholder="Password"
                    onChange={(event) => onChange(event)}
                    />
                </div>
                <button
                    type="submit"
                    className="btn btn-lg btn-primary btn-block"
                >
                    Register
                </button>
                </form>
            </div>
            </div>
        </div>
    );
}

export default withRouter(Register);