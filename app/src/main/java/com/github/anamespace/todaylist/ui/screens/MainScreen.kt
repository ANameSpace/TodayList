package com.github.anamespace.todaylist.ui.screens

//import com.google.accompanist.swiperefresh.SwipeRefresh
//import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
//import com.google.accompanist.pullrefresh.PullRefreshIndicator
//import com.google.accompanist.pullrefresh.pullRefresh
//import com.google.accompanist.pullrefresh.rememberPullRefreshState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.anamespace.todaylist.R
import com.github.anamespace.todaylist.data.ITask
import com.github.anamespace.todaylist.model.AppDatabase
import com.github.anamespace.todaylist.ui.UiConstants
import com.github.anamespace.todaylist.ui.components.CardItem
import com.github.anamespace.todaylist.ui.components.WeeklyTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToCreateTask: () -> Unit,
    database: AppDatabase
) {
    val listState = rememberLazyListState()
    val showTopBar by remember {
        derivedStateOf {
            !listState.isScrollInProgress || !listState.canScrollBackward
        }
    }

    var selectedData by remember { mutableStateOf(LocalDate.now()) }
    var tasks by remember { mutableStateOf(listOf<ITask>()) }
    var isLoading by remember { mutableStateOf(true) }

    suspend fun loadData(selectedDate: LocalDate) {
        isLoading = true
        tasks = database.taskDao().getTasksByDate(selectedDate).sortedBy { it.taskStartTime }
        isLoading = false
    }

    LaunchedEffect(selectedData) {
        loadData(selectedData)
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showTopBar,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                WeeklyTopBar(
                    onDaySelected = { date ->
                        selectedData = date
                    },
                    onSettingsClick = onNavigateToSettings,
                    onProfileClick = {}
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showTopBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onNavigateToCreateTask() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.icons_add),
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        },
        content = { innerPadding ->
            MyLazyColumn(
                listState = listState,
                tasks = tasks,
                isLoading = isLoading,
                database = database,
                onRefresh = {
                    CoroutineScope(Dispatchers.Default).launch {
                        loadData(selectedData)
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLazyColumn(
    listState: LazyListState,
    tasks: List<ITask>,
    isLoading: Boolean,
    database: AppDatabase,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(UiConstants.CARD_ROW_HEIGHT))
            }
            items(tasks) { task ->
                CardItem(
                    task = task,
                    onDelete = { t ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.taskDao().delete(t)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}