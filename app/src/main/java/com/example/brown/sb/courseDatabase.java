package com.example.brown.sb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brown on 12/14/2016.
 */

public class courseDatabase {
    courseHelper helper;
    SQLiteDatabase db,dbs;

    public courseDatabase(Context context){helper = new courseHelper(context);}

    public courseDatabase open(){
        db = helper.getWritableDatabase();
        dbs = helper.getReadableDatabase();
        return this;
    }
    public long insertData(String user, String subject, String name, String number, String building, String room, String timein, String timeout, String days, String dayin, String dayout) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(courseHelper.KEY_USER, user);
        contentValues.put(courseHelper.KEY_SUBJECT, subject);
        contentValues.put(courseHelper.KEY_NAME, name);
        contentValues.put(courseHelper.KEY_NUMBER, number);
        contentValues.put(courseHelper.KEY_BUILDING, building);
        contentValues.put(courseHelper.KEY_ROOM, room);
        contentValues.put(courseHelper.KEY_TIMEIN, timein);
        contentValues.put(courseHelper.KEY_TIMEOUT, timeout);
        contentValues.put(courseHelper.KEY_DAYS, days);
        contentValues.put(courseHelper.KEY_START, dayin);
        contentValues.put(courseHelper.KEY_END, dayout);
        long id = db.insert(courseHelper.TABLE_NAME, null, contentValues);
        return id;
    }
    public Cursor checkEmpty(){

        Cursor c = db.rawQuery("SELECT " + helper.KEY_ID + " FROM " + helper.TABLE_NAME,null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllRows(String un){
        String where = courseHelper.KEY_USER+"=?";
        String[] whereArgs = {un};
        String[] ALL_KEYS = new String[]{courseHelper.KEY_ID, courseHelper.KEY_NUMBER, courseHelper.KEY_NAME, courseHelper.KEY_TIMEIN, courseHelper.KEY_TIMEOUT, courseHelper.KEY_DAYS};
        Cursor c = 	db.query(true, courseHelper.TABLE_NAME, ALL_KEYS, where, whereArgs, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getMonday(String un){
        String where = courseHelper.KEY_USER+"=?" + " AND " + courseHelper.KEY_DAYS + " LIKE ?";
        String[] whereArgs = {un,"%Monday%"};

        String[] KEYS = new String[]{courseHelper.KEY_ID, courseHelper.KEY_NUMBER, courseHelper.KEY_NAME, courseHelper.KEY_TIMEIN, courseHelper.KEY_TIMEOUT, courseHelper.KEY_DAYS};
        Cursor c = db.query(true, courseHelper.TABLE_NAME, KEYS, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getTuesday(String un){
        String where = courseHelper.KEY_USER+"=?" + " AND " + courseHelper.KEY_DAYS + " LIKE ?";
        String[] whereArgs = {un,"%Tuesday%"};

        String[] KEYS = new String[]{courseHelper.KEY_ID, courseHelper.KEY_NUMBER, courseHelper.KEY_NAME, courseHelper.KEY_TIMEIN, courseHelper.KEY_TIMEOUT, courseHelper.KEY_DAYS};
        Cursor c = db.query(true, courseHelper.TABLE_NAME, KEYS, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getWednesday(String un){
        String where = courseHelper.KEY_USER+"=?" + " AND " + courseHelper.KEY_DAYS + " LIKE ?";
        String[] whereArgs = {un,"%Wednesday%"};

        String[] KEYS = new String[]{courseHelper.KEY_ID, courseHelper.KEY_NUMBER, courseHelper.KEY_NAME, courseHelper.KEY_TIMEIN, courseHelper.KEY_TIMEOUT, courseHelper.KEY_DAYS};
        Cursor c = db.query(true, courseHelper.TABLE_NAME, KEYS, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getThursday(String un){
        String where = courseHelper.KEY_USER+"=?" + " AND " + courseHelper.KEY_DAYS + " LIKE ?";
        String[] whereArgs = {un,"%Thursday%"};

        String[] KEYS = new String[]{courseHelper.KEY_ID, courseHelper.KEY_NUMBER, courseHelper.KEY_NAME, courseHelper.KEY_TIMEIN, courseHelper.KEY_TIMEOUT, courseHelper.KEY_DAYS};
        Cursor c = db.query(true, courseHelper.TABLE_NAME, KEYS, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getFriday(String un){
        String where = courseHelper.KEY_USER+"=?" + " AND " + courseHelper.KEY_DAYS + " LIKE ?";
        String[] whereArgs = {un,"%Friday%"};

        String[] KEYS = new String[]{courseHelper.KEY_ID, courseHelper.KEY_NUMBER, courseHelper.KEY_NAME, courseHelper.KEY_TIMEIN, courseHelper.KEY_TIMEOUT, courseHelper.KEY_DAYS};
        Cursor c = db.query(true, courseHelper.TABLE_NAME, KEYS, where, whereArgs, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public List<String> getAllSubjects(){
        List<String> subjects = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  "+helper.KEY_SUBJECT+" FROM " + helper.TABLE_NAME;

        Cursor cursor = dbs.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                subjects.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        dbs.close();

        // returning lables
        return subjects;
    }

    public List<String> getAllCourses(String un){
        open();
        List<String> courses = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  "+helper.KEY_NAME+" FROM " + helper.TABLE_NAME + " WHERE " + helper.KEY_USER + " = " + "'"+un+"'";

        Cursor cursor = dbs.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                courses.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        dbs.close();

        // returning lables
        return courses;
    }
    public List<String> getTimes(String un){
        open();
        List<String> times = new ArrayList<>();

        String selectTime = "SELECT "+helper.KEY_TIMEIN+" FROM " + helper.TABLE_NAME + " WHERE " + helper.KEY_USER + " = " + "'"+un+"'";

        Cursor cursor = dbs.rawQuery(selectTime,null);

        if(cursor.moveToFirst()){
            do{
                times.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return times;
    }

    public List<String> getDates(String un){
        open();
        List<String> dates = new ArrayList<>();

        String selectDate = "SELECT "+helper.KEY_DAYS+" FROM " + helper.TABLE_NAME + " WHERE " + helper.KEY_USER + " = " + "'"+un+"'";

        Cursor cursor = dbs.rawQuery(selectDate,null);

        if(cursor.moveToFirst()){
            do {
                dates.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return dates;
    }

    public void deleteClass(int pos){
        open();
        String where = courseHelper.KEY_ID + "=" + pos;
        db.delete(courseHelper.TABLE_NAME,where,null);
    }

    class courseHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "courseschedule";
        public static final String TABLE_NAME = "COURSE_TABLE";
        private static final int DATABASE_VERSION = 8;

        public static final String KEY_ID = "_id";
        public static final String KEY_USER = "_user";
        public static final String KEY_SUBJECT = "_subject";
        public static final String KEY_NAME = "_name";
        public static final String KEY_NUMBER = "_number";
        public static final String KEY_BUILDING = "_building";
        public static final String KEY_ROOM = "_room";
        public static final String KEY_TIMEIN = "_timein";
        public static final String KEY_TIMEOUT = "_timeout";
        public static final String KEY_DAYS = "_days";
        public static final String KEY_START = "_start";
        public static final String KEY_END = "_end";


        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_USER + " VARCHAR(255), "
                        + KEY_SUBJECT + " VARCHAR(255), "
                        + KEY_NAME + " VARCHAR(255), "
                        + KEY_NUMBER + " VARCHAR(255), "
                        + KEY_BUILDING + " VARCHAR(255), "
                        + KEY_ROOM + " VARCHAR(255), "
                        + KEY_TIMEIN + " VARCHAR(255), "
                        + KEY_TIMEOUT + " VARCHAR(255), "
                        + KEY_DAYS + " VARCHAR(255), "
                        + KEY_START + " VARCHAR(255), "
                        + KEY_END + " VARCHAR(255)" + ");";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;
        private Context context;

        public courseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
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
