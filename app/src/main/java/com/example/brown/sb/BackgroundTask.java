package com.example.brown.sb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Brown on 11/2/2016.
 */
class BackgroundTask extends AsyncTask<String,Void,String> {

    AlertDialog alertDialog;
    ProgressDialog pdLoading;
    String login_url,add_info_url,add_location;
    Context context;

    public BackgroundTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login");

        pdLoading = new ProgressDialog(context);
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        login_url = "https://gradebook2go.000webhostapp.com/login_1.php";//"http://gradebook2go.000webhostapp.com/login.php";
        add_info_url = "http://gradebook2go.000webhostapp.com/insert_info.php";
        add_location = "http://gradebook2go.000webhostapp.com/insert_location.php";
    }

    @Override
    protected String doInBackground(String... args) {

        String name,email,password,method;
        method = args[0];
        if (method.equals("register")) {
            name = args[1];
            email = args[2];
            password = args[3];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                httpURLConnection.disconnect();
                return "Registration Success...";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return null;
        }
        else if(method.equals("login")){

            String login_email = args[1];
            String login_pass = args[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("login_email", "UTF-8") + "=" + URLEncoder.encode(login_email, "UTF-8") + "&" +
                        URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                //String response = "";
                StringBuilder response = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine())!=null)
                {
                    //response+= line;
                    response.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return(response.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    /*    else if(method.equals("location")){
            String lat = args[1];
            String lng = args[2];
            //String subject = args[3];
            String un = args[3];

            float latitude = Float.parseFloat(lat);
            float longitude = Float.parseFloat(lng);

            try {
                URL url = new URL(add_location);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data_string = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode(lng, "UTF-8")+ "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(un, "UTF-8");

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
        }*/
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        pdLoading.dismiss();

        try{
            JSONObject pull = new JSONObject(result);
            JSONObject user_data = pull.getJSONObject("user_data");
            String name = user_data.getString("name");

            if(name.equals("")){
                if(!((Activity)context).isFinishing()) {
                    alertDialog.setMessage("Incorrect Email or Password");
                    alertDialog.show();
                    alertDialog.dismiss();
                }
            }else {
                alertDialog.setMessage("Welcome " + name);
                alertDialog.show();
                alertDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

       // Toast.makeText(context,result,Toast.LENGTH_LONG).show();

    /*    if (result.equals("Registration Success...") || result.contains("Hello welcome")){
            MessageToast.message(context,result);
        }
            else
        {
            if(!((Activity)context).isFinishing()) {
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }*/
    }
}