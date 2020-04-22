import axios from 'axios';

const ADD_CLASS = 'ADD_CLASS';

export function AC_ADD_CLASS(data) {
  return { type: ADD_CLASS, data }
}

function success() {
  return 1;
}
