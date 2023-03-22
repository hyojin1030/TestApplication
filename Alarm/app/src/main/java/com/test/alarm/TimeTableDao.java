package com.test.alarm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimeTableDao {
    @Query("SELECT * FROM TimeTable")
    List<TimeTable> getAll();

    @Insert
    void insertAll(TimeTable... timeTables);

    @Update
    void updateData(TimeTable timeTable);

    @Delete
    void delete(TimeTable timeTable);
}
