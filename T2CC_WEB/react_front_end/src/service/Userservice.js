import axios from 'axios';
import { auth } from "../properties/firebase";

export const register = newUser => {

  return auth.createUserWithEmailAndPassword(newUser.email, newUser.password)
  .then((data) => {
    newUser["uid"] = data.user.uid;
    axios.post('/api/teacher/add', newUser);
  }) 
  .catch(err => {
    throw err;
  });
  
}

export const getTeacher = email => {
  return axios.get('/api/teacher/authenticate/' + email);
};

export const login = user => {
  return axios.get('/api/teacher/authenticate/' + user.email)
    .then(teacher => {
      let data = teacher.data;
      if(Object.keys(data).length === 0) {
        throw new Error('Please enter an existing email.');
      } else {
        return auth.signInWithEmailAndPassword(user.email, user.password);
      }
    });
}

export const logout = () => auth.signOut();
