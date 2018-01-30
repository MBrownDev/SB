package com.example.brown.sb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class pending_list extends AppCompatActivity {

    friendsDatabase frDatabase;
    SimpleCursorAdapter pendingCursorAdapter;
    SQLiteDatabase dbs;
    ListView pL;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_list);
        frDatabase = new friendsDatabase(this);
        dbs = frDatabase.helper.getReadableDatabase();
        frDatabase.open();

        pL = (ListView)findViewById(R.id.pList);
        final String user = getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");
        cursor = frDatabase.getAllPending(user);
        Log.d("Name",user);

        String[] fromFieldNames = new String[]{frDatabase.helper.KEY_PENDNAME, frDatabase.helper.KEY_PENDEMAIL};
        int[] twoViewIds = new int[]{R.id.frPend,R.id.frPendE};
        pendingCursorAdapter = new SimpleCursorAdapter(this,R.layout.friendslist_item,cursor,fromFieldNames,twoViewIds,0);

//        pendingCursorAdapter = new SimpleCursorAdapter(this,R.layout.friendslist_item,cursor,fromFieldNames,twoViewIds,0);
//        {
//            @Override
//            public View getView(final int position, final View convertView, final ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
//
//                final View accButton = v.findViewById(R.id.acc);
//                final View decButton = v.findViewById(R.id.dec);
//                accButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String ttt = fList.getItemAtPosition(position).toString();
//                        //LinearLayout row = (LinearLayout)((LinearLayout)convertView).getChildAt(0);
//                        Cursor cursor = (Cursor) parent.getChildAt(position);
//                        String name = cursor.getString(cursor.getColumnIndex(frDatabase.helper.KEY_NAME));
//                        MessageToast.message(getActivity(),name);
//
//
//                    }
//                });
//
//                decButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        frDatabase.deletePending(user,);
//                    }
//                });
//                return v;
//            }
//        };
//        pL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
//
//                final View accButton = parent.findViewById(R.id.acc);
//                final View decButton = parent.findViewById(R.id.dec);
//                accButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //String ttt = fList.getItemAtPosition(position).toString();
//                        //LinearLayout row = (LinearLayout)((LinearLayout)convertView).getChildAt(0);
//                        Cursor cursor = (Cursor) parent.getChildAt(position);
//                        String name = cursor.getString(cursor.getColumnIndex(frDatabase.helper.KEY_NAME));
//                        MessageToast.message(pending_list.this,name);
//
//
//                    }
//                });
//
//                decButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        //frDatabase.deletePending(user,);
//                    }
//                });
//            }
//        });
        pL.setAdapter(pendingCursorAdapter);
    }
}
