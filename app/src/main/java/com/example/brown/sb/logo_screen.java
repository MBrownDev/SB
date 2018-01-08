package com.example.brown.sb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class logo_screen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public boolean preferences;
    public SharedPreferences.Editor edit;
    public int value;
    Context context = this;
    //public int open = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_screen);

        //final login_screen login = new login_screen();
        storeInfo();
        isNetworkAvailable();

        new Handler().postDelayed(new Runnable() {
            /*

             * Showing splash screen with a timer. This will be useful when you

             * want to show case your app logo / company

             */
            @Override

            public void run() {
                //value = preferences.getInt("accessCount",0);
                //preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
            if(isNetworkAvailable()) {
                // This method will be executed once the timer is over

                // Start your app main activity

                if (preferences) {
                    Intent intent = new Intent(logo_screen.this, register_screen.class);
                    startActivity(intent);
                    getSharedPreferences("userInfo", MODE_PRIVATE).edit().putBoolean("firstRun", false).commit();
                    //preferences.edit().putBoolean("accessCount",false);
                    finish();
                } else {
                    String un = getSharedPreferences("logInfo", MODE_PRIVATE).getString("username", "");
                    if (un.equals("")) {
                        Intent intent = new Intent(logo_screen.this, login_screen.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(logo_screen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }else{
                MessageToast.message(context,"No Connection");
            }
            }

        }, SPLASH_TIME_OUT);
    }

    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void storeInfo(){
        //preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences = getSharedPreferences("userInfo", MODE_PRIVATE).getBoolean("firstRun",true);
        /*edit = preferences.edit();
        edit.putBoolean("accessCount",true);
        edit.apply();*/
    }
}
