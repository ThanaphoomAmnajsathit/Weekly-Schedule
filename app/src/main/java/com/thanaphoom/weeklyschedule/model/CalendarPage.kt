package com.thanaphoom.weeklyschedule.model

import java.time.YearMonth

data class CalendarPage(
    val months: List<YearMonth> = listOf(),
    var totalPage: Int = 0,
    var currentPage: Int = 0
)