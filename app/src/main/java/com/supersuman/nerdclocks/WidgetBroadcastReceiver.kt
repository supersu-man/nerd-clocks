package com.supersuman.nerdclocks

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class WidgetBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            context?.let {
                BinaryClockReceiverr().glanceAppWidget.updateAll(it)
                FibonacciClockReceiverr().glanceAppWidget.updateAll(it)
                println("force updating widgets")
            }
        }
        cancelAlarmManager(context)
        setAlarmManager(context)
    }
}

@SuppressLint("ScheduleExactAlarm")
fun setAlarmManager(context: Context?) {
    val intent = Intent(context, WidgetBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context?.applicationContext, 234324243, intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    alarmManager?.setExact(AlarmManager.RTC, ((System.currentTimeMillis()/60000) + 1) * 60000, pendingIntent)
    val sdf = SimpleDateFormat("hh:mm")
    val currentDate = sdf.format(((System.currentTimeMillis()/60000) + 1) * 60000.toLong())
    println("alarm set at $currentDate")
}

fun cancelAlarmManager(context: Context?) {
    val intent = Intent(context, WidgetBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context?.applicationContext, 234324243, intent,
        PendingIntent.FLAG_IMMUTABLE)
    val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    alarmManager?.cancel(pendingIntent)
    println("alarm cancelled")
}

fun enableWidget(context: Context) {
    val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val n = sharedPref.getInt("widgets", 0)
    sharedPref.edit().putInt("widgets", n+1).apply()
}

fun disableWidget(context: Context) {
    val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val n = sharedPref.getInt("widgets", 1)
    if(n>0) sharedPref.edit().putInt("widgets", n-1).apply()
}