package com.raspberry.iot;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button emailBtn,signupBtn;
    private LinearLayout linear;
    private boolean isLinearOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailBtn = (Button)findViewById(R.id.emailButton);
        signupBtn = (Button)findViewById(R.id.button2);
        linear = (LinearLayout)findViewById(R.id.linearLayout);
        Button loginBtn = (Button)findViewById(R.id.button5);
        final EditText idText = (EditText)findViewById(R.id.emailEdit);
        final EditText passText = (EditText)findViewById(R.id.passwordEdit);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = idText.getText().toString();
                String pass = passText.getText().toString();
                Toast.makeText(LoginActivity.this, "로그인 요청중", Toast.LENGTH_SHORT).show();
                mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        TextView textView1 = (TextView)findViewById(R.id.textView4);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                ab.setTitle("비밀번호 찾기");
                View view = View.inflate(getApplicationContext(),R.layout.dialog_passwordfind,null);
                ab.setView(view);
                final EditText et = (EditText)view.findViewById(R.id.findemail);
                ab.setPositiveButton("찾기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String emailAddress = et.getText().toString();

                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "입력하신 이메일로 인증코드가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(LoginActivity.this, "오류가 발생하였습니다.\n이메일을 다시 한번 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
                ab.show();
            }
        });
        setListener();
    }
    private void setListener(){
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLinearOpen){
                    //열린 상태
                    linear.setVisibility(View.GONE);
                    openLinear();
                    isLinearOpen = false;

                }else{
                    //닫힌 상태
                    linear.setVisibility(View.VISIBLE);
                    closeLinear();
                    isLinearOpen = true;
                }
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private void closeLinear(){
     for(int i=0;i<linear.getChildCount();i++){
         View v = linear.getChildAt(i);
         ObjectAnimator ob = ObjectAnimator.ofFloat(v,"alpha",0.0f);
         ob.setDuration(0);
         ob.start();
         ObjectAnimator oa = ObjectAnimator.ofFloat(v,"alpha",1.0f);
         oa.setDuration(500);
         oa.start();
     }
    }
    public void openLinear(){
        for(int i=0;i<linear.getChildCount();i++){
            View v = linear.getChildAt(i);
            ObjectAnimator oa = ObjectAnimator.ofFloat(v,"alpha",0.0f);
            oa.setDuration(500);
            oa.start();
        }
    }
}
