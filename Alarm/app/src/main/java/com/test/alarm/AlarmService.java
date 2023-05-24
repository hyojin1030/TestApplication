package com.test.alarm;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Locale;


public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private TextToSpeech textToSpeech;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mRootView;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private View alertView = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TEST_LOG", "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TEST_LOG", "onStartCommand");
        // Foreground 에서 실행되면 Notification 을 보여줘야 됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Oreo(26) 버전 이후 버전부터는 channel 이 필요함
            String channelId =  createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder
                    //.setOngoing(true)
                    .setContentTitle("알람테스트")
                    .setContentText("111111")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    //.setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            startForeground(1, notification);

            startAlarm(intent.getStringExtra("state"));
            showDialog();

        }

        return START_NOT_STICKY;
    }

    private void showDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("테스트")
                .setMessage("알람 종료하기")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        stopAlarm();
                    }
                })
                .create();

        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
    }

    private void startAlarm(String state) {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (state.equals("on")) {
            // 알람음 재생 OFF, 알람음 시작 상태
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_101);
            this.mediaPlayer.start();

            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{500, 500}, new int[]{50, 0}, 0));

            Log.d("AlarmService", "Alarm Start");

            /*Intent intent = new Intent(AlarmService.this, TestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    vibrator.cancel();
                }
            }, 1000);


            textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int state) {
                    if (state == TextToSpeech.SUCCESS) {
                        int lang = textToSpeech.setLanguage(Locale.KOREAN);
                        textToSpeech.setPitch(1.0F);
                        textToSpeech.setSpeechRate(1.0F);

                        if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.d("AlarmService", "한국어 미지원");
                        } else {
                            Log.d("AlarmService", "한국어 지원 ");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int speechState = textToSpeech.speak("TTS test", TextToSpeech.QUEUE_FLUSH, null, "TESTID");
                                    if (speechState == TextToSpeech.ERROR) {
                                        Log.d("AlarmService", "TTS 에러");
                                    }
                                }
                            }, mediaPlayer.getDuration());

                        }
                    }
                }
            });
        }
    }

    private void stopAlarm() {
        this.mediaPlayer.stop();
        this.mediaPlayer.reset();

        Log.d("AlarmService", "Alarm Stop");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        this.vibrator.cancel();

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        //channel.setDescription(channelName);
        channel.setSound(null, null);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }


}