package com.supersuman.nerdclocks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import com.supersuman.nerdclocks.ui.widgets.BinaryClock
import com.supersuman.nerdclocks.ui.widgets.FibonacciClock
import com.supersuman.nerdclocks.ui.widgets.TextClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        WidgetUpdateScheduler.forceUpdateWidgets(context)
        println("AlarmReceiver")
        WidgetUpdateScheduler.scheduleNextMinuteUpdate(context)
    }
}




