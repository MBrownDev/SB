package com.example.brown.sb;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Brown on 1/30/2018.
 */

public class pending_friends extends Fragment {
    ListView fL;
    Cursor cursor;
    TextView pReq;
    Button add,decline;
    friendsDatabase fb;
    SQLiteDatabase dbs;
    SimpleCursorAdapter pendingCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.friends_list,container,false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fL = (ListView)getActivity().findViewById(R.id.friends_listview);

        fb = new friendsDatabase(getActivity());
        dbs = fb.helper.getReadableDatabase();
        fb.open();

        Intent fr = new Intent(getActivity(),FriendService.class);
        getActivity().startService(fr);

        pReq = (TextView)getActivity().findViewById(R.id.pRequests);
        pReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),pending_list.class);
                startActivity(i);
            }
        });

        final String user = getActivity().getSharedPreferences("logInfo",getActivity().MODE_PRIVATE).getString("username","");
        Log.d("TAG",user);
        cursor = fb.getAllPending(user);
        String[] fromFieldNames = new String[]{fb.helper.KEY_PENDNAME, fb.helper.KEY_PENDEMAIL};
        final int[] twoViewIds = new int[]{R.id.frName,R.id.frEmail};
        pendingCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.friends_item,cursor,fromFieldNames,twoViewIds,0){
            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                View v = super.getView(position,convertView,parent);

                final View add = v.findViewById(R.id.Accept);
                final View decline = v.findViewById(R.id.Decline);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String frname = cursor.getString(cursor.getColumnIndex(fb.helper.KEY_PENDNAME));
                        String em = cursor.getString(cursor.getColumnIndex(fb.helper.KEY_PENDEMAIL));

                        fb.insertFriend(user,frname,em);
                        fb.deletePending(user,em);
                        pendingCursorAdapter.notifyDataSetChanged();
                        Log.d("NAME",frname);
                        Log.d("EMAIL",em);
                    }
                });

                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String frname = cursor.getString(cursor.getColumnIndex(fb.helper.KEY_PENDNAME));
                        String em = cursor.getString(cursor.getColumnIndex(fb.helper.KEY_PENDEMAIL));

                        fb.deletePending(user,em);
                        pendingCursorAdapter.notifyDataSetChanged();
                        Log.d("NAME",frname);
                        Log.d("EMAIL",em);
                    }
                });
                return v;
            }
        };
        fL.setAdapter(pendingCursorAdapter);
    }
}
