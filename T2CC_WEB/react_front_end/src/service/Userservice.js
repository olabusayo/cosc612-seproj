import axios from 'axios';
import { auth } from "../properties/firebase";
import param from "./prop";

export const register = newUser => {
  const headers = {
    'Content-Type': 'application/json',
  }

  return auth.createUserWithEmailAndPassword(newUser.email, newUser.password)
  .then((data) => {
    newUser["uid"] = data.user.uid;
    // let teacher = auth.currentUser;
    // teacher.updateProfile({
    //   displayName: newUser.fname,
    // }).then(() => {
      
    // });
    axios.post(param.baseUrl + '/api/teacher/add', newUser , { headers: param.headers });
  }) 
  .catch(err => {
    throw err;
  });
  
}

export const getTeacher = email => {
  return axios.get(param.baseUrl + '/api/teacher/authenticate/' + email);
};

export const login = user => {
  return axios.get(param.baseUrl + '/api/teacher/authenticate/' + user.email)
    .then(teacher => {
      let data = teacher.data;
      if(Object.keys(data).length === 0) {
        throw new Error('Please enter an existing email.');
      } else {
        console.log('hehe');
        return auth.signInWithEmailAndPassword(user.email, user.password);
      }
    });

  
}

export const logout = () => auth.signOut();
