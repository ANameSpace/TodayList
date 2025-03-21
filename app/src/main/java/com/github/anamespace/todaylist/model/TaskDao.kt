package com.github.anamespace.todaylist.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: UserTask)

    @Query("SELECT * FROM tasks WHERE taskDate = :date")
    suspend fun getTasksByDate(date: LocalDate): List<UserTask>

    @Delete
    suspend fun delete(task: UserTask)
}