package com.example.brown.sb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Brown on 3/28/2017.
 */

public class NotificationService extends Service {

    courseDatabase cdHelper;
    SQLiteDatabase db;
    Calendar calendar;
    Date currentTime;

    NotificationCompat.Builder SANotification;
    private static final int uniqueID = 114030;

    @Override
    public void onCreate() {
        cdHelper = new courseDatabase(this);
        db = cdHelper.helper.getWritableDatabase();
        cdHelper.open();

        Notify();
        final String user = getSharedPreferences("logInfo",MODE_PRIVATE).getString("username","");
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        List<String> times = cdHelper.getTimes(user);
        List<String> days = cdHelper.getDates(user);
        List<String> course = cdHelper.getAllCourses(user);
        currentTime = Calendar.getInstance().getTime();

        for(int i = 0; i < days.size(); i++){

            switch (day){
                case Calendar.MONDAY:
                    if(days.get(i).contains("Monday")){
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            Date time1 = format.parse(times.get(i));
                            Date time2 = currentTime;

                            long mills = time2.getTime() - time1.getTime();

                            int Hours = (int) (mills / (1000 * 60 * 60));
                            int Mins = (int) (mills / (1000 * 60)) % 60;

                            String diff = Hours +":"+Mins;

                            if(diff.equals("0:15")){
                                sendNotification(course.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Calendar.TUESDAY:
                    if(days.get(i).contains("Tuesday")){
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            Date time1 = format.parse(times.get(i));
                            Date time2 = currentTime;

                            long mills = time2.getTime() - time1.getTime();

                            int Hours = (int) (mills / (1000 * 60 * 60));
                            int Mins = (int) (mills / (1000 * 60)) % 60;

                            String diff = Hours +":"+Mins;

                            if(diff.equals("0:15")){
                                sendNotification(course.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if (days.get(i).contains("Wednesday")){
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            Date time1 = format.parse(times.get(i));
                            Date time2 = currentTime;

                            long mills = time2.getTime() - time1.getTime();

                            int Hours = (int) (mills / (1000 * 60 * 60));
                            int Mins = (int) (mills / (1000 * 60)) % 60;

                            String diff = Hours +":"+Mins;

                            if(diff.equals("0:15")){
                                sendNotification(course.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Calendar.THURSDAY:
                    if (days.get(i).contains("Thursday")){
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            Date time1 = format.parse(times.get(i));
                            Date time2 = currentTime;

                            long mills = time2.getTime() - time1.getTime();

                            int Hours = (int) (mills / (1000 * 60 * 60));
                            int Mins = (int) (mills / (1000 * 60)) % 60;

                            String diff = Hours +":"+Mins;

                            if(diff.equals("0:15")){
                                sendNotification(course.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Calendar.FRIDAY:
                    if (days.get(i).contains("Friday")){
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            Date time1 = format.parse(times.get(i));
                            Date time2 = currentTime;

                            long mills = time2.getTime() - time1.getTime();

                            int Hours = (int) (mills / (1000 * 60 * 60));
                            int Mins = (int) (mills / (1000 * 60)) % 60;

                            String diff = Hours +":"+Mins;

                            if(diff.equals("0:15")){
                                sendNotification(course.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public void Notify() {
        SANotification = new NotificationCompat.Builder(this);
        SANotification.setAutoCancel(true);
    }

    public void sendNotification(String clss) {
        SANotification.setSmallIcon(R.drawable.icon3);
        SANotification.setTicker("Don't Be Late!");
        SANotification.setWhen(System.currentTimeMillis());
        SANotification.setContentTitle("15 minutes till " + clss);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SANotification.setContentIntent(pIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, SANotification.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
