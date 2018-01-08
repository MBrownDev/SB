package com.example.brown.sb;

import android.content.Context;

/**
 * Created by Brown on 11/3/2017.
 */

public class FriendRequest {

    Context context;
    public String uname,mail,column,frname,frmail;

    public FriendRequest() {

    }

    public void setUsername(String un){
        this.uname = un;
    }

    public void setMail(String em){
        this.mail = em;
    }

    public void setColumn(String col){
        this.column = col;
    }

    public void setFrname(String frn){
        this.frname = frn;
    }

    public void setFrmail(String frm){
        this.frmail = frm;
    }

    public String getUsername(){
        return this.uname;
    }

    public String getMail(){
        return this.mail;
    }

    public String getColumn(){
        return this.column;
    }

    public String getFrname(){
        return this.frname;
    }

    public String getFrmail(){
        return this.frmail;
    }
}
