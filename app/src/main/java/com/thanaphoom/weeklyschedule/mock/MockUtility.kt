package com.thanaphoom.weeklyschedule.mock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.thanaphoom.weeklyschedule.extension.parseColor
import com.thanaphoom.weeklyschedule.model.CalendarDayRangePill
import com.thanaphoom.weeklyschedule.model.CalendarPage
import com.thanaphoom.weeklyschedule.model.ScheduleEvent
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.CalendarUiState
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.ScheduleUiState
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.ScheduleSize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

fun getMockCalendarUiState(): CalendarUiState {
    return CalendarUiState(
        dayRange = DayRange.THREE_DAYS,
        currentCalendarMonth = YearMonth.now(),
        startCalendarMonth = YearMonth.now(),
        endCalendarMonth = YearMonth.now().plusMonths(2),
        dayOfWeek = daysOfWeek(),
        currentDate = LocalDate.now(),
        calendarPage = CalendarPage(),
        dayRangePill = CalendarDayRangePill(
            startPillCalendarDay = CalendarDay(LocalDate.now(), DayPosition.InDate),
            dayRange = DayRange.THREE_DAYS,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(3)
        )
    )
}

fun getMockScheduleUiState(dayRange: DayRange): ScheduleUiState {
    return ScheduleUiState(
        daySize = ScheduleSize.FixedCount(dayRange.range),
        hourSize = ScheduleSize.FixedSize(60.dp),
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusDays(6),
        startTime = LocalTime.MIN,
        endTime = LocalTime.MAX,
        numDays = 7,
        numMinutes = ChronoUnit.MINUTES.between(LocalTime.MIN, LocalTime.MAX).toInt() + 1,
        numHours = (ChronoUnit.MINUTES.between(LocalTime.MIN, LocalTime.MAX).toInt() + 1).toFloat() / 60F
    )
}

fun generateEvents(dayRange: DayRange): List<ScheduleEvent> {
    val items = mutableListOf<ScheduleEvent>()

    (LocalDate.now().dayOfMonth .. LocalDate.now().dayOfMonth.plus(3)).mapIndexed { index, day ->
        var i = 0
        repeat(Random.nextInt(2, 4)) {
            val startHour = Random.nextInt(6 + i, 18)
            val endHour = Random.nextInt(startHour+1, startHour + Random.nextInt(2, 6))
            val halfHour = Random.nextInt(0, 60)
            val colors = listOf("#FF5630", "#FF9D0D", "#8BC34A", "#467669", "#01B8AA", "#42A5F5", "#1A77F2", "#CA58FF")

            val item = ScheduleEvent(
                id = UUID.randomUUID().toString(),
                code = "TEST_$day",
                name = "Weekly Schedule $day",
                section = "Section $day",
                location = "Lorem ipsum dolor sit amet consectetur.",
                color = colors[Random.nextInt(colors.size)].parseColor() ?: Color.Gray,
                start = LocalDateTime.of(2024, YearMonth.now().month, day, startHour, 0),
                end = LocalDateTime.of(2024, YearMonth.now().month, day, endHour, halfHour)
            )
            i += 1
            items.add(item)
        }
    }

    return items
}