package com.github.anamespace.todaylist

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.anamespace.todaylist.alarm.NotificationScheduler
import com.github.anamespace.todaylist.model.AppDatabase
import com.github.anamespace.todaylist.model.UserTask
import com.github.anamespace.todaylist.ui.screens.CreateTaskScreen
import com.github.anamespace.todaylist.ui.screens.MainScreen
import com.github.anamespace.todaylist.ui.screens.SettingsScreen
import com.github.anamespace.todaylist.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: AppDatabase
    private lateinit var notificationScheduler: NotificationScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE)
        notificationScheduler = NotificationScheduler(this)


        //sharedPreferences.getBoolean("app-notify", false)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Navigation(sharedPreferences, database, notificationScheduler)
            }
        }
    }
}


@Composable
fun Navigation(sharedPreferences: SharedPreferences, database: AppDatabase, notificationScheduler: NotificationScheduler){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                onNavigateToSettings = { navController.navigate("settings_screen") },
                onNavigateToCreateTask = { navController.navigate("create_task_screen") },
                database = database
            )
        }
        composable("create_task_screen") {
            CreateTaskScreen(
                onBack = { navController.popBackStack() },
                onSave = { name, description, date, startTime, endTime, notify ->
                    val newTask = UserTask(
                        taskDate = date, taskStartTime = startTime,
                        taskEndTime = endTime,
                        taskName = name,
                        taskShortLore = description,
                        taskFullLore = description,
                        sendNotify = notify
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        database.taskDao().insert(newTask)

                        /*if (notify && date.isAfter(LocalDate.now()) && startTime.isAfter(LocalTime.now())) {
                            val calendar = Calendar.getInstance().apply {
                                set(Calendar.YEAR, date.year)
                                set(Calendar.MONTH, date.month.value)
                                set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                                set(Calendar.HOUR_OF_DAY, startTime.hour)
                                set(Calendar.MINUTE, startTime.minute)
                            }

                            val triggerAtMillis = calendar.timeInMillis
                            val requestCode = newTask.id.toInt()

                            notificationScheduler.scheduleNotification(
                                timeInMillis = triggerAtMillis,
                                requestCode = requestCode,
                                title = "Напоминание",
                                text = name
                            )


                            // notificationScheduler.cancelScheduledNotification(requestCode)
                        }*/
                    }
                    navController.popBackStack()
                },
                notifyEnabled = sharedPreferences.getBoolean("app-notify", false)
            )
        }
        composable("settings_screen") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onSave = { useNotify ->
                    sharedPreferences.edit {
                        putBoolean("app-notify", useNotify)
                    }
                    navController.popBackStack()
                },
                currentNotify = sharedPreferences.getBoolean("app-notify", false)
            )
        }
    }
}