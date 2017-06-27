const database = require('./firebase.js');
const serial_number = require('./serialNumber.js');

var standard ={


};

database.ref().child("embeded-raspberry/"+serial_number).child("standard").on("value",function(snapshot){
  var val = snapshot.val();
//  console.log(val);
  standard['tmax'] = val.temperature_max;
  standard['tmin'] = val.temperature_min;
  standard['hmax'] = val.humidity_max;
  standard['hmin'] = val.humidity_min;
  standard['dmin'] = val.distance_min;
  module.exports = standard;
});

module.exports = standard;
