package com.example.brown.sb;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brown on 1/8/2018.
 */

public class FriendService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FriendRetrievalTask friendRetrievalTask = new FriendRetrievalTask(this);
        friendRetrievalTask.execute();
    }

    class FriendRetrievalTask extends AsyncTask<Void,Void,Void> {

        String FR,NAME;

        private Context mContext;
        public String JSONString;
        public String un;
        friendsDatabase fb;
        SQLiteDatabase dbs;
        friends_list fL;
        SimpleCursorAdapter pendingCursorAdapter;
        public static final String TAG_JSON_ARRAY="user_data";
        public static final String KEY_USER_TAGS="requester";
        public static final String KEY_USER_TAG="requester_name";

        public FriendRetrievalTask(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(FR);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line=bufferedReader.readLine())!= null){
                    stringBuilder.append(line+"\n");
                }

                httpURLConnection.disconnect();
                String jsonString = stringBuilder.toString().trim();

                JSONObject jo = new JSONObject(jsonString);
                JSONArray ja = jo.getJSONArray(TAG_JSON_ARRAY);
                int count = 0;

                while(count<ja.length()){
                    JSONObject jsonObject = ja.getJSONObject(count);
                    count++;

                    //if(!fb.checkIfExists(un,jsonObject.getString(KEY_USER_TAGS))){
                        fb.insertPending(un, jsonObject.getString(KEY_USER_TAG), jsonObject.getString(KEY_USER_TAGS));

                        Log.d("DATABASE", fb.getAllPending("Michael Brown").toString());
                        Log.d("JSON Obj", jsonObject.getString(KEY_USER_TAGS));
                        Log.d("JSON", jsonObject.getString("requester_name"));
                    //}
                }

                Log.d("JSON_Strin",jsonString);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            un = getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");
            FR = "http://gradebook2go.000webhostapp.com/stack.php?email="+un;
            fb = new friendsDatabase(mContext);
            dbs = fb.helper.getReadableDatabase();
            fb.open();

            Log.d("Username",un);
        }

//    @Override
//    protected Void doInBackground(String... params) {
//        String email = params[0];
//
//        String data = "";
//            int tmp;
//            try {
//                URL url = new URL(FR);
//                String urlParams = "email="+email;
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while((line=bufferedReader.readLine())!= null){
//                    stringBuilder.append(line+"\n");
//                }
//
//                httpURLConnection.disconnect();
//                String jsonString = stringBuilder.toString().trim();
//
//                Log.d("JSON_Strin",jsonString);
//
//                return data;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                return "Exception: " + e.getMessage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "Exception: " + e.getMessage();
//            }
//    }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Cursor cursor = fb.getAllPending("Michael Brown");
            String[] fromFieldNames = new String[]{fb.helper.KEY_PENDNAME, fb.helper.KEY_PENDEMAIL};
            int[] twoViewIds = new int[]{R.id.textView2,R.id.textView};
            pendingCursorAdapter = new SimpleCursorAdapter(getApplication(),R.layout.list_item,cursor,fromFieldNames,twoViewIds,0);

            //fL.fList.setAdapter(pendingCursorAdapter);
        }

        public void JSONPull(){
            JSONObject object = null;
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
            try {
                object = new JSONObject(JSONString);
                JSONArray result = object.getJSONArray(TAG_JSON_ARRAY);

                for(int i = 0; i < result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    NAME = jo.getString(KEY_USER_TAGS);

                    HashMap<String,String> employees = new HashMap<>();
                    employees.put(KEY_USER_TAGS,NAME);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
