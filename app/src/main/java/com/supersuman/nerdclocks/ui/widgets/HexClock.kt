package com.supersuman.nerdclocks.ui.widgets

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.supersuman.nerdclocks.R
import com.supersuman.nerdclocks.WidgetUpdateScheduler
import com.supersuman.nerdclocks.ui.screens.getShowTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HexClockReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HexClock()
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetUpdateScheduler.scheduleNextMinuteUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetUpdateScheduler.cancelIfNoWidgets(context)
    }
}

class HexClock : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val hexTime = String.format(Locale.getDefault(), "0x%02X:0x%02X", hour, minute)
        val epochSeconds = System.currentTimeMillis() / 1000
        val epochHex = String.format(Locale.getDefault(), "0x%X", epochSeconds)
        val now = calendar.time
        val timeText = SimpleDateFormat("hh:mm", Locale.getDefault()).format(now)
        val showTime = getShowTime(context)

        provideContent {
            Column(
                modifier = GlanceModifier.padding(12.dp)
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.rounded_bg)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HEX TIME",
                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp)
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = hexTime,
                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 18.sp)
                )
                Spacer(modifier = GlanceModifier.height(12.dp))
                Text(
                    text = "EPOCH HEX",
                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp)
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = epochHex,
                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp)
                )
                if (showTime) {
                    Spacer(modifier = GlanceModifier.height(8.dp))
                    Row {
                        Text(timeText, style = TextStyle(color = ColorProvider(Color.White)))
                    }
                }
            }
        }
    }
}
