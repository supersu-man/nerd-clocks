package com.supersuman.nerdclocks

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import java.util.*


class BinaryClock : AppWidgetProvider() {

    val action = "FALUDA"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("Binary Enabled")
        setAlarmNextMinute(context, Calendar.getInstance(), action)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("Binary Disabled")
        removeAlarm(context, action)
    }
}



private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    removeAlarm(context,"FALUDA")
    setAlarmNextMinute(context, Calendar.getInstance(),"FALUDA")
    val views = RemoteViews(context.packageName, R.layout.binary_clock)
    val cal = Calendar.getInstance()
    var hour = cal.get(Calendar.HOUR)
    if (hour==0) hour=12
    val minute = cal.get(Calendar.MINUTE)
    views.setTextViewText(R.id.binaryTimeText, "$hour : $minute")

    clearImages(views)
    setImage(views, 1, hour%10)
    setImage(views, 0, hour/10)
    setImage(views, 3, minute%10)
    setImage(views, 2, minute/10)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private val map = mapOf(
    0 to listOf(0, 0, R.id.a3, R.id.a4),
    1 to listOf(R.id.b1, R.id.b2, R.id.b3, R.id.b4),
    2 to listOf(R.id.c1, R.id.c2, R.id.c3, R.id.c4),
    3 to listOf(R.id.d1, R.id.d2, R.id.d3, R.id.d4)
)

private fun clearImages(views: RemoteViews){
    for(i in map.values){
        for (j in i){
            if (j!=0){
                views.setImageViewResource(j, R.drawable.ic_outline_brightness_1_24)
            }
        }
    }
}

private fun setImage(views: RemoteViews,column : Int, digit:Int){
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


class UpdateWidgetService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        val manager = AppWidgetManager.getInstance(this)
        val ids = manager.getAppWidgetIds(ComponentName(application, BinaryClock::class.java))
        println("Updating from Binary service")
        for (id in ids) {
            updateAppWidget(applicationContext, manager, id)
        }
    }
}