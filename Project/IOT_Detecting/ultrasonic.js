const gpio = require('./gpio.js');
const database = require('./firebase.js');
const standard = require('./standard.js');
const TRIG = gpio.TRIG; // 트리거
const ECHO = gpio.ECHO; // 에코
var startTime;   // 초음파 시작
var EndTime;  // 초음파 끝
const serialNumber = require('./serialNumber.js');
const sonicRf = database.ref().child('embeded-raspberry/'+serialNumber+'/detecting');
const Triggering = function (){
  gpio.digitalWrite(TRIG,gpio.LOW);
  gpio.delayMicroseconds(2);
  gpio.digitalWrite(TRIG,gpio.HIGH);
  gpio.delayMicroseconds(10);
  gpio.digitalWrite(TRIG,gpio.LOW);

  while(gpio.digitalRead(ECHO)==gpio.LOW);

  startTime = gpio.micros();
  while(gpio.digitalRead(ECHO)==gpio.HIGH);
  EndTime = gpio.micros() - startTime;

  distance = EndTime/58;
  ultrasonic_data =  distance;
  sen.distance = distance;
   console.log("distance : %d \n", distance);
   if(standard.dmin!=null){
     if(distance>standard.dmin){
       console.log("Ultrasonic is detected");
       var key = sonicRf.push();
       var dt = new Date(); //날짜
       var month = dt.getMonth()+1;
       var day = dt.getDate();
       var year = dt.getFullYear();
       var hour = dt.getHours();
       var minute = dt.getMinutes();
       key.set({
         type : "ultrasonic",
         data : distance.toString(),
         date: year + '년 '+month+'월 '+day+'일'+hour+' 시'+minute +' 분',
       });
       setTimeout(Triggering,10000);
     }
     else{
       setTimeout(Triggering,500);
     }
   }
  else{
        setTimeout(Triggering,500);
  }
}
setTimeout(Triggering,500);
var sen = {
  distance: 0
};
module.exports = sen;
