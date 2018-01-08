package com.example.brown.sb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class add_assignment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    EditText name,date;
    Spinner type, subject, courses;
    courseDatabase cdHelper;
    assignmentDatabase asHelper;
    SQLiteDatabase db, dbs;
    String nm,dt,tp,sj,co;

    Button addassignment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_assignment,container,false);
        openData();
        //loadSpinner();

        name = (EditText)v.findViewById(R.id.assignName);
        date = (EditText)v.findViewById(R.id.dueDate);
        type = (Spinner)v.findViewById(R.id.assignType);
        subject = (Spinner)v.findViewById(R.id.assignSubject);
        courses = (Spinner)v.findViewById(R.id.assignCourse);

        addassignment = (Button)v.findViewById(R.id.submit_assignment);
        addassignment.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openData();
        loadSpinner();
    }

//    public void submitAssignment(View view){
//
//        nm = name.getText().toString();
//        dt = date.getText().toString();
//        tp = type.getSelectedItem().toString();
//        sj = subject.getSelectedItem().toString();
//
//        asHelper.insertData(sj,tp,nm,dt);
//    }

    private void openData() {

        asHelper = new assignmentDatabase(getActivity());
        cdHelper = new courseDatabase(getActivity());
        db = cdHelper.helper.getWritableDatabase();
        dbs = cdHelper.helper.getReadableDatabase();
        cdHelper.open();
        asHelper.open();
    }

    public void loadSpinner(){
        final String user = getActivity().getSharedPreferences("logInfo",Context.MODE_PRIVATE).getString("username","");
        List<String> subs = cdHelper.getAllSubjects();
        List<String> course = cdHelper.getAllCourses(user);
        String[] types = new String[]{"Assignment", "Quiz", "Exam", "Reminder"};

        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,subs);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,types);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,course);

        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subject.setAdapter(subAdapter);
        type.setAdapter(typeAdapter);
        courses.setAdapter(courseAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        final String un = getActivity().getSharedPreferences("logInfo", Context.MODE_PRIVATE).getString("username","");

        nm = name.getText().toString();
        dt = date.getText().toString();
        tp = type.getSelectedItem().toString();
        sj = subject.getSelectedItem().toString();
        co = courses.getSelectedItem().toString();

        asHelper.insertData(un,co,sj,tp,nm,dt);
    }
}
