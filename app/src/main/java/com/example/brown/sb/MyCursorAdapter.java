package com.example.brown.sb;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Brown on 3/24/2017.
 */

public class MyCursorAdapter extends SimpleCursorAdapter {
    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if(v.getText().toString().contains("CSC")){
            v.setBackgroundColor(Color.MAGENTA);
        } else if(v.getText().toString().contains("MATH")){
            v.setBackgroundColor(Color.RED);
        }else {

        }
    }
}
