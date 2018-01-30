package com.example.brown.sb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brown on 12/22/2017.
 */

public class CardAdapter extends ArrayAdapter<Integer> {
    private String address = "http://gradebook2go.000webhostapp.com/tester.php";
    friendsDatabase fb;
    SQLiteDatabase dbs;
    private String user;
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;
    String[] data2;


    String Words[] = {"Campbell","Tire","Stats"};

    public CardAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void openData(){
        user = getContext().getSharedPreferences("logInfo",MODE_PRIVATE).getString("name","");
        fb = new friendsDatabase(getContext());
        dbs = fb.helper.getReadableDatabase();
        fb.open();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //ImageView imgView = (ImageView)convertView.findViewById(R.id.image_content);
        openData();
        getData();

        TextView nameView = (TextView)convertView.findViewById(R.id.name);
        TextView emailView = (TextView)convertView.findViewById(R.id.email);
        //imgView.setImageResource(getItem(position));
        nameView.setText(data[position]);
        emailView.setText(data2[position]);
        return convertView;
    }

    private void getData(){
        Runnable name = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = fb.getPendingName(user);
                data = new String[cursor.getCount()-1];

                if (cursor.moveToFirst()){
                    for(int i = 0; i<cursor.getCount(); i++){
                        data[i]= cursor.getString(i);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                dbs.close();
            }
        };

        Runnable email = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = fb. getPendingEmail(user);
                data2 = new String[cursor.getCount()-1];

                if (cursor.moveToFirst()){
                    for(int i = 0; i<cursor.getCount(); i++){
                        data2[i]= cursor.getString(i);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                dbs.close();
            }
        };

        Thread users = new Thread(name);
        Thread emails = new Thread(email);
        users.start();
        emails.start();

    }

//    private void getData(){
//        try{
//            URL url = new URL(address);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//
//            is = new BufferedInputStream(con.getInputStream());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        try{
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine())!= null){
//                sb.append(line+"\n");
//            }
//
//            is.close();
//            result=sb.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        try{
//
//            JSONArray ja = new JSONArray(result);
//            JSONObject jo = null;
//
//
//            data = new String[ja.length()];
//
//            if(ja != null) {
//                for (int i = 0; i < ja.length(); i++) {
//                    jo = ja.getJSONObject(i);
//                    data[i] = (String) jo.get("requested");
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
