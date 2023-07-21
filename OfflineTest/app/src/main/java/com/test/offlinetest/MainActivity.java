package com.test.offlinetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    Button btnCheckIn;

    AlertDialog projectDialog;

    DBHelper dbHelper;
    SQLiteDatabase db;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        btnCheckIn = (Button) findViewById(R.id.btn_check_in);

        String prefState = PrefUtil.getValue(mContext, mContext.getString(R.string.pref_state));
        if (!prefState.equals("")) {
            btnCheckIn.setText(prefState);
        }

        dbHelper = new DBHelper(this, "test_check_in.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_project_list, null);
        dialogBuilder.setView(dialogView);
        projectDialog = dialogBuilder.create();

    }

    public void onClickCheckIn(View view) {
        String buttonState = btnCheckIn.getText().toString();

        insertCheckInData(buttonState);

        if (buttonState.equals(mContext.getString(R.string.str_start))) {
            btnCheckIn.setText(mContext.getString(R.string.str_end));
        } else if (buttonState.equals(mContext.getString(R.string.str_end))) {
            btnCheckIn.setText(mContext.getString(R.string.str_start));
        }

        PrefUtil.setValue(mContext, getString(R.string.pref_state), btnCheckIn.getText().toString());
    }

    private void insertCheckInData(String state) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        ContentValues values = new ContentValues();
        values.put("ProjectCode", "1000-0000");
        values.put("Date", formatter.format(new Date()));
        values.put("State", state);
        db.insert("TABLE_CHECK_IN", null, values);

    }

    public void onClickProject(View view) {
        projectDialog.show();
    }

    private void dismissProjectDialog() {
        if (projectDialog != null) {
            projectDialog.dismiss();
        }
    }

}