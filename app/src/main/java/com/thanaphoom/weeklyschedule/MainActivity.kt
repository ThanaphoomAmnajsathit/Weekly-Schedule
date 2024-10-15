package com.thanaphoom.weeklyschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.ui.theme.WeeklyScheduleTheme
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.UiEvent
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.WeeklyScheduleRoot
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.WeeklyScheduleScreen

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                WeeklyScheduleRoot(
                    modifier = Modifier.systemBarsPadding(),
                    uiEvent = UiEvent(
                        onBackPressed = ::finish
                    )
                )
            }
        }
    }
}