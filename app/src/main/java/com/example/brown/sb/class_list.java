package com.example.brown.sb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brown on 12/15/2016.
 */

public class class_list extends Fragment {

    ListView myList;
    SimpleCursorAdapter myCursorAdapter, mCursorAdapter, tCursorAdapter, wCursorAdapter, thCursorAdapter, fCursorAdapter;
    courseDatabase cdHelper;
    SQLiteDatabase db;
    AlertDialog.Builder alertDialog;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;

    Calendar calendar;
    TextView header,classn,ti,tt;

    MyCursorAdapter cursorAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.class_list,container,false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf");
/*
        header = (TextView)v.findViewById(R.id.item_header);
        classn = (TextView)v.findViewById(R.id.item_name);
        ti = (TextView)v.findViewById(R.id.item_time);
        tt = (TextView)v.findViewById(R.id.item_timeout);

        header.setTypeface(tf);
        classn.setTypeface(tf);
        ti.setTypeface(tf);
        tt.setTypeface(tf);
*/
        return v;
    }
    private void openData() {

        cdHelper = new courseDatabase(getActivity());
        db = cdHelper.helper.getWritableDatabase();
        cdHelper.open();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //openData();
        //populateList();
        cdHelper = new courseDatabase(getActivity());
        db = cdHelper.helper.getWritableDatabase();
        cdHelper.open();

        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete Course");
        alertDialog.setMessage("Are you sure want to delete this course?");

        final String user = getActivity().getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Cursor cursor = cdHelper.getAllRows(user);
        Cursor mCursor = cdHelper.getMonday(user);
        Cursor tCursor = cdHelper.getTuesday(user);
        Cursor wCursor = cdHelper.getWednesday(user);
        Cursor thCursor = cdHelper.getThursday(user);
        Cursor fCursor = cdHelper.getFriday(user);

        String[] fromFieldNames = new String[]{cdHelper.helper.KEY_ID, cdHelper.helper.KEY_NUMBER, cdHelper.helper.KEY_NAME, cdHelper.helper.KEY_TIMEIN,cdHelper.helper.KEY_TIMEOUT, cdHelper.helper.KEY_DAYS};
        int[] twoViewIds = new int[]{R.id.id ,R.id.item_header, R.id.item_name, R.id.item_time, R.id.item_timeout, R.id.dotw};

        myCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, cursor, fromFieldNames, twoViewIds, 0);
        mCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, mCursor, fromFieldNames, twoViewIds, 0);
        tCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, tCursor, fromFieldNames, twoViewIds, 0);
        wCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, wCursor, fromFieldNames, twoViewIds, 0);
        thCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, thCursor, fromFieldNames, twoViewIds, 0);
        fCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, fCursor, fromFieldNames, twoViewIds, 0);

        myList = (ListView)getActivity().findViewById(R.id.course_list);
        //myList.setSelector(R.drawable.selector);

        switch (day){
            case Calendar.SUNDAY:
                myList.setAdapter(mCursorAdapter);
                break;
            case Calendar.MONDAY:
                myList.setAdapter(mCursorAdapter);
                break;
            case Calendar.TUESDAY:
                myList.setAdapter(tCursorAdapter);
                break;
            case Calendar.WEDNESDAY:
                myList.setAdapter(wCursorAdapter);
                break;
            case Calendar.THURSDAY:
                myList.setAdapter(thCursorAdapter);
                break;
            case Calendar.FRIDAY:
                myList.setAdapter(fCursorAdapter);
                break;
            case Calendar.SATURDAY:
                myList.setAdapter(mCursorAdapter);
                break;
        }

        alertDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = getActivity().getSharedPreferences("regInfo", MODE_PRIVATE).getInt("position",0);
                cdHelper.deleteClass(id);
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                regStore(position);
                Intent i = new Intent(getActivity(), assignment_screen.class);
                startActivity(i);
            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                regStore(position);
                alertDialog.show();

                return true;
            }
        });
    }

    public void regStore(int id){
        preferences = getActivity().getSharedPreferences("regInfo", MODE_PRIVATE);
        edit = preferences.edit();
        edit.putInt("position",id);
        edit.commit();
    }

//    private void populateList() {
//        Cursor cursor = cdHelper.getAllRows();
//        String[] fromFieldNames = new String[]{cdHelper.helper.KEY_ID, cdHelper.helper.KEY_NUMBER, cdHelper.helper.KEY_NAME, cdHelper.helper.KEY_TIMEIN};
//        int[] twoViewIds = new int[]{R.id.id ,R.id.item_header, R.id.item_name, R.id.item_time};
//        myCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, cursor, fromFieldNames, twoViewIds, 0);
//        myList = (ListView)getActivity().findViewById(R.id.course_list);
//        //myList.setSelector(R.drawable.selector);
//        myList.setAdapter(myCursorAdapter);
//
//
//    }
}
