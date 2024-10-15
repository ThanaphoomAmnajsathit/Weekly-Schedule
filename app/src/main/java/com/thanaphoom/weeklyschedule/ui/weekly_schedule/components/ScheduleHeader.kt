package com.thanaphoom.weeklyschedule.ui.weekly_schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.thanaphoom.weeklyschedule.extension.toYearMonth
import com.thanaphoom.weeklyschedule.theme.Caption1BoldStyle
import com.thanaphoom.weeklyschedule.theme.Caption1RegularStyle
import com.thanaphoom.weeklyschedule.theme.LocalLineTheme
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.theme.color.LocalGeneralColors
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun ScheduleHeader(
    modifier: Modifier = Modifier,
    currentCalendarMonth: YearMonth,
    endCalendarMonth: YearMonth,
    startDate: LocalDate,
    endDate: LocalDate,
    dayWidth: Dp,
    dayRange: DayRange
) {
    val textTheme = LocalTextTheme.current
    val lineTheme = LocalLineTheme.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val numDays = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

        for (dayIndex in 0..numDays) {
            Box(
                modifier = Modifier
                    .width(dayWidth)
                    .fillMaxHeight()
                    .drawBehind {
                        drawLine(
                            color = lineTheme.primary,
                            start = Offset(0F, 0F),
                            end = Offset(0F, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
            ) {
                val currentDayIndex = startDate.plusDays(dayIndex.toLong())
                val isEndOfCalendarMonth = currentDayIndex.toYearMonth() > endCalendarMonth

                if (!isEndOfCalendarMonth) {
                    val isDayOutOfMonth = currentDayIndex.toYearMonth() > currentCalendarMonth
                    val textColor = if (isDayOutOfMonth) textTheme.placeholder else textTheme.primary

                    Day(
                        modifier = Modifier.align(Alignment.Center),
                        isToday = LocalDate.now() == currentDayIndex,
                        day = currentDayIndex,
                        dayRange = dayRange,
                        textColor = textColor
                    )
                } else {
                    Box(Modifier.fillMaxSize()) {}
                }
            }
        }
    }
}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    isToday: Boolean = false,
    day: LocalDate,
    dayRange: DayRange,
    textColor: Color
) {
    val dayFormatter = DateTimeFormatter.ofPattern(dayRange.dateFormat).withLocale(Locale.ENGLISH)

    if (isToday) {
        val textTheme = LocalTextTheme.current
        val generalColors = LocalGeneralColors.current

        Box(
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
                .background(
                    color = generalColors.blue500,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .align(Alignment.Center),
                text = day.format(dayFormatter),
                style = Caption1BoldStyle,
                color = textTheme.contrast,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            text = day.format(dayFormatter),
            style = Caption1RegularStyle,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}