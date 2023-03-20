package com.test.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                /*AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();

                TimeTableDao timeTableDao = db.timeTableDao();

                TimeSet timeSet1 = new TimeSet();
                timeSet1.setTime("2023-03-17 18:30:00");
                timeSet1.setOn(true);
                timeSet1.setMin(10);

                TimeSet timeSet2 = new TimeSet();
                timeSet2.setTime("2023-03-17 18:30:00");
                timeSet2.setOn(false);
                timeSet2.setMin(10);

                TimeIndex timeIndex = new TimeIndex();
                timeIndex.setStart(timeSet1);
                timeIndex.setEnd(timeSet2);

                TimeTable timeTable = new TimeTable();
                timeTable.setId("testid");
                timeTable.setAm(timeIndex);
                timeTable.setPm(timeIndex);

                timeTableDao.insertAll(timeTable);

                List<TimeTable> tables = timeTableDao.getAll();

                for (TimeTable table : tables) {
                    Log.d("TEST", "id : " + table.am.start.time);
                    Log.d("TEST", "id : " + table.am.start.isOn);
                    Log.d("TEST", "id : " + table.am.end.min);
                }*/

                setNotice("2023-03-20 16:13:00", 1);

                setNotice("2023-03-20 16:14:00", 2);

            }
        }).start();




    }

    private void setNotice(String datetime, int alarmId) {
        //알람을 수신할 수 있도록 하는 리시버로 인텐트 요청
        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.putExtra("state", "on");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, receiverIntent, PendingIntent.FLAG_IMMUTABLE);


        Calendar calendar = Calendar.getInstance();
        //date타입으로 변경된 알람시간을 캘린더 타임에 등록
        try {
            Date date = formatter.parse(datetime);
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

}
