package com.raspberry.iot;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText nameText = (EditText)findViewById(R.id.nameSignEdit);
        final EditText passwordText = (EditText)findViewById(R.id.passwordSignEdit);
        final EditText passwordReText = (EditText)findViewById(R.id.passwordReSignEdit);
        final EditText emailText = (EditText)findViewById(R.id.emailSignEdit);
        Button signupButton = (Button)findViewById(R.id.button3);
        fa = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameText.getText().toString();
                final String password = passwordText.getText().toString();
                String repassword = passwordReText.getText().toString();
                String email = emailText.getText().toString();
                if(password.equals(repassword)){
                //    Toast.makeText(SignUpActivity.this, "회원가입 요청"+email+",,"+password, Toast.LENGTH_SHORT).show();
                    fa.createUserWithEmailAndPassword(email,password).addOnFailureListener(SignUpActivity.this,new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e.getMessage().equals("The email address is alrealy in use by another account.")){
                                AlertDialog.Builder ab = new AlertDialog.Builder(SignUpActivity.this);
                                ab.setTitle("회원가입 실패");
                                ab.setMessage("해당 이메일 주소는 이미 사용중 입니다.");
                                ab.show();
                            }
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String uid = authResult.getUser().getUid();
                            FirebaseDatabase.getInstance().getReference().child("user/"+uid).child("name").setValue(name);
                            FirebaseDatabase.getInstance().getReference().child("user/"+uid).child("passwd").setValue(password);
                            AlertDialog.Builder ab = new AlertDialog.Builder(SignUpActivity.this);
                            ab.setTitle("회원가입 성공");
                            ab.setMessage("회원가입에 성공하였습니다.");
                            ab.show();
                            ab.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            });
                        }
                    });
                }
                else{
                    AlertDialog.Builder ab = new AlertDialog.Builder(SignUpActivity.this);
                    ab.setTitle("회원가입 실패");
                    ab.setMessage("비밀번호를 동일하게 입력했는지 확인해주세요.");
                    ab.show();
                }

            }
        });

    }
}
