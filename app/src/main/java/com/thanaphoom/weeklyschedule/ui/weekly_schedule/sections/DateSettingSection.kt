package com.thanaphoom.weeklyschedule.ui.weekly_schedule.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.theme.FootnoteBoldStyle
import com.thanaphoom.weeklyschedule.theme.LocalBackgroundTheme
import com.thanaphoom.weeklyschedule.theme.LocalLineTheme
import com.thanaphoom.weeklyschedule.theme.LocalTintTheme
import com.thanaphoom.weeklyschedule.ui.theme.AppIcon
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DateSettingSection(
    modifier: Modifier = Modifier,
    isShowCalendarDialog: Boolean,
    dayRange: DayRange,
    currentMonth: YearMonth,
    onCalendarClicked: () -> Unit = {},
    onDaySelectionClicked: () -> Unit = {}
) {
    val backgroundTheme = LocalBackgroundTheme.current
    val tintTheme = LocalTintTheme.current
    val lineTheme = LocalLineTheme.current

    val currentMonthText by remember(currentMonth) {
        mutableStateOf(
            buildString {
                append(currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                append(" ")
                append(currentMonth.year)
            }
        )
    }

    Row(modifier) {
        OutlinedButton(
            modifier = Modifier.weight(0.2F),
            border = BorderStroke(1.dp, lineTheme.primary),
            shape = RoundedCornerShape(100.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = tintTheme.primary,
                containerColor = Color.Transparent
            ),
            onClick = onDaySelectionClicked
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = dayRange.rangeName,
                    style = FootnoteBoldStyle
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = AppIcon.ArrowHeadDownIcon),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            modifier = Modifier.weight(0.8F),
            border = BorderStroke(1.dp, lineTheme.primary),
            shape = RoundedCornerShape(100.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = tintTheme.primary,
                containerColor = if (isShowCalendarDialog) backgroundTheme.secondary else Color.Transparent
            ),
            onClick = onCalendarClicked
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.weight(1F),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentMonthText,
                        style = FootnoteBoldStyle
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = AppIcon.CalendarIcon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = AppIcon.ArrowHeadDownIcon),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateSettingSectionPreview() {
    AppTheme {
        DateSettingSection(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            isShowCalendarDialog = false,
            dayRange = DayRange.SEVEN_DAYS,
            currentMonth = YearMonth.now()
        )
    }
}