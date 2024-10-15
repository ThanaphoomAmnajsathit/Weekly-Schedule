package com.thanaphoom.weeklyschedule.di

import com.thanaphoom.weeklyschedule.ui.weekly_schedule.WeeklyScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::WeeklyScheduleViewModel)
}