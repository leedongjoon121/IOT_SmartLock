const database = require('./firebase.js');
const serial_number = require('./serialNumber.js');
const dht = require('./dht.js');
const flame = require('./flame.js');
const sound = require('./sound.js');
const tilt = require('./tilt.js');
const ultrasonic = require('./ultrasonic.js');
var command = database.ref().child('embeded-raspberry/'+serial_number).child('command');
console.log('embeded-raspberry'+serial_number);
command.on("child_added",function(snapshot,prevChildKey){
      var command_data = snapshot.val();
      var key = snapshot.key;
      console.log("캡쳐");
      var data = database.ref().child('embeded-raspberry/'+serial_number).child('detect_log');
      if(command_data.type=='dht'){
        data.child('dht').set({
          humidity : ""+dht.humidity+"",
          temperature : ""+dht.temperature+""
        });
        command.child(key).remove();
      }else if(command_data.type=='flame'){
        data.child('flame').set({
          data : flame.data.toString()
        });
        command.child(key).remove();
      }
      else if(command_data.type=='sound'){
        data.child('sound').set({
          data : sound.data.toString()
        });
        command.child(key).remove();
      }
      else if(command_data.type=='tilt'){
        data.child('tilt').set({
          data : tilt.data.toString()
        });
        command.child(key).remove();
      }
      else if(command_data.type=='ultrasonic'){
        data.child('ultrasonic').set({
          data : ultrasonic.distance.toFixed(2)
        });
        command.child(key).remove();
      }
});
