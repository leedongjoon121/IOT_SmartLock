var firebase = require('firebase');
var config = {
 apiKey: "AIzaSyCEbJoCg4NCbo3SQrpdAdjXJ1bF1zzliV8",
 authDomain: "raspberry-1bf24.firebaseapp.com",
 databaseURL: "https://raspberry-1bf24.firebaseio.com",
 projectId: "raspberry-1bf24",
 storageBucket: "raspberry-1bf24.appspot.com",
 messagingSenderId: "69610193598"
};

firebase.initializeApp(config);
var database = firebase.database();
module.exports = database;
