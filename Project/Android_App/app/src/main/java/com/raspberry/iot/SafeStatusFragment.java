package com.raspberry.iot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 현재 금고의 열림 상태를 알려주는 프래그먼트.
 */

public class SafeStatusFragment extends Fragment {
    private ArrayList<String> selectList ;
    private ArrayAdapter<String> ad;
    private Spinner spinner;
    private boolean isLock = false;
    public SafeStatusFragment() {
    }
    private TextView tv;
    private TextView refreshTv;
    private DatabaseReference safeDr;
    private DatabaseReference stateDr;
    private String uid;
    private ValueEventListener checkStatus = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try{
                String checkLock = (String)dataSnapshot.getValue();
                if(checkLock.equals("unlock")){
                    //열린 상태
                    isLock = false;
                    tv.setBackground(getResources().getDrawable(R.drawable.green_circle_tv));
                    tv.setText("현재 금고는 열려있습니다.");
                }else if(checkLock.equals("lock")){
                    //닫힌 삳태
                    isLock = true;
                    tv.setBackgroundResource(R.drawable.red_circle_tv);
                    //tv.setBackground(getResources().getDrawable(R.drawable.red_circle_tv));
                    tv.setText("현재 금고는 닫혀있습니다.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    boolean isFirst = false;
    String nowSerial;
    private ValueEventListener spinnerListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String checkLock = (String)dataSnapshot.getValue();
            if(checkLock.equals("unlock")){
                //열린 상태
                isLock = false;
                tv.setBackground(getResources().getDrawable(R.drawable.green_circle_tv));
                tv.setText("현재 금고는 열려있습니다.");
            }else if(checkLock.equals("lock")){
                //닫힌 삳태
                isLock = true;
                tv.setBackground(getResources().getDrawable(R.drawable.red_circle_tv));
                tv.setText("현재 금고는 닫혀있습니다.");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ChildEventListener cel=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String, String> hashMap = (HashMap) dataSnapshot.getValue();
            if (hashMap != null) {
                String serialNumber = hashMap.get("serialNumber");
                selectList.add(serialNumber);
                ad.notifyDataSetChanged();
                if(isFirst) {


                }else{

                    isFirst = true;
                    nowSerial = serialNumber; //현재 시리얼
                    commandCheckLock();
                }
            }
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
    public void commandCheckLock(){
        if(nowSerial!=null){
      //      Toast.makeText(getContext(), "스피너리스너"+nowSerial, Toast.LENGTH_SHORT).show();
            stateDr = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry/"+nowSerial).child("lock");
            stateDr.addValueEventListener(checkStatus);
            DatabaseReference push = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry/"+nowSerial).child("command").push();
            push.child("type").setValue("checkLock");
        }
    }
    public void commandOpen(){
            DatabaseReference push = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry/"+nowSerial).child("command").push();
            push.child("type").setValue("unlock");
    }

    public void commandClose(){
        DatabaseReference push = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry/"+nowSerial).child("command").push();
        push.child("type").setValue("lock");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = container.inflate(getContext(),R.layout.safe_status,null);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        selectList = new ArrayList<>();
        ad = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,selectList);
        spinner.setAdapter(ad);
        tv = (TextView)view.findViewById(R.id.textView5);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Toast.makeText(getContext(), "wqdqd", Toast.LENGTH_SHORT).show();
                if(isLock){
                    //잠김 상태 열어줘!
                    commandOpen();
                }
                else{
                    //연 상태 잠가줘...
                    commandClose();
                }
            }
        });
        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }safeDr = df.child("user/"+uid ).child("safe");
        safeDr.addChildEventListener(cel);
        changeSerial();
        return view;
    }
    @Override
    public void onDestroy() {
        safeDr.removeEventListener(cel);
        if(stateDr!=null)
            stateDr.removeEventListener(checkStatus);
        if(lockRefer!=null)
            lockRefer.removeEventListener(spinnerListener);
        super.onDestroy();
    }
    DatabaseReference lockRefer;
    public  void changeSerial(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String serial =  ad.getItem(position);
                nowSerial = serial;
                lockRefer= FirebaseDatabase.getInstance().getReference().child("embeded-raspberry/"+serial).child("lock");
                lockRefer.addValueEventListener(spinnerListener);
                commandCheckLock();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
