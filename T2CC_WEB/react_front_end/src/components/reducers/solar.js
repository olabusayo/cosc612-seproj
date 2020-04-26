import CONFIG  from './../../common/config';
const initialState = {
  energyData              : {},
  ac_monthly_data         : [],
  ac_hourly_data          : [],
  dc_hourly_data          : [],
  dc_monthly_data         : [],
  solar_monthly_data      : [],
  all_energy_monthly_data : [],
  location                : {lat:CONFIG.LAT,lng:CONFIG.LNG},
  isFetching              : false,
  isError                 : false,
  errorInfo               : []
};

const SolarReducer = (state = initialState, action) => {
  switch (action.type) {
    // original data from server
    case "GET_SOLAR_INFO":
      return Object.assign({}, state, {
        isFetching              : true,
        energyData              : action.payload.result.outputs,
        ac_monthly_data         : action.payload.result.outputs.ac_monthly,
        dc_monthly_data         : action.payload.result.outputs.dc_monthly,
        solar_monthly_data      : action.payload.result.outputs.solrad_monthly,
        ac_hourly_data          : FormMonthlyData(action.payload.result.outputs.ac),
        dc_hourly_data          : FormMonthlyData(action.payload.result.outputs.dc),
        all_energy_monthly_data : FormChatDataFromPayload(action.payload.result.outputs.solrad_monthly,action.payload.result.outputs.ac_monthly,action.payload.result.outputs.dc_monthly),
        isError                 : false
      });
      // error data from server
      case "ERROR_SOLAR_INFO":
        return Object.assign({}, state, {
          isFetching             : true,
          errorInfo              : action.payload.result,
          isError                : true,
        });
    // sample data from common folder
    case "TEST_GET_SOLAR_INFO":
      return Object.assign({}, state, {
        isFetching              : true,
        energyData              : action.payload,
        ac_monthly_data         : action.payload.ac_monthly,
        dc_monthly_data         : action.payload.dc_monthly,
        solar_monthly_data      : action.payload.solrad_monthly,
        ac_hourly_data          : FormMonthlyData(action.payload.ac),
        dc_hourly_data          : FormMonthlyData(action.payload.dc),
        all_energy_monthly_data : FormChatDataFromPayload(action.payload.solrad_monthly,action.payload.ac_monthly,action.payload.dc_monthly),
        isError                 : false
      });
      //adding sample location
      case "TEST_ADD_LOCATION":
        return Object.assign({}, state, {
          isFetching              : true,
          location                : {lat:13.06,lng:80.05},
          isError                 : false
        });
    default:
      return state;
  }
};

// manipulating monthly data here
function  FormMonthlyData(data) {
  var janArray = [];
  var febArray = [];
  var marArray = [];
  var aprArray = [];
  var mayArray = [];
  var junArray = [];
  var julArray = [];
  var augArray = [];
  var sepArray = [];
  var octArray = [];
  var novArray = [];
  var decArray = [];

  for (var i = 0; i < data.length; i++) {
    if(i < 743) {
      janArray.push(data[i]);
    }
    if(i > 744 && i < 1441) {
      febArray.push(data[i]);
    }
    if(i > 1442 && i < 2162) {
      marArray.push(data[i]);
    }
    if(i > 2163 && i < 2907) {
      aprArray.push(data[i]);
    }
    if(i > 2908 && i < 3628) {
      mayArray.push(data[i]);
    }
    if(i > 3629 && i < 4373) {
      junArray.push(data[i]);
    }
    if(i > 4374 && i < 5094) {
      julArray.push(data[i]);
    }
    if(i > 5095 && i < 5839) {
      augArray.push(data[i]);
    }
    if(i > 5840 && i < 6560) {
      sepArray.push(data[i]);
    }
    if(i > 6561 && i < 7305) {
      octArray.push(data[i]);
    }
    if(i > 7306 && i < 8026) {
      novArray.push(data[i]);
    }
    if(i > 8027 && i < 8771) {
      decArray.push(data[i]);
    }
  }
  return [janArray,febArray,marArray,aprArray,mayArray,junArray,julArray,augArray,sepArray,octArray,novArray,decArray];
}

// manipulating chartdata here for month
function  FormChatDataFromPayload(solar,ac,dc) {
    var result = [];
    result.push(['Year', 'Solar', 'AC', 'DC']);
    var month = "";
    for (var i = 0; i < solar.length; i++) {
      // For x axis data
      if(i == 0) {
        month = 'Jan';
      }
      if(i == 1) {
        month = 'Feb';
      }
      if(i == 2) {
        month = 'Mar';
      }
      if(i == 3) {
        month = 'Apr';
      }
      if(i == 4) {
        month = 'May';
      }
      if(i == 5) {
        month = 'Jun';
      }
      if(i == 6) {
        month = 'Jul';
      }
      if(i == 7) {
        month = 'Aug';
      }
      if(i == 8) {
        month = 'Sep';
      }
      if(i == 9) {
        month = 'Oct';
      }
      if(i == 10) {
        month = 'nov';
      }
      if(i == 11) {
        month = 'Dec';
      }
      var formData = [month,solar[i],ac[i],dc[i]];
      result.push(formData);
    }
    return result;
}

export default SolarReducer;
