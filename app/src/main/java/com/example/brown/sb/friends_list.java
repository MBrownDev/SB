package com.example.brown.sb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brown on 4/18/2017.
 */

public class friends_list extends Fragment {

    ListView fList;
    friendsDatabase frDatabase;
    SimpleCursorAdapter pendingCursorAdapter,friendCursorAdapter;
    SQLiteDatabase db;

    Button acceppt,decline;
    Cursor cursor;
    TextView pReq;
    Spinner lists;
    String fr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_list, container,false);

        //lists = (Spinner)v.findViewById(R.id.flists);
        //fList = (ListView)v.findViewById(R.id.friends_listview);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        frDatabase = new friendsDatabase(getActivity());
        db = frDatabase.helper.getReadableDatabase();
        frDatabase.open();

        fr = "";
        pReq = (TextView)getActivity().findViewById(R.id.pRequests);
        pReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),pending_list.class);
                startActivity(i);
            }
        });
        //lists = (Spinner)getActivity().findViewById(R.id.flists);

        //Retrieves username from Shared Preferences to determine who's pending friends list to show.
        final String user = getActivity().getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");

        final BackgroundDeleteTask deleteTask = new BackgroundDeleteTask();

        //Populate List View with email and username from pending friends.
        cursor = frDatabase.getAllPending(user);
        Cursor fCursor = frDatabase.getAllFriends(user);

        String[] fromFieldNames = new String[]{frDatabase.helper.KEY_PENDNAME, frDatabase.helper.KEY_PENDEMAIL};
        String[] fieldName = new String[]{frDatabase.helper.KEY_NAME, frDatabase.helper.KEY_EMAIL};
        int[] twoViewIds = new int[]{R.id.frPend,R.id.frPendE};
        int[] viewIds = new int[]{R.id.frName,R.id.frEmail};

        fList = (ListView)getActivity().findViewById(R.id.friends_listview);

        //Adds accept and decline Buttons to List View; Allows Buttons to be clickable.
        pendingCursorAdapter = new SimpleCursorAdapter(getContext(),R.layout.friendslist_item,cursor,fromFieldNames,twoViewIds,0)
        {
            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                acceppt = (Button)v.findViewById(R.id.acc);
                decline = (Button)v.findViewById(R.id.dec);

                final View accButton = v.findViewById(R.id.acc);
                final View decButton = v.findViewById(R.id.dec);
                accButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ttt = fList.getItemAtPosition(position).toString();
                        //LinearLayout row = (LinearLayout)((LinearLayout)convertView).getChildAt(0);
                        Cursor cursor = (Cursor) parent.getChildAt(position);
                        String name = cursor.getString(cursor.getColumnIndex(frDatabase.helper.KEY_NAME));
                        MessageToast.message(getActivity(),name);


                    }
                });

                decButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteTask.execute(user);
                    }
                });
                return v;
            }
        };

        friendCursorAdapter = new SimpleCursorAdapter(getContext(),R.layout.friends_item,fCursor, fieldName,viewIds,0);

        //loadSpinner();
        fList.setAdapter(friendCursorAdapter);

        MessageToast.message(getContext(),"???");
    }


    public void loadSpinner(){
        String[] types = new String[]{"Friends", "Pending"};

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,types);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lists.setAdapter(typeAdapter);

        fr = lists.getSelectedItem().toString();
        switch (fr){
            case "Friends":
                fList.setAdapter(friendCursorAdapter);
                break;
            case "Pending":
                Intent i = new Intent(getActivity(),pending_list.class);
                startActivity(i);
                break;
        }

    }
}
