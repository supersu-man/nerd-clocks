package com.supersuman.nerdclocks

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import java.util.*
import android.content.Intent

import android.content.IntentFilter
import kotlin.concurrent.thread
import kotlin.math.min


class BinaryClock : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        removeAlarm(context)
    }


    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        val cal = Calendar.getInstance()
        val manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, Receiver::class.java)
        intent.action = "FALUDA"
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        setAlarmNextMinute(context, Calendar.getInstance())
    }
}

fun setAlarmNextMinute(context: Context?, calendar: Calendar){

    calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + 60000)
    val newCal = Calendar.getInstance()
    newCal.set(Calendar.SECOND,0)
    newCal.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE))
    newCal.set(Calendar.HOUR,calendar.get(Calendar.HOUR))
    val manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    val intent = Intent(context, Receiver::class.java)
    intent.action = "FALUDA"
    val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    manager!!.setExact(AlarmManager.RTC, newCal.timeInMillis, alarmIntent)
}

fun removeAlarm(context: Context?){
    val manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    val intent = Intent(context, Receiver::class.java)
    intent.action = "FALUDA"
    val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    manager!!.cancel(alarmIntent)
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    removeAlarm(context)
    setAlarmNextMinute(context, Calendar.getInstance())
    val views = RemoteViews(context.packageName, R.layout.binary_clock)
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR)
    val minute = cal.get(Calendar.MINUTE)
    views.setTextViewText(R.id.appwidget_text, "$hour : $minute")

    clearImages(views)
    setImage(views, 1, hour%10)
    setImage(views, 0, hour/10)
    setImage(views, 3, minute%10)
    setImage(views, 2, minute/10)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

val map = mapOf(
    0 to listOf(0, 0, R.id.a3, R.id.a4),
    1 to listOf(R.id.b1, R.id.b2, R.id.b3, R.id.b4),
    2 to listOf(R.id.c1, R.id.c2, R.id.c3, R.id.c4),
    3 to listOf(R.id.d1, R.id.d2, R.id.d3, R.id.d4)
)

fun clearImages(views: RemoteViews){
    for(i in map.values){
        for (j in i){
            if (j!=0){
                views.setImageViewResource(j, R.drawable.ic_outline_brightness_1_24)
            }
        }
    }
}

fun setImage(views: RemoteViews,column : Int, digit:Int){
    when(digit){
        1 -> views.setImageViewResource(map[column]!![3], R.drawable.ic_baseline_brightness_1_24)
        2 -> views.setImageViewResource(map[column]!![2], R.drawable.ic_baseline_brightness_1_24)
        3 ->{
            views.setImageViewResource(map[column]!![3], R.drawable.ic_baseline_brightness_1_24)
            views.setImageViewResource(map[column]!![2], R.drawable.ic_baseline_brightness_1_24)
        }
        4 ->{
            views.setImageViewResource(map[column]!![1], R.drawable.ic_baseline_brightness_1_24)
        }
        5 ->{
            views.setImageViewResource(map[column]!![1], R.drawable.ic_baseline_brightness_1_24)
            views.setImageViewResource(map[column]!![3], R.drawable.ic_baseline_brightness_1_24)
        }
        6 ->{
            views.setImageViewResource(map[column]!![1], R.drawable.ic_baseline_brightness_1_24)
            views.setImageViewResource(map[column]!![2], R.drawable.ic_baseline_brightness_1_24)
        }
        7 ->{
            views.setImageViewResource(map[column]!![1], R.drawable.ic_baseline_brightness_1_24)
            views.setImageViewResource(map[column]!![2], R.drawable.ic_baseline_brightness_1_24)
            views.setImageViewResource(map[column]!![3], R.drawable.ic_baseline_brightness_1_24)
        }
        8 ->{
            views.setImageViewResource(map[column]!![0], R.drawable.ic_baseline_brightness_1_24)
        }
        9 ->{
            views.setImageViewResource(map[column]!![0], R.drawable.ic_baseline_brightness_1_24)
            views.setImageViewResource(map[column]!![3], R.drawable.ic_baseline_brightness_1_24)
        }
    }
}


class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == "FALUDA")
            JobIntentService.enqueueWork(context!!, UpdateWidgetService::class.java, 1000, intent)
    }
}

class UpdateWidgetService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        val manager = AppWidgetManager.getInstance(this)
        val ids = manager.getAppWidgetIds(ComponentName(application, BinaryClock::class.java))
        println("Updating from service")
        for (id in ids) {
            updateAppWidget(applicationContext, manager, id)
        }
    }
}