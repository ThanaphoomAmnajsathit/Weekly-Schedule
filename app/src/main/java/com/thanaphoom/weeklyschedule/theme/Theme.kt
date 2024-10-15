package com.thanaphoom.weeklyschedule.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import com.thanaphoom.weeklyschedule.theme.color.Black50
import com.thanaphoom.weeklyschedule.theme.color.Black500
import com.thanaphoom.weeklyschedule.theme.color.Black800
import com.thanaphoom.weeklyschedule.theme.color.DarkBackgroundPlaceholder
import com.thanaphoom.weeklyschedule.theme.color.DarkBackgroundPrimary
import com.thanaphoom.weeklyschedule.theme.color.DarkBackgroundSecondary
import com.thanaphoom.weeklyschedule.theme.color.DarkBackgroundTertiary
import com.thanaphoom.weeklyschedule.theme.color.DarkLinePrimary
import com.thanaphoom.weeklyschedule.theme.color.DarkTextContrast
import com.thanaphoom.weeklyschedule.theme.color.DarkTextPlaceholder
import com.thanaphoom.weeklyschedule.theme.color.DarkTextPrimary
import com.thanaphoom.weeklyschedule.theme.color.DarkTextSecondary
import com.thanaphoom.weeklyschedule.theme.color.LightBackgroundPlaceholder
import com.thanaphoom.weeklyschedule.theme.color.LightBackgroundPrimary
import com.thanaphoom.weeklyschedule.theme.color.LightBackgroundSecondary
import com.thanaphoom.weeklyschedule.theme.color.LightBackgroundTertiary
import com.thanaphoom.weeklyschedule.theme.color.LightLinePrimary
import com.thanaphoom.weeklyschedule.theme.color.LightTextContrast
import com.thanaphoom.weeklyschedule.theme.color.LightTextPlaceholder
import com.thanaphoom.weeklyschedule.theme.color.LightTextPrimary
import com.thanaphoom.weeklyschedule.theme.color.LightTextSecondary

val LightColorScheme = lightColorScheme(
    primary = Color.Red,
    onPrimary = Color.White,
    primaryContainer = Color.Red,
    onPrimaryContainer = Color.Red,
    secondary = Color.Unspecified,
    onSecondary = Color.Unspecified,
    secondaryContainer = Color.Unspecified,
    onSecondaryContainer = Color.Unspecified,
    tertiary = Color.Unspecified,
    onTertiary = Color.Unspecified,
    tertiaryContainer = Color.Unspecified,
    onTertiaryContainer = Color.Unspecified,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red,
    background = Color.White,
    onBackground = Black500,
    surface = Color.White,
    onSurface = Black500,
    surfaceVariant = Color.Unspecified,
    inverseSurface = Color.Unspecified,
    inverseOnSurface = Color.Unspecified,
    outline = LightLinePrimary,
    outlineVariant = LightLinePrimary,
)

val DarkColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.White,
    secondary = Color.White,
    onSecondary = Color.White,
    secondaryContainer = Color.White,
    onSecondaryContainer = Color.White,
    tertiary = Color.White,
    onTertiary = Color.White,
    tertiaryContainer = Color.White,
    onTertiaryContainer = Color.White,
    error = Color.White,
    onError = Color.White,
    errorContainer = Color.White,
    onErrorContainer = Color.White,
    background = Color.White,
    onBackground = Color.White,
    surface = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color.White,
    inverseSurface = Color.White,
    inverseOnSurface = Color.White,
    outline = Color.White,
    outlineVariant = Color.White,
)

private val LightBackgroundTheme = BackgroundTheme(
    primary = LightBackgroundPrimary,
    secondary = LightBackgroundSecondary,
    tertiary = LightBackgroundTertiary,
    placeholder = LightBackgroundPlaceholder
)

private val DarkBackgroundTheme = BackgroundTheme(
    primary = DarkBackgroundPrimary,
    secondary = DarkBackgroundSecondary,
    tertiary = DarkBackgroundTertiary,
    placeholder = DarkBackgroundPlaceholder
)

private val LightTextTheme = TextTheme(
    primary = LightTextPrimary,
    secondary = LightTextSecondary,
    placeholder = LightTextPlaceholder,
    contrast = LightTextContrast
)

private val DarkTextTheme = TextTheme(
    primary = DarkTextPrimary,
    secondary = DarkTextSecondary,
    placeholder = DarkTextPlaceholder,
    contrast = DarkTextContrast
)

private val LightLineTheme = LineTheme(
    primary = LightLinePrimary
)

private val DarkLineTheme = LineTheme(
    primary = DarkLinePrimary
)

private val LightTintTheme = TintTheme(
    primary = Black800,
    secondary = LightTextSecondary,
    placeholder = LightTextPlaceholder
)

private val DarkTintTheme = TintTheme(
    primary = Black50,
    secondary = DarkTextSecondary,
    placeholder = DarkTextPlaceholder
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme by remember(isDarkTheme) {
        mutableStateOf(if (isDarkTheme) DarkColorScheme else LightColorScheme)
    }

    val backgroundTheme by remember(isDarkTheme) {
        mutableStateOf(if (isDarkTheme) DarkBackgroundTheme else LightBackgroundTheme)
    }

    val textTheme by remember(isDarkTheme) {
        mutableStateOf(if (isDarkTheme) DarkTextTheme else LightTextTheme)
    }

    val lineTheme by remember(isDarkTheme) {
        mutableStateOf(if (isDarkTheme) DarkLineTheme else LightLineTheme)
    }

    val tintTheme by remember(isDarkTheme) {
        mutableStateOf(if (isDarkTheme) DarkTintTheme else LightTintTheme)
    }

    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme,
        LocalTextTheme provides textTheme,
        LocalLineTheme provides lineTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

