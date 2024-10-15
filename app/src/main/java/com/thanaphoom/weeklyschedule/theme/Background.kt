package com.thanaphoom.weeklyschedule.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BackgroundTheme(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified,
    val placeholder: Color = Color.Unspecified,
)

val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }