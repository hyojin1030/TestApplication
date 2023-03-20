package com.test.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();

                TimeTableDao timeTableDao = db.timeTableDao();

                Date date = new Date();

                TimeSet timeSet = new TimeSet(formatter.format(date), true, 1);
                TimeIndex index = new TimeIndex(timeSet, timeSet);
                TimeTable timeTable = new TimeTable(111, index, index);

                timeTableDao.insertAll(timeTable);

                List<TimeTable> tables = timeTableDao.getAll();

                for (TimeTable table : tables) {
                    if (table.getAm().getStart().isOn()) {
                        setNotice(table.getAm().getStart().getTime(), table.id, table.getAm().getStart().getMin());
                        setNotice(table.getAm().getStart().getTime(), table.id + 1, table.getAm().getStart().getMin() + 1);
                    }
                }
            }
        }).start();




    }

    private void setNotice(String datetime, int alarmId, int delay) {
        //알람을 수신할 수 있도록 하는 리시버로 인텐트 요청
        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.putExtra("state", "on");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, receiverIntent, PendingIntent.FLAG_IMMUTABLE);


        Calendar calendar = Calendar.getInstance();
        //date타입으로 변경된 알람시간을 캘린더 타임에 등록
        try {
            Date date = formatter.parse(datetime);
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

}
