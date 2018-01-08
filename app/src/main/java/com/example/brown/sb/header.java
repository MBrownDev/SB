package com.example.brown.sb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brown on 4/12/2017.
 */

public class header extends Fragment {

    TextView name,email;
    String nm,em;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.header,container,false);
        name = (TextView)v.findViewById(R.id.username);
        email = (TextView)v.findViewById(R.id.email);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nm = getActivity().getSharedPreferences("logInfo",MODE_PRIVATE).getString("name","");
        em = getActivity().getSharedPreferences("logInfo",MODE_PRIVATE).getString("email","");

        name.setText(nm);
        email.setText(em);
    }
}
