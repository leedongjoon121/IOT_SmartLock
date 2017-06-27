package com.raspberry.iot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by poket on 2017-06-03.
 */

public class SafeRegistFragment extends Fragment {
    public SafeRegistFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.safe_serial_regist,null);
        final EditText registEdit = (EditText)view.findViewById(R.id.registSerial);
        final EditText registPasswordEdit = (EditText)view.findViewById(R.id.registSerialPasswd);
        Button registBtn = (Button)view.findViewById(R.id.registBtn);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String serial = registEdit.getText().toString();
                final String pass = registPasswordEdit.getText().toString();
                final DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
                dr.child("embeded-raspberry").addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean check = false;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       HashMap<String,Object> hashMap = (HashMap)dataSnapshot.getValue();
                        for(String s : hashMap.keySet()){
                            if(s.equals(serial)){
                                //발견
                                check=true;
                                break;
                            }
                        //    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        }
                        if(check){
                            //발견!!
                            dr.child("embeded-raspberry").child(serial).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pass2 = (String)dataSnapshot.getValue();
                                    if(pass.equals(pass2)){
                                        //성공
                                        dr.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("safe").addListenerForSingleValueEvent(new ValueEventListener() {
                                            boolean check=false;
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                HashMap<String , Object> hash = (HashMap)dataSnapshot.getValue();
                                                if(hash!=null) {
                                                    for (String s : hash.keySet()) {
                                                        HashMap<String, String> hh = (HashMap) hash.get(s);
                                                        if (hh.get("serialNumber").equals(serial)) {
                                                            check = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if(check){
                                                    //중복
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                                    alert.setTitle("금고 등록 실패");
                                                    alert.setMessage("이미 등록된 금고입니다.");
                                                    alert.show();
                                                }else{
                                                    dr.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("safe").push().child("serialNumber").setValue(serial);
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                                    alert.setTitle("금고 등록 성공");
                                                    alert.setMessage("금고가 정상적으로 등록되었습니다.");
                                                    alert.show();
                                                }
                                                registEdit.setText("");
                                                registPasswordEdit.setText("");
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else{
                                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                        alert.setTitle("금고 등록 실패");
                                        alert.setMessage("금고의 비밀번호가 맞지 않습니다.");
                                        alert.show();
                                        registEdit.setText("");
                                        registPasswordEdit.setText("");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("금고 등록 실패");
                            alert.setMessage("금고의 일련번호를 확인해주세요");
                            alert.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
