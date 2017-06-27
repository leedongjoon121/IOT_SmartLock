
const gcloud = require('@google-cloud/storage')({
 projectId: "raspberry-1bf24",
  keyFilename: 'key.json'
});
const shelljs = require('shelljs');

const takePicture = function(serial_number,date){
  var bucket = gcloud.bucket('raspberry-1bf24.appspot.com');
  shelljs.exec('raspistill -o '+serial_number+date+'normal.png -t 1');
  bucket.upload(serial_number+date+'normal.png',{
 },function(err,file){
   console.log(err);
 });
};

module.exports = takePicture;
