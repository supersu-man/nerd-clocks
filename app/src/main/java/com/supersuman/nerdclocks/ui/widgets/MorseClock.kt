package com.supersuman.nerdclocks.ui.widgets

import android.content.Context
import androidx.compose.runtime.Composable
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

class MorseClockReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MorseClock()
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetUpdateScheduler.scheduleNextMinuteUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetUpdateScheduler.cancelIfNoWidgets(context)
    }
}

class MorseClock : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val calendar = Calendar.getInstance()
        val now = calendar.time
        val timeText = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
        val showTime = getShowTime(context)

        val h1 = timeText[0]
        val h2 = timeText[1]
        val m1 = timeText[3]
        val m2 = timeText[4]

        provideContent {
            Column(
                modifier = GlanceModifier.padding(12.dp)
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.rounded_bg)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MORSE CLOCK",
                    style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp)
                )
                Spacer(modifier = GlanceModifier.height(6.dp))
                // Hours
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MorseDigitColumn(h1)
                    Spacer(modifier = GlanceModifier.padding(6.dp))
                    MorseDigitColumn(h2)
                }
                Spacer(modifier = GlanceModifier.height(6.dp))
                // Minutes
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MorseDigitColumn(m1)
                    Spacer(modifier = GlanceModifier.padding(6.dp))
                    MorseDigitColumn(m2)
                }
                if (showTime) {
                    Spacer(modifier = GlanceModifier.height(6.dp))
                    Row {
                        Text(timeText, style = TextStyle(color = ColorProvider(Color.White)))
                    }
                }
            }
        }
    }

    @Composable
    fun MorseDigitColumn(digit: Char) {
        val morse = getMorseForDigit(digit)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = morse,
                style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp)
            )
        }
    }
}

fun getMorseForDigit(digit: Char): String {
    return when (digit) {
        '0' -> "— — — — —"
        '1' -> "• — — — —"
        '2' -> "• • — — —"
        '3' -> "• • • — —"
        '4' -> "• • • • —"
        '5' -> "• • • • •"
        '6' -> "— • • • •"
        '7' -> "— — • • •"
        '8' -> "— — — • •"
        '9' -> "— — — — •"
        else -> ""
    }
}
