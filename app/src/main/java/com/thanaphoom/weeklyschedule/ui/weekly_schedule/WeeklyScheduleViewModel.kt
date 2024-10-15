package com.thanaphoom.weeklyschedule.ui.weekly_schedule

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.thanaphoom.weeklyschedule.extension.parseColor
import com.thanaphoom.weeklyschedule.model.CalendarDayRangePill
import com.thanaphoom.weeklyschedule.model.CalendarPage
import com.thanaphoom.weeklyschedule.extension.toYearMonth
import com.thanaphoom.weeklyschedule.model.ScheduleEvent
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.WeeklyScheduleViewModel.Companion.endYearMonth
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.WeeklyScheduleViewModel.Companion.startYearMonth
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.ScheduleSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

class WeeklyScheduleViewModel: ViewModel() {

    companion object {
        val startYearMonth: YearMonth = YearMonth.of(2024,1)
        val endYearMonth: YearMonth = startYearMonth.plusMonths(2)
    }

    private val _uiState = MutableStateFlow(WeeklyScheduleUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        initCalendarPage()
        initCalendar()
        generateScheduleEvents()
    }

    private fun initCalendarPage() {
        val months: MutableList<YearMonth> = mutableListOf()
        var startCalendarMonth = _uiState.value.calendarUiState.startCalendarMonth
        val endCalendarMonth = _uiState.value.calendarUiState.endCalendarMonth

        while (startCalendarMonth <= endCalendarMonth) {
            months.add(startCalendarMonth)
            startCalendarMonth = startCalendarMonth.plusMonths(1)
        }

        _uiState.update {
            it.copy(
                calendarUiState = it.calendarUiState.copy(
                    calendarPage = it.calendarUiState.calendarPage.copy(
                        months = months.toList(),
                        totalPage = months.size,
                        currentPage = 1
                    )
                )
            )
        }
    }

    private fun initCalendar() {
        val startDate = LocalDate.now() //_uiState.value.calendarUiState.startCalendarMonth.atStartOfMonth()
        setCurrentCalendarMonth(startDate.yearMonth)
        setCalendarDayRangePill(CalendarDay(startDate, DayPosition.MonthDate))
    }

    fun onHandleCalendarDialogController() {
        val isShowCalendarDialog = _uiState.value.isShowCalendarDialog

        // When closing the dialog sheet, if the selected date pill does not match the current calendar page,
        // it will roll back to the calendar page where the pill was selected.
        if (isShowCalendarDialog) {
            setShowCalendarDialog(false)

            val startPillDate = _uiState.value.calendarUiState.dayRangePill.startDate
            val currentCalendarMonth = _uiState.value.calendarUiState.currentCalendarMonth
            val isSelectedDateOutsideCalendarCurrentMonth = startPillDate.toYearMonth() != currentCalendarMonth

            if (isSelectedDateOutsideCalendarCurrentMonth) {
                setCurrentCalendarMonth(startPillDate.toYearMonth())
                setCalendarPage(findCalendarPageIndex(startPillDate.toYearMonth()))
            }
        } else {
            setShowCalendarDialog(true)
        }
    }

    private fun generateScheduleEvents() {
        val items = mutableListOf<ScheduleEvent>()
        val date = YearMonth.now().atStartOfMonth()

        (0..28).mapIndexed { index, day ->
            var i = 0
            repeat(Random.nextInt(2, 4)) {
                val startHour = Random.nextInt(6 + i, 18)
                val endHour = Random.nextInt(startHour+1, startHour + Random.nextInt(2, 6))
                val halfHour = Random.nextInt(0, 60)
                val colors = listOf("#FF5630", "#FF9D0D", "#8BC34A", "#467669", "#01B8AA", "#42A5F5", "#1A77F2", "#CA58FF")

                val item = ScheduleEvent(
                    id = UUID.randomUUID().toString(),
                    code = "COURSE $day",
                    name = "Course Name $day",
                    section = "Section $day",
                    location = "null",
                    color = colors[Random.nextInt(colors.size)].parseColor() ?: Color.Gray,
                    start = LocalDateTime.of(date.year, date.month, date.dayOfMonth + index, startHour, 0),
                    end = LocalDateTime.of(date.year, date.month, date.dayOfMonth + index, endHour, halfHour)
                )
                i += 1
                items.add(item)
            }
            date.plusDays((index + 1).toLong())
        }

        _uiState.update {
            it.copy(
                scheduleEvents = items
            )
        }
    }

    fun onHandleSelectionCalendarDateController() {
        val startPill = _uiState.value.calendarUiState.dayRangePill.startPillCalendarDay

        when (startPill.position) {
            DayPosition.InDate -> setCalendarPreviousMonth()
            DayPosition.OutDate -> setCalendarNextMonth()
            DayPosition.MonthDate -> {}
        }
    }

    fun setShowDaySelectionDialog(isShow: Boolean = true) {
        _uiState.update {
            it.copy(isShowDaySelectionDialog = isShow)
        }
    }

    fun setShowCalendarDialog(isShow: Boolean) {
        _uiState.update {
            it.copy(isShowCalendarDialog = isShow)
        }
    }

    fun setDayRange(dayRange: DayRange) {
        _uiState.update {
            it.copy(
                calendarUiState = it.calendarUiState.copy(
                    dayRange = dayRange
                )
            )
        }

        setCalendarDayRangePill(_uiState.value.calendarUiState.dayRangePill.startPillCalendarDay)
    }

