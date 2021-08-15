package com.supersuman.nerdclocks

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import java.util.*


class FibonacciClock : AppWidgetProvider() {

    val action = "RASGULA"

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        println("Fibonacci Enabled")
        setAlarmNextMinute(context, Calendar.getInstance(),action)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        println("Fibonacci Disabled")
        removeAlarm(context, action)
    }
}

private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    removeAlarm(context,"RASGULA")
    setAlarmNextMinute(context, Calendar.getInstance(),"RASGULA")
    val views = RemoteViews(context.packageName, R.layout.fibonacci_clock)
    clearViews(context,views)
    var hour = Calendar.getInstance().get(Calendar.HOUR)
    if (hour==0) hour=12
    val minute = Calendar.getInstance().get(Calendar.MINUTE)
    views.setTextViewText(R.id.fibnoacciTimeText,"$hour : $minute")
    val mapBoxes = mapOf(1 to listOf(R.id.one1,R.id.one2), 2 to listOf(R.id.two), 3 to listOf(R.id.three), 5 to listOf(R.id.five))
    val mapColors = mapOf(
        "r" to ContextCompat.getColor(context, R.color.widgetRed),
        "g" to ContextCompat.getColor(context, R.color.widgetGreen),
        "b" to ContextCompat.getColor(context, R.color.widgetBlue)
    )
    val rgb = rgb(hour,minute/5)
    var firstOne = true
    val random = mutableListOf(0,1)
    for(i in rgb.keys) {
        for (j in rgb[i]!!){
            if (j==1){
                if (firstOne){
                    val pos = random.random()
                    views.setInt(mapBoxes[j]!![pos],"setColorFilter", mapColors[i]!!)
                    random.remove(pos)
                    firstOne=false
                }else{
                    views.setInt(mapBoxes[j]!![random.random()],"setColorFilter", mapColors[i]!!)
                }
            }else{
                views.setInt(mapBoxes[j]!![0],"setColorFilter", mapColors[i]!!)
            }
        }
    }
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun clearViews(context: Context, views: RemoteViews) {
    views.setInt(R.id.one1,"setColorFilter", ContextCompat.getColor(context, R.color.Grey))
    views.setInt(R.id.one2,"setColorFilter", ContextCompat.getColor(context, R.color.Grey))
    views.setInt(R.id.two,"setColorFilter", ContextCompat.getColor(context, R.color.Grey))
    views.setInt(R.id.three,"setColorFilter", ContextCompat.getColor(context, R.color.Grey))
    views.setInt(R.id.five,"setColorFilter", ContextCompat.getColor(context, R.color.Grey))
}

private fun rgb(hour : Int, minute : Int): Map<String, MutableList<Int>> {
    val map : Map<String, MutableList<Int>> = mapOf("r" to mutableListOf(),"g" to mutableListOf(),"b" to mutableListOf())
    val h = filterPartitions(hour).random()
    val m = filterPartitions(minute).random()
    val hm = HashSet(h.plus(m))
    if (h.count { it==1 }==2 && m.count { it==1 }==2){
        map["b"]?.add(1)
    }else if (h.count { it==1 }==2){
        map["r"]?.add(1)
    }else if (m.count { it==1 }==2){
        map["g"]?.add(1)
    }
    for (i in hm){
        if (i==0) continue
        if (i in m && i in h ){
            map["b"]?.add(i)
        }else if (i in h && !m.contains(i)){
            map["r"]?.add(i)
        }else if (i in m && !h.contains(i)){
            map["g"]?.add(i)
        }
    }
    return map
}

private fun filterPartitions(num : Int): MutableList<MutableList<Int>> {
    val arr = partition(num)
    val v = mutableListOf<MutableList<Int>>()
    for (i in arr){
        if (i.count { it==1 }<=2 && i.count { it==2 }<=1 &&
            i.count { it==3 }<=1 && i.count { it==5 }<=1 &&
            !i.contains(4) && !i.contains(6) && !i.contains(7) &&
            !i.contains(8) && !i.contains(9) && !i.contains(10) &&
            !i.contains(11) && !i.contains(12))
        {
            v.add(i)
        }
    }
    return v
}

private fun partition(num: Int): MutableSet<MutableList<Int>> {
    val partitions = mutableSetOf<MutableList<Int>>()
    partitions.add(mutableListOf(num))
    for(i in 1 until num){
        for (j in partition(num-i)){
            j.add(i)
            j.sort()
            partitions.add(j)
        }
    }
    return partitions
}


class UpdateFibonacciWidgetService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        val manager = AppWidgetManager.getInstance(this)
        val ids = manager.getAppWidgetIds(ComponentName(application, FibonacciClock::class.java))
        println("Updating from Fibonacci service")
        for (id in ids) {
            updateAppWidget(applicationContext, manager, id)
        }
    }
}