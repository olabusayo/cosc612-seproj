import axios from 'axios';

export const add = newClass => {
    
    return axios.post('/api/class/add', newClass);
}

export const getAll = email => {

    return axios.get('/api/class/getAll/' + email);
}