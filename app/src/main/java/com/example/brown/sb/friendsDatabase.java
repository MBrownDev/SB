package com.example.brown.sb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Brown on 3/21/2017.
 */

public class friendsDatabase {
    friendsHelper helper;
    SQLiteDatabase db,dbs;

    public friendsDatabase(Context context){helper = new friendsHelper(context);}

    public friendsDatabase open(){
        db = helper.getWritableDatabase();
        dbs = helper.getReadableDatabase();
        return this;
    }
    public Cursor getAllFriends(String un){
        String where = helper.KEY_USER+"=?";
        String[] whereArgs = {un};
        String[] ALL_KEYS = new String[]{friendsHelper.KEY_ID, friendsHelper.KEY_NAME, friendsHelper.KEY_EMAIL};
        Cursor c = 	db.query(true, friendsDatabase.friendsHelper.TABLE_NAME, ALL_KEYS, where, whereArgs, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getPendingName(String un){
        String where = helper.KEY_USER+"=?";
        String[] whereArgs = {un};
        String[] KEY = new String[]{friendsHelper.KEY_PENDNAME};
        Cursor c = db.query(true, friendsHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getPendingEmail(String un){
        String where = helper.KEY_USER+"=?";
        String[] whereArgs = {un};
        String[] KEY = new String[]{friendsHelper.KEY_PENDEMAIL};
        Cursor c = db.query(true, friendsHelper.TABLE_NAME, KEY, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllPending(String un){
        String where = helper.KEY_USER+"=?";
        String[] whereArgs = {un};
        String[] ALL_KEYS = new String[]{friendsHelper.KEY_ID, friendsHelper.KEY_PENDNAME, friendsHelper.KEY_PENDEMAIL};
        Cursor c = 	db.query(true, friendsDatabase.friendsHelper.TABLE_NAME, ALL_KEYS, where, whereArgs, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean checkIfExists(String email){
        open();

        String where = helper.KEY_PENDEMAIL+"=?";
        String[] whereArgs = {email};
        String[] KEYS = new String[]{friendsHelper.KEY_PENDEMAIL};
        Cursor c = dbs.query(true, friendsHelper.TABLE_NAME,KEYS,where,whereArgs,null,null,null,null);
        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public boolean checkIfEmpty(String un){
        open();

        String where = helper.KEY_USER+"=?";
        String[] whereArgs = {un};
        String[] KEYS = new String[]{friendsHelper.KEY_PENDEMAIL};
        Cursor c = dbs.query(true, friendsHelper.TABLE_NAME,KEYS,where,whereArgs,null,null,null,null);
        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }

    public void deletePending(String un, String pe){

        String deleteQuery = "DELETE FROM " + friendsHelper.TABLE_NAME + " WHERE " + friendsHelper.KEY_USER +" = "
                + un + " AND " +friendsHelper.KEY_PENDEMAIL + " = " + pe;

        Cursor c = db.rawQuery(deleteQuery,null);

        if (c != null) {
            c.moveToFirst();
        }

        c.close();
        db.close();
    }

    public long insertPending(String user, String name, String email){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(friendsHelper.KEY_USER,user);
        contentValues.put(friendsHelper.KEY_PENDNAME, name);
        contentValues.put(friendsHelper.KEY_PENDEMAIL, email);
        long id = db.insert(friendsDatabase.friendsHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertFriend(String name, String email){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(friendsHelper.KEY_NAME,name);
        contentValues.put(friendsHelper.KEY_EMAIL, email);
        long id = db.insert(friendsHelper.TABLE_NAME,null, contentValues);
        return id;
    }

    class friendsHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "friendslist";
        public static final String TABLE_NAME = "FRIENDS_TABLE";
        private static final int DATABASE_VERSION = 3;

        public static final String KEY_ID = "_id";
        public static final String KEY_USER = "_user";
        public static final String KEY_NAME = "_name";
        public static final String KEY_EMAIL = "_email";
        public static final String KEY_PENDNAME = "_pendname";
        public static final String KEY_PENDEMAIL = "_pendemail";

        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_USER + " VARCHAR(255), "
                        + KEY_NAME + " VARCHAR(255), "
                        + KEY_EMAIL + " VARCHAR(255), "
                        + KEY_PENDNAME + " VARCHAR(255), "
                        + KEY_PENDEMAIL + " VARCHAR(255)" + ");";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;
        private Context context;

        public friendsHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE);
                MessageToast.message(context, "onCreate");
            }catch(SQLException e){
                // MessageToast.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //MessageToast.message(context, "onUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch(SQLException e){
                // MessageToast.message(context,""+e);
            }
        }
    }
}
