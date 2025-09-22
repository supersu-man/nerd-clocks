package com.supersuman.nerdclocks

import android.app.AlarmManager
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.supersuman.nerdclocks.ui.components.Background
import com.supersuman.nerdclocks.ui.screens.Home
import com.supersuman.nerdclocks.ui.components.TopBar
import com.supersuman.nerdclocks.ui.screens.BinaryClockInfo
import com.supersuman.nerdclocks.ui.screens.FibonacciClockInfo

class MainActivity : ComponentActivity() {
    private val pages = listOf("Home", "Binary", "Fibonacci")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val pagerState = rememberPagerState(pageCount = { pages.size })

            Background {
                Scaffold(topBar = { TopBar(pagerState, pages, rememberCoroutineScope()) },
                    modifier = Modifier.statusBarsPadding()) { paddingValues ->
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 0.dp,
                        modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) { page ->
                        when (page) {
                            0 -> Home()
                            1 -> BinaryClockInfo()
                            2 -> FibonacciClockInfo()
                        }
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if(isPermissionGranted(this)) {
            WidgetUpdateScheduler.scheduleNextMinuteUpdate(this)
        }
    }

}

fun isPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}