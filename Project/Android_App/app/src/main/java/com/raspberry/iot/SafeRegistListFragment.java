package com.raspberry.iot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by poket on 2017-06-03.
 */

public class SafeRegistListFragment extends Fragment {
    public SafeRegistListFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.safe_serial_list,null);
        ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter());
        return view;
    }
    public class MyAdapter extends BaseAdapter{
        ArrayList<String> arrayList = new ArrayList<>();
        public MyAdapter() {
            DatabaseReference df = FirebaseDatabase.getInstance().getReference();
            df.child("user/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("safe").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,Object> mm = (HashMap)dataSnapshot.getValue();
                    for(String s : mm.keySet()){
                        HashMap<String,String> aaa =(HashMap<String, String>) mm.get(s);
                        arrayList.add(aaa.get("serialNumber"));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = View.inflate(getContext(),R.layout.safe_item,null);
            }
            TextView serial = (TextView)convertView.findViewById(R.id.mySerial);
            serial.setText(arrayList.get(position));
   //         Toast.makeText(getContext(), arrayList.get(position), Toast.LENGTH_SHORT).show();
            Button deleteButton = (Button)convertView.findViewById(R.id.deleteBtn);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference dr ;

                    dr = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference hq= dr.child("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("safe");
                    hq.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String,Object> hhh = (HashMap)dataSnapshot.getValue();
                            for(String s : hhh.keySet()){
                                HashMap<String,String> sss =(HashMap<String, String>) hhh.get(s);
                                if(sss.get("serialNumber").equals(arrayList.get(position))){
                                    hq.child(s).removeValue();
                                    arrayList.remove(position);
                                    MyAdapter.this.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
            return convertView;
        }
    }
}
