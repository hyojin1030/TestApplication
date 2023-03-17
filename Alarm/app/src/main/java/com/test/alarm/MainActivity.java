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

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AlarmManager alarmManager;

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

                setNotice("2023-03-17 18:38:00");

            }
        }).start();




    }

    private void setNotice(String datetime) {

        //알람을 수신할 수 있도록 하는 리시버로 인텐트 요청
        Intent receiverIntent = new Intent(this, NotificationReceiver.class);
        receiverIntent.putExtra("content", "알람등록 테스트");

        /**
         * PendingIntent란?
         * - Notification으로 작업을 수행할 때 인텐트가 실행되도록 합니다.
         * Notification은 안드로이드 시스템의 NotificationManager가 Intent를 실행합니다.
         * 즉 다른 프로세스에서 수행하기 때문에 Notification으로 Intent수행시 PendingIntent의 사용이 필수 입니다.
         */

        /**
         * 브로드캐스트로 실행될 pendingIntent선언 한다.
         * Intent가 새로 생성될때마다(알람을 등록할 때마다) intent값을 업데이트 시키기 위해, FLAG_UPDATE_CURRENT 플래그를 준다
         * 이전 알람을 취소시키지 않으려면 requestCode를 다르게 줘야 한다.
         * */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123, receiverIntent, PendingIntent.FLAG_IMMUTABLE);


        Calendar calendar = Calendar.getInstance();
        //date타입으로 변경된 알람시간을 캘린더 타임에 등록
        try {
            Date date = formatter.parse(datetime);
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //알람시간 설정
        //param 1)알람의 타입
        //param 2)알람이 울려야 하는 시간(밀리초)을 나타낸다.
        //param 3)알람이 울릴 때 수행할 작업을 나타냄
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

    }

}
