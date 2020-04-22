import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';
import ClassReducer from './components/reducers/class';

// connect all reducers here
const createRootReducer = (history) => combineReducers({
  router          : connectRouter(history),
  ClassReducer    : ClassReducer
})

export default createRootReducer;
