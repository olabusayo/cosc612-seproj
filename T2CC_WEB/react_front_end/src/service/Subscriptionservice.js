import axios from 'axios';

export const getSubs = (id) => {
    return axios.get('/api/subscription/getALl/' + id);
};

export const getStudent = async (id) => {
    return await axios.get('/api/student/' + id);
};

export const approve = (data) => {
    return axios.post('/api/subscription/approve', data);
} 

export const deny = (data) => {
    return axios.post('/api/subscription/deny', data);
} 