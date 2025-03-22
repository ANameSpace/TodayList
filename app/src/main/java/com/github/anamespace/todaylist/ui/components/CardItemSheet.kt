package com.github.anamespace.todaylist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.anamespace.todaylist.R
import com.github.anamespace.todaylist.data.ITask
import com.github.anamespace.todaylist.model.UserTask
import com.github.anamespace.todaylist.data.getExampleTask
import com.github.anamespace.todaylist.ui.UiConstants
import com.github.anamespace.todaylist.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItemSheet(
    task: ITask,
    sheetState: SheetState,
    scope: CoroutineScope,
    onDelete: (UserTask) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        // Name
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = task.taskName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        // Time
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                )
                .padding(8.dp),
            text = task.taskDate.format(UiConstants.CARD_DATE_FORMAT) +
                    "  |  " +
                    task.taskStartTime.format(UiConstants.CARD_TIME_FORMAT) +
                    " - " +
                    task.taskEndTime.format(UiConstants.CARD_TIME_FORMAT),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        // Notify
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(MaterialTheme.colorScheme.onSecondary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.settings_notify),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(3.dp)
            )
            Checkbox(
                checked = task.sendNotify,
                enabled = false,
                onCheckedChange = {},
                modifier = Modifier
                    .padding(3.dp)
            )
        }
        // Lore
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = task.taskFullLore,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(8.dp),
            color = MaterialTheme.colorScheme.secondary
        )
        // Delete button
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    sheetState.hide()
                    if(task is UserTask) {
                        onDelete(task)
                    }
                }
            },
            enabled = !task.isExternal,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.delete))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CardSheetPreview() {
    AppTheme {
        CardItemSheet(
            task = getExampleTask(),
            sheetState = rememberModalBottomSheetState(),
            scope = rememberCoroutineScope()
        ) {}
    }
}