package com.supersuman.nerdclocks

import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import androidx.glance.appwidget.updateAll
import com.supersuman.nerdclocks.ui.theme.AppTheme
import com.supersuman.nerdclocks.ui.theme.my_blue
import com.supersuman.nerdclocks.ui.theme.my_green
import com.supersuman.nerdclocks.ui.theme.my_red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val pages = listOf("Home", "Binary", "Fibonacci")
    private lateinit var alarmManager: AlarmManager
    private lateinit var isPerms: MutableState<Boolean>
    private lateinit var showTime: MutableState<Boolean>

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmManager = getSystemService<AlarmManager>()!!

        setContent {
            AppTheme {
                Surface {
                    val pagerState = rememberPagerState(0, 0F) { 3 }
                    Column {
                        TabLayout(pagerState)
                        PagerLayout(pagerState)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::isPerms.isInitialized)
            isPerms.value = isPermissionGranted()
        updateAndEnableAlarm(CoroutineScope(Dispatchers.Main))
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabLayout(pagerState: PagerState) {
        val scope = rememberCoroutineScope()
        TabRow(selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
                TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]))
        }) {
            pages.forEachIndexed { index, title ->
                Tab(text = { Text(text = title) }, selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PagerLayout(pagerState: PagerState) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> Home()
                1 -> BinaryClockInfo()
                2 -> FibonacciClockInfo()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Home() {
        isPerms = remember { mutableStateOf(isPermissionGranted()) }
        showTime = remember { mutableStateOf(isShowTime(this)) }
        val coroutineScope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row {
                Text("If the widget isn’t updating every minute, consider removing all Nerd Clock widgets from the screen and then adding them back. Alternatively, you can use the ‘Sync time’ button.", textAlign = TextAlign.Center)
            }
            Row {
                Button(onClick = { updateAndEnableAlarm(coroutineScope) }) {
                    Text("Sync time")
                }
            }
            Row {
                Text("Showing and hiding time can take 1-2 minutes to take effect.", textAlign = TextAlign.Center, modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Show time")
                Checkbox(checked = showTime.value, onCheckedChange = {
                    showTime.value = it
                    saveShowTime(it)
                    updateAndEnableAlarm(CoroutineScope(Dispatchers.Main))
                })
            }

            Text("Thanks for using the app")
            if (!isPerms.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Button(onClick = {
                    val intent = Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.setData(Uri.parse("package:$packageName"))
                    startActivity(intent)
                }) {
                    Text("Grant permission")
                }
            }
        }
    }

    private fun updateAndEnableAlarm(coroutineScope: CoroutineScope) = coroutineScope.launch {
        val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val n = sharedPref.getInt("widgets", 0)
        if (n>0 && isPermissionGranted()) {
            cancelAlarmManager(this@MainActivity)
            setAlarmManager(this@MainActivity)
            BinaryClockReceiverr().glanceAppWidget.updateAll(this@MainActivity)
            FibonacciClockReceiverr().glanceAppWidget.updateAll(this@MainActivity)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun saveShowTime(boolean: Boolean) {
        val sharedPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("showTime", boolean).apply()
    }

    @Composable
    fun BinaryClockInfo() {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())) {
            Text("Binary Clock", modifier = Modifier.padding(0.dp, 20.dp), fontSize = 30.sp)
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.Center) {
                    Column(modifier = Modifier
                        .width(40.dp)
                        .padding(3.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_baseline_brightness_1_24), contentDescription = "")
                        Text(text = "1")
                    }
                    Column(modifier = Modifier
                        .width(40.dp)
                        .padding(3.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_baseline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Text(text = "2")
                    }
                    Column(modifier = Modifier
                        .width(40.dp)
                        .padding(3.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_baseline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_baseline_brightness_1_24), contentDescription = "")
                        Text(text = "4+1")
                    }
                    Column(modifier = Modifier
                        .width(40.dp)
                        .padding(3.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.ic_baseline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_outline_brightness_1_24), contentDescription = "")
                        Image(painter = painterResource(id = R.drawable.ic_baseline_brightness_1_24), contentDescription = "")
                        Text(text = "8+1")
                    }
                    Column {
                        Text(text = "8", modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                            .weight(1f, false))
                        Text(text = "4", modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                            .weight(1f, false))
                        Text(text = "2", modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                            .weight(1f, false))
                        Text(text = "1", modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight()
                            .weight(1f, false))
                        Text(text = "", modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f, false))
                    }
                }
            }
            Text("Time = 12:59", modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(), textAlign = TextAlign.Center)
            Text("Each horizontal row has a value. From bottom, first row has 1 (2^0), second has 2 (2^1), third has 4 (2^2), fourth has 8 (2^3).", modifier = Modifier.padding(0.dp, 10.dp))
            Text("To get value of a vertical column, add all the lighted circle values. For the above example: value of first column is 1, value of second column is 2, third column is 5 (both 1 and 4 are lighted), fourth column is 9 (both 8 and 1 are lighted)", modifier = Modifier.padding(0.dp, 10.dp))
        }
    }

    @Composable
    fun FibonacciClockInfo() {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())) {
            Text("Fibonacci Clock", modifier = Modifier.padding(0.dp, 20.dp), fontSize = 30.sp)
            Row (Modifier.height(250.dp)) {
                Column (
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)) {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)){
                        Column(
                            Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(my_green)) {
                            Text(text = "2", textAlign = TextAlign.Center, modifier = Modifier
                                .fillMaxSize()
                                .wrapContentHeight(), fontSize = 24.sp, color = Color.Black)
                        }
                        Column(
                            Modifier
                                .fillMaxHeight()
                                .weight(1f)) {
                            Row(
                                Modifier
                                    .background(Color.LightGray)
                                    .weight(1f)
                                    .fillMaxWidth()) {
                                Text(text = "1", textAlign = TextAlign.Center, modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(), fontSize = 24.sp, color = Color.Black)
                            }
                            Row(
                                Modifier
                                    .background(my_red)
                                    .weight(1f)
                                    .fillMaxWidth()) {
                                Text(text = "1", textAlign = TextAlign.Center, modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(), fontSize = 24.sp, color = Color.Black)
                            }
                        }
                    }
                    Row (
                        Modifier
                            .background(my_blue)
                            .fillMaxWidth()
                            .weight(1f)) {
                        Text(text = "3", textAlign = TextAlign.Center, modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(), fontSize = 24.sp, color = Color.Black)
                    }
                }
                Column (
                    Modifier
                        .background(my_red)
                        .fillMaxHeight()
                        .weight(1f)) {
                    Text(text = "5", textAlign = TextAlign.Center, modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(), fontSize = 24.sp, color = Color.Black)
                }
            }
            Text("The Fibonacci sequence is the sequence beginning 1, 1 and where each number is the sum of the previous two. Its first five digits are: 1, 1, 2, 3, 5", modifier = Modifier.padding(0.dp, 10.dp))
            Text("The squares lit up in red tell you the hour, and the squares lit up in green give you the minutes (in multiples of five). A square lit up in blue means it is to be added for both hour and minute. Grey squares are ignored.", modifier = Modifier.padding(0.dp, 10.dp))
            Text("For the above example: for hours, you have red 5, red 1 and blue 3. 5 + 1 + 3 = 9 o’clock. For minutes: green 2 and blue 3. 2 + 3 = 5. Then 5 x 5 = 25minutes. So, the time is 9.25.", modifier = Modifier.padding(0.dp, 10.dp))
        }
    }
}