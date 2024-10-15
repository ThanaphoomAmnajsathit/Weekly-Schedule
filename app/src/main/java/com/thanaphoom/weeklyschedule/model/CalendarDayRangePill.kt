package com.thanaphoom.weeklyschedule.model

import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import java.time.LocalDate

data class CalendarDayRangePill(
    var startPillCalendarDay: CalendarDay = CalendarDay(LocalDate.now(), DayPosition.MonthDate),
    val dayRange: DayRange = DayRange.ONE_DAYS,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now()
)
