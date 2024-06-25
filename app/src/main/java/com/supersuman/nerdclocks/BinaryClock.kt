package com.supersuman.nerdclocks

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
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
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
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
import com.supersuman.nerdclocks.ui.theme.almost_black
import java.text.SimpleDateFormat
import java.util.Date


class BinaryClockReceiverr : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = BinaryClockk()
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

class BinaryClockk : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sdf = SimpleDateFormat("hh:mm")
        val currentDate = sdf.format(Date())
        println("updating widget at $currentDate")
        val showTime = isShowTime(context)
        provideContent {
            Column(
                modifier = GlanceModifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .cornerRadiusCompat(15, almost_black.hashCode()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    MyColumn(currentDate, 0)
                    MyColumn(currentDate, 1)
                    MyColumn(currentDate, 3)
                    MyColumn(currentDate, 4)
                }
                if (showTime) {
                    Row {
                        Text(currentDate, style = TextStyle(color = ColorProvider(Color.White)))
                    }
                }
            }
        }
    }

    @Composable
    private fun MyColumn(currentDate: String, col: Int) {
        Column {
            Row {
                Column(modifier = GlanceModifier.then(
                    if (currentDate[col] in arrayOf('8', '9')) GlanceModifier.background(ImageProvider(R.drawable.ic_baseline_brightness_1_24))
                    else GlanceModifier.background(ImageProvider(R.drawable.ic_outline_brightness_1_24))
                )) {}
            }
            Row {
                Column(modifier = GlanceModifier.then(
                    if (currentDate[col] in arrayOf('4', '5', '6', '7')) GlanceModifier.background(ImageProvider(R.drawable.ic_baseline_brightness_1_24))
                    else GlanceModifier.background(ImageProvider(R.drawable.ic_outline_brightness_1_24))
                )) {}
            }
            Row {
                Column(modifier = GlanceModifier.then(
                    if (currentDate[col] in arrayOf('2', '3', '6', '7')) GlanceModifier.background(ImageProvider(R.drawable.ic_baseline_brightness_1_24))
                    else GlanceModifier.background(ImageProvider(R.drawable.ic_outline_brightness_1_24))
                )) {}
            }
            Row {
                Column(modifier = GlanceModifier.then(
                    if (currentDate[col] in arrayOf('1', '3', '5', '7', '9')) GlanceModifier.background(ImageProvider(R.drawable.ic_baseline_brightness_1_24))
                    else GlanceModifier.background(ImageProvider(R.drawable.ic_outline_brightness_1_24))
                )) {}
            }
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