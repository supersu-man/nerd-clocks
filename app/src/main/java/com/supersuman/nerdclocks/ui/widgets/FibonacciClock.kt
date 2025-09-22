package com.supersuman.nerdclocks.ui.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
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
import kotlin.collections.mutableMapOf
import kotlin.random.Random


class FibonacciClockReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FibonacciClock()
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetUpdateScheduler.scheduleNextMinuteUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetUpdateScheduler.cancelIfNoWidgets(context)
    }
}

class FibonacciClock : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val now = Calendar.getInstance().time
        var timeText: String? = SimpleDateFormat("hh:mm", Locale.getDefault()).format(now)
        val showTime = getShowTime(context)
        if(!showTime) timeText = null
        val colorMap = getColorsMap()
        provideContent {
            FibonacciUi(colorMap, timeText)
        }
    }

    fun getColorsMap(): MutableMap<Number, ImageProvider> {
        val hour = Calendar.getInstance().get(Calendar.HOUR)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val hourList = partitions(hour)[Random.nextInt(0, partitions(hour).size)]
        val minuteList = partitions(minute/5)[Random.nextInt(0, partitions(minute/5).size)]
        val colors = mutableMapOf<Number, ImageProvider>(
            1.1 to ImageProvider(R.drawable.gray),
            1.2 to ImageProvider(R.drawable.gray),
            2 to ImageProvider(R.drawable.gray),
            3 to ImageProvider(R.drawable.gray),
            5 to ImageProvider(R.drawable.gray)
        )
        colors.keys.forEach {
            if(hourList.contains(it) && minuteList.contains(it)) {
                colors[it] = ImageProvider(R.drawable.blue)
            } else if (hourList.contains(it)) {
                colors[it] = ImageProvider(R.drawable.red)
            } else if (minuteList.contains(it)) {
                colors[it] = ImageProvider(R.drawable.green)
            }
        }
        return colors
    }

    private fun partitions(integer: Int): List<List<Number>>{
        return when(integer) {
            12 -> listOf(listOf(5,3,2,1.1,1.2))
            11 -> listOf(listOf(5,3,2,1.1), listOf(5,3,2,1.2))
            10 -> listOf(listOf(5,3,2), listOf(5,3,1.1,1.2))
            9 -> listOf(listOf(5,3,1.1), listOf(5,3,1.2), listOf(5,2,1.1,1.2))
            8 -> listOf(listOf(5,3), listOf(5,2,1.1), listOf(5,2,1.2))
            7 -> listOf(listOf(5,2), listOf(5,1.1,1.2), listOf(3,2,1.1,1.2))
            6 -> listOf(listOf(5,1.1), listOf(5,1.2), listOf(3,2,1.1), listOf(3,2,1.2))
            5 -> listOf(listOf(5), listOf(3,2), listOf(3, 1.1, 1.2))
            4 -> listOf(listOf(3, 1.1), listOf(3, 1.2), listOf(2, 1.1, 1.2))
            3 -> listOf(listOf(3), listOf(2, 1.1), listOf(2, 1.2))
            2 -> listOf(listOf(2), listOf(1.1, 1.2))
            1 -> listOf(listOf(1.1), listOf(1.2))
            else -> listOf()
        }
    }

    @Composable
    fun FibonacciUi(colors: MutableMap<Number, ImageProvider>, currentDate: String?) {
        Row (modifier = GlanceModifier.fillMaxSize().padding(bottom = 20.dp)) {
            Column (modifier = GlanceModifier.fillMaxHeight().defaultWeight()) {
                Row (modifier = GlanceModifier.fillMaxWidth().defaultWeight()) {
                    Column (modifier = GlanceModifier.background(colors[2] as ImageProvider).fillMaxHeight().defaultWeight()) {}
                    Column (modifier = GlanceModifier.fillMaxHeight().defaultWeight()) {
                        Row (modifier = GlanceModifier.background(colors[1.1] as ImageProvider).fillMaxWidth().defaultWeight()) {}
                        Row (modifier = GlanceModifier.background(colors[1.2] as ImageProvider).fillMaxWidth().defaultWeight()) {}
                    }
                }
                Row (modifier = GlanceModifier.background(colors[3] as ImageProvider).fillMaxWidth().defaultWeight()){}
            }
            Column (modifier = GlanceModifier.background(colors[5] as ImageProvider).fillMaxHeight().defaultWeight()){}
        }
        if(currentDate != null) {
            Row (modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.Vertical.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(currentDate, style = TextStyle(color = ColorProvider(Color.White)))
            }
        }
    }

}