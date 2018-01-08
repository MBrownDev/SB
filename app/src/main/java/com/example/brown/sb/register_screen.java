package com.example.brown.sb;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class register_screen extends AppCompatActivity {

    EditText UName,Email,Password;
    String uname,email,password;
    TextView Login,PW;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;

    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        UName = (EditText) findViewById(R.id.input_name);
        Email = (EditText) findViewById(R.id.input_email);
        Password = (EditText) findViewById(R.id.input_password);

        Login = (TextView) findViewById(R.id.link_login);
        PW = (TextView) findViewById(R.id.pw);
        PW.setTextColor(Color.RED);

        String ps = Password.getText().toString();
        Password.addTextChangedListener(new TextWatcher() {
            String pass;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pass = Password.getText().toString();
                if (bloomFilter(pass) == false) {
                    MessageToast.message(register_screen.this, "Password not strong enough");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass = Password.getText().toString();

                if (pass.length() < 8) {

                    PW.setTextColor(Color.RED);

                } else {
                    PW.setTextColor(Color.rgb(0,0,102));
                    if (bloomFilter(pass) == false) {
                        MessageToast.message(register_screen.this, "Password not strong enough");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void regStore(String username, String email){
        preferences = getSharedPreferences("regInfo", MODE_PRIVATE);
        edit = preferences.edit();
        edit.putString("username",username);
        edit.putString("email",email);
        edit.commit();
    }

    public boolean bloomFilter(String pw){
        int FILTER_SIZE = 10;
        int[] filter = new int[FILTER_SIZE];
        String[] samples = {"password","password1","cat", "abc123"};
        String character;
        int total = 0;
        int value = 0;

        for(int i = 0; i < samples.length; i++){
            for(int j = 0; j < samples[i].length(); j++) {

                int ascii = samples[i].charAt(j);
                total += ascii;

            }
            int sample = total%FILTER_SIZE;
            if(filter[sample] != 0){

            }else {
                filter[sample] = sample;
            }
        }

        for(int i=0; i < pw.length(); i++){
            int asc = pw.charAt(i);

            value += asc;
        }
        for(int i = 0; i < filter.length; i++){

            if(value == filter[i]){
                return false;
            }
        }
        return true;
    }

    public void createAccount(View view){

        uname = UName.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();
        final String method = "register";

        String subject = "Student Connect Account Registration.";
        int rand = (int)(Math.random()*9000)+1000;
        final String ran = Integer.toString(rand);

        final BackgroundTask backgroundTask = new BackgroundTask(this);
        final BackgroundFriendTask friendTask = new BackgroundFriendTask();
        SendMail sm = new SendMail(this, email, subject, ran);


            if (email.contains("@students.jsums.edu")) {
                //backgroundTask.execute(method, uname, email, password);
                sm.execute();

                alert = new AlertDialog.Builder(this);

                alert.setTitle("Verify Account");
                alert.setMessage("Enter account verification code");

                final EditText input = new EditText(register_screen.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alert.setView(input);

                alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String vc = input.getText().toString();

                        if(vc.equals(ran)){
                            backgroundTask.execute(method, uname, email, password);
                            friendTask.execute(email);
                            alert.setTitle("Verification Successful!");
                            alert.setMessage("Account Registration Complete.");

                            finish();
                        }else if(!vc.equals(ran)){
                            alert.setTitle("Incorrect Code!");
                            alert.setMessage("Please try again or resubmit email address.");
                        }
                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();
                //finish();
            } else {
                MessageToast.message(this, "Not a valid student email.");
            }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void loginLink(View view){
        startActivity(new Intent(this,login_screen.class));
    }

}