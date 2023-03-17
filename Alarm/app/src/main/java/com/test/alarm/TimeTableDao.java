package com.test.alarm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimeTableDao {
    @Query("SELECT * FROM TimeTable")
    List<TimeTable> getAll();

    @Insert
    void insertAll(TimeTable... timeTables);

    @Delete
    void delete(TimeTable timeTable);
}
