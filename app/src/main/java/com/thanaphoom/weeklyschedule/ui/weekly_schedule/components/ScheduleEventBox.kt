package com.thanaphoom.weeklyschedule.ui.weekly_schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thanaphoom.weeklyschedule.extension.parseColor
import com.thanaphoom.weeklyschedule.model.PositionedEvent
import com.thanaphoom.weeklyschedule.theme.AppTheme
import com.thanaphoom.weeklyschedule.theme.Caption3BoldStyle
import com.thanaphoom.weeklyschedule.theme.Caption3RegularStyle
import com.thanaphoom.weeklyschedule.theme.LocalTextTheme
import com.thanaphoom.weeklyschedule.ui.theme.AppIcon
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.DayRange
import com.thanaphoom.weeklyschedule.ui.weekly_schedule.utility.SplitType
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleEventCard(
    modifier: Modifier = Modifier,
    positionedEvent: PositionedEvent,
    dayRange: DayRange,
    onClicked: () -> Unit
) {
    val event = positionedEvent.event
    val diffMin = ChronoUnit.MINUTES.between(positionedEvent.start, positionedEvent.end).toInt()
    val eventTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val timePeriodFormatted = "${event.start.format(eventTimeFormatter)} - ${event.end.format(eventTimeFormatter)}"

    if (dayRange == DayRange.SEVEN_DAYS) {
        XSEventCard(
            modifier = modifier,
            splitType = positionedEvent.splitType,
            eventColor = event.color,
            onClicked = onClicked
        )
    } else {
        when {
            diffMin in (5..59) -> {
                XSEventCard(
                    modifier = modifier,
                    splitType = positionedEvent.splitType,
                    eventColor = event.color,
                    onClicked = onClicked
                )
            }
            diffMin in (60..89) -> {
                SEventCard(
                    modifier = modifier,
                    splitType = positionedEvent.splitType,
                    eventColor = event.color,
                    timePeriod = timePeriodFormatted,
                    code = event.code,
                    section = event.section,
                    onClicked = onClicked
                )
            }
            diffMin in (90..119) -> {
                MEventCard(
                    modifier = modifier,
                    splitType = positionedEvent.splitType,
                    eventColor = event.color,
                    timePeriod = timePeriodFormatted,
                    code = event.code,
                    name = event.name,
                    section = event.section,
                    onClicked = onClicked
                )
            }
            diffMin in (120..179) -> {
                LEventCard(
                    modifier = modifier,
                    splitType = positionedEvent.splitType,
                    eventColor = event.color,
                    timePeriod = timePeriodFormatted,
                    code = event.code,
                    name = event.name,
                    section = event.section,
                    location = event.location,
                    onClicked = onClicked
                )
            }
            diffMin >= 180 -> {
                XLEventCard(
                    modifier = modifier,
                    splitType = positionedEvent.splitType,
                    eventColor = event.color,
                    timePeriod = timePeriodFormatted,
                    code = event.code,
                    name = event.name,
                    section = event.section,
                    location = event.location,
                    onClicked = onClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCardColumnScope(
    modifier: Modifier = Modifier,
    splitType: SplitType,
    eventColor: Color,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val topRadius = if (splitType == SplitType.Start || splitType == SplitType.Both) 0.dp else 4.dp
    val bottomRadius = if (splitType == SplitType.End || splitType == SplitType.Both) 0.dp else 4.dp

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(
                end = 2.dp,
                bottom = if (splitType == SplitType.End) 0.dp else 2.dp
            )
            .clipToBounds(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = eventColor),
        shape = RoundedCornerShape(
            topStart = topRadius,
            topEnd = topRadius,
            bottomEnd = bottomRadius,
            bottomStart = bottomRadius,
        ),
        onClick = { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

@Composable
private fun XSEventCard(
    modifier: Modifier = Modifier,
    splitType: SplitType,
    eventColor: Color,
    onClicked: () -> Unit = {}
) {
    EventCardColumnScope(
        modifier = modifier,
        splitType = splitType,
        eventColor = eventColor,
        onClick = onClicked
    ) {
        Box {}
    }
}

@Composable
private fun SEventCard(
    modifier: Modifier = Modifier,
    splitType: SplitType,
    eventColor: Color,
    timePeriod: String,
    code: String,
    section: String,
    onClicked: () -> Unit = {}
) {
    val textTheme = LocalTextTheme.current

    EventCardColumnScope(
        modifier = modifier,
        splitType = splitType,
        eventColor = eventColor,
        onClick = onClicked
    ) {
        Text(
            text = timePeriod,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = code,
            style = Caption3BoldStyle,
            color = textTheme.contrast
        )
        Text(
            text = section,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
    }
}

@Composable
private fun MEventCard(
    modifier: Modifier = Modifier,
    splitType: SplitType,
    eventColor: Color,
    timePeriod: String,
    code: String,
    name: String,
    section: String,
    onClicked: () -> Unit = {}
) {
    val textTheme = LocalTextTheme.current

    EventCardColumnScope(
        modifier = modifier,
        splitType = splitType,
        eventColor = eventColor,
        onClick = onClicked
    ) {
        Text(
            text = timePeriod,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = code,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Text(
            text = name,
            style = Caption3RegularStyle,
            overflow = TextOverflow.Ellipsis,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = section,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
    }
}

@Composable
private fun LEventCard(
    modifier: Modifier = Modifier,
    splitType: SplitType,
    eventColor: Color,
    timePeriod: String,
    code: String,
    name: String,
    section: String,
    location: String,
    onClicked: () -> Unit = {}
) {
    val textTheme = LocalTextTheme.current

    EventCardColumnScope(
        modifier = modifier,
        splitType = splitType,
        eventColor = eventColor,
        onClick = onClicked
    ) {
        Text(
            text = timePeriod,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = code,
            style = Caption3BoldStyle,
            color = textTheme.contrast
        )
        Text(
            text = name,
            style = Caption3RegularStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = section,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = AppIcon.CalendarIcon),
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = location,
                style = Caption3RegularStyle,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}

@Composable
private fun XLEventCard(
    modifier: Modifier = Modifier,
    splitType: SplitType,
    eventColor: Color,
    timePeriod: String,
    code: String,
    name: String,
    section: String,
    location: String,
    onClicked: () -> Unit = {}
) {
    val textTheme = LocalTextTheme.current

    EventCardColumnScope(
        modifier = modifier,
        splitType = splitType,
        eventColor = eventColor,
        onClick = onClicked
    ) {
        Text(
            text = timePeriod,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = code,
            style = Caption3BoldStyle,
            color = textTheme.contrast
        )
        Text(
            text = name,
            style = Caption3RegularStyle,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = section,
            style = Caption3RegularStyle,
            color = textTheme.contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = AppIcon.CalendarIcon),
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = location,
                style = Caption3RegularStyle,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = textTheme.contrast
            )
        }
    }
}

@Preview
@Composable
private fun XSEventCardPreview() {
    AppTheme {
        XSEventCard(
            modifier = Modifier.size(110.dp, 32.dp),
            splitType = SplitType.None,
            eventColor = "#FF5630".parseColor() ?: Color.Gray
        )
    }
}

@Preview
@Composable
private fun SEventCardPreview() {
    AppTheme {
        SEventCard(
            modifier = Modifier.size(110.dp, 80.dp),
            splitType = SplitType.None,
            eventColor = "#FF9D0D".parseColor() ?: Color.Gray,
            timePeriod = "09:00 - 12:00",
            code = "TMCD515",
            section = "1"
        )
    }
}

@Preview
@Composable
private fun MEventCardPreview() {
    AppTheme {
        MEventCard(
            modifier = Modifier.size(110.dp, 100.dp),
            splitType = SplitType.None,
            eventColor = "#8BC34A".parseColor() ?: Color.Gray,
            timePeriod = "09:00 - 12:00",
            code = "TMCD515",
            name = "Clinical Pharmacokinetics I",
            section = "1"
        )
    }
}

@Preview
@Composable
private fun LEventCardPreview() {
    AppTheme {
        LEventCard(
            modifier = Modifier.size(110.dp, 140.dp),
            splitType = SplitType.None,
            eventColor = "#467669".parseColor() ?: Color.Gray,
            timePeriod = "09:00 - 12:00",
            code = "TMCD515",
            name = "Clinical Pharmacokinetics I",
            section = "1",
            location = "Room Chamlong Harinasuta Building"
        )
    }
}

@Preview
@Composable
private fun XLEventCardPreview() {
    AppTheme {
        XLEventCard(
            modifier = Modifier.size(110.dp, 180.dp),
            splitType = SplitType.None,
            eventColor = "#01B8AA".parseColor() ?: Color.Gray,
            timePeriod = "09:00 - 12:00",
            code = "TMCD515",
            name = "Clinical Pharmacokinetics I",
            section = "1",
            location = "Room Chamlong Harinasuta Building"
        )
    }
}