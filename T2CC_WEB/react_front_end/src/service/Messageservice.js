import axios from 'axios';

export const create = message => {
    return axios.post('/api/message/create', message);
}

export const saveDraft = message => {
    return axios.post('/api/message/save-draft', message);
}