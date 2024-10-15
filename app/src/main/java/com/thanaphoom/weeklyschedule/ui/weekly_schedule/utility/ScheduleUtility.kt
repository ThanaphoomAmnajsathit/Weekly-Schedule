package com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.thanaphoom.weeklyschedule.model.PositionedEvent
import com.thanaphoom.weeklyschedule.model.ScheduleEvent
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class SplitType private constructor(val value: Int) {
    companion object {
        val None = SplitType(0)
        val Start = SplitType(1)
        val End = SplitType(2)
        val Both = SplitType(3)
    }
}

enum class DayRange(val rangeName: String, val dateFormat: String, val range: Int) {
    ONE_DAYS(rangeName = "1D", dateFormat = "EEE dd", range = 1),
    THREE_DAYS(rangeName = "3D", dateFormat = "EEE dd", range = 3),
    SEVEN_DAYS(rangeName = "7D", dateFormat = "dd", range = 7)
}

sealed class ScheduleSize {
    class FixedSize(val size: Dp) : ScheduleSize()
    class FixedCount(val count: Float) : ScheduleSize() {
        constructor(count: Int) : this(count.toFloat())
    }
    class Adaptive(val minSize: Dp) : ScheduleSize()
}

private class EventDataModifier(val positionedEvent: PositionedEvent, ) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = positionedEvent
}

fun Modifier.eventData(positionedEvent: PositionedEvent) = this.then(EventDataModifier(positionedEvent))

fun PositionedEvent.overlapsWith(other: PositionedEvent): Boolean {
    return date == other.date && start < other.end && end > other.start
}

fun List<PositionedEvent>.timesOverlapWith(event: PositionedEvent): Boolean {
    return any { it.overlapsWith(event) }
}

fun splitEvents(events: List<ScheduleEvent>): List<PositionedEvent> {
    return events.map { event ->
            val startDate = event.start.toLocalDate()
            val endDate = event.end.toLocalDate()
            if (startDate == endDate) {
                listOf(PositionedEvent(event, SplitType.None, event.start.toLocalDate(), event.start.toLocalTime(), event.end.toLocalTime()))
            } else {
                val days = ChronoUnit.DAYS.between(startDate, endDate)
                val splitEvents = mutableListOf<PositionedEvent>()
                for (i in 0..days) {
                    val date = startDate.plusDays(i)
                    splitEvents += PositionedEvent(
                        event,
                        splitType = if (date == startDate) SplitType.End else if (date == endDate) SplitType.Start else SplitType.Both,
                        date = date,
                        start = if (date == startDate) event.start.toLocalTime() else LocalTime.MIN,
                        end = if (date == endDate) event.end.toLocalTime() else LocalTime.MAX,
                    )
                }
                splitEvents
            }
        }.flatten()
}