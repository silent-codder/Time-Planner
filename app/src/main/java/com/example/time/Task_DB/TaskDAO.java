package com.example.time.Task_DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void AddTask(Task task);

    @Query("select * from task ORDER BY id DESC")
    public List<Task> getTask();
}
