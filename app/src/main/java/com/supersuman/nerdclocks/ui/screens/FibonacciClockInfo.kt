package com.supersuman.nerdclocks.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.supersuman.nerdclocks.R


@Composable
fun FibonacciClockInfo() {

    val context = LocalContext.current
    val greenDrawable =  ContextCompat.getDrawable(context, R.drawable.green)
    val redDrawable =  ContextCompat.getDrawable(context, R.drawable.red)
    val blueDrawable =  ContextCompat.getDrawable(context, R.drawable.blue)
    val grayDrawable = ContextCompat.getDrawable(context, R.drawable.gray)


    Column(Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {

        Spacer(modifier = Modifier.height(20.dp))
        Text("Fibonacci Clock", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.height(250.dp)) {
            Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Box(modifier = Modifier.fillMaxHeight().weight(1f).paint(rememberDrawablePainter(drawable = greenDrawable))) {
                        Text("2", modifier = Modifier.align(Alignment.Center), color = Color.Black)
                    }
                    Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
                        Box(modifier = Modifier.fillMaxWidth().weight(1f).paint(rememberDrawablePainter(drawable = grayDrawable))) {
                            Text("1", modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }
                        Box(modifier = Modifier.fillMaxWidth().weight(1f).paint(rememberDrawablePainter(drawable = redDrawable))) {
                            Text("1", modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().weight(1f).paint(rememberDrawablePainter(drawable = blueDrawable))) {
                    Text("3", modifier = Modifier.align(Alignment.Center), color = Color.Black)
                }
            }
            Box(modifier = Modifier.fillMaxHeight().weight(1f).paint(rememberDrawablePainter(drawable = redDrawable))) {
                Text("5", modifier = Modifier.align(Alignment.Center), color = Color.Black)
            }
        }

        Spacer(Modifier.height(10.dp))
        Text("How to Read the Fibonacci Clock", fontSize = 24.sp)
        Spacer(Modifier.height(10.dp))

        Text("The Fibonacci sequence starts with 1 and 1, and each new number is the sum of the two before it. The first five numbers are: 1, 1, 2, 3, 5.")
        Spacer(Modifier.height(10.dp))
        Text("Colors help you read the clock:")
        Text("• Red -> Hours")
        Text("• Green -> Minutes (in 5-minute steps)")
        Text("• Blue -> Counted for both hours and minutes")
        Text("• Gray -> Not used")
        Spacer(Modifier.height(10.dp))
        Text("Example:")
        Text("Hours -> 5 (red) + 1 (red) + 3 (blue) = 9")
        Text("Minutes -> 2 (green) + 3 (blue) = 5 -> 5 × 5 = 25")
        Text("So the time shown is 9:25.")

    }
}