package com.supersuman.nerdclocks.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.supersuman.nerdclocks.ui.theme.AppTheme

@Composable
fun Background (callback: @Composable () -> Unit) {
    AppTheme {
        Surface {
            callback()
        }
    }
}