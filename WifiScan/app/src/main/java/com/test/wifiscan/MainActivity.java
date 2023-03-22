package com.test.wifiscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context mContext = getApplicationContext();

        String LogData = "";
        int count = 0;
        List<HashMap<String, Object>> list = null;


        // [로직 처리 실시]
        try {

            // [퍼미션 권한 체크 실시]
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                // [퍼미션이 부여되어있지 않은 경우 종료]
                LogData = "Permission not granted";
            }
            else {

                // [리턴 변수에 삽입]
                LogData = "Success Permission";

                // [와이파이 스캔 결과 확인]
                list = new ArrayList<>();

                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> scanResultList = wifiManager.getScanResults();

                for (ScanResult result : scanResultList) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("SSID", result.SSID);
                    map.put("BSSID", result.BSSID);
                    //map.put("capabilities", result.capabilities);

                    list.add(map);

                }

                count = list.size();
            }

        }
        catch (Exception e){
            e.printStackTrace();
            LogData = e.getMessage();
        }


    }
}