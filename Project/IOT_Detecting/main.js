const display = require('./display.js');
const camera = require('./camera.js');
const serial_number = require('./serialNumber.js');
const commmand = require('./command.js');
const express = require('express');
const app = express();
//const ultra = require('./ultrasonic.js');
const dht = require('./dht.js');
const tilt = require('./tilt.js');
const sound = require('./sound.js');
const flame = require('./flame.js');
app.listen(3000,function(){
	console.log('sever open');
});
app.get('/display',function(req,res){
	var text = req.query.text;
	display(text);
	res.send('success');
});
app.get('/camera',function(req,res){
	camera(serial_number);
	res.send('success');
});