    fun setCalendarNextMonth() {
        val nextMonth = _uiState.value.calendarUiState.currentCalendarMonth.nextMonth
        val nextPageIndex = _uiState.value.calendarUiState.calendarPage.currentPage.plus(1)

        setCurrentCalendarMonth(nextMonth)
        setCalendarPage(nextPageIndex)
    }

    fun setCalendarPreviousMonth() {
        val previousMonth = _uiState.value.calendarUiState.currentCalendarMonth.previousMonth
        val previousPageIndex = _uiState.value.calendarUiState.calendarPage.currentPage.minus(1)

        setCurrentCalendarMonth(previousMonth)
        setCalendarPage(previousPageIndex)
    }

    fun setCalendarDayRangePill(startPill: CalendarDay) {
        _uiState.update {
            it.copy(
                calendarUiState = it.calendarUiState.copy(
                    dayRangePill = CalendarDayRangePill(
                        startPillCalendarDay = startPill,
                        dayRange = _uiState.value.calendarUiState.dayRange,
                        startDate = startPill.date,
                        endDate = startPill.date.plusDays(getNumDays())
                    )
                )
            )
        }

        syncScheduleUiState()
    }


    private fun setCalendarPage(page: Int) {
        _uiState.update {
            it.copy(
                calendarUiState = it.calendarUiState.copy(
                    calendarPage = it.calendarUiState.calendarPage.copy(
                        currentPage = page
                    )
                )
            )
        }
    }

    private fun setCurrentCalendarMonth(yearMonth: YearMonth) {
        _uiState.update {
            it.copy(
                calendarUiState = it.calendarUiState.copy(
                    currentCalendarMonth = yearMonth
                )
            )
        }
    }

    private fun syncScheduleUiState() {
        val range: Int = _uiState.value.calendarUiState.dayRange.range
        val startDate: LocalDate = _uiState.value.calendarUiState.dayRangePill.startDate
        val endDate: LocalDate = _uiState.value.calendarUiState.dayRangePill.endDate
        val startTime: LocalTime = _uiState.value.scheduleUiState.startTime
        val endTime: LocalTime = _uiState.value.scheduleUiState.endTime
        val numDays: Int = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
        val numMinutes: Int = ChronoUnit.MINUTES.between(startTime, endTime).toInt() + 1
        val numHours: Float = numMinutes.toFloat() / 60F

        _uiState.update {
            it.copy(
                scheduleUiState = it.scheduleUiState.copy(
                    daySize = ScheduleSize.FixedCount(range),
                    startDate = startDate,
                    endDate = endDate,
                    numDays = numDays,
                    numMinutes = numMinutes,
                    numHours = numHours
                )
            )
        }
    }

    private fun findCalendarPageIndex(month: YearMonth): Int {
        val calendarMonths = _uiState.value.calendarUiState.calendarPage.months
        return calendarMonths.indexOf(month) + 1
    }

    private fun getNumDays(): Long =
        when (_uiState.value.calendarUiState.dayRange) {
            DayRange.ONE_DAYS -> 0L
            DayRange.THREE_DAYS -> 2L
            DayRange.SEVEN_DAYS -> 6L
        }
}

data class WeeklyScheduleUiState(
    val isShowCalendarDialog: Boolean = false,
    val isShowDaySelectionDialog: Boolean = false,
    val scheduleEvents: List<ScheduleEvent> = listOf(),
    val calendarUiState: CalendarUiState = CalendarUiState(),
    val scheduleUiState: ScheduleUiState = ScheduleUiState()
)

data class CalendarUiState(
    val dayRange: DayRange = DayRange.SEVEN_DAYS,
    // launchedEffect will trigger this when currentCalendarMonth changed
    val currentCalendarMonth: YearMonth = YearMonth.now(),
    val startCalendarMonth: YearMonth = YearMonth.now(),
    val endCalendarMonth: YearMonth = YearMonth.now(),
    // calendar day header.
    val dayOfWeek: List<DayOfWeek> = daysOfWeek(),
    val calendarPage: CalendarPage = CalendarPage(),
    // color DOT on calendarDay
    val currentDate: LocalDate = LocalDate.now(),
    val dayRangePill: CalendarDayRangePill = CalendarDayRangePill()
)

data class ScheduleUiState(
    val daySize: ScheduleSize = ScheduleSize.FixedCount(DayRange.SEVEN_DAYS.range),
    val hourSize: ScheduleSize = ScheduleSize.FixedSize(60.dp),
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.MIN,
    val endTime: LocalTime = LocalTime.MAX,
    val numDays: Int = 0,
    val numMinutes: Int = 0,
    val numHours: Float = 0.0F
)

data class UiEvent(
    val onBackPressed: () -> Unit = {},
    val setShowDaySelectionDialog: (Boolean) -> Unit = {},
    val setShowCalendarDialog: (Boolean) -> Unit = {},
    val setDayRange: (DayRange) -> Unit = {},
    val setCalendarNextMonth: () -> Unit = {},
    val setCalendarPreviousMonth: () -> Unit = {},
    val setDayRangePill: (CalendarDay) -> Unit = {},
    val onHandleCalendarDialogController: () -> Unit = {},
    val onHandleSelectionCalendarDateController: () -> Unit = {}
)