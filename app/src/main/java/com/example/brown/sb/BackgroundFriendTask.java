package com.example.brown.sb;

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
 * Created by Brown on 4/18/2017.
 */

public class BackgroundFriendTask extends AsyncTask<String,Void,String> {
    String SUBMIT;

    @Override
    protected String doInBackground(String... params) {

        String username = params[0];
        SUBMIT = "https://gradebook2go.000webhostapp.com/sumbitrequest.php";

        try {
            URL url = new URL(SUBMIT);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data_string = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

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

        return null;
    }
}
