import React, { useState } from 'react';
import { useHistory } from "react-router-dom";
import { Link } from "react-router-dom";
import { register, logout } from '../../service/Userservice';
import { auth } from "../../properties/firebase";

const Regsiter = () => {
 
  const [fname, setFname] = useState('');
  const [lname, setLname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [pwdVerify, setPwdVerify] = useState('');
  const [error, setError] = useState('');

  let history = useHistory();

  const handleSubmit = (e) => {
      e.preventDefault()

      if(!email || !password || !fname || !lname || !pwdVerify) {
        setError("Please fill in the required fields");
      } else if (password.localeCompare(pwdVerify) != 0){
        setError("Passwords don'ts match");
      }
      else {

        const newUser = {
          fname: fname,
          lname: lname,
          email: email,
          password: password
        }

        register(newUser)
            .then((user) => {
              let currentUser = auth.currentUser;
              currentUser.sendEmailVerification().then(function() {
                logout();
                console.log("email sent");
                history.push("/");
              }).catch(function(error) {
                console.log(error.message);
              });
            }).catch(error => {
                setError(error.message);
            });
        }
     
  }

  const handleChange = (event)  => {
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
        else if(name === 'pwdVerify'){
          setPwdVerify(value);
      }  
  }

    return(
      <div className="register-page">

        <div className="card">
          <div className="card-body register-card-body" style={{width:'400px'}}>
            <h2 className="login-box-msg">Welcome to T2CC</h2>
            <p style={{color: "red"}} >{error}</p>
            <form action="../../index.html" method="post">
              <div className="input-group mb-3">
                <input type="text" className="form-control" name="fname" placeholder="First name" onChange={e => handleChange(e)}/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-user"></span>
                  </div>
                </div>
              </div>
              <div className="input-group mb-3">
                <input type="text" className="form-control" name="lname" placeholder="Last name" onChange={e => handleChange(e)}/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-user"></span>
                  </div>
                </div>
              </div>
              <div className="input-group mb-3">
                <input type="email" className="form-control" name="email" placeholder="Email" onChange={e => handleChange(e)}/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-envelope"></span>
                  </div>
                </div>
              </div>
              <div className="input-group mb-3">
                <input type="password" className="form-control" name="password" placeholder="Password" onChange={e => handleChange(e)}/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-lock"></span>
                  </div>
                </div>
              </div>
              <div className="input-group mb-3">
                <input type="password" className="form-control" name="pwdVerify" placeholder="Confirm password" onChange={e => handleChange(e)}/>
                <div className="input-group-append">
                  <div className="input-group-text">
                    <span className="fas fa-lock"></span>
                  </div>
                </div>
              </div>
            </form>

            <div className="social-auth-links text-center">
              <button className="btn btn-block btn-primary" onClick={e => handleSubmit(e)}>
                Register
              </button>
            </div>

            <Link to="/" className="text-center">I already have an account</Link>
          </div>
        </div>
      </div>
  )

}

export default Regsiter
