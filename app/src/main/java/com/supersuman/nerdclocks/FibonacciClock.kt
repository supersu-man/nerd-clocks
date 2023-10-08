package com.supersuman.nerdclocks

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.BitmapImageProvider
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
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
import com.supersuman.nerdclocks.ui.theme.my_blue
import com.supersuman.nerdclocks.ui.theme.my_green
import com.supersuman.nerdclocks.ui.theme.my_red
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.random.Random


class FibonacciClockReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FibonacciClock()
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("setting alarm")
        cancelAlarmManager(context)
        setAlarmManager(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("cancelling alarm")
        cancelAlarmManager(context)
    }
}

class FibonacciClock : GlanceAppWidget() {


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sdf = SimpleDateFormat("hh:mm")
        val currentDate = sdf.format(Date())
        println("updating widget at $currentDate")

        val colors = mutableMapOf<Number, Color>(
            1.1 to Color.LightGray,
            1.2 to Color.LightGray,
            2 to Color.LightGray,
            3 to Color.LightGray,
            5 to Color.LightGray
        )
        val hour = Calendar.getInstance().get(Calendar.HOUR)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val hourList = partitions(hour)[Random.nextInt(0, partitions(hour).size)]
        val minuteList = partitions(minute/5)[Random.nextInt(0, partitions(minute/5).size)]
        colors.keys.forEach {
            if(hourList.contains(it) && minuteList.contains(it)) {
                colors[it] = my_blue
            } else if (hourList.contains(it)) {
                colors[it] = my_red
            } else if (minuteList.contains(it)) {
                colors[it] = my_green
            }
        }
        provideContent {
            Widget(colors, currentDate)
        }
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
    fun Widget(colors: Map<Number, Color>, currentDate: String) {
        Row (modifier = GlanceModifier.fillMaxSize().padding(bottom = 20.dp)){
            Column (modifier = GlanceModifier.fillMaxHeight().defaultWeight()) {
                Row (modifier = GlanceModifier.fillMaxWidth().defaultWeight()) {
                    Column (modifier = GlanceModifier.cornerRadiusCompat(15, colors[2].hashCode()).fillMaxHeight().defaultWeight()) {}
                    Column (modifier = GlanceModifier.fillMaxHeight().defaultWeight()) {
                        Row (modifier = GlanceModifier.cornerRadiusCompat(15, colors[1.1].hashCode()).fillMaxWidth().defaultWeight()) {}
                        Row (modifier = GlanceModifier.cornerRadiusCompat(15, colors[1.2].hashCode()).fillMaxWidth().defaultWeight()) {}
                    }
                }
                Row (modifier = GlanceModifier.cornerRadiusCompat(15, colors[3].hashCode()).fillMaxWidth().defaultWeight()){}
            }
            Column (modifier = GlanceModifier.cornerRadiusCompat(15, colors[5].hashCode()).fillMaxHeight().defaultWeight()){}
        }
        Row (modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.Vertical.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(currentDate, style = TextStyle(color = ColorProvider(Color.White)))
        }
    }

    private fun GlanceModifier.cornerRadiusCompat(
        cornerRadius: Int,
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) backgroundAlpha: Float = 1f,
    ): GlanceModifier {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.background(Color(color).copy(alpha = backgroundAlpha))
                .cornerRadius(cornerRadius.dp)
        } else {
            val radii = FloatArray(8) { cornerRadius.toFloat() }
            val shape = ShapeDrawable(RoundRectShape(radii, null, null))
            shape.paint.color = ColorUtils.setAlphaComponent(color, (255 * backgroundAlpha).toInt())
            val bitmap = shape.toBitmap(width = 150, height = 75)
            this.background(BitmapImageProvider(bitmap))
        }
    }

}