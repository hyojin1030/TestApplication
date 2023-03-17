package com.test.alarm;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimeTable {
    @PrimaryKey
    @NonNull
    public String id;
    @Embedded(prefix = "am_")
    public TimeIndex am;
    @Embedded(prefix = "pm_")
    public TimeIndex pm;
    @Embedded(prefix = "etc_")
    public TimeIndex etc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
