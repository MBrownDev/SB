package com.example.brown.sb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Brown on 4/11/2017.
 */

public class assignment_list extends Fragment {

    ListView assignList;
    Spinner lists;
    SimpleCursorAdapter cursorAdapter;
    assignmentDatabase asDatabse;
    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.assignment_list,container,false);
        assignList = (ListView)v.findViewById(R.id.assign_list);
        lists = (Spinner)v.findViewById(R.id.listtype);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        asDatabse = new assignmentDatabase(getActivity());
        db = asDatabse.helper.getWritableDatabase();
        asDatabse.open();
        loadSpinner();

        final String user = getActivity().getSharedPreferences("logInfo", Context.MODE_PRIVATE).getString("username","");

        assignList = (ListView)getActivity().findViewById(R.id.assign_list);
        Cursor cursor = asDatabse.getAllAssignments(user);
        final Cursor c2 = asDatabse.getEarliestAssignments(user);
        final Cursor c3 = asDatabse.getLastAssignments(user);
        final String[] fieldNames = new String[]{asDatabse.helper.KEY_ID,asDatabse.helper.KEY_COURSE, asDatabse.helper.KEY_NAME,asDatabse.helper.KEY_SUBJECT,asDatabse.helper.KEY_TIMEIN};
        final int[] twoViewIds = new int[]{R.id.id , R.id.item_header, R.id.item_name, R.id.item_time, R.id.dotw};
        cursorAdapter = new SimpleCursorAdapter(getContext(),R.layout.assignment_item,cursor,fieldNames,twoViewIds,0);

        assignList.setAdapter(cursorAdapter);

        //Spinner selections switch the order of assignment list
        lists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sel;
                sel = parent.getItemAtPosition(position).toString();

                switch (sel){
                    case "Standard":
                        assignList.setAdapter(cursorAdapter);
                        break;
                    case "Soonest Due":
                        cursorAdapter = new SimpleCursorAdapter(getContext(),R.layout.assignment_item,c2,fieldNames,twoViewIds);
                        assignList.setAdapter(cursorAdapter);
                        break;
                    case "Lastest Due":
                        cursorAdapter = new SimpleCursorAdapter(getContext(),R.layout.assignment_item,c3,fieldNames,twoViewIds);
                        assignList.setAdapter(cursorAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Populates spinner with sorting options
    public void loadSpinner(){

        String[] sorts = new String[]{"Standard","Soonest Due","Latest Due"};

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,sorts);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lists.setAdapter(sortAdapter);

    }
}
