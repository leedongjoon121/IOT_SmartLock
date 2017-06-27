const gpio = require('./gpio.js');
const database = require('./firebase.js');
const SOUND = gpio.SOUND;
const serialNumber = require('./serialNumber.js');
const soundRf = database.ref().child('embeded-raspberry/'+serialNumber+'/detecting');
const soundFunction = function(){
      sen.data = gpio.digitalRead(SOUND);
  if(gpio.digitalRead(SOUND)==1){
	    console.log("Sound is detected");
      var key = soundRf.push();
      var dt = new Date(); //날짜
      var month = dt.getMonth()+1;
      var day = dt.getDate();
      var year = dt.getFullYear();
      var hour = dt.getHours();
      var minute = dt.getMinutes();
      key.set({
        type : "sound",
        date: year + '년 '+month+'월 '+day+'일'+hour+' 시'+minute +' 분',
      });

      setTimeout(soundFunction,10000);
	}
  else{
		 console.log("No sound");
      setTimeout(soundFunction,500);
	}
};

setTimeout(soundFunction,100);
var sen = {
  data: 0
};
module.exports = sen;
