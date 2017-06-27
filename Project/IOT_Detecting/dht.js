const gpio = require('./gpio.js');
const sensorLib = require('node-dht-sensor'); // dht 온습도 센서 모듈
const standard = require('./standard.js');
const database = require('./firebase.js');
const serialNumber = require('./serialNumber');
const detecting = database.ref().child('embeded-raspberry/'+serialNumber+'/detecting');
const DHT = gpio.DHT;
const sensor = {
    read : function(){
        var s = sensorLib.read(11,DHT);
        sen.temperature = s.temperature.toFixed(1);
        sen.humidity = s.humidity.toFixed(1);
        console.log("temperature : "+ s.temperature.toFixed(1) + "  humidity : " + s.humidity.toFixed(1)+"%");
        if(standard.tmax!=null){
          if(standard.tmax<=s.temperature||s.temperature<=standard.tmin||s.humidity<=standard.hmin||s.humidity>=standard.hmax){
            var key = detecting.push();
            var  dt = new Date(); //날짜
            var month = dt.getMonth()+1;
            var day = dt.getDate();
            var year = dt.getFullYear();
            var hour = dt.getHours();
            var minute = dt.getMinutes();

            key.set({
              type : "dht",
              date: year + '년 '+month+'월 '+day+'일'+hour+' 시'+minute +' 분',
              temperature : ""+s.temperature+"",
              humidity : ""+s.humidity+""
            });
            setTimeout(function(){sensor.read();},10000);
          }
          else{
            setTimeout(function(){sensor.read();},1000);
          }
        }else{
            setTimeout(function(){sensor.read();},1000);
        }
    }
};

var sen = {
  temperature : 0,
  humidity : 0
};
sensor.read();
module.exports = sen;
