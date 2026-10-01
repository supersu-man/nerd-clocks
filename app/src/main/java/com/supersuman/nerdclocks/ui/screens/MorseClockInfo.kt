package com.supersuman.nerdclocks.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supersuman.nerdclocks.ui.widgets.getMorseForDigit

@Composable
fun MorseClockInfo() {
    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Morse Code Clock", fontSize = 30.sp)
        Spacer(Modifier.height(20.dp))

        Text("How Morse Code Time Works", fontSize = 24.sp)
        Spacer(Modifier.height(10.dp))
        Text("Morse code is a telecommunication method used to encode text characters as standardized sequences of two signal durations, called dots (•) and dashes (—).")
        Spacer(Modifier.height(8.dp))
        Text("In the Morse Code Clock, each digit of the current time (HH:MM) is translated into its corresponding International Morse Code representation using dots and dashes:")
        
        Spacer(Modifier.height(15.dp))
        Text("Digit Reference:", fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))
        for (i in 0..9) {
            val digit = i.toString()[0]
            val morse = getMorseForDigit(digit)
            Text("• $digit : $morse")
        }

        Spacer(Modifier.height(20.dp))
        Text("Example", fontSize = 24.sp)
        Spacer(Modifier.height(10.dp))
        Text("If the time is 12:34:")
        Text("• 1 -> • — — — —")
        Text("• 2 -> • • — — —")
        Text("• 3 -> • • • — —")
        Text("• 4 -> • • • • —")
        Spacer(Modifier.height(10.dp))
        Text("A brilliant fusion of amateur radio history and modern telemetry on your home screen!")
    }
}
