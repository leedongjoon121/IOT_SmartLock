const database = require('./firebase.js');
const serial_number = require('./serialNumber.js');
const firebasePassword = database.ref('embeded-raspberry/'+serial_number+'/password');
const detecting = database.ref('embeded-raspberry/'+serial_number+'/detecting');
const lockjs = require('./lock.js');
const prompt  = require('prompt');
const takePicture = require('./camera.js');
const sendText = require('./request.js');
const buzzer = require('./buzzer.js');
prompt.start();
var passwordCount =  0;
const PromptFunction = function(err,result){
  firebasePassword.once("value",function(snapshot){
     var test = snapshot.val();
     var temp_password = test;
       sendText('Your password :'+result.password);
     if(result.password==temp_password){
    //    console.log("section2");
        lockjs.unlockSafe();
        passwordCount = 0;
      }
      else{
        sendText("Password wrong! :"+result.password);
        passwordCount++;
            buzzer.turnBuzzer(1500);
        //    console.log("passwordCount!!!!!!!!!!!!!!!!!!!!! : "+passwordCount);
        if(passwordCount>=3){
        // 부저 소리 ..
              buzzer.turnBuzzer(500);
              buzzer.turnBuzzer(500);
              var dateNow = Date.now();
              takePicture(serial_number,dateNow); // 카메라 사진 촬영
              sendText("You have incorrect password more than three times.");
    //      shell.exec('raspistill -o test.png');   // 카메라 사진 촬영
          var upload_incorrectpassword = detecting.push(); // 비밀번호 3회 이상 틀렸을 경우 위험 상황
          var  dt = new Date(); //날짜
          var month = dt.getMonth()+1;
          var day = dt.getDate();
          var year = dt.getFullYear();
          var hour = dt.getHours();
          var minute = dt.getMinutes();
          upload_incorrectpassword.set({
            type:"incorrectpassword",
            date: year + '년 '+month+'월 '+day+'일'+hour+' 시'+minute +' 분',
            imgSrc : dateNow
          });
      }
    }
    prompt.get(['password'],PromptFunction);

  });
}
prompt.get(['password'],PromptFunction);
