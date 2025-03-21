package com.github.anamespace.todaylist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.anamespace.todaylist.model.AppDatabase
import com.github.anamespace.todaylist.model.UserTask
import com.github.anamespace.todaylist.ui.screens.CreateTaskScreen
import com.github.anamespace.todaylist.ui.screens.MainScreen
import com.github.anamespace.todaylist.ui.screens.SettingsScreen
import com.github.anamespace.todaylist.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE)

        //sharedPreferences.getString("app-lang", "").orEmpty()

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Navigation(sharedPreferences, database)
            }
        }
    }
}


@Composable
fun Navigation(sharedPreferences: SharedPreferences, database: AppDatabase) {
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
                onSave = { name, description, date, startTime, endTime ->
                    val newTask = UserTask(
                        taskDate = date, taskStartTime = startTime,
                        taskEndTime = endTime,
                        taskName = name,
                        taskShortLore = description,
                        taskFullLore = description
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        database.taskDao().insert(newTask)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable("settings_screen") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onSave = { languageCode, themeName ->
                    sharedPreferences.edit {
                        putString("app-lang", languageCode)
                        putString("app-theme", themeName)
                    }
                    if(languageCode.isNotEmpty()) {
                        val locale = Locale(languageCode)
                        Locale.setDefault(locale)
                    } else {
                        Locale.setDefault(Locale.getDefault())
                    }
                    navController.popBackStack()
                },
                currentLanguage = sharedPreferences.getString("app-lang", "").orEmpty(),
                currentTheme = sharedPreferences.getString("app-theme", "").orEmpty()
            )
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodayListTheme {
        Greeting("Android")
    }
}*/