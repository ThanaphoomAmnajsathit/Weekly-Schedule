package com.thanaphoom.weeklyschedule.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class ScheduleEvent(
    val id: String,
    val code: String,
    val name: String,
    val section: String,
    val location: String,
    val color: Color,
    val start: LocalDateTime,
    val end: LocalDateTime
)