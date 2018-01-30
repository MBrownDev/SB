package com.example.brown.sb;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Brown on 4/16/2017.
 */

public class BackgroundRequestTask extends AsyncTask<String, Void, String>{

    String REQUEST,PLACE,COLUMN;
    String[] COLUMNS;
    FriendRequest friendRequest = new FriendRequest();

    @Override
    protected void onPreExecute() {
        COLUMNS = new String[]{"frienduser","friend_user2","friend_user3",
                "friend_user4","friend_user5","friend_user6","friend_user7",
                "friend_user8","friend_user9","friend_user0"};
        REQUEST = "http://gradebook2go.000webhostapp.com/request2.php";
        PLACE = "http://gradebook2go.000webhostapp.com/friend_sort.php";
    }

    @Override
    protected String doInBackground(String... args) {
        String method = args[0];
        String fre = args[1];
        //String fru = args[2];
        String un = args[2];
        String name = args[3];

        friendRequest.setFrmail(fre);
        //friendRequest.setFrname(fru);
        friendRequest.setUsername(un);
        friendRequest.setColumn(name);

//        if(method.equals("request")){
//
//            String data = "";
//            int tmp;
//            try {
//                URL url = new URL(REQUEST);
//                String urlParams = "column="+column+"&email="+fre;
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                OutputStream os = httpURLConnection.getOutputStream();
//                os.write(urlParams.getBytes());
//                os.flush();
//                os.close();
//
//                InputStream is = httpURLConnection.getInputStream();
//                while((tmp=is.read())!=-1){
//                    data+= (char)tmp;
//                }
//
//                is.close();
//                httpURLConnection.disconnect();
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
//        }
//        else
            if(method.equals("place")){

            //MessageToast.message(context,"Location");
            try {
                URL url = new URL(PLACE);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = /*URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(fru, "UTF-8") + "&" + */
                        URLEncoder.encode("useremail", "UTF-8") + "=" + URLEncoder.encode(un, "UTF-8")+ "&" +
                        URLEncoder.encode("friendemail", "UTF-8") + "=" + URLEncoder.encode(fre, "UTF-8")+ "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                httpURLConnection.disconnect();
                return "Insertion Success...";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //JSONPull(s);
    }
//
//    public void JSONPull(String result){
//        try {
//            JSONObject root = new JSONObject(result);
//            JSONArray user_data = root.getJSONArray("user_data");
//            for(int i=0; i<user_data.length(); i++) {
//                JSONObject JObj = user_data.getJSONObject(i);
//                if(JObj.has("column")) {
//                    COLUMN = JObj.getString("column");
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if("yes".equals(COLUMN)){
//           BackgroundRequestTask requestTask = new BackgroundRequestTask();
//
//            requestTask.execute("place",friendRequest.getFrmail(),friendRequest.getFrname(),friendRequest.getUsername(),friendRequest.column);
//        }else {
//            int i = 0;
//            String col = COLUMNS[1+i];
//            BackgroundRequestTask requestTask = new BackgroundRequestTask();
//
//            requestTask.execute("request",friendRequest.getFrmail(),friendRequest.getFrname(),friendRequest.getUsername(),col);
//            i++;
//        }
//    }
}
