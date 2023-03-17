package com.test.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();

                UserDao userDao = db.userDao();

                User user = new User();
                user.setUid(0000);
                user.setFirstName("first");
                user.setLastName("last");

                TestData testData = new TestData();
                testData.setData1(1111);
                testData.setData2(2222);

                user.setTestData(testData);

                userDao.insertAll(user);

                List<User> users = userDao.getAll();

                for (User test : users) {
                    Log.d("TEST", "id : " + test.getUid());
                    Log.d("TEST", "first : " + test.getFirstName());
                    Log.d("TEST", "last : " + test.getLastName());
                    Log.d("TEST", "testdata 1 : " + test.getTestData().getData1());
                    Log.d("TEST", "testdata 2 : " + test.getTestData().getData2());
                }
            }
        }).start();



    }
}