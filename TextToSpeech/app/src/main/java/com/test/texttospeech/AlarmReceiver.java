package com.test.texttospeech;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    private Context mContext;
    private AlarmManager alarmManager;
    private Vibrator vibrator;
    private int vibType, milliSec, soundType;

    private TextToSpeech textToSpeech;
    private String ttsString;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int state) {
                if (state == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.KOREAN);
                    textToSpeech.setPitch(1.0F);
                    textToSpeech.setSpeechRate(1.0F);

                    if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d(TAG, "한국어 미지원");
                    } else {
                        Log.d(TAG, "한국어 지원 ");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int speechState = textToSpeech.speak(ttsString, TextToSpeech.QUEUE_FLUSH, null, "TESTID");
                                if (speechState == TextToSpeech.ERROR) {
                                    Log.d(TAG, "TTS 에러");
                                }
                            }
                        }, 500);

                    }
                }
            }
        });

        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        vibType = intent.getIntExtra("vibType", 0);
        milliSec = intent.getIntExtra("milliSec", 0);
        ttsString = intent.getStringExtra("description");

        Log.d(TAG, "TTS : " + ttsString);

        initAlarm(vibType, milliSec);



        /*PendingIntent pendingIntent = PendingIntent.getService(context, 999, intent, PendingIntent.FLAG_IMMUTABLE);
        Toast.makeText(context, "alarm test", Toast.LENGTH_LONG).show();
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }*/
    }

    private void initAlarm(int vibType, int milliSec) {

        if (vibType != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                initVibrate(vibType, milliSec);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initVibrate(int type, int milliSec) {
        Log.d(TAG, "check_point");
        if (type == 1) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliSec, 100));
        } else {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{100, 100}, new int[]{100, 0}, 0));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    vibrator.cancel();
                }
            }, milliSec * 2);

        }

    }
}
