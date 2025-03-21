package com.github.anamespace.todaylist.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.anamespace.todaylist.R
import com.github.anamespace.todaylist.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

@Composable
fun CreateTaskScreen(
    onBack: () -> Unit,
    onSave: (String, String, LocalDate, LocalTime, LocalTime) -> Unit
) {
    var taskName by remember { mutableStateOf(TextFieldValue("")) }
    var taskDescription by remember { mutableStateOf(TextFieldValue("")) }
    var taskDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.of(0, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(0, 0)) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val isNameValid = taskName.text.isNotEmpty()
    var isEditName by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(id = R.string.create_task_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = taskName,
            onValueChange = {
                isEditName = true
                if (it.text.length <= 16) taskName = it
            },
            label = { Text(stringResource(id = R.string.create_task_name)) },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (!isNameValid && isEditName) {
                    Text(
                        text = stringResource(id = R.string.create_task_empty_field),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            isError = !isNameValid && isEditName
        )


        // Lore
        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text(stringResource(id = R.string.create_task_lore)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Date
        OutlinedTextField(
            value = taskDate.toString(),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.create_task_data)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = stringResource(id = R.string.create_task_data)
                    )
                }
            }
        )

        // Time (start)
        OutlinedTextField(
            value = startTime.toString(),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.create_task_start)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showStartTimePicker = true }) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = stringResource(id = R.string.create_task_start)
                    )
                }
            }
        )

        // Time (end)
        OutlinedTextField(
            value = endTime.toString(),
            onValueChange = {},
            label = { Text(stringResource(id = R.string.create_task_end)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showEndTimePicker = true }) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = stringResource(id = R.string.create_task_end)
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (startTime.isAfter(endTime)) {
                        val temp = startTime
                        startTime = endTime
                        endTime = temp
                    }
                    onSave(taskName.text, taskDescription.text, taskDate, startTime, endTime)
                },
                enabled = isNameValid
            ) {
                Text(stringResource(id = R.string.dufault_save))
            }

            Button(
                onClick = { onBack() }
            ) {
                Text(stringResource(id = R.string.default_back))
            }
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->
                taskDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        showDatePicker = false
    }

    if (showStartTimePicker) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            LocalContext.current,
            { _, hourOfDay, minute ->
                startTime = LocalTime.of(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
        showStartTimePicker = false
    }

    if (showEndTimePicker) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            LocalContext.current,
            { _, hourOfDay, minute ->
                endTime = LocalTime.of(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
        showEndTimePicker = false
    }
}


@Preview(showBackground = true)
@Composable
fun CreateTaskScreenPreview() {
    AppTheme {
        CreateTaskScreen(
            onSave = { _, _, _, _, _ -> },
            onBack = {}
        )
    }
}