var database = require('./firebase.js');
const lockjs = require('./lock.js');
const serial_number = require('./serialNumber.js');
var command = database.ref().child('embeded-raspberry/'+serial_number).child('command');
command.on("child_added",function(snapshot,prevChildKey){
      var command_data = snapshot.val();
      var key = snapshot.key;
      if(command_data.type=='unlock'){
        lockjs.unlockSafe();
        command.child(key).remove();
      }else if(command_data.type=='lock'){
        lockjs.lockSafe();
        command.child(key).remove();
      }else if(command_data.type=='checkLock'){
        var data = database.ref().child('embeded-raspberry/'+serial_number);
        if(lockjs.isOpen==1){
          data.child('lock').set("unlock");
        }else{
          data.child('lock').set("lock");
        }
        command.child(key).remove();
      }
});
