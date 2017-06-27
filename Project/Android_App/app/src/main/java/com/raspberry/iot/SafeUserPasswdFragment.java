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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by poket on 2017-06-03.
 */

public class SafeUserPasswdFragment extends Fragment {
    public SafeUserPasswdFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.safe_user_repassword,null);
        final EditText nowPassword = (EditText)view.findViewById(R.id.passwd);
        final EditText changePassword = (EditText)view.findViewById(R.id.changePasswd);
        final EditText changeRePassword = (EditText)view.findViewById(R.id.changeRePasswd);
        Button okBtn = (Button)view.findViewById(R.id.passwordBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference().child("user").child(fu.getUid()).child("passwd").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String ss = (String)dataSnapshot.getValue();
                        if(ss.equals(nowPassword.getText().toString())){
                            String change = changePassword.getText().toString();
                            if(change.equals(changeRePassword.getText().toString())){
                                fu.updatePassword(change);
                                FirebaseDatabase.getInstance().getReference().child("user").child(fu.getUid()).child("passwd").setValue(change);
                                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                                ab.setTitle("비밀번호 변경 성공");
                                ab.setMessage("정상적으로 비밀번호가 변경되었습니다.");
                                ab.show();
                            }else{
                                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                                ab.setTitle("비밀번호 오류");
                                ab.setMessage("바꾸실 비밀번호를 다시 확인해주세요.");
                                ab.show();
                            }
                        }
                        else{
                            AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                            ab.setTitle("비밀번호 오류");
                            ab.setMessage("비밀번호가 틀렸습니다.");
                            ab.show();
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
