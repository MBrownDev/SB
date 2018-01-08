package com.example.brown.sb;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by theon_000 on 1/6/2016.
 */
public class MessageToast {
    public static void message(Context context, String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
