package com.example.brown.sb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Brown on 2/23/2017.
 */

public class assignmentDatabase {
    assignmentHelper helper;
    SQLiteDatabase db;

    public assignmentDatabase(Context context){helper = new assignmentHelper(context);}

    public assignmentDatabase open(){
        db = helper.getWritableDatabase();
        return this;
    }

    public Cursor getAllAssignments(String un){
        String where = assignmentHelper.KEY_USER +"=?";
        String[] whereArgs = {un};
        String[] ALL_KEYS = new String[]{assignmentHelper.KEY_ID, assignmentHelper.KEY_COURSE, assignmentHelper.KEY_SUBJECT, assignmentHelper.KEY_NAME,assignmentHelper.KEY_TYPE, assignmentHelper.KEY_TIMEIN};
        Cursor c = 	db.query(true, assignmentHelper.TABLE_NAME, ALL_KEYS, where, whereArgs, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getReminders(){
        String where = assignmentHelper.KEY_TYPE+"=?";
        String[] whereArgs = {"Reminder"};
        String[] KEYS = new String[]{assignmentHelper.KEY_ID, assignmentHelper.KEY_SUBJECT, assignmentHelper.KEY_NAME,assignmentHelper.KEY_TYPE, assignmentHelper.KEY_TIMEIN};

        Cursor c = db.query(true, assignmentHelper.TABLE_NAME, KEYS, where, whereArgs, null,null,null,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAssignment(){
        String where = assignmentHelper.KEY_TYPE+"=?";
        String[] whereArgs = {"Assignment"};
        String[] KEYS = new String[]{assignmentHelper.KEY_ID, assignmentHelper.KEY_SUBJECT, assignmentHelper.KEY_NAME,assignmentHelper.KEY_TYPE, assignmentHelper.KEY_TIMEIN};

        Cursor c = db.query(true, assignmentHelper.TABLE_NAME, KEYS, where, whereArgs, null,null,null,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getQuizzes(){
        String where = assignmentHelper.KEY_TYPE+"=?";
        String[] whereArgs = {"Quiz"};
        String[] KEYS = new String[]{assignmentHelper.KEY_ID, assignmentHelper.KEY_SUBJECT, assignmentHelper.KEY_NAME,assignmentHelper.KEY_TYPE, assignmentHelper.KEY_TIMEIN};

        Cursor c = db.query(true, assignmentHelper.TABLE_NAME, KEYS, where, whereArgs, null,null,null,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getExams(){
        String where = assignmentHelper.KEY_TYPE+"=?";
        String[] whereArgs = {"Exam"};
        String[] KEYS = new String[]{assignmentHelper.KEY_ID, assignmentHelper.KEY_SUBJECT, assignmentHelper.KEY_NAME,assignmentHelper.KEY_TYPE, assignmentHelper.KEY_TIMEIN};

        Cursor c = db.query(true, assignmentHelper.TABLE_NAME, KEYS, where, whereArgs, null,null,null,null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

    public long insertData(String user, String course, String subject, String type, String name, String date){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(assignmentHelper.KEY_USER, user);
        contentValues.put(assignmentHelper.KEY_COURSE,course);
        contentValues.put(assignmentDatabase.assignmentHelper.KEY_SUBJECT, subject);
        contentValues.put(assignmentDatabase.assignmentHelper.KEY_TYPE, type);
        contentValues.put(assignmentDatabase.assignmentHelper.KEY_NAME, name);
        contentValues.put(assignmentDatabase.assignmentHelper.KEY_TIMEIN, date);
        long id = db.insert(assignmentDatabase.assignmentHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    class assignmentHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "assignmentschedule";
        public static final String TABLE_NAME = "ASSIGNMENT_TABLE";
        private static final int DATABASE_VERSION = 3;

        public static final String KEY_ID = "_id";
        public static final String KEY_USER = "_user";
        public static final String KEY_COURSE = "_course";
        public static final String KEY_SUBJECT = "_subject";
        public static final String KEY_NAME = "_name";
        public static final String KEY_TYPE = "_type";
        public static final String KEY_TIMEIN = "_timedue";

        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KEY_USER + " VARCHAR(255), "
                        + KEY_COURSE + " VARCHAR(255), "
                        + KEY_SUBJECT + " VARCHAR(255), "
                        + KEY_NAME + " VARCHAR(255), "
                        + KEY_TYPE + " VARCHAR(255), "
                        + KEY_TIMEIN + " DATE" + ");";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                + TABLE_NAME;
        private Context context;

        public assignmentHelper(Context context) {
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
