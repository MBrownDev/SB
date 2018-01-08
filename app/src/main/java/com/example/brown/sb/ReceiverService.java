package com.example.brown.sb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brown on 4/16/2017.
 */

public class ReceiverService extends Service {

//    String RECEIVE;
//    public String NAME,EMAIL;
    SQLiteDatabase db;
    friendsDatabase frDatabase;
    NotificationCompat.Builder SANotification;
    private static final int uniqueID = 114030;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;
    String email;
    Boolean exist;
    ReceivedTask receivedTask;
    ReceiverTask receiverTask;
    StatusTask statusTask;

    @Override
    public void onCreate() {
        //RECEIVE = "https://gradebook2go.000webhostapp.com/receiverequest.php";
        //Notify();
        MessageToast.message(this,"Run");
        openData();

        email = getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");
        receivedTask = new ReceivedTask(this);
        receiverTask = new ReceiverTask(this);
        statusTask = new StatusTask();
        final BackgroundDeleteTask deleteTask = new BackgroundDeleteTask();

//        Runnable nm = new Runnable() {
//            @Override
//            public void run() {
//                receivedTask.execute(email);
//            }
//        };
//
//        Runnable em = new Runnable() {
//            @Override
//            public void run() {
//                receiverTask.execute(email);
//            }
//        };
//
//        Thread one = new Thread(nm);
//        Thread two = new Thread(em);
//        one.start();
//        two.start();
        statusTask.execute(email);

        //String un = getSharedPreferences("logInfo",MODE_PRIVATE).getString("name","");
        String fr = getSharedPreferences("friendInfo",MODE_PRIVATE).getString("pendingFriend","");
        String ee = getSharedPreferences("friendInfo",MODE_PRIVATE).getString("pendingEmail","");
        exist = frDatabase.checkIfExists(ee);
        if(exist == false){
            frDatabase.insertPending(email,fr,ee);
        }

        //deleteTask.execute(email);
        MessageToast.message(this,fr);
    }

    public void openData(){
        frDatabase = new friendsDatabase(this);
        db = frDatabase.helper.getWritableDatabase();
        frDatabase.open();
    }

    public void insertName(String name){
        preferences = getSharedPreferences("friendInfo",MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString("pendingFriend",name);
        edit.commit();
    }
    public void insertEmail(String email){
        preferences = getSharedPreferences("friendInfo",MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString("pendingEmail",email);
        edit.commit();
    }

    public void Notify() {
        SANotification = new NotificationCompat.Builder(this);
        SANotification.setAutoCancel(true);
    }

    class StatusTask extends AsyncTask<String,Void,String>{

        String RETRIEVE;
        String STATUS;

        @Override
        protected void onPreExecute() {
            RETRIEVE = "https://gradebook2go.000webhostapp.com/receiverequest.php";
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL(RETRIEVE);
                String urlParams = "user_email=" + email;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                STATUS = user_data.getString("status");

                switch (STATUS){
                    case "invitation":
                        Runnable nm = new Runnable() {
                            @Override
                            public void run() {
                                receivedTask.execute(email);
                            }
                        };

                        Runnable em = new Runnable() {
                            @Override
                            public void run() {
                                receiverTask.execute(email);
                            }
                        };

                        Thread one = new Thread(nm);
                        Thread two = new Thread(em);
                        one.start();
                        two.start();
                        break;
                    case "accepted":
                        break;
                    case "declined":
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ReceivedTask extends AsyncTask<String,Void,String> {

        String RECEIVE;
        Context context;
        public String NAME, EMAIL;

        public ReceivedTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            RECEIVE = "https://gradebook2go.000webhostapp.com/receiverequest.php";
            Notify();
        }

        public void Notify() {
            SANotification = new NotificationCompat.Builder(context);
            SANotification.setAutoCancel(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL(RECEIVE);
                String urlParams = "user_email=" + email;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
//                JSONArray root = new JSONArray(result);
//                JSONObject user_data = root.getJSONObject(0);
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("friend");
                MessageToast.message(context, NAME);

                if(NAME != null || !NAME.equals("")) {
                    insertName(NAME);
                    sendNotification(NAME);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void sendNotification(String clss) {
            SANotification.setSmallIcon(R.drawable.icon3);
            SANotification.setTicker("New Friend Request");
            SANotification.setWhen(System.currentTimeMillis());
            SANotification.setContentTitle(clss + " wants to add you");

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            SANotification.setContentIntent(pIntent);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(uniqueID, SANotification.build());
        }
    }

    class ReceiverTask extends AsyncTask<String, Void, String >{

        Context context;
        String RECEIVER, EMAIL;

        public ReceiverTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            RECEIVER = "https://gradebook2go.000webhostapp.com/test.php";
            Notify();
        }

        public void Notify() {
            SANotification = new NotificationCompat.Builder(context);
            SANotification.setAutoCancel(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL(RECEIVER);
                String urlParams = "user_email=" + email;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                EMAIL = user_data.getString("frienduser");
                MessageToast.message(context, EMAIL);

                if(EMAIL != null || !EMAIL.equals("")) {
                    insertEmail(EMAIL);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification(String clss) {
        SANotification.setSmallIcon(R.drawable.icon3);
        SANotification.setTicker("New Friend Request");
        SANotification.setWhen(System.currentTimeMillis());
        SANotification.setContentTitle(clss + " wants to add you");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SANotification.setContentIntent(pIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, SANotification.build());
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
