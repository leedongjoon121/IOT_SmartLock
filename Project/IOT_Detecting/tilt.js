const gpio = require('./gpio.js');
const database = require('./firebase.js');
const TILT = gpio.TILT;
const serialNumber = require('./serialNumber.js');
const tiltRf = database.ref().child('embeded-raspberry/'+serialNumber+'/detecting');
const tiltFunction = function(){
        sen.data = gpio.digitalRead(TILT);
  if(gpio.digitalRead(TILT)==1){
	    console.log("Angle is changed");
      var key = tiltRf.push();
      var  dt = new Date(); //날짜
      var month = dt.getMonth()+1;
      var day = dt.getDate();
      var year = dt.getFullYear();
      var hour = dt.getHours();
      var minute = dt.getMinutes();
      key.set({
        type : "tilt",
        date: year + '년 '+month+'월 '+day+'일'+hour+' 시'+minute +' 분',
      });

      setTimeout(tiltFunction,10000);
	}
  else{
		 console.log("No detection");
      setTimeout(tiltFunction,1000);
	}
};

setTimeout(tiltFunction,100);
var sen = {
  data: 0
};
module.exports = sen;
