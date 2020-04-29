import axios from 'axios';

export const add = newClass => {
    
    return axios.post('/api/class/add', newClass);
}

export const getAll = email => {
    return axios.get('/api/class/getAll/' + email);
}

export const isActive = (term, year) => {
    const currentDate = new Date();
    const currentMonth = currentDate.getMonth();
    const currentYear = currentDate.getFullYear();
    let season = "Winter";
    if (3 <= currentMonth && currentMonth <= 5) {
        season = 'Spring';
    }

    else if (6 <= currentMonth && currentMonth <= 8) {
        season = 'Summer';
    }

    else if (9 <= currentMonth && currentMonth <= 11) {
        season = 'Fall';
    }

    return (term == season && year == currentYear);
}

export const getYear = () => {
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const nextYear = currentYear + 1;
    let res = [];
    res.push(currentYear);
    res.push(nextYear);
    return res;
}