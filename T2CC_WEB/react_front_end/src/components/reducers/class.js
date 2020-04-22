import CONFIG  from './../../common/config';
const initialState = {
  isFetching              : false,
  isError                 : false,
  errorInfo               : [],
  classList               : [
                              {
                                classtitle          : 'title-1',
                                courseno            : 'title-1',
                                createanewclass     : 'title-1',
                                classdesc           : 'title-1',
                                section             : 'title-1',
                                term                : 'title-1',
                                year                : 'title-1'
                              },
                              {
                                classtitle          : 'title-2',
                                courseno            : 'title-1',
                                createanewclass     : 'title-1',
                                classdesc           : 'title-2',
                                section             : 'title-1',
                                term                : 'title-1',
                                year                : 'title-1'
                              },
                              {
                                classtitle          : 'title-3',
                                courseno            : 'title-1',
                                createanewclass     : 'title-1',
                                classdesc           : 'title-3',
                                section             : 'title-1',
                                term                : 'title-1',
                                year                : 'title-1'
                              }
                           ]
};

const ClassReducer = (state = initialState, action) => {
  console.log('--=-=-=-=',action.data);
  switch (action.type) {
    case "ADD_CLASS":
        return {
          ...state,
          classList: [...state.classList, action.data]
      }
    default:
      return state;
  }
};

export default ClassReducer;
