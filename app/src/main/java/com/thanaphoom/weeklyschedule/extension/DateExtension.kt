package com.thanaphoom.weeklyschedule.extension

import java.time.LocalDate
import java.time.YearMonth

fun LocalDate.toYearMonth(): YearMonth {
    return YearMonth.of(this.year, this.month)
}