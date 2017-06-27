const gpio = require('./gpio.js');
const database = require('./firebase.js');
const FLAME = gpio.FLAME;
const serialNumber = require('./serialNumber.js');
const flameRf = database.ref().child('embeded-raspberry/'+serialNumber+'/detecting');
const flameFunction = function(){
      sen.data = gpio.digitalRead(FLAME);
  if(gpio.digitalRead(FLAME)==1){
	    console.log("Flame is detected");
      var key = flameRf.push();
      var dt = new Date(); //날짜
      var month = dt.getMonth()+1;
      var day = dt.getDate();
      var year = dt.getFullYear();
      var hour = dt.getHours();
      var minute = dt.getMinutes();
      key.set({
        type : "flame",
        date: year + '년 '+month+'월 '+day+'일'+hour+' 시'+minute +' 분',
      });

      setTimeout(flameFunction,10000);
	}
  else{
		 console.log("No flame");
      setTimeout(flameFunction,500);
	}
};

setTimeout(flameFunction,100);
var sen = {
  data: 0
};
module.exports = sen;
