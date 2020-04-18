import React, { Component } from 'react'
import { register } from '../service/Userservice'

const emailRegex= RegExp(/^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/);
const passRegex=RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/);

const formValid=errors=>{
    let valid=true;
    Object.values(errors).forEach(val => {
        val.length>0 && (valid= false);
    });

    return valid;
};

class Register extends Component {
  constructor() {
    super()
    this.state = {
      first_name: '',
      last_name: '',
      email: '',
      password: '',
      errors: {
        first_name: '',
        last_name: '',
        email: '',
        password: '',
      }
    }

    this.onChange = this.onChange.bind(this)
    this.onSubmit = this.onSubmit.bind(this)
  }

  onChange(e) {
    this.setState({ [e.target.name]: e.target.value })
    const{name,value}=e.target;
    let formErrors=this.state.errors;

    //console.log("name:",name);
    //console.log("value:",value);
    switch(name){
        case 'first_name':
            formErrors.first_name=value.length < 3 && value.length > 0 ? 'minimun 3 characters requiered':'';
            break;
        case 'email':
            formErrors.email=emailRegex.test(value) && value.length > 0 ? '':' invalid email address';
            break;
        case 'password':
                formErrors.password= passRegex.test(value) && value.length > 0 ? '':' invalid email password';
                break;
        default:
            break;
    }

    this.setState({formErrors, [name]:value}, ()=>console.log(this.state));

  }
  onSubmit(e) {
    e.preventDefault()

    // if(formValid(this.state.errors)) {
    //     console.log('FirstName:'+this.state.first_name+
    //     "LastName:"+this.state.last_name+
    //     "Password"+  this.state.password      )
              
    // } else {
    //   console.error("Form invalid")
    // }

    const newUser = {
      fname: this.state.first_name,
      lname: this.state.last_name,
      email: this.state.email,
      password: this.state.password
    }

    register(newUser).then((user) => {
      console.log(user);
      this.props.history.push('/login');
    });
    
  }

  render() {

    const { errors}= this.state;
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-6 mt-5 mx-auto">
            <form noValidate onSubmit={this.onSubmit}>
              <h1 className="h3 mb-3 font-weight-normal">Register</h1>
              <div className="form-group">
                <label htmlFor="name">First name</label>
                <input
                  type="text"
                  className="form-control"
                  name="first_name"
                  placeholder="Enter your first name"
                  value={this.state.first_name}
                  onChange={this.onChange}
                />

                {errors.first_name.length > 0 && (
                    <span className="errorMessage">{errors.first_name}</span>
                )}
              </div>
              <div className="form-group">
                <label htmlFor="name">Last name</label>
                <input
                  type="text"
                  className="form-control"
                  name="last_name"
                  placeholder="Enter your lastname name"
                  value={this.state.last_name}
                  onChange={this.onChange}
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">Email address</label>
                <input
                  type="email"
                  className="form-control"
                  name="email"
                  placeholder="Enter email"
                  value={this.state.email}
                  onChange={this.onChange}
                />
              </div>
              <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                  type="password"
                  className="form-control"
                  name="password"
                  placeholder="Password"
                  value={this.state.password}
                  onChange={this.onChange}
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
    )
  }
}

export default Register