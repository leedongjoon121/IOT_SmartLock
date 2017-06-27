package com.raspberry.iot;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        url = getIntent().getStringExtra("url");
        final ImageView iv = (ImageView)findViewById(R.id.imageView);
        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(getIntent().getIntExtra("kind",0)==0){
            //일반 카메라
            StorageReference sr = FirebaseStorage.getInstance().getReference();
            sr.child(getIntent().getStringExtra("serialNumber")+"normal.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    final Uri uri2 = uri;
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          try{
                              Thread.sleep(1000);
                              iv.invalidate();
                              url = uri2.toString();
                              Picasso.with(CameraActivity.this).load(uri2).into(iv);
                              Toast.makeText(CameraActivity.this, "캡쳐완료", Toast.LENGTH_SHORT).show();
                          }catch (Exception e){
                              e.printStackTrace();
                          }
                      }
                  });
                }
            });
        }else{
            //적외선 카메라
            StorageReference sr = FirebaseStorage.getInstance().getReference();
            sr.child(getIntent().getStringExtra("serialNumber")+"noIR.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    final Uri uri2 = uri;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(1000);
                                iv.invalidate();
                                url = uri2.toString();
                                Picasso.with(CameraActivity.this).load(uri2).into(iv);
                                Toast.makeText(CameraActivity.this, "캡쳐완료", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
