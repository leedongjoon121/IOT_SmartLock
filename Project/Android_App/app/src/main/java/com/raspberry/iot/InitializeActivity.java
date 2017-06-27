package com.raspberry.iot;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InitializeActivity extends AppCompatActivity {
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        processing();
    }

    public void processing(){
        bar = (ProgressBar)findViewById(R.id.progressBar);
        ProgressAsync asc = new ProgressAsync();
        asc.execute();
    }

    public class ProgressAsync extends AsyncTask<String,Integer,Integer> {
        public ProgressAsync() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            Intent intent  = new Intent(InitializeActivity.this,LoginActivity.class);
            startActivity(intent);
            InitializeActivity.this.finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(String... params) {
            for(int i=0;i<5;i++){
                //생명주기 5개 넣자.
            }
            DatabaseReference df = FirebaseDatabase.getInstance().getReference();
            df.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           //         Toast.makeText(InitializeActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
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
            });
            checkLogin();
            return null;
        }
    }
    public void checkLogin(){
        FirebaseAuth.AuthStateListener  mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent  = new Intent(InitializeActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                 //   Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
}
