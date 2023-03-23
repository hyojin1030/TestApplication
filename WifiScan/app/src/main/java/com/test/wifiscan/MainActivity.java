package com.test.wifiscan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    WifiManager wifiManager;
    ListView wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("permission", "checkSelfPermission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Log.d("permission", "shouldShowRequestPermissionRationale");
                // 사용자에게 설명을 보여줍니다.
                // 권한 요청을 다시 시도합니다.

            } else {
                // 권한요청

                Log.d("permission", "권한 요청");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_WIFI_STATE,
                                android.Manifest.permission.CHANGE_WIFI_STATE},
                        1000);

            }

        }

        wifiList = findViewById(R.id.wifi_list);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // 권한 획득 성공
                    Log.d("permission", "권한 획득 성공");

                } else {

                    // 권한 획득 실패
                    Log.d("permission", "권한 획득 실패");
                }
                return;
            }

        }
    }

    public void onClickScan(View view) {
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        List<ScanResult> results = wifiManager.getScanResults();

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);

        for (ScanResult result : results) {

            adapter.add(result.SSID);

        }

        wifiList.setAdapter(adapter);
        Log.e("TESTLOG","dsffdsfdsd");

    }
}