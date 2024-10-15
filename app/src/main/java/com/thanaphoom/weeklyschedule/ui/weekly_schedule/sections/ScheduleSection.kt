package com.thanaphoom.weeklyschedule.ui.weekly_schedule.sections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.thanaphoom.weeklyschedule.mock.generateEvents
import com.thanaphoom.weeklyschedule.mock.getMockCalendarUiState
import com.thanaphoom.weeklyschedule.mock.getMockScheduleUiState
import com.thanaphoom.weeklyschedule.model.PositionedEvent
import com.thanaphoom.weeklyschedule.model.ScheduleEvent
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.theme.BackgroundTheme
import com.thanaphoom.weeklyschedule.theme.Caption1RegularStyle
import com.thanaphoom.weeklyschedule.theme.LineTheme
import com.thanaphoom.weeklyschedule.theme.LocalBackgroundTheme
import com.thanaphoom.weeklyschedule.theme.LocalLineTheme
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.CalendarUiState
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.ScheduleUiState
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.components.ScheduleEventCard
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.components.ScheduleHeader
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.components.ScheduleSidebar
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.ScheduleSize
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.eventData
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.splitEvents
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleSection(
    modifier: Modifier = Modifier,
    scheduleUiState: ScheduleUiState,
    calendarUiState: CalendarUiState,
    scheduleEvents: List<ScheduleEvent>,
    onEventClicked: (PositionedEvent) -> Unit = {}
) {
    val backgroundTheme = LocalBackgroundTheme.current
    val textTheme = LocalTextTheme.current
    val lineTheme = LocalLineTheme.current

    val hourHeightPx = with(LocalDensity.current) {
        (scheduleUiState.hourSize as ScheduleSize.FixedSize).size.toPx()
    }

    val verticalScrollState = rememberScrollState(((hourHeightPx * (6)).toInt()))
    val horizontalScrollState = rememberScrollState()
    val isEnableHorizontalScroll by remember(scheduleUiState.daySize) {
        mutableStateOf(scheduleUiState.daySize is ScheduleSize.Adaptive)
    }

    var sidebarSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        BoxWithConstraints(modifier = modifier) {
            val dayWidth: Dp = when (scheduleUiState.daySize) {
                is ScheduleSize.FixedSize -> scheduleUiState.daySize.size
                is ScheduleSize.FixedCount -> with(LocalDensity.current) { ((constraints.maxWidth - sidebarSize.width) / scheduleUiState.daySize.count).toDp() }
                is ScheduleSize.Adaptive -> with(LocalDensity.current) {
                    maxOf(((constraints.maxWidth - sidebarSize.width) / scheduleUiState.numDays).toDp(), scheduleUiState.daySize.minSize)
                }
            }
            val hourHeight: Dp = when (scheduleUiState.hourSize) {
                is ScheduleSize.FixedSize -> scheduleUiState.hourSize.size
                is ScheduleSize.FixedCount -> with(LocalDensity.current) { ((constraints.maxHeight - sidebarSize.height) / scheduleUiState.hourSize.count).toDp() }
                is ScheduleSize.Adaptive -> with(LocalDensity.current) {
                    maxOf(((constraints.maxHeight - sidebarSize.height) / scheduleUiState.numHours).toDp(), scheduleUiState.hourSize.minSize)
                }
            }

            Column {
                Row(
                    modifier = Modifier
                        .background(color = backgroundTheme.secondary)
                        .border(width = 1.dp, color = lineTheme.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .drawBehind {
                            drawLine(
                                color = lineTheme.primary,
                                start = Offset(size.width, 0f),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        .onGloballyPositioned {
                            sidebarSize = it.size
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "GMT +7",
                            style = Caption1RegularStyle,
                            color = textTheme.primary
                        )
                    }

                    ScheduleHeader(
                        modifier = Modifier
                            .height(with(LocalDensity.current) { sidebarSize.height.toDp() })
                            .horizontalScroll(
                                state = horizontalScrollState,
                                enabled = isEnableHorizontalScroll
                            ),
                        currentCalendarMonth = calendarUiState.currentCalendarMonth,
                        endCalendarMonth = calendarUiState.endCalendarMonth,
                        dayRange = calendarUiState.dayRange,
                        startDate = scheduleUiState.startDate,
                        endDate = scheduleUiState.endDate,
                        dayWidth = dayWidth
                    )
                }

                Row {
                    ScheduleSidebar(
                        modifier = Modifier
                            .width(with(LocalDensity.current) { sidebarSize.width.toDp() })
                            .verticalScroll(verticalScrollState),
                        hourHeight = hourHeight,
                        minTime = scheduleUiState.startTime,
                        maxTime = scheduleUiState.endTime
                    )

                    ScheduleView(
                        modifier = Modifier
                            .verticalScroll(verticalScrollState)
                            .horizontalScroll(
                                state = horizontalScrollState,
                                enabled = isEnableHorizontalScroll
                            ),
                        scheduleEvents = scheduleEvents,
                        dayRange = calendarUiState.dayRange,
                        startDate = scheduleUiState.startDate,
                        endDate = scheduleUiState.endDate,
                        startTime = scheduleUiState.startTime,
                        endTime = scheduleUiState.endTime,
                        dayWidth = dayWidth,
                        hourHeight = hourHeight,
                        onEventClicked = onEventClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleView(
    modifier: Modifier = Modifier,
    scheduleEvents: List<ScheduleEvent>,
    dayRange: DayRange,
    startDate: LocalDate,
    endDate: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    dayWidth: Dp,
    hourHeight: Dp,
    onEventClicked: (PositionedEvent) -> Unit = {}
) {
    val numDays = (ChronoUnit.DAYS.between(startDate, endDate) + 1).toInt()
    val numMinutes = (ChronoUnit.MINUTES.between(startTime, endTime) + 1).toInt()
    val numHours = numMinutes / 60

    val positionedEvents = remember(scheduleEvents) {
        splitEvents(scheduleEvents.sortedBy(ScheduleEvent::start))
            .filter { it.end > startTime && it.start < endTime }
    }

    Layout(
        modifier = modifier
            .drawScheduleBackground(
                numDays = numDays,
                numHours = numHours,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                minTime = startTime,
                minDate = startDate
            ),
        content = {
            positionedEvents.forEach { positionedEvent ->
                ScheduleEventContent(
                    positionedEvent = positionedEvent,
                    dayRange = dayRange,
                    onEventClicked = onEventClicked
                )
            }
        }
    ) { measureables, constraints ->
        val height = (hourHeight.toPx() * (numMinutes / 60f)).roundToInt()
        val width = (dayWidth.toPx() * numDays).roundToInt()

        val placeableWithEvents = measureables.map { measurable ->
            val splitEvent = measurable.parentData as PositionedEvent
            val eventDurationMinutes = ChronoUnit.MINUTES.between(splitEvent.start, minOf(splitEvent.end, endTime))
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val eventWidth = (dayWidth.toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = eventWidth,
                    maxWidth = eventWidth,
                    minHeight = eventHeight,
                    maxHeight = eventHeight
                )
            )
            Pair(placeable, splitEvent)
        }

        layout(width, height) {
            placeableWithEvents.forEach { (placeable, splitEvent) ->
                val eventOffsetMinutes = if (splitEvent.start > startTime) {
                    ChronoUnit.MINUTES.between(startTime, splitEvent.start)
                } else {
                    0
                }

                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val eventOffsetDays = ChronoUnit.DAYS.between(startDate, splitEvent.date).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX, eventY)
            }
        }
    }
}

@Composable
private fun Modifier.drawScheduleBackground(
    numDays: Int,
    numHours: Int,
    dayWidth: Dp,
    hourHeight: Dp,
    minTime: LocalTime,
    minDate: LocalDate
): Modifier {
    val lineTheme = LocalLineTheme.current
    val backgroundTheme = LocalBackgroundTheme.current

    return this.drawBehind {
        val firstHourOffset = calculateFirstHourOffset(minTime, hourHeight)

        drawDayLines(numDays, dayWidth, minDate, lineTheme, backgroundTheme)
        drawHourLines(numHours, hourHeight, firstHourOffset, lineTheme)
        drawHalfHourLines(numHours, hourHeight, firstHourOffset, lineTheme)
    }
}

private fun DrawScope.calculateFirstHourOffset(minTime: LocalTime, hourHeight: Dp): Float {
    val firstHour = minTime.truncatedTo(ChronoUnit.HOURS)
    val firstHourOffsetMinutes = if (firstHour == minTime) 0 else ChronoUnit.MINUTES.between(minTime, firstHour.plusHours(1))
    return (firstHourOffsetMinutes / 60f) * hourHeight.toPx()
}

private fun DrawScope.drawDayLines(
    numDays: Int,
    dayWidth: Dp,
    minDate: LocalDate,
    lineTheme: LineTheme,
    backgroundTheme: BackgroundTheme
) {
    repeat(numDays) { dayIndex ->
        val dayOffset = dayIndex * dayWidth.toPx()
        val isToday = LocalDate.now() == minDate.plusDays(dayIndex.toLong())

        if (isToday) {
            drawRect(
                color = backgroundTheme.secondary,
                size = Size(dayWidth.toPx(), size.height),
                topLeft = Offset(dayOffset, 0F)
            )
        }
        drawLine(
            color = lineTheme.primary,
            start = Offset(dayOffset, 0f),
            end = Offset(dayOffset, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun DrawScope.drawHourLines(
    numHours: Int,
    hourHeight: Dp,
    firstHourOffset: Float,
    lineTheme: LineTheme
) {
    repeat(numHours) { hourIndex ->
        val hourOffset = hourIndex * hourHeight.toPx() + firstHourOffset
        drawLine(
            color = lineTheme.primary,
            start = Offset(0f, hourOffset),
            end = Offset(size.width + 10F, hourOffset),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun DrawScope.drawHalfHourLines(
    numHours: Int,
    hourHeight: Dp,
    firstHourOffset: Float,
    lineTheme: LineTheme
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 2.dp.toPx()), 0f)
    repeat(numHours * 2) { halfHourIndex ->
        if (halfHourIndex % 2 == 0) {
            val halfHourOffset = (halfHourIndex + 1) * (hourHeight / 2).toPx() + firstHourOffset
            drawLine(
                color = lineTheme.primary,
                start = Offset(0f, halfHourOffset),
                end = Offset(size.width, halfHourOffset),
                strokeWidth = 1.dp.toPx(),
                pathEffect = pathEffect
            )
        }
    }
}

@Composable
private fun ScheduleEventContent(
    positionedEvent: PositionedEvent,
    dayRange: DayRange,
    onEventClicked: (PositionedEvent) -> Unit
) {
    Box(
        modifier = Modifier.eventData(positionedEvent)
    ) {
        ScheduleEventCard(
            positionedEvent = positionedEvent,
            dayRange = dayRange,
            onClicked = {
                onEventClicked(positionedEvent)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleSectionOneDayPreview() {
    AppTheme {
        ScheduleSection(
            calendarUiState = getMockCalendarUiState(),
            scheduleUiState = getMockScheduleUiState(DayRange.ONE_DAYS),
            scheduleEvents = generateEvents(DayRange.ONE_DAYS),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleSectionThreeDayPreview() {
    AppTheme {
        ScheduleSection(
            calendarUiState = getMockCalendarUiState(),
            scheduleUiState = getMockScheduleUiState(DayRange.THREE_DAYS),
            scheduleEvents = generateEvents(DayRange.THREE_DAYS),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleSectionSevenDayPreview() {
    AppTheme {
        ScheduleSection(
            calendarUiState = getMockCalendarUiState(),
            scheduleUiState = getMockScheduleUiState(DayRange.SEVEN_DAYS),
            scheduleEvents = generateEvents(DayRange.SEVEN_DAYS)
        )
    }
}