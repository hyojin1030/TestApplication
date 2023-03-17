package com.test.alarm;

import androidx.room.Embedded;

public class TimeIndex {
    @Embedded(prefix = "start_")
    public TimeSet start;
    @Embedded(prefix = "end_")
    public TimeSet end;

    public TimeSet getStart() {
        return start;
    }

    public void setStart(TimeSet start) {
        this.start = start;
    }

    public TimeSet getEnd() {
        return end;
    }

    public void setEnd(TimeSet end) {
        this.end = end;
    }
}
