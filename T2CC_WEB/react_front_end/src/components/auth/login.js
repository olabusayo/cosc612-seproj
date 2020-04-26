import React, { useState } from 'react';
import { Link, useHistory } from "react-router-dom";
import { login, logout } from '../../service/Userservice';

const Login = () =>  {

  let history = useHistory();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();

    logout();
    
    if(!email || !password) {
      setError("Please fill in the required fields");
    } else {
      const user = {
        email: email,
        password: password
      }
  
      login(user)
              .then((data) =>{
                  if (!data.user.emailVerified) {
                    setError("Please verify your email to continue");
                  } else {
                    console.log("tonga eee");
                    history.push("/"); 
                  }    
              })
              .catch(e => {
                  setError(e.message);
              })
    }
    
  }

  const handleChange = (event) => {
    const {name, value} = event.currentTarget;

    if(name === 'email') {
        setEmail(value);
    }
    else if(name === 'password'){
        setPassword(value);
    }

  }

    return(
      <div className="login-page">
        <div className="card">
          <div className="card-body login-card-body" style={{width:'400px'}}>
            <h2 className="login-box-msg">Please sign in</h2>
            <p style={{color: "red"}} >{error}</p>
            <form method="post">
              <div className="input-group mb-3">
                <input type="email" name="email" className="form-control" placeholder="Email" onChange={e => handleChange(e)} required/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-envelope"></span>
                  </div>
                </div>
              </div>
              <div className="input-group mb-3" style={{boder:"1px solid red"}}>
                <input type="password" name="password" className="form-control" placeholder="Password"  onChange={e => handleChange(e)} required/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-lock"></span>
                  </div>
                </div>
              </div>
                <button type="submit" className="btn btn-primary btn-block" onClick={e => handleSubmit(e)}>Sign In</button>
            </form>
            <br></br>
            <p className="mb-0">
              <Link to="/register" className="text-center">Register for an account</Link>
            </p>
          </div>
        </div>
      </div>
  )
}

export default Login
