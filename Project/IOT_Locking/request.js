
const request = require('request');
const sendText = function(text){
  request({
    url: 'http://192.168.0.80:3000/display?text='+text,
}, function(err, res, html) {
    if (err) {
        console.log(err);
        return;
    }
    console.log("received server data:");
    console.log(html);
  });
}

module.exports = sendText;
