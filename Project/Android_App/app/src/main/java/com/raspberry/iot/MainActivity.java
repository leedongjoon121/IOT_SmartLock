package com.raspberry.iot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int fragment= 0;
    private Fragment currentFragment;
    private FirebaseAuth.AuthStateListener au;
    public void changeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        switch (fragment){
            case 0:
                currentFragment = new SafeStatusFragment();
                fm.beginTransaction().replace(R.id.fragmentFrame,currentFragment).commit();
                break;
            case 1:
                currentFragment = new SafeRegistFragment();
                fm.beginTransaction().replace(R.id.fragmentFrame,currentFragment).commit();
                break;
            case 2:
                currentFragment = new SafeRegistPasswdFragment();
                fm.beginTransaction().replace(R.id.fragmentFrame,currentFragment).commit();
                break;
            case 3:
                currentFragment = new SafeRegistListFragment();
                fm.beginTransaction().replace(R.id.fragmentFrame,currentFragment).commit();
                break;
            case 4:
                currentFragment = new SafeUserPasswdFragment();
                fm.beginTransaction().replace(R.id.fragmentFrame,currentFragment).commit();
                break;
            case 5:
                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragment();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        au = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fu = firebaseAuth.getCurrentUser();
                if(fu!=null){
                    final TextView title = (TextView)findViewById(R.id.naviUserName);
                    TextView email = (TextView)findViewById(R.id.naviEmail);
                        if(fu.getEmail()!=null&&email!=null)
                         email.setText(fu.getEmail());
                    FirebaseDatabase.getInstance().getReference().child("user/"+fu.getUid() ).child("safe").addChildEventListener(cel);
                    FirebaseDatabase.getInstance().getReference().child("user/"+fu.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(title!=null)
                            title.setText((String)dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    finish();
                }

            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(au);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private int selectSerial = 0;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_regist) {
            //금고 등록 선택
            fragment =1;
        } else if (id == R.id.nav_passwdChange) {
            //금고 비밀번호 변경
            fragment = 2;
        } else if (id == R.id.nav_registList) {
            //금고 등록 리스트
            fragment = 3;
        } else if (id == R.id.nav_manage) {
            //사용자 비밀번호 변경
            fragment = 4;
        } else if (id == R.id.nav_logout) {
            //로그아웃
            FirebaseAuth.getInstance().signOut();
            finish();
        } else if(id==R.id.nav_home){
            //메인화면으로 돌아가기
            fragment =0;
        } else if(id==R.id.nav_de){
            //특수상황 보기
            selectSerial("특수상황 확인",EmergencyActivity.class);
        }else if(id==R.id.nav_detect){
           selectSerial("탐지값 확인",DetectActivity.class);

        }else if(id==R.id.nav_chart){
            selectSerial("차트 확인",SafeChartActivity.class);
        }
        else if(id==R.id.nav_camera){
            //카메라 확인
            selectCamera(CameraActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        changeFragment();
        return true;
    }
    public boolean selectSerial(String message,final Class selectClass){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(message);
        if(selectList.size()==0){
            ab.setMessage("금고를 먼저 등록해주세요.");
            ab.show();
            return true;
        }
        String[] arr = new String[selectList.size()];
        for(int i=0;i<selectList.size();i++){
            arr[i] = selectList.get(i);
        }
        ab.setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectSerial = which;
                //   startActivity(intent);

            }
        });
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this,selectClass);
                intent.putExtra("serialNumber",selectList.get(selectSerial));
                startActivity(intent);
            }
        });
        ab.show();
        return true;
    }
    int selectCamera;
    public boolean selectCamera(final Class selectClass){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("카메라 확인");
        if(selectList.size()==0){
            ab.setMessage("금고를 먼저 등록해주세요.");
            ab.show();
            return true;
        }
        String[] arr = new String[selectList.size()];
        for(int i=0;i<selectList.size();i++){
            arr[i] = selectList.get(i);
        }
        ab.setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectSerial = which;
                //   startActivity(intent);

            }
        });
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent intent = new Intent(MainActivity.this,selectClass);
                intent.putExtra("serialNumber",selectList.get(selectSerial));
                AlertDialog.Builder ab2 = new AlertDialog.Builder(MainActivity.this);
                ab2.setTitle("카메라 종류 선택");
                String[] arr2= {"외부","내부"};
                ab2.setSingleChoiceItems(arr2, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     selectCamera=which;
                    }
                });
                ab2.setPositiveButton("사진캡쳐", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       FirebaseDatabase.getInstance().getReference().child("embeded-raspberry").child(selectList.get(selectSerial)).child("command").push().child("type").setValue("camera");
                        intent.putExtra("kind",selectCamera);
                        startActivity(intent);
                    }
                });
                ab2.show();
            }
        });
        ab.show();
        return true;
    }
    @Override
    protected void onDestroy() {
        FragmentManager fm = getSupportFragmentManager();
       // fm.beginTransaction().remove(currentFragment).commit();
        super.onDestroy();
        try{
            FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("safe").removeEventListener(cel);
        }catch (Exception e){

        }

    }
    ArrayList<String> selectList = new ArrayList<>();
    private ChildEventListener cel=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String, String> hashMap = (HashMap) dataSnapshot.getValue();
            if (hashMap != null) {
                String serialNumber = hashMap.get("serialNumber");
                selectList.add(serialNumber);
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
    @Override
    public void onStop() {
        super.onStop();
        if (au != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(au);
        }
    }
}
