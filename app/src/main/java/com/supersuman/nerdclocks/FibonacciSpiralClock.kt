package com.supersuman.nerdclocks

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class FibonacciSpiralClock : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) updateAppWidget(context, appWidgetManager, appWidgetId)
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.fibonacci_spiral_clock)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}