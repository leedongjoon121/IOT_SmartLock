const fs = require('fs');
const v = require('voca');
var data = fs.readFileSync('./config.txt');
module.exports = v.trim(data);
