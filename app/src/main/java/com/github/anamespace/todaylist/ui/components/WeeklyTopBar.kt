package com.github.anamespace.todaylist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.anamespace.todaylist.R
import com.github.anamespace.todaylist.ui.theme.AppTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyTopBar(
    onDaySelected: (LocalDate) -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val currentDate = LocalDate.now()
    val daysOfWeek = DayOfWeek.entries.toTypedArray()

    val weeks = remember { mutableStateListOf<LocalDate>() }
    val initialWeeks = 3
    val weeksToLoad = 2

    for (i in -initialWeeks..initialWeeks) {
        weeks.add(currentDate.plusWeeks(i.toLong()))
    }

    val pagerState = rememberPagerState(initialPage = 3, pageCount = { weeks.size })

    var selectedDay by remember { mutableStateOf(currentDate) }

    fun addWeeks(direction: Int) {
        val lastWeek = weeks.last()
        if (direction > 0) {
            val newWeeks = (1..weeksToLoad).map { lastWeek.plusWeeks(it * direction.toLong()) }
            weeks.addAll(newWeeks)
        } else {
            val newWeeks = (1..weeksToLoad).map { lastWeek.minusWeeks(it * kotlin.math.abs(direction).toLong()) }
            weeks.addAll(0, newWeeks)
        }
    }

    Column {
        TopAppBar(
            title = {
                Spacer(modifier = Modifier.height(48.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) { page ->
                    if (page == weeks.size - 2) {
                        addWeeks(1)
                    }/* else if (page == 1) {
                        addWeeks(-1)
                    }
                    TODO: fix
                    */

                    val weekStart = weeks[page]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        daysOfWeek.forEachIndexed { _, day ->
                            val date = weekStart.with(day)
                            val isToday = date == currentDate
                            val isSelected = date == selectedDay

                            Column(
                                modifier = Modifier.padding(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Day int
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = if (isToday) Color.Red else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable {
                                        selectedDay = date
                                        onDaySelected(date)
                                    },
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                // Day name
                                Text(
                                    text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .padding(top = 2.dp)
                                            .background(
                                                MaterialTheme.colorScheme.secondary,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.icons_settings),
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.icons_profile),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WeeklyTopBarPreview() {
    AppTheme {
        WeeklyTopBar(onSettingsClick = {}, onDaySelected = {}, onProfileClick = {})
    }
}