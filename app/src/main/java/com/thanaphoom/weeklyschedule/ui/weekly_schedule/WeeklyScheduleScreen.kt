package com.thanaphoom.weeklyschedule.ui.weekly_schedule

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.thanaphoom.weeklyschedule.R
import com.thanaphoom.weeklyschedule.mock.generateEvents
import com.thanaphoom.weeklyschedule.mock.getMockCalendarUiState
import com.thanaphoom.weeklyschedule.mock.getMockScheduleUiState
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.theme.LocalBackgroundTheme
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.theme.HeadlineBoldStyle
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.dialog.DaySelectionSheetDialog
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.sections.CalendarSection
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.sections.DateSettingSection
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.sections.ScheduleSection
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeeklyScheduleRoot(
    modifier: Modifier = Modifier,
    viewModel: WeeklyScheduleViewModel = koinViewModel(),
    uiEvent: UiEvent
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeeklyScheduleScreen(
        modifier = modifier,
        uiState = uiState,
        uiEvent = uiEvent.copy(
            setDayRange = viewModel::setDayRange,
            setDayRangePill = viewModel::setCalendarDayRangePill,
            setCalendarNextMonth = viewModel::setCalendarNextMonth,
            setCalendarPreviousMonth = viewModel::setCalendarPreviousMonth,
            setShowCalendarDialog = viewModel::setShowCalendarDialog,
            setShowDaySelectionDialog = viewModel::setShowDaySelectionDialog,
            onHandleCalendarDialogController = viewModel::onHandleCalendarDialogController,
            onHandleSelectionCalendarDateController = viewModel::onHandleSelectionCalendarDateController
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyScheduleScreen(
    modifier: Modifier = Modifier,
    uiState: WeeklyScheduleUiState,
    uiEvent: UiEvent
) {
    val backgroundTheme = LocalBackgroundTheme.current
    val textTheme = LocalTextTheme.current

    val coroutineScope = rememberCoroutineScope()
    val daySelectionSheetState = rememberModalBottomSheetState()

    val calendarState = rememberCalendarState(
        startMonth = uiState.calendarUiState.startCalendarMonth,
        endMonth = uiState.calendarUiState.endCalendarMonth,
        firstVisibleMonth = uiState.calendarUiState.startCalendarMonth,
        firstDayOfWeek = uiState.calendarUiState.dayOfWeek.first(),
    )

    BackHandler {
        uiEvent.onBackPressed()
    }

    LaunchedEffect(key1 = uiState.calendarUiState.currentCalendarMonth) {
        coroutineScope.launch {
            calendarState.animateScrollToMonth(uiState.calendarUiState.currentCalendarMonth)
        }
    }

    Surface(modifier = modifier) {
        Scaffold(
            containerColor = backgroundTheme.primary,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = backgroundTheme.primary),
                    title = {
                        Text(
                            text = stringResource(R.string.weekly_schedule_title),
                            style = HeadlineBoldStyle,
                            color = textTheme.primary,
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DateSettingSection(
                    modifier = Modifier
                        .background(backgroundTheme.primary)
                        .padding(horizontal = 16.dp),
                    isShowCalendarDialog = false,
                    dayRange = uiState.calendarUiState.dayRange,
                    currentMonth = uiState.calendarUiState.currentCalendarMonth,
                    onCalendarClicked = {
                        uiEvent.onHandleCalendarDialogController()
                    },
                    onDaySelectionClicked = {
                        if (uiState.isShowCalendarDialog) {
                            uiEvent.setShowCalendarDialog(false)
                            uiEvent.setShowDaySelectionDialog(true)
                        } else {
                            uiEvent.setShowDaySelectionDialog(true)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Box {
                    ScheduleSection(
                        scheduleEvents = uiState.scheduleEvents,
                        scheduleUiState = uiState.scheduleUiState,
                        calendarUiState = uiState.calendarUiState,
                        onEventClicked = {
                            //todo
                        }
                    )

                    this@Column.AnimatedVisibility(
                        visible = uiState.isShowCalendarDialog,
                        enter = fadeIn(), exit = fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Black.copy(0.4F),
                            onClick = {
                                uiEvent.onHandleCalendarDialogController()
                            }
                        ) {}
                    }

                    this@Column.AnimatedVisibility(
                        visible = uiState.isShowCalendarDialog,
                        enter = expandVertically(), exit = shrinkVertically()
                    ) {
                        CalendarSection(
                            calendarState = calendarState,
                            calendarUiState = uiState.calendarUiState,
                            onNextMonthClicked = {
                                uiEvent.setCalendarNextMonth()
                            },
                            onPreviousMonthClicked = {
                                uiEvent.setCalendarPreviousMonth()
                            },
                            onSelectDateClicked = { startPillCalendarDay ->
                                uiEvent.setDayRangePill(startPillCalendarDay)
                                uiEvent.onHandleSelectionCalendarDateController()
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.isShowDaySelectionDialog) {
        DaySelectionSheetDialog(
            sheetState = daySelectionSheetState,
            onSelectedDayRange = { dayRange ->
                coroutineScope.launch {
                    daySelectionSheetState.hide()
                    uiEvent.setDayRange(dayRange)
                    uiEvent.setShowDaySelectionDialog(false)
                }
            },
            onDismissRequest = {
                coroutineScope.launch {
                    daySelectionSheetState.hide()
                    uiEvent.setShowDaySelectionDialog(false)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleSectionPreview() {
    val weeklyUiState = WeeklyScheduleUiState(
        calendarUiState = getMockCalendarUiState(),
        scheduleUiState = getMockScheduleUiState(DayRange.THREE_DAYS),
        scheduleEvents = generateEvents(DayRange.THREE_DAYS)
    )

    AppTheme {
        WeeklyScheduleScreen(
            uiState = weeklyUiState,
            uiEvent = UiEvent(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleSectionWithCalendarDialogPreview() {
    val weeklyUiState = WeeklyScheduleUiState(
        isShowCalendarDialog = true,
        calendarUiState = getMockCalendarUiState(),
        scheduleUiState = getMockScheduleUiState(DayRange.THREE_DAYS),
        scheduleEvents = generateEvents(DayRange.THREE_DAYS)
    )

    AppTheme {
        WeeklyScheduleScreen(
            uiState = weeklyUiState,
            uiEvent = UiEvent(),
        )
    }
}