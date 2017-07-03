var firebase = require('firebase');
var config = {
  // 내부 키값은 Firebase로 부터 할당 받는다.(보안유지상 제외하였음)
};

firebase.initializeApp(config);
var database = firebase.database();
module.exports = database;
