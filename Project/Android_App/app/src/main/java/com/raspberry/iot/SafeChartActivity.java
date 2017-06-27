package com.raspberry.iot;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class SafeChartActivity extends AppCompatActivity {
    private LineChart lc;
    private ArrayList<Entry> humiArray = new ArrayList<>();
    private ArrayList<Entry> temperArray = new ArrayList<>();
    private DatabaseReference dr;
    private Query query;
    @Override
    protected void onStart() {
        super.onStart();
        dr = FirebaseDatabase.getInstance().getReference().child("embeded-raspberry").child(getIntent().getStringExtra("serialNumber")).child("detecting");
        query = dr.orderByKey();
        query.addChildEventListener(mChild);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(query!=null&&mChild!=null)
         query.removeEventListener(mChild);
    }
    LineDataSet dataSet;
    LineDataSet dataSet2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_chart);
        lc = (LineChart)findViewById(R.id.chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        humiArray.add(new Entry(0,0));
        temperArray.add(new Entry(0,0));
        dataSet = new LineDataSet(humiArray, "습도");
        dataSet.notifyDataSetChanged();
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet2 = new LineDataSet(temperArray, "온도");
        dataSet2.setColor(Color.BLUE);
        dataSet2.setValueTextColor(Color.WHITE);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        lineData.addDataSet(dataSet2);
        lc.setData(lineData);
        lc.setAutoScaleMinMaxEnabled(true);
        lc.getLegend().setTextColor(Color.WHITE);
        lc.setFitsSystemWindows(true);
        lc.getXAxis().setAxisLineColor(Color.WHITE);
        lc.getXAxis().setTextColor(Color.WHITE);
        lc.getXAxis().setGridColor(Color.WHITE);
        lc.getXAxis().setDrawAxisLine(false);
        lc.getAxisLeft().setAxisLineColor(Color.WHITE);
        lc.getAxisLeft().setTextColor(Color.WHITE);
        lc.getAxisLeft().setGridColor(Color.WHITE);
        lc.getAxisRight().setDrawTopYLabelEntry(false);
        lc.getAxisRight().setDrawZeroLine(false);
        lc.getAxisRight().setDrawAxisLine(false);
        lc.getDescription().setTextColor(Color.WHITE);
        lc.getDescription().setText("");
    }
    private ChildEventListener mChild = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String,String> hashMap = (HashMap)dataSnapshot.getValue();
            if(hashMap!=null){
                    HashMap<String,String> hh = hashMap;
                    if(hh.get("type").equals("dht")){
                        if(humiArray.get(0).getY()==0){
                            humiArray.remove(0);
                        }
                        if(temperArray.get(0).getY()==0){
                            temperArray.remove(0);
                        }
                        humiArray.add(new Entry(humiArray.size()+1,Float.parseFloat(hh.get("humidity"))));
                        temperArray.add(new Entry(temperArray.size()+1,Float.parseFloat(hh.get("temperature"))));

                    }
                    dataSet.notifyDataSetChanged();;
                dataSet2.notifyDataSetChanged();
                lc.invalidate();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
