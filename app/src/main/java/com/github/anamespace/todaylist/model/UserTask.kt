package com.github.anamespace.todaylist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.anamespace.todaylist.data.ITask
import com.github.anamespace.todaylist.data.TaskStatus
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "tasks")
data class UserTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    override val taskDate: LocalDate,
    override val taskStartTime: LocalTime,
    override val taskEndTime: LocalTime,
    override val taskName: String,
    override val taskShortLore: String,
    override val taskFullLore: String
) : ITask {
    override val taskStatus: TaskStatus
        get() {
            val currentDay = LocalDate.now()
            return if (currentDay.isBefore(taskDate)) {
                TaskStatus.FUTURE
            } else if (currentDay.isAfter(taskDate)) {
                TaskStatus.PAST
            } else {
                var currentTime = LocalTime.now()
                if (currentTime.isBefore(taskStartTime)) {
                    TaskStatus.FUTURE
                } else if (currentTime.isAfter(taskEndTime)) {
                    TaskStatus.PAST
                } else {
                    TaskStatus.CURRENT
                }
            }
        }
    override val isExternal: Boolean
        get() = false
}