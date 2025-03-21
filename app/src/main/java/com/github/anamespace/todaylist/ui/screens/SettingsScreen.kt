package com.github.anamespace.todaylist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.anamespace.todaylist.R
import com.github.anamespace.todaylist.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSave: (String, String) -> Unit,
    onBack: () -> Unit,
    currentLanguage: String,
    currentTheme: String
) {
    val languages = mapOf(
        "" to "System",
        "en" to "English",
        "ru" to "Русский"
    )
    val themes = mapOf(
        "" to "System"
    )

    var expandedLanguage by remember { mutableStateOf(false) }
    var expandedTheme by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.settings_title),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Language
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.settings_lang),
            color = MaterialTheme.colorScheme.onBackground
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expandedLanguage = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = languages.getOrDefault(currentLanguage, "-"))
                Icon(
                    imageVector = Icons.Default.ArrowDropDownCircle,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = expandedLanguage,
                onDismissRequest = { expandedLanguage = false }
            ) {
                languages.keys.forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(text = languages.getOrDefault(language, ""))
                        },
                        onClick = {
                            selectedLanguage = language
                            expandedLanguage = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Theme
        Text(
            text = stringResource(id = R.string.settings_color),
            color = MaterialTheme.colorScheme.onBackground
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expandedTheme = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            ) {
                Text(text = selectedTheme)
            }
            DropdownMenu(
                expanded = expandedTheme,
                onDismissRequest = { expandedTheme = false }
            ) {
                themes.keys.forEach { theme ->
                    DropdownMenuItem(
                        text = {
                            Text(text = themes.getOrDefault(theme, ""))
                        },
                        onClick = {
                        selectedTheme = theme
                        expandedTheme = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    onSave(selectedLanguage, selectedTheme)
                }
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
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            onSave = { _, _ -> },
            onBack = {},
            currentLanguage = "",
            currentTheme = ""
        )
    }
}