package com.test.offlinetest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists TABLE_CHECK_IN ("
                + "_id integer primary key autoincrement, "
                + "ProjectCode text, "
                + "Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "State text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists TABLE_CHECK_IN";

        db.execSQL(sql);
        onCreate(db);
    }

    public void deletePrevTable(SQLiteDatabase db) {
        String sql = "DROP TABLE if exists TABLE_CHECK_IN";
        db.execSQL(sql);
    }


    public Cursor selectColumns(SQLiteDatabase db){
        return db.query("TABLE_ALARM", null, null, null, null, null, null);
    }
}