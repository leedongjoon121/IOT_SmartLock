const gpio = require('./gpio.js');
const RED = gpio.Red;
const BLUE = gpio.Blue;
const GREEN = gpio.Green;
console.log('LED OFF');
gpio.digitalWrite(RED,1);
gpio.digitalWrite(BLUE,1);
gpio.digitalWrite(GREEN,1);

var LED = {
  turnLED : function(R,G,B){
    gpio.digitalWrite(RED,R);
    gpio.digitalWrite(GREEN,G);
    gpio.digitalWrite(BLUE,B);
  }
}
module.exports = LED;
