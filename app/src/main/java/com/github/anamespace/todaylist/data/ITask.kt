package com.github.anamespace.todaylist.data

import java.time.LocalDate
import java.time.LocalTime

interface ITask {
    val taskDate: LocalDate
    val taskStartTime: LocalTime
    val taskEndTime: LocalTime
    val taskName: String
    val taskShortLore: String
    val taskFullLore: String
    val taskStatus: TaskStatus
    val isExternal: Boolean
}

enum class TaskStatus{
    PAST,
    CURRENT,
    FUTURE
}