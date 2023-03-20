package com.test.alarm;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timeTable")
public class TimeTable {
    @PrimaryKey
    @NonNull
    public int id;
    @Embedded(prefix = "am_")
    public TimeIndex am;
    @Embedded(prefix = "pm_")
    public TimeIndex pm;
    @Embedded(prefix = "etc_")
    public TimeIndex etc;

    public TimeTable() {

    }

    @Ignore
    public TimeTable(int id, TimeIndex am, TimeIndex pm) {
        this.id = id;
        this.am = am;
        this.pm = pm;
    }

    @Ignore
    public TimeTable(int id, TimeIndex am, TimeIndex pm, TimeIndex etc) {
        this.id = id;
        this.am = am;
        this.pm = pm;
        this.etc = etc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeIndex getAm() {
        return am;
    }

    public void setAm(TimeIndex am) {
        this.am = am;
    }

    public TimeIndex getPm() {
        return pm;
    }

    public void setPm(TimeIndex pm) {
        this.pm = pm;
    }

    public TimeIndex getEtc() {
        return etc;
    }

    public void setEtc(TimeIndex etc) {
        this.etc = etc;
    }
}
