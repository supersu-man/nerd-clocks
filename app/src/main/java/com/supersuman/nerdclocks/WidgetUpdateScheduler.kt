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
import com.supersuman.nerdclocks.ui.widgets.TextClock
import com.supersuman.nerdclocks.ui.widgets.TextClockReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

object WidgetUpdateScheduler {
    private const val REQUEST_CODE = 1001


    fun forceUpdateWidgets(context: Context) = CoroutineScope(Dispatchers.Default).launch {
        BinaryClock().updateAll(context)
        FibonacciClock().updateAll(context)
        TextClock().updateAll(context)
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

        return widget1.isNotEmpty() || widget2.isNotEmpty() || widget3.isNotEmpty()
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

        // Align to next minute boundary
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
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