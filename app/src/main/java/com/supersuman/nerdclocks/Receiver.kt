package com.supersuman.nerdclocks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import java.util.*

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "FALUDA")
            JobIntentService.enqueueWork(context!!, UpdateWidgetService::class.java, 1000, intent)
        if (intent?.action == "RASGULA")
            JobIntentService.enqueueWork(context!!, UpdateFibonacciWidgetService::class.java, 999, intent)
    }
}

fun setAlarmNextMinute(context: Context?, calendar: Calendar, action : String){

    calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + 60000)
    val newCal = Calendar.getInstance()
    newCal.set(Calendar.SECOND,0)
    newCal.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE))
    newCal.set(Calendar.HOUR,calendar.get(Calendar.HOUR))
    val manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    val intent = Intent(context, Receiver::class.java)
    intent.action = action
    val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    manager!!.setExact(AlarmManager.RTC, newCal.timeInMillis, alarmIntent)
}

fun removeAlarm(context: Context?, action: String){
    val manager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    val intent = Intent(context, Receiver::class.java)
    intent.action = action
    val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    manager!!.cancel(alarmIntent)
}