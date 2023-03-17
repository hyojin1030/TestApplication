package com.test.alarm;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class TimeSet {
    @ColumnInfo(name = "time")
    public String time ;
    @ColumnInfo(name = "isOn")
    public boolean isOn;
    @ColumnInfo(name = "min")
    public int min;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
