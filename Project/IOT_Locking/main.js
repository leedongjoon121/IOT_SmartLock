
const serial_number = require('./serialNumber.js');
const prompt = require('./prompt.js');
const command = require('./command.js');
const led = require('./led.js');
const express = require('express');

const app = express();
led.turnLED(0,0,1);
app.listen(3000,function(){
	console.log('port : 3000');
});
