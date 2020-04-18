import axios from 'axios';
import { auth } from "../properties/firebase";
import param from "./prop";

export const register = newUser => {
  const headers = {
    'Content-Type': 'application/json',
  }

  auth.createUserWithEmailAndPassword(newUser.email, newUser.password).catch(err => {
    throw err;
  });

  return axios.post(param.baseUrl + '/api/teacher/add', newUser , { headers: param.headers })

}

export const login = user => {
  return axios.get(param.baseUrl + '/api/teacher/authenticate/' + user.email)
    .then(teacher => {
      let data = teacher.data;
      if(Object.keys(data).length === 0) {
        throw new Error('Teacher does not exist');
      } else {
        auth.signInWithEmailAndPassword(user.email, user.password).catch(function(error) {
          throw error;
        });
      }
    });

  
}

export const logout = () => auth.signOut();
