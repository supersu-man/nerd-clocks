package com.supersuman.nerdclocks.ui.screens

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.net.toUri
import com.supersuman.nerdclocks.WidgetUpdateScheduler
import com.supersuman.nerdclocks.isPermissionGranted

@Composable
fun Home() {
    val context = LocalContext.current
    var isPerms by remember { mutableStateOf(true) }
    var showTime by remember { mutableStateOf(false) }

    val infoText = "If your widget stops refreshing every minute, try removing it and adding it again. You can also tap ‘Sync time’ below to manually refresh."
    val infoText2 = "Turning time on or off may take a minute or two to show up on your widget."

    LaunchedEffect(Unit) {
        isPerms = isPermissionGranted(context)
        showTime = getShowTime(context)
    }

    fun forceUpdateWidgets() {
        WidgetUpdateScheduler.forceUpdateWidgets(context)
        WidgetUpdateScheduler.scheduleNextMinuteUpdate(context)
        Toast.makeText(context, "Syncing time...", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Text(infoText, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { forceUpdateWidgets() }) {
            Text("Sync time")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(infoText2, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Show time")
            Checkbox(checked = showTime, onCheckedChange = {
                showTime = it
                saveShowTime(context, it)
            })
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("We really appreciate you using our app ✨")
        Spacer(modifier = Modifier.height(20.dp))
        if (!isPerms && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Button(onClick = { requestPermission(context) }) {
                Text("Grant permission")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
private fun requestPermission(context: Context) {
    val intent = Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
    intent.data = "package:${context.packageName}".toUri()
    context.startActivity(intent)
}

private fun saveShowTime(context: Context, boolean: Boolean) {
    val sharedPref = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
    sharedPref.edit { putBoolean("showTime", boolean) }
}

fun getShowTime(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
    return sharedPref.getBoolean("showTime", false)
}