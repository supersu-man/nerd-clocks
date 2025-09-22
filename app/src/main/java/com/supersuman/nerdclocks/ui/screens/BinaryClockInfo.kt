package com.supersuman.nerdclocks.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supersuman.nerdclocks.R

@Composable
fun BinaryClockInfo() {
    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {

        Spacer(modifier = Modifier.height(20.dp))
        Text("Binary Clock", fontSize = 30.sp)
        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Column(modifier = Modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Dot(false)
                Dot(false)
                Dot(false)
                Dot(true)
                Text(text = "1")
            }
            Column(modifier = Modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Dot(false)
                Dot(false)
                Dot(true)
                Dot(false)
                Text(text = "2")
            }
            Column(modifier = Modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Dot(false)
                Dot(true)
                Dot(false)
                Dot(true)
                Text(text = "4+1")
            }
            Column(modifier = Modifier.width(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Dot(true)
                Dot(false)
                Dot(false)
                Dot(true)
                Text(text = "8+1")
            }
            Column {
                Text(text = "8")
                Text(text = "4")
                Text(text = "2")
                Text(text = "1")
                Text(text = "")
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("Time = 12:59", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(10.dp))

        Text("How to Read the Binary Clock", fontSize = 24.sp)

        Spacer(Modifier.height(10.dp))

        Text("Each row represents a value based on powers of 2:")
        Text("Bottom row -> 1 (2⁰)")
        Text("Next row -> 2 (2¹)")
        Text("Next row -> 4 (2²)")
        Text("Top row -> 8 (2³)")

        Spacer(Modifier.height(10.dp))

        Text("To find the value of a column, just add up the lit circles in that column.")
        Spacer(Modifier.height(10.dp))
        Text("Example (above):")
        Text("Column 1 = 1")
        Text("Column 2 = 2")
        Text("Column 3 = 5 (1 + 4)")
        Text("Column 4 = 9 (8 + 1)")
        Text("Put the columns together, and you get the current time.")
    }
}

@Composable
fun Dot(filled: Boolean) {
    Image(
        painter = painterResource(
            if (filled) R.drawable.ic_baseline_brightness_1_24
            else R.drawable.ic_outline_brightness_1_24
        ),
        contentDescription = null
    )
}
