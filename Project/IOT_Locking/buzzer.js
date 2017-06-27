const gpio = require('./gpio.js');
const BUZZER = gpio.Buzzer;
const buzzer = {
  turnBuzzer : function(time){
      gpio.digitalWrite(BUZZER,1);
      gpio.delay(time);
      gpio.digitalWrite(BUZZER,0);
  }
}
module.exports = buzzer;
