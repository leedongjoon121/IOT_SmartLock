package com.raspberry.iot;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EmergencyActivity extends AppCompatActivity {


    ArrayList<MessageData> arrList = new ArrayList<>();
    String serialNumber="";
    DatabaseReference dr;
    MyAdater myAdater = new MyAdater();
    private Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        serialNumber = getIntent().getStringExtra("serialNumber");
        dr = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry").child(serialNumber).child("detecting");
        query = dr.orderByKey();
        query.addChildEventListener(vel);
        ListView listView = (ListView)findViewById(R.id.messageList);
        listView.setAdapter(myAdater);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    public class MyAdater extends BaseAdapter{
        @Override
        public int getCount() {
            return arrList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = View.inflate(EmergencyActivity.this,R.layout.safe_message_list,null);
            }
            ImageView iv = (ImageView)convertView.findViewById(R.id.cameraImage);
            TextView type = (TextView)convertView.findViewById(R.id.typeText);
            TextView date = (TextView)convertView.findViewById(R.id.dateTv);
            TextView detail=(TextView)convertView.findViewById(R.id.detailTv);
            MessageData md = arrList.get(position);
            type.setText(md.type);
            date.setText(md.date);
            detail.setText("");
            if(md.type.equals("dht")){
                type.setText("온도 감지");
                detail.setText("온도는"+md.data1+"이며, 습도는 "+md.data2);
            }
            else if(md.type.equals("비밀번호 3회이상 틀림")){
                detail.setText("비밀번호를 연속적으로 3회이상 틀렸습니다.");
            }
            else if(md.type.equals("초음파 감지")){
                detail.setText(md.data1+" 값이 감지되었습니다.");
            }
            else{

            }
            return convertView;
        }

    }
    ChildEventListener vel = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String,Object> hh = (HashMap)dataSnapshot.getValue();
            HashMap<String,String> aa = (HashMap)hh;
            String type = (String)aa.get("type");
            MessageData messageData = new MessageData();
            if(type!=null){
                if(type.equals("dht")){
                    String data1 = aa.get("humidity");
                    String data2 = aa.get("temperature");
                    messageData.data1 = data1;
                    messageData.data2 = data2;
                    messageData.type = "dht";
                }else if(type.equals("flame")){
                    String data1 = aa.get("data");
                    //       messageData.data1=data1;
                    messageData.type ="화재 감지";
                }else if(type.equals("sound")){
                    String data1 = aa.get("data");
                    messageData.type="사운드 감지";
                }else if(type.equals("tilt")){
                    String data1 = aa.get("data");
                    messageData.type="기울기 감지";
                }else if(type.equals("ultrasonic")){
                    String data1 = aa.get("data");
                    messageData.data1=data1;
                    messageData.type="초음파 감지";
                }else if(type.equals("incorrectpassword")){
                    //String data1 = aa.get("data");
                    //  messageData.data1=data1;
                    messageData.type="비밀번호 3회이상 틀림";
                }
                else{
                    return;
                }
            }else{
                return;
            }
            messageData.date = aa.get("date");
            arrList.add(messageData);

            myAdater.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        if(query!=null&&vel!=null)
            query.removeEventListener(vel);
    }
    public class MessageData{
        String type;
        String data1;
        String data2;
        String date;
    }
}
