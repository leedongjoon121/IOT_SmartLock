const adafruit = require('adafruit-mcp23008-ssd1306-node-driver');
const DisplayDriver = adafruit.DisplayDriver;
const busNumber = 1;
const displayAddress = 0x3c;
const displayDriver = new DisplayDriver(busNumber,displayAddress);
module.exports = function(text){
  displayDriver.clear();
  displayDriver.writeText(text);
};
