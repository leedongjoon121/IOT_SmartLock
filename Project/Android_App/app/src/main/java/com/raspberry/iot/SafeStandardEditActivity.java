package com.raspberry.iot;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SafeStandardEditActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SafeStandardEditActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DatabaseReference drr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        SafeStandardEditActivity.PlaceholderFragment.serialNumber = getIntent().getStringExtra("serialNumber");
        mSectionsPagerAdapter = new SafeStandardEditActivity.SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        drr= dr.child("embeded-raspberry").child(   SafeStandardEditActivity.PlaceholderFragment.serialNumber).child("detect_log");
        drr.addValueEventListener(vel);

        for(int i=0;i<5;i++){
            btns[i] = (Button)findViewById(btnIds[i]);
        }
        // Set up the ViewPager with the sections adapter.

        btns[0].setBackgroundColor(Color.RED);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<5;i++){
                    btns[i].setBackground(null);
                }
                btns[position].setBackgroundColor(Color.RED);  }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    int[] btnIds = {R.id.humiBtn,R.id.flameBtn,R.id.ultraBtn,R.id.soundBtn,R.id.tiltBtn};
    static Button[] btns = new Button[5];
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.humiBtn:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.flameBtn:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ultraBtn:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.soundBtn:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.tiltBtn:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        static String flame="";
        static String sound="";
        static String tilt="";
        static String ultrasonic="";
        static String humidity="";
        static String temperature="";
        static String serialNumber = "";
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static  SafeStandardEditActivity.PlaceholderFragment newInstance(int sectionNumber) {

            SafeStandardEditActivity.PlaceholderFragment fragment = new  SafeStandardEditActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionId=  getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView tv = (TextView)rootView.findViewById(R.id.textView5);
            View.OnClickListener vc=null;
            final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry").child(serialNumber).child("command").push().child("type");
            switch (sectionId){
                case 1:
                    //온도 및 습도
                    //    FirebaseDatabase.getInstance().getReference().child("embeded-raspberry").child()
                    vc=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dr.setValue("dht");
                        }
                    };
                    tv.setText("현재 온도는"+temperature+"이며\n습도는"+humidity+"\n입니다.");
                    break;
                case 2:
                    //화재 감지
                    vc=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dr.setValue("flame");
                        }
                    };
                    tv.setText("현재 화재감지 상태는\n"+flame+"\n입니다.");
                    break;
                case 3:
                    vc=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dr.setValue("ultrasonic");
                        }
                    };
                    tv.setText("현재 초음파감지 거리는\n"+ultrasonic+"\n입니다.");
                    //초음파 감지
                    break;
                case 4:
                    vc=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dr.setValue("sound");
                        }
                    };
                    tv.setText("현재 사운드감지 값은\n"+sound+"\n입니다.");
                    //사운드 감지
                    break;
                case 5:
                    vc=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dr.setValue("tilt");
                        }
                    };
                    tv.setText("현재 기울기 값은\n"+tilt+"\n입니다.");
                    //기울기 감지
                    break;
            }
            tv.setOnClickListener(vc);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return  SafeStandardEditActivity.PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
                case 4:
                    return "SECTION 5";
            }
            return null;
        }
    }
    ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String,Object> hhh = (HashMap)dataSnapshot.getValue();
            for(String s : hhh.keySet()){
                HashMap<String,String> aa = (HashMap)hhh.get(s);
                if(s.equals("flame")){
                    //화재 감지센서
                    SafeStandardEditActivity.PlaceholderFragment.flame = aa.get("data");
                }else if(s.equals("humidity")){
                    //온습도 센서
                    SafeStandardEditActivity.PlaceholderFragment.humidity = aa.get("humidity");
                    SafeStandardEditActivity.PlaceholderFragment.temperature = aa.get("temperature");
                } else if (s.equals("tilt")) {
                    //기울기 센서
                    SafeStandardEditActivity.PlaceholderFragment.tilt = aa.get("data");
                }else if(s.equals("sound")){
                    //사운드 센서
                    SafeStandardEditActivity.PlaceholderFragment.sound = aa.get("data");
                }else if(s.equals("ultrasonic")){
                    //초음파 센서
                    SafeStandardEditActivity.PlaceholderFragment.ultrasonic = aa.get("data");
                }
            }
            mSectionsPagerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    @Override
    protected void onDestroy() {
        drr.removeEventListener(vel);
        super.onDestroy();

    }
}
