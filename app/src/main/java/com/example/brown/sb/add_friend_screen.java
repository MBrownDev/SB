package com.example.brown.sb;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class add_friend_screen extends AppCompatActivity {
    String myJSON;

    ListView lv;
    EditText search;
    JSONArray peoples = null;
    ArrayAdapter<String> adapter;
    SimpleAdapter adapt;
    String address = "http://gradebook2go.000webhostapp.com/retrieve.php";
    String address2 = "http://gradebook2go.000webhostapp.com/retrieveemail.php";
    InputStream is;
    Button add;
    String line = null;
    String result = null;
    String[] data;
    String[] name,email;

    friendsDatabase frDatabase;
    SQLiteDatabase dbs;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_screen);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        openData();

        lv = (ListView) findViewById(R.id.listView);
        //add = (Button) findViewById(R.id.add_button);
        //search =(EditText) findViewById(R.id.search);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        getData();
        getEmail();
        simpleArray();


//        lv.addView(getButtonView());

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        //lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //adapt.getView(position,getButtonView(view),parent);
            }
        });

/*
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                add_friend_screen.this.adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

//    public View getButtonView(View view){
//        //View view = View.inflate(this,R.layout.friend_item,null);
//
//        add = (Button)view.findViewById(R.id.add_button);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add.setBackground(getDrawable(R.drawable.add_button_pressed));
//                add.setTextColor(Color.WHITE);
//            }
//        });
//
//        return view;
//    }
    public void openData(){
        frDatabase = new friendsDatabase(this);
        dbs = frDatabase.helper.getReadableDatabase();
        frDatabase.open();
    }

    private void getData(){
        try
        {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;

            data = new String[js.length()];

            for(int i = 0; i < js.length(); i++){
                jo = js.getJSONObject(i);
                data[i] = jo.getString("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getEmail(){
        try
        {
            URL url = new URL(address2);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray js = new JSONArray(result);
            JSONObject jo = null;

            email = new String[js.length()];

            for(int i = 0; i < js.length(); i++){
                jo = js.getJSONObject(i);
                email[i] = jo.getString("email");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void simpleArray(){

        String[] from = new String[] {"rowid", "col_1"};
        int[] to = new int[] { R.id.user_name, R.id.user_email};

        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

        for(int i = 0; i < data.length; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rowid", "" + data[i]);
            map.put("col_1", "" + email[i]);
            fillMaps.add(map);
        }

        adapt = new SimpleAdapter(this, fillMaps, R.layout.friend_item, from, to)
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                add = (Button)v.findViewById(R.id.add_button);
                final View listButton = v.findViewById(R.id.add_button);
                listButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listButton.setBackground(getDrawable(R.drawable.checked_user_male24));
                        String ttt = lv.getItemAtPosition(position).toString();
                        String tt = ttt.split(",")[0];
                        String t = tt.split("=")[1];
                        String sss = ttt.split(",")[1];
                        String ss = sss.split("=")[1];
                        String s = ss.replace("}","");
                        MessageToast.message(add_friend_screen.this,t);
                        MessageToast.message(add_friend_screen.this,s);

                        String method = "place";
                        String status = "invite";
                        String un = getSharedPreferences("logInfo",MODE_PRIVATE).getString("name","");
                        String em = getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");
                        BackgroundRequestTask requestTask = new BackgroundRequestTask();
                        frDatabase.insertFriend(em,s,t);
                        requestTask.execute(method,t,em,un);

                    }
                });
                return v;
            }
        };
        lv.setAdapter(adapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapt.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
