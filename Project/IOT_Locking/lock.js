const database = require('./firebase.js');
const gpio = require('./gpio.js');
const led = require('./led.js');
const serial_number = require('./serialNumber.js');
const camera = require('./camera.js');
camera(serial_number);
const firebaseLock = database.ref('embeded-raspberry/'+serial_number+'/lock');


const DetectHall = function(){
    if(gpio.digitalRead(gpio.HALL)==1){
          if(isOpen!=1){
              led.turnLED(1,0,0);
              Lock.lockSafe();
          }
          Lock.isOpen =1;   // 닫았을 때
    }else{
         Lock.passwordCount = 0; // 패스워드 카운트
         firebaseLock.set('unlock');
         led.turnLED(0,1,0);
          Lock.isOpen = 0;
    }
    setTimeout(DetectHall,250);
}
setTimeout(DetectHall,1);

var Lock = {
  passwordCount : 0,
  isOpen : 0,
  lockSafe:function(){
    led.turnLED(1,0,0);
    gpio.digitalWrite(gpio.MotorPin,1);
    gpio.digitalWrite(gpio.MotorPin2,0);
    gpio.delay(3000);
    gpio.digitalWrite(gpio.MotorPin,0);
    firebaseLock.set('lock');
  },
  unlockSafe:function(){
    led.turnLED(0,1,0);
    gpio.digitalWrite(gpio.MotorPin,0);
    gpio.digitalWrite(gpio.MotorPin2,1);
    gpio.delay(3000);
    gpio.digitalWrite(gpio.MotorPin2,0);
    firebaseLock.set('unlock');
  }
};

module.exports = Lock;
