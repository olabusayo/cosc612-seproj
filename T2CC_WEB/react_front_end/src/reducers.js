import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';
import EmployeeReducer from './components/reducers/employee';
import ClassReducer from './components/reducers/class';

// connect all reducers here
const createRootReducer = (history) => combineReducers({
  router          : connectRouter(history),
  EmployeeReducer : EmployeeReducer,
  ClassReducer    : ClassReducer
})

export default createRootReducer;
