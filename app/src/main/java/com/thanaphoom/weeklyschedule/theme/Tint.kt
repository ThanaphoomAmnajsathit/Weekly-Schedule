package com.thanaphoom.weeklyschedule.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class TintTheme(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val placeholder: Color = Color.Unspecified
)

val LocalTintTheme = staticCompositionLocalOf { TintTheme() }