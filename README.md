# Javascript wiring-pi Api 를 이용한 
# IOT Embedded Smart Lock Project

<hr/>

## Producer Introduce 
### name :  Dongjoonlee
### nation : south korea
### date of birth : 1993.04.06
### univ : gachon university
### email : ehdwns46@naver.com
### day of production : 2017.05.15~ 2017.06.21

<br/>

### coworker :  <a href = "https://github.com/poketred12">SeungJoolee</a> 

<br/>

<hr/>

## Raspberry Pi 3 를 이용한 Embedded Project
## OS : Rasbian Linux
## Node js Server + Android App 을 통한 스마트 금고 제어

<br/><br/>

## 시스템 구성도

- 총 두대의 Rasbperry Pi 기기를 통해(잠금기기와 탐지기기) 금고를 구성하고 모바일로 금고를 제어하며 모든 데이터는 파이어베이스에 저장 

### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/system_arc.PNG?raw=true)

<br/>

## 스마트금고 외관1
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/lock1.PNG?raw=true)


<br/>

## 스마트금고 외관2
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/lock2.PNG?raw=true)


<br/>

## 스마트금고 외관3
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/lock3.PNG?raw=true)

<br/>

## 잠금기기 Activity Diagram
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/activity_lock.PNG?raw=true)

<br/>

## 탐지기기 Activity Diagram
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/activity_detect.PNG?raw=true)

<br/>

## 안드로이드 Mobile Activity Diagram
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/activity_mobile.PNG?raw=true)

<br/>

## Firebase  Activity Diagram
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/activity_cloud.PNG?raw=true)


<br/>

## 모바일 - 메인메뉴
```
모바일 기기 사용 메뉴얼
1. 금고 잠금/해제
2. 금고 탐지값 확인
3. 차트
4. 카메라
5. 특수상황 확인
6. 금고 일련번호 등록
7. 금고 비밀번호 변경
8. 금고 등록 확인
9. 사용자 비밀번호 변경
```

### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/mobile1.PNG?raw=true)


<br/>

## 금고의 잠금 상태 확인 및 화면을 터치하면 금고의 잠금을 제어 가능하다 
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/mobile2.PNG?raw=true)

<br/>

## 금고의 내부 센서 값 확인 

```
 금고 내부 탐지 기능
1. 온도 및 습도
2. 화재감지
3. 거리감지(초음파)
4. 사운드감지
5. 기울기감지
```
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/mobile3.PNG?raw=true)

<br/>

## 금고 내부의 특이사항 발생 데이터 관리 ( 파이어베이스 서버에 저장된 데이터값 )
### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/mobile4.PNG?raw=true)

<br/>

## 총 2대의 카메라 센서로 구성
### 1.금고외부의 침입자 발생 경우(비밀번호 3회이상 틀렸을 경우) 카메라 센서를 통한 사진촬영
### 2.사용자가 언제든지 금고의 내부를 카메라센서를 통해 확인가능
- 해당 사진 데이터도 Firebase에 저장

### ![사진](https://github.com/leedongjoon121/IOT_SmartLock/blob/master/Document/img/mobile5.PNG?raw=true)


