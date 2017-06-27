
const gcloud = require('@google-cloud/storage')({
 projectId: "raspberry-1bf24",
  keyFilename: 'key.json'
});
const shelljs = require('shelljs');
var bucket = gcloud.bucket('raspberry-1bf24.appspot.com');
const takePicture = function(serial_number){
  var mili = Date.now();
  shelljs.exec('raspistill -o '+serial_number+mili+'.png -t 1');
  bucket.upload(serial_number+'noIR'+mili+'.png',{
 },function(err,file){
    console.log(err);
 });
};

module.exports = takePicture;
