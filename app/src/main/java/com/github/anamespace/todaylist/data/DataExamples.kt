package com.github.anamespace.todaylist.data

import com.github.anamespace.todaylist.model.UserTask
import java.time.LocalDate
import java.time.LocalTime

fun getExampleTask(): UserTask {
    return UserTask(
        taskDate = LocalDate.now(),
        taskStartTime = LocalTime.now(),
        taskEndTime = LocalTime.now(),
        taskName = "Name",
        taskShortLore = "Lore lore lore lore",
        taskFullLore = "Lore lore lore lore lore lore lore lore lore lore lore lore\nLore lore \nLore lore",
    )
}