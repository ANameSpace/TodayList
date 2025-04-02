package com.github.anamespace.todaylist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSave: (Boolean) -> Unit,
    onBack: () -> Unit,
    currentNotify: Boolean
) {
    var useNotify by remember { mutableStateOf(currentNotify) }
    /*
        if (useNotify == true/* && Build.VERSION.SDK_INT >= 33*/) {
            if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                LocalActivity.current?.let { ActivityCompat.requestPermissions(it, Array(1){Manifest.permission.POST_NOTIFICATIONS},101) };
            }
            /*if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                LocalActivity.current?.let { ActivityCompat.requestPermissions(it, Array(1){Manifest.permission.SCHEDULE_EXACT_ALARM},102) };
            }*/
        }*/

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.settings_lang),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(3.dp)
            )
            Text(
                text = Locale.getDefault().displayLanguage,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(3.dp)
            )
        }

        // Theme
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.settings_color),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(3.dp)
            )
            var themeNameresId = if (isSystemInDarkTheme()) {
                R.string.theme_dark
            } else {
                R.string.theme_white
            }
            Text(
                text = stringResource(id = themeNameresId),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(3.dp)
            )
        }

        // Notify
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.settings_notify),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(3.dp)
            )
            Switch(
                checked = useNotify,
                onCheckedChange = {
                    useNotify = it
                },
                modifier = Modifier
                    .padding(3.dp),
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onBack() }
            ) {
                Text(stringResource(id = R.string.default_back))
            }

            Button(
                onClick = {
                    onSave(useNotify)
                }
            ) {
                Text(stringResource(id = R.string.dufault_save))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            onSave = { _ -> },
            onBack = {},
            currentNotify = false
        )
    }
}