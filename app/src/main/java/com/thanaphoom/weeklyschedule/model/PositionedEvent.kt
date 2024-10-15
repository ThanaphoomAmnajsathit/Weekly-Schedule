package com.thanaphoom.weeklyschedule.model

import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.SplitType
import java.time.LocalDate
import java.time.LocalTime

class PositionedEvent(
    val event: ScheduleEvent,
    val splitType: SplitType,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime,
    val col: Int = 0,
    val colSpan: Int = 1,
    val colTotal: Int = 1,
)