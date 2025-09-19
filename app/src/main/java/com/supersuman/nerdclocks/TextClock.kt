package com.supersuman.nerdclocks

import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.unit.ColorProvider
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class TextClockReceiverr : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = TextClockk()
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("setting alarm")
        context?.let { enableWidget(it) }
        cancelAlarmManager(context)
        setAlarmManager(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        context?.let { disableWidget(it) }
        println("cancelling alarm")
        cancelAlarmManager(context)
    }
}

class TextClockk : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val black = TextStyle(color = ColorProvider(R.color.grey))
        val white = TextStyle(color = ColorProvider(R.color.white))

        fun getTextStyle(time: String, text: String): TextStyle {
            if(time.contains(text)) return white
            return black
        }

        provideContent {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)

            val hourText = getHourText(hour, minute)
            val minText = getMinuteText(minute)

            Column(GlanceModifier.padding(12.dp)
                .background(ImageProvider(R.drawable.rounded_bg))) {
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("IT", style = white)
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("IS", style = white)
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("HALF", style = getTextStyle(minText, "HALF"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("TEN", style = getTextStyle(minText, "TEN"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("QUARTER", style = getTextStyle(minText, "QUARTER"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("TWENTY", style = getTextStyle(minText, "TWENTY"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("FIVE", style = getTextStyle(minText, "FIVE"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("MINUTES", style = getTextStyle(minText, "MINUTES"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("TO", style = getTextStyle(minText, "TO"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("PAST", style = getTextStyle(minText, "PAST"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("TWO", style = getTextStyle(hourText, "TWO"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("THREE", style = getTextStyle(hourText, "THREE"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("ONE", style = getTextStyle(hourText, "ONE"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("FOUR", style = getTextStyle(hourText, "FOUR"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("FIVE", style = getTextStyle(hourText, "FIVE"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("SIX", style = getTextStyle(hourText, "SIX"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("SEVEN", style = getTextStyle(hourText, "SEVEN"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("EIGHT", style = getTextStyle(hourText, "EIGHT"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("NINE", style = getTextStyle(hourText, "NINE"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("TEN", style = getTextStyle(hourText, "TEN"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("ELEVEN", style = getTextStyle(hourText, "ELEVEN"))
                }
                Row(GlanceModifier.fillMaxWidth()) {
                    Text("TWELVE", style = getTextStyle(hourText, "TWELVE"))
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text("O'CLOCK", style = getTextStyle(minText, "O'CLOCK"))
                }
            }
        }
    }
}

private fun getMinuteText(minute: Int) : String {
    if(minute >=0 && minute <=4) return "O'CLOCK"
    if(minute >= 5 && minute <= 9) return "FIVE MINUTES PAST"
    if(minute >= 10 && minute <= 14) return "TEN MINUTES PAST"
    if(minute >= 15 && minute <= 19) return "QUARTER PAST"
    if(minute >= 20 && minute <= 24) return "TWENTY MINUTES PAST"
    if(minute >= 25 && minute <= 29) return "TWENTY FIVE MINUTES PAST"
    if(minute >= 30 && minute <= 34) return "HALF PAST"
    if(minute >= 35 && minute <= 39) return "TWENTY FIVE MINUTES TO"
    if(minute >= 40 && minute <= 44) return "TWENTY MINUTES TO"
    if(minute >= 45 && minute <= 49) return "QUARTER TO"
    if(minute >= 50 && minute <= 54) return "TEN MINUTES TO"
    if(minute >= 55 && minute <= 59) return "FIVE MINUTES TO"
    return ""
}

private fun getHourText(hour: Int, min: Int) : String {
    var newHour = if(min <= 34) hour else hour+1
    if(newHour > 12) newHour = 1
    
    if(newHour == 1) return "ONE"
    if(newHour == 2) return "TWO"
    if(newHour == 3) return "THREE"
    if(newHour == 4) return "FOUR"
    if(newHour == 5) return "FIVE"
    if(newHour == 6) return "SIX"
    if(newHour == 7) return "SEVEN"
    if(newHour == 8) return "EIGHT"
    if(newHour == 9) return "NINE"
    if(newHour == 10) return "TEN"
    if(newHour == 11) return "ELEVEN"
    if(newHour == 12) return "TWELVE"
    return ""
}
