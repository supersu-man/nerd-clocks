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

@Composable
fun HexClockInfo() {
    Column(modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Hexadecimal / Epoch Clock", fontSize = 30.sp)
        Spacer(Modifier.height(20.dp))

        Text("How Hexadecimal Time Works", fontSize = 24.sp)
        Spacer(Modifier.height(10.dp))
        Text("Standard time uses base-10 (decimal). Hexadecimal time represents hours, minutes, and seconds in base-16:")
        Spacer(Modifier.height(8.dp))
        Text("• Hours (0-23) become 0x00 to 0x17")
        Text("• Minutes (0-59) become 0x00 to 0x3B")
        Text("• Seconds (0-59) become 0x00 to 0x3B")
        Spacer(Modifier.height(10.dp))
        Text("Example: 23:45:30 in decimal is 0x17:0x2D:0x1E in hexadecimal.")

        Spacer(Modifier.height(20.dp))
        Text("Unix Epoch Seconds (Hex)", fontSize = 24.sp)
        Spacer(Modifier.height(10.dp))
        Text("The Unix Epoch is the number of seconds that have elapsed since January 1, 1970 (midnight UTC/GMT).")
        Spacer(Modifier.height(8.dp))
        Text("Displaying this timestamp in hexadecimal format (base-16) is a classic hacker/sysadmin way of looking at time, appealing to low-level systems engineers and programmers.")
    }
}
