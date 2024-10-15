package com.thanaphoom.weeklyschedule.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class LineTheme(
    val primary: Color = Color.Unspecified
)

val LocalLineTheme = staticCompositionLocalOf { LineTheme() }