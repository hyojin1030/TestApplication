package com.test.gpssatellitetest;

import static com.test.gpssatellitetest.GpsManager.ACTION_UPDATE_NMEA;
import static com.test.gpssatellitetest.GpsManager.KEY_NMEA_FROM_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = GpsManager.class.getSimpleName();
    private GpsManager gpsManager;
    private TextView txtGps;
    private Location location;
    String lat, lon;
    GpsReceiver receiver;
    StringBuffer stringBuffer;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        gpsManager = new GpsManager(this);
        stringBuffer = new StringBuffer();
        txtGps = (TextView) findViewById(R.id.txt_gps);

        receiver = new GpsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE_NMEA);
        registerReceiver(receiver, filter);




    }

    public class GpsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_UPDATE_NMEA)){

                //Log.e(TAG, intent.getStringExtra(KEY_NMEA_FROM_SERVICE));

                String[] splitNmeaMessage;
                splitNmeaMessage = intent.getStringExtra(KEY_NMEA_FROM_SERVICE).split(",");

                if (!splitNmeaMessage[6].equals("0")) {
                    int time = Integer.parseInt(splitNmeaMessage[1].substring(0,4)) + 900;
                    lat = String.format("%.7f", 37.0 + gpsParser(splitNmeaMessage[2].substring(2)));
                    lon = String.format("%.7f", 127.0 + gpsParser(splitNmeaMessage[4].substring(3)));
                    String sat = splitNmeaMessage[7];

                    stringBuffer.append("(" + splitNmeaMessage[6] + ") " + time + " > " + "lat : " + lat + ", lon : " + lon + ", sat : " + sat + "\n");

                    txtGps.setText(stringBuffer.toString());
                } else {
                    stringBuffer.append("(" + splitNmeaMessage[6] + ") " + "\n");
                }




            }
        }
    }

    private double gpsParser(String str) {
        return Double.parseDouble(str) / 60;
    }

    public void onClickClean(View view) {
        location = gpsManager.getLocation();
        if (location != null) {
            txtGps.setText("");
            stringBuffer.delete(0, stringBuffer.length());
            Toast.makeText(this, formatter.format(location.getTime()), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCopy(View view) {
        String copyText = lat + ", " + lon;
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copyText", copyText);
        clipboardManager.setPrimaryClip(clipData);
    }
}