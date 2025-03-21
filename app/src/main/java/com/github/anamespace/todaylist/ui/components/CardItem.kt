package com.github.anamespace.todaylist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.anamespace.todaylist.R
import com.github.anamespace.todaylist.data.ITask
import com.github.anamespace.todaylist.data.TaskStatus
import com.github.anamespace.todaylist.data.getExampleTask
import com.github.anamespace.todaylist.model.UserTask
import com.github.anamespace.todaylist.ui.UiConstants
import com.github.anamespace.todaylist.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(
    task: ITask,
    onDelete: (UserTask) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(UiConstants.CARD_ROW_HEIGHT)
            .shadow(10.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    scope.launch {
                        sheetState.show()
                    }
                }
        ) {
            // Indicator
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(UiConstants.CARD_ROW_HEIGHT / 2)
                    .align(Alignment.CenterVertically)
                    .background(
                        color = when (task.taskStatus) {
                            TaskStatus.PAST -> Color.Gray
                            TaskStatus.CURRENT -> Color.Green
                            TaskStatus.FUTURE -> Color.Yellow
                        },
                        shape = RoundedCornerShape(
                            topEnd = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
            )
            // Content
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                // Name
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = task.taskName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Time
                    Text(
                        modifier = Modifier.fillMaxHeight(),
                        text = "${
                            task.taskStartTime.format(UiConstants.CARD_TIME_FORMAT)
                        }\n—\n${task.taskEndTime.format(UiConstants.CARD_TIME_FORMAT)}",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    // Lore
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        modifier = Modifier.fillMaxHeight(),
                        text = "\n" + task.taskShortLore,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Left,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                // Tag
                if (task.isExternal) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onTertiary,
                                shape = RoundedCornerShape(
                                    size = 10.dp
                                )
                            )
                            .wrapContentSize()
                            .padding(1.dp)
                            .align(Alignment.BottomEnd),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(15.dp),
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(id = R.string.icons_lock)
                        )
                        Text(
                            text = stringResource(id = R.string.tag_external),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }

    // Bottom sheet
    if (sheetState.isVisible) {
        ModalBottomSheet(
            modifier = Modifier,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            sheetState = sheetState
        ) {
            // Content
            CardItemSheet(task, sheetState, scope) { t -> onDelete(t) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    AppTheme {
        CardItem(
            task = getExampleTask(),
            onDelete = {}
        )
    }
}