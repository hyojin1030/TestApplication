package com.test.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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


        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 111);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();
        String time = formatter.format(date);

        setNotice(time, 1111, 10);


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
            calendar.add(Calendar.SECOND, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

}
