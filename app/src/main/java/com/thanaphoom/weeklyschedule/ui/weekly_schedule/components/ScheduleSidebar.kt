package com.thanaphoom.weeklyschedule.ui.weekly_schedule.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thanaphoom.weeklyschedule.theme.Caption1RegularStyle
import com.thanaphoom.weeklyschedule.theme.LocalLineTheme
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.ui.theme.BorderColor
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleSidebar(
    modifier: Modifier = Modifier,
    minTime: LocalTime,
    maxTime: LocalTime,
    hourHeight: Dp
) {
    val lineTheme = LocalLineTheme.current

    val numMinutes = ChronoUnit.MINUTES.between(minTime, maxTime).toInt() + 1
    val numHours = numMinutes / 60
    val firstHour = minTime.truncatedTo(ChronoUnit.HOURS)
    val firstHourOffsetMinutes = if (firstHour == minTime) 0 else ChronoUnit.MINUTES.between(minTime, firstHour.plusHours(1))
    val firstHourOffset = hourHeight * (firstHourOffsetMinutes / 60f)
    val startTime = if (firstHour == minTime) firstHour else firstHour.plusHours(1)

    Column(
        modifier = modifier.drawBehind {
            drawLine(
                color = lineTheme.primary,
                start = Offset(size.width, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(numHours) { index ->
            Box(
                modifier = Modifier
                    .then(
                        if (index == 0) Modifier.height(hourHeight - 10.dp) else Modifier.height(hourHeight)
                    )
            ) {
                if (index != 0 && index != numHours) {
                    ClassScheduleSidebarLabel(
                        time = startTime.plusHours(index.toLong())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ClassScheduleSidebarLabel(
    modifier: Modifier = Modifier,
    time: LocalTime
) {
    val textTheme = LocalTextTheme.current
    val hourFormatter = DateTimeFormatter.ofPattern("H:mm")

    Text(
        modifier = modifier.fillMaxHeight(),
        text = time.format(hourFormatter),
        style = Caption1RegularStyle,
        color = textTheme.primary
    )
}
