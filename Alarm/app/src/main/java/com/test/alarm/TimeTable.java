package com.test.alarm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimeTable {
    @PrimaryKey
    public int uid;

    public String startAm;
    public String endAm;
    public String startPm;
    public String endPm;
    public String startEtc;
    public String endEtc;
}
