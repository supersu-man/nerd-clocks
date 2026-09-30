package com.supersuman.nerdclocks

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import com.supersuman.nerdclocks.ui.widgets.BinaryClock
import com.supersuman.nerdclocks.ui.widgets.BinaryClockReceiver
import com.supersuman.nerdclocks.ui.widgets.FibonacciClock
import com.supersuman.nerdclocks.ui.widgets.FibonacciClockReceiver
import com.supersuman.nerdclocks.ui.widgets.HexClock
import com.supersuman.nerdclocks.ui.widgets.HexClockReceiver
import com.supersuman.nerdclocks.ui.widgets.TextClock
import com.supersuman.nerdclocks.ui.widgets.TextClockReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

object WidgetUpdateScheduler {
    private const val REQUEST_CODE = 1001
    private val schedulerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)


    fun forceUpdateWidgets(context: Context): Job = schedulerScope.launch {
        BinaryClock().updateAll(context)
        FibonacciClock().updateAll(context)
        TextClock().updateAll(context)
        HexClock().updateAll(context)
    }

    fun cancelIfNoWidgets(context: Context) {
        if (!hasAnyWidgets(context)) {
            cancelUpdates(context)
        }
    }

    private fun hasAnyWidgets(context: Context): Boolean {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        val widget1 = appWidgetManager.getAppWidgetIds(
            ComponentName(context, BinaryClockReceiver::class.java)
        )
        val widget2 = appWidgetManager.getAppWidgetIds(
            ComponentName(context, FibonacciClockReceiver::class.java)
        )
        val widget3 = appWidgetManager.getAppWidgetIds(
            ComponentName(context, TextClockReceiver::class.java)
        )
        val widget4 = appWidgetManager.getAppWidgetIds(
            ComponentName(context, HexClockReceiver::class.java)
        )

        return widget1.isNotEmpty() || widget2.isNotEmpty() || widget3.isNotEmpty() || widget4.isNotEmpty()
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNextMinuteUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Align to next minute boundary using java.time
        val nextMinute = LocalDateTime.now().plusMinutes(1).withSecond(0).withNano(0)
        val triggerAtMillis = nextMinute.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
        println("scheduleNextMinuteUpdate")
    }

    private fun cancelUpdates(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        println("cancelUpdates")
    }

}