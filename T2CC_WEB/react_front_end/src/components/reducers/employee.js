import CONFIG  from './../../common/config';
const initialState = {
  isFetching              : false,
  isError                 : false,
  errorInfo               : []
};

const EmployeeReducer = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_TODO":
      return Object.assign({}, state, {
        isFetching              : true,
      });
    default:
      return state;
  }
};

export default EmployeeReducer;
