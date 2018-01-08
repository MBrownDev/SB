package com.example.brown.sb;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

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
 * Created by Brown on 3/2/2017.
 */

public class BackgroundLocationTask extends AsyncTask<String, Void, String> {
    AlertDialog alertDialog;
    String add_location;
    Context context;
    @Override
    protected void onPreExecute() {

//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Login");

        add_location = "http://gradebook2go.000webhostapp.com/insert_location.php";
    }

    @Override
    protected String doInBackground(String... args) {
        String method = args[0];
        if(method.equals("location")){
            String lat = args[1];
            String lng = args[2];
            String un = args[3];

            float latitude = Float.parseFloat(lat);
            float longitude = Float.parseFloat(lng);

            //MessageToast.message(context,"Location");
            try {
                URL url = new URL(add_location);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8")+ "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(un, "UTF-8");

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
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
