package com.example.brown.sb;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brown on 11/19/2016.
 */

public class add_screen extends Fragment implements View.OnClickListener {

    courseDatabase cdHelper;
    SQLiteDatabase db;
    EditText name, subject, code, building, room;
    EditText time_in, time_out, date_in, date_out;
    AutoCompleteTextView days;
    TextView daywk;
    String nm, sub, c, build, r;
    String ti,to,di,dt, day;
    String dayofthewk;
    String timestart,timeend;
    SimpleDateFormat sdf;
    String sd;
    String ed;
    Date startDate = null;
    Date endDate = null;
    Button button;
    Button m,t,w,th,f;


    public int mcount;
    public int tcount;
    public int wcount;
    public int thcount;
    public int fcount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_class,container,false);

        m = (Button)v.findViewById(R.id.monday);
        t = (Button)v.findViewById(R.id.tuesday);
        w = (Button)v.findViewById(R.id.wednesday);
        th = (Button)v.findViewById(R.id.thursday);
        f = (Button)v.findViewById(R.id.friday);

        m.setOnClickListener(this);
        t.setOnClickListener(this);
        w.setOnClickListener(this);
        th.setOnClickListener(this);
        f.setOnClickListener(this);

        return v;
    }

    public void openData(){
        cdHelper = new courseDatabase(getActivity());
        db = cdHelper.helper.getWritableDatabase();
        cdHelper.open();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        openData();

        mcount=0;
        tcount=0;
        wcount=0;
        thcount=0;
        fcount=0;

        name = (EditText)getActivity().findViewById(R.id.course_name);
        subject = (EditText)getActivity().findViewById(R.id.subject);
        code = (EditText)getActivity().findViewById(R.id.course_code);
        building = (EditText)getActivity().findViewById(R.id.building);
        room = (EditText)getActivity().findViewById(R.id.room);

        time_in = (EditText)getActivity().findViewById(R.id.start_time);
        time_out = (EditText)getActivity().findViewById(R.id.end_time);
        date_in = (EditText)getActivity().findViewById(R.id.course_starts);
        date_out = (EditText)getActivity().findViewById(R.id.course_end);

        dayofthewk = "";

        //days = (AutoCompleteTextView)getActivity().findViewById(R.id.course_days);
        //daywk = (TextView)getActivity().findViewById(R.id.dotw);

        //loadTextView();

        button = (Button)getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String un = getActivity().getSharedPreferences("logInfo", MODE_PRIVATE).getString("username","");

                sdf = new SimpleDateFormat("HH:mm");

                nm = name.getText().toString();
                sub = subject.getText().toString();
                c = code.getText().toString();
                build = building.getText().toString();
                r = room.getText().toString();

                ti = time_in.getText().toString();
                to = time_out.getText().toString();

                day = dayofthewk;

                di = date_in.getText().toString();
                dt = date_out.getText().toString();

                //timestart = sdf.format(ti);
                //timeend = sdf.format(to);
                String method = "class info";

                //MessageToast.message(getContext(),timestart + " " + timeend);
                cdHelper.insertData(un,sub,nm,c,build,r,ti,to,day,di,dt);

                home_screen cl = new home_screen();
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.replace(R.id.content,cl);
                tran.addToBackStack(null);
                tran.commit();
            }
        });

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.monday:
                String m = "Monday";
                mcount++;
                DOTW(m,mcount);
                break;
            case R.id.tuesday:
                String t = "Tuesday";
                tcount++;
                DOTW(t,tcount);
                break;
            case R.id.wednesday:
                String w = "Wednesday";
                wcount++;
                DOTW(w,wcount);
                break;
            case R.id.thursday:
                String th = "Thursday";
                thcount++;
                DOTW(th,thcount);
                break;
            case R.id.friday:
                String f = "Friday";
                fcount++;
                DOTW(f,fcount);
                break;
        }
    }

    public void DOTW(String day, int count){
        if(count%2 == 1){
            switch (day){
                case "Monday":
                    m.setBackground(getActivity().getDrawable(R.drawable.m_fill));
                    m.setTextColor(Color.WHITE);
                    if(dayofthewk.equals("")){
                        dayofthewk += (day);
                    }else {
                        dayofthewk += ("/"+day);
                    }
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Tuesday":
                    t.setBackground(getActivity().getDrawable(R.drawable.t_fill));
                    t.setTextColor(Color.WHITE);
                    if(dayofthewk.equals("")){
                        dayofthewk += (day);
                    }else {
                        dayofthewk += ("/"+day);
                    }
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Wednesday":
                    w.setBackground(getActivity().getDrawable(R.drawable.w_fill));
                    w.setTextColor(Color.WHITE);
                    if(dayofthewk.equals("")){
                        dayofthewk += (day);
                    }else {
                        dayofthewk += ("/"+day);
                    }
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Thursday":
                    th.setBackground(getActivity().getDrawable(R.drawable.th_fill));
                    th.setTextColor(Color.WHITE);
                    if(dayofthewk.equals("")){
                        dayofthewk += (day);
                    }else {
                        dayofthewk += ("/"+day);
                    }
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Friday":
                    f.setBackground(getActivity().getDrawable(R.drawable.f_fill));
                    f.setTextColor(Color.WHITE);
                    if(dayofthewk.equals("")){
                        dayofthewk += (day);
                    }else {
                        dayofthewk += ("/"+day);
                    }
                    MessageToast.message(getContext(),dayofthewk);
                    break;
            }
        }else if(count%2 == 0) {
            switch (day) {
                case "Monday":
                    m.setBackground(getActivity().getDrawable(R.drawable.m_button));
                    m.setTextColor(getResources().getColor(R.color.button1));
                    dayofthewk = dayofthewk.replace("Monday", "");
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Tuesday":
                    t.setBackground(getActivity().getDrawable(R.drawable.t_button));
                    t.setTextColor(getResources().getColor(R.color.button2));
                    dayofthewk = dayofthewk.replace("Tuesday", "");
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Wednesday":
                    w.setBackground(getActivity().getDrawable(R.drawable.w_button));
                    w.setTextColor(getResources().getColor(R.color.button3));
                    dayofthewk = dayofthewk.replace("Wednesday", "");
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Thursday":
                    th.setBackground(getActivity().getDrawable(R.drawable.th_button));
                    th.setTextColor(getResources().getColor(R.color.button4));
                    dayofthewk = dayofthewk.replace("Thursday", "");
                    MessageToast.message(getContext(),dayofthewk);
                    break;
                case "Friday":
                    f.setBackground(getActivity().getDrawable(R.drawable.f_button));
                    f.setTextColor(getResources().getColor(R.color.toolbarColor));
                    dayofthewk = dayofthewk.replace("Friday", "");
                    MessageToast.message(getContext(),dayofthewk);
                    break;
            }
        }
    }
    public void loadTextView(){
        final String[] dotw = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,dotw);

        days.setThreshold(0);
        days.setAdapter(dayAdapter);

        days.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                int pos = Arrays.asList(dotw).indexOf(selected);
                String tmp = dotw[pos];

                if(daywk.getText().toString().equals("")){
                    daywk.setText(tmp);
                    days.setText("");
                }else {
                    daywk.append("/"+tmp);
                    days.setText("");
                }
            }
        });
    }
}
