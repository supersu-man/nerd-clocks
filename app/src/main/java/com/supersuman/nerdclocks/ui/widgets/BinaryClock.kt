package com.supersuman.nerdclocks.ui.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
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


class BinaryClockReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = BinaryClock()
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetUpdateScheduler.scheduleNextMinuteUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetUpdateScheduler.cancelIfNoWidgets(context)
    }
}

class BinaryClock : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val now = Calendar.getInstance().time
        val timeText = SimpleDateFormat("hh:mm", Locale.getDefault()).format(now)
        val showTime = getShowTime(context)

        provideContent {
            Column(
                modifier = GlanceModifier.padding(5.dp)
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.rounded_bg)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    MyColumn(timeText[0])
                    MyColumn(timeText[1])
                    MyColumn(timeText[3])
                    MyColumn(timeText[4])
                }
                if (showTime) {
                    Row {
                        Text(timeText, style = TextStyle(color = ColorProvider(Color.White)))
                    }
                }
            }
        }
    }

    @Composable
    fun MyColumn(char: Char) {
        Column {
            Row {
                if (char in arrayOf('8', '9')) {
                    Image(ImageProvider(R.drawable.ic_baseline_brightness_1_24), null)
                } else {
                    Image(ImageProvider(R.drawable.ic_outline_brightness_1_24), null)
                }
            }
            Row {
                if (char in arrayOf('4', '5', '6', '7')) {
                    Image(ImageProvider(R.drawable.ic_baseline_brightness_1_24), null)
                } else {
                    Image(ImageProvider(R.drawable.ic_outline_brightness_1_24), null)
                }
            }
            Row {
                if (char in arrayOf('2', '3', '6', '7')) {
                    Image(ImageProvider(R.drawable.ic_baseline_brightness_1_24), null)
                } else {
                    Image(ImageProvider(R.drawable.ic_outline_brightness_1_24), null)
                }
            }
            Row {
                if (char in arrayOf('1', '3', '5', '7', '9')) {
                    Image(ImageProvider(R.drawable.ic_baseline_brightness_1_24), null)
                } else {
                    Image(ImageProvider(R.drawable.ic_outline_brightness_1_24), null)
                }
            }
        }
    }

}