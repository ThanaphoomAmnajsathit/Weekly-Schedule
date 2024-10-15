package com.thanaphoom.weeklyschedule.ui.weekly_schedule.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thanaphoom.weeklyschedule.R
import com.thanaphoom.weeklyschedule.extension.noRippleClickable
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.theme.CallOutBoldStyle
import com.thanaphoom.weeklyschedule.theme.FootnoteBoldStyle
import com.thanaphoom.weeklyschedule.theme.LocalBackgroundTheme
import com.thanaphoom.weeklyschedule.theme.LocalLineTheme
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.theme.LocalTintTheme
import com.thanaphoom.weeklyschedule.ui.theme.AppIcon
import com.thanaphoom.weeklyschedule.ui.theme.BorderColor
import com.thanaphoom.weeklyschedule.ui.theme.PrimaryBackgroundColor
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaySelectionSheetDialog(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onSelectedDayRange: (DayRange) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val backgroundTheme = LocalBackgroundTheme.current

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        containerColor = backgroundTheme.primary,
        contentColor = backgroundTheme.primary,
        onDismissRequest = { onDismissRequest() },
        content = {
            DaySelectionContent(
                onSelectedDayRange = onSelectedDayRange
            )
        }
    )
}

@Composable
private fun DaySelectionContent(
    modifier: Modifier = Modifier,
    onSelectedDayRange: (DayRange) -> Unit = {}
) {
    val lineTheme = LocalLineTheme.current
    val textTheme = LocalTextTheme.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.select_day_range_sheet_title),
            style = CallOutBoldStyle,
            color = textTheme.primary
        )
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = lineTheme.primary
        )

        Column(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DaySelectionItem(
                dayRange = DayRange.ONE_DAYS,
                onClick = { onSelectedDayRange(DayRange.ONE_DAYS) }
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = lineTheme.primary
            )

            DaySelectionItem(
                dayRange = DayRange.THREE_DAYS,
                onClick = { onSelectedDayRange(DayRange.THREE_DAYS) }
            )

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = lineTheme.primary
            )

            DaySelectionItem(
                dayRange = DayRange.SEVEN_DAYS,
                onClick = { onSelectedDayRange(DayRange.SEVEN_DAYS) }
            )
        }
    }

    Spacer(Modifier.navigationBarsPadding())
}

@Composable
private fun DaySelectionItem(
    modifier: Modifier = Modifier,
    dayRange: DayRange,
    onClick: () -> Unit
) {
    val textTheme = LocalTextTheme.current
    val tintTheme = LocalTintTheme.current

    val (numDays, icon) = when (dayRange) {
        DayRange.ONE_DAYS -> Pair(
            DayRange.ONE_DAYS.range,
            AppIcon.CalendarOneDayIcon
        )
        DayRange.THREE_DAYS -> Pair(
            DayRange.THREE_DAYS.range,
            AppIcon.CalendarThreeDayIcon
        )
        DayRange.SEVEN_DAYS -> Pair(
            DayRange.SEVEN_DAYS.range,
            AppIcon.CalendarSevenDayIcon
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .noRippleClickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = icon),
            tint = tintTheme.primary,
            contentDescription = null
        )
        Text(
            text = pluralStringResource(R.plurals.days_plural_format, numDays, numDays),
            style = FootnoteBoldStyle,
            color = textTheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DaySelectionContentPreview() {
    AppTheme {
        DaySelectionContent()
    }
}