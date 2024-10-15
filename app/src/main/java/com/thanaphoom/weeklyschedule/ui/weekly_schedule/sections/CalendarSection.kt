package com.thanaphoom.weeklyschedule.ui.weekly_schedule.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.thanaphoom.weeklyschedule.R
import com.thanaphoom.weeklyschedule.model.CalendarDayRangePill
import com.thanaphoom.weeklyschedule.extension.noRippleClickable
import com.thanaphoom.weeklyschedule.extension.toYearMonth
import com.thanaphoom.weeklyschedule.mock.getMockCalendarUiState
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.theme.Caption1BoldStyle
import com.thanaphoom.weeklyschedule.theme.Caption1RegularStyle
import com.thanaphoom.weeklyschedule.theme.LocalBackgroundTheme
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.theme.LocalTintTheme
import com.thanaphoom.weeklyschedule.theme.color.LocalFundamentalColors
import com.thanaphoom.weeklyschedule.theme.color.LocalGeneralColors
import com.thanaphoom.weeklyschedule.ui.theme.AppIcon
import com.thanaphoom.weeklyschedule.ui.theme.BlackV25
import com.thanaphoom.weeklyschedule.ui.theme.PrimaryBackgroundColor
import com.thanaphoom.weeklyschedule.ui.theme.PrimaryFillColor
import com.thanaphoom.weeklyschedule.ui.theme.TextPlaceholderColor
import com.thanaphoom.weeklyschedule.ui.theme.TextPrimaryColor
import com.thanaphoom.weeklyschedule.ui.theme.TextSecondaryColor
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.CalendarUiState
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarSection(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    calendarUiState: CalendarUiState,
    onNextMonthClicked: () -> Unit = {},
    onPreviousMonthClicked: () -> Unit = {},
    onSelectDateClicked: (CalendarDay) -> Unit = {}
) {
    val backgroundTheme = LocalBackgroundTheme.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(backgroundTheme.primary)
    ) {
        Column(modifier = Modifier.padding(horizontal = 0.dp)) {
            HorizontalCalendar(
                state = calendarState,
                userScrollEnabled = false,
                monthHeader = {
                    CalendarDaysOfWeek(
                        modifier = Modifier.padding(bottom = 8.dp),
                        daysOfWeek = calendarUiState.dayOfWeek
                    )
                },
                dayContent = { calendarDayItem ->
                    if (!getHiddenOutOfDate(calendarDayItem, calendarUiState.startCalendarMonth, calendarUiState.endCalendarMonth)) {
                        CalendarDayContent(
                            currentDate = calendarUiState.currentDate,
                            dayRange = calendarUiState.dayRange,
                            dayRangePill = calendarUiState.dayRangePill,
                            calendarDayItem = calendarDayItem,
                            onClick = { onSelectDateClicked(calendarDayItem) },
                        )
                    }
                },
            )

            CalendarPageSection(
                calendarUiState = calendarUiState,
                onNextMonthClicked = { onNextMonthClicked() },
                onPreviousMonthClicked = { onPreviousMonthClicked() }
            )

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun CalendarDaysOfWeek(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek>
) {
    Row(modifier = modifier.fillMaxWidth()) {
        for (day in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                color = TextSecondaryColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CalendarDayContent(
    currentDate: LocalDate,
    dayRange: DayRange,
    dayRangePill: CalendarDayRangePill?,
    calendarDayItem: CalendarDay,
    onClick: (CalendarDay) -> Unit,
) {
    val backgroundTheme = LocalBackgroundTheme.current
    val generalColors = LocalGeneralColors.current
    val tintTheme = LocalTintTheme.current
    val textTheme = LocalTextTheme.current

    val isSelectedCalendarDate by remember {
        mutableStateOf(currentDate == calendarDayItem.date)
    }

    fun Modifier.getSelectedCalendarDateModifier(): Modifier {
        return if (isSelectedCalendarDate) {
            this.background(color = generalColors.blue500, shape = RoundedCornerShape(10.dp))
        } else {
            this.background(color = Color.Transparent)
        }
    }

    fun Modifier.getDayRangePillModifier(): Modifier {
        return if (calendarDayItem.date >= dayRangePill?.startDate && calendarDayItem.date <= dayRangePill?.endDate) {
            this.background(
                color = backgroundTheme.tertiary,
                shape = getDaySelectionCornerShape(
                    dayRange = dayRange,
                    currentDay = calendarDayItem.date,
                    startDatePill = dayRangePill?.startDate,
                    endDatePill = dayRangePill?.endDate
                )
            )
        } else {
            this
        }
    }

    fun getDayTextColor(): Color {
        return when (calendarDayItem.position) {
            DayPosition.InDate, DayPosition.OutDate -> {
                textTheme.placeholder
            }
            else -> {
                textTheme.primary
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .aspectRatio(2f)
            .getDayRangePillModifier()
            .noRippleClickable {
                onClick(calendarDayItem)
            }
    ) {
        Box(
            modifier = Modifier
                .getSelectedCalendarDateModifier()
                .fillMaxSize()
                .width(48.dp)
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelectedCalendarDate) {
                Text(
                    text = calendarDayItem.date.dayOfMonth.toString(),
                    style = Caption1BoldStyle,
                    color = textTheme.contrast
                )
            } else {
                Text(
                    text = calendarDayItem.date.dayOfMonth.toString(),
                    style = Caption1RegularStyle,
                    color = getDayTextColor()
                )
            }
        }
    }
}

@Composable
private fun CalendarPageSection(
    modifier: Modifier = Modifier,
    calendarUiState: CalendarUiState,
    onNextMonthClicked: () -> Unit = {},
    onPreviousMonthClicked: () -> Unit = {}
) {
    val textTheme = LocalTextTheme.current
    val tintTheme = LocalTintTheme.current

    var previousIconColor by remember {
        mutableStateOf(tintTheme.primary)
    }
    var nextIconColor by remember {
        mutableStateOf(tintTheme.primary)
    }

    LaunchedEffect(calendarUiState.currentCalendarMonth) {
        when (calendarUiState.currentCalendarMonth) {
            calendarUiState.startCalendarMonth -> {
                previousIconColor = tintTheme.placeholder
                nextIconColor = tintTheme.primary
            }
            calendarUiState.endCalendarMonth -> {
                nextIconColor = tintTheme.placeholder
                previousIconColor = tintTheme.primary
            }
            else -> {
                previousIconColor = tintTheme.primary
                nextIconColor = tintTheme.primary
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = calendarUiState.currentCalendarMonth.previousMonth >= calendarUiState.startCalendarMonth,
            content = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = AppIcon.ChevronLeftIcon),
                    tint = previousIconColor,
                    contentDescription = null
                )
            },
            onClick = onPreviousMonthClicked
        )

        Row {
            Text(
                text = buildString {
                    append(calendarUiState.calendarPage.currentPage.toString())
                    append("/")
                    append(calendarUiState.calendarPage.totalPage.toString())
                },
                style = Caption1RegularStyle,
                color = textTheme.primary
            )
        }

        IconButton(
            enabled = calendarUiState.currentCalendarMonth.nextMonth <= calendarUiState.endCalendarMonth,
            content = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = AppIcon.ChevronRightIcon),
                    tint = nextIconColor,
                    contentDescription = null
                )
            },
            onClick = onNextMonthClicked
        )
    }
}

private fun getDaySelectionCornerShape(
    dayRange: DayRange,
    currentDay: LocalDate,
    startDatePill: LocalDate?,
    endDatePill: LocalDate?
): RoundedCornerShape {
    return if (dayRange != DayRange.ONE_DAYS) {
        when (currentDay) {
            startDatePill -> {
                RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
            }
            endDatePill -> {
                RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
            }
            else -> {
                RoundedCornerShape(0.dp)
            }
        }
    } else {
        RoundedCornerShape(10.dp)
    }
}

private fun getHiddenOutOfDate(
    day: CalendarDay,
    startMonth: YearMonth,
    endMonth: YearMonth
): Boolean {
    return when {
        day.position == DayPosition.InDate && day.date.toYearMonth() < startMonth -> true
        day.position == DayPosition.OutDate && day.date.toYearMonth() > endMonth -> true
        else -> false
    }
}

@Preview
@Composable
private fun CalendarSectionPreview() {
    val calendarState = rememberCalendarState(
        startMonth = YearMonth.now(),
        endMonth = YearMonth.now().plusMonths(2),
        firstVisibleMonth = YearMonth.now(),
        firstDayOfWeek = daysOfWeek().first(),
    )

    AppTheme {
        CalendarSection(
            calendarState = calendarState,
            calendarUiState = getMockCalendarUiState()
        )
    }
}