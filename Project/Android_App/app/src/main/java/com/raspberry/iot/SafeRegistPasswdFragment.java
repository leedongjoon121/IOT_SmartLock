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

public class SafeRegistPasswdFragment extends Fragment{
    public SafeRegistPasswdFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.safe_serial_regist_passwd,null);
        final EditText registEdit = (EditText)view.findViewById(R.id.registSerial);
        final EditText registPasswordEdit = (EditText)view.findViewById(R.id.registSerialPasswd);
        final EditText registChangePasswordEdit = (EditText)view.findViewById(R.id.registSerialChangePasswd);
        final EditText registChangeRePasswordEdit = (EditText)view.findViewById(R.id.registSerialChangeRePasswd);
        Button registBtn = (Button)view.findViewById(R.id.registBtn);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String serial = registEdit.getText().toString();
                final String pass = registPasswordEdit.getText().toString();
                final String changePw = registChangePasswordEdit.getText().toString();
                final String changePw2 = registChangeRePasswordEdit.getText().toString();
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
                       //     Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        }
                        if(check){
                            //발견!!
                            dr.child("embeded-raspberry").child(serial).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pass2 = (String)dataSnapshot.getValue();
                                    if(pass.equals(pass2)){
                                        //성공
                                        if(changePw.equals(changePw2)){
                                            //같음
                                            dr.child("embeded-raspberry").child(serial).child("password").setValue(changePw);
                                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                            alert.setTitle("비밀번호 변경 성공");
                                            alert.setMessage("정상적으로 변경하였습니다.");
                                            alert.show();
                                            registEdit.setText("");
                                            registPasswordEdit.setText("");
                                            registChangePasswordEdit.setText("");
                                            registChangeRePasswordEdit.setText("");
                                        }
                                        else{
                                            //다름
                                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                            alert.setTitle("비밀번호 변경 실패");
                                            alert.setMessage("바꾸실 비밀번호를 다시 써주세요.");
                                            alert.show();
                                            registChangePasswordEdit.setText("");
                                            registChangeRePasswordEdit.setText("");

                                        }
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
                            registEdit.setText("");
                            registPasswordEdit.setText("");
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
