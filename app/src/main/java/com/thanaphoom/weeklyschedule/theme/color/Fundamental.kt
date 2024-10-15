package com.thanaphoom.weeklyschedule.theme.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val Black25 = Color(0xfff5f5f5)
internal val Black50 = Color(0xffe8e9e9)
internal val Black100 = Color(0xffb8b9ba)
internal val Black200 = Color(0xff969899)
internal val Black300 = Color(0xff66686b)
internal val Black400 = Color(0xff484b4e)
internal val Black500 = Color(0xff1a1e22)
internal val Black600 = Color(0xff181b1f)
internal val Black700 = Color(0xff121518)
internal val Black800 = Color(0xff0e1113)

// Light
internal val LightBackgroundPrimary = Color(0xffffffff)
internal val LightBackgroundSecondary = Color(0xfff8f9fa)
internal val LightBackgroundTertiary = Color(0xfff5f5f5)
internal val LightBackgroundPlaceholder = Color(0xffced4da)

internal val LightLinePrimary = Color(0xffdee2e6)

internal val LightTextPrimary = Color(0xff1a1e22)
internal val LightTextSecondary = Color(0xff868e96)
internal val LightTextPlaceholder = Color(0xffced4da)
internal val LightTextContrast = Color(0xffffffff)

// Dark
internal val DarkBackgroundPrimary = Color(0xFF1C1C1E)
internal val DarkBackgroundSecondary = Color(0xFF2C2C2E)
internal val DarkBackgroundTertiary = Color(0xFF3C3C3E)
internal val DarkBackgroundPlaceholder = Color(0xFF69696F)

internal val DarkLinePrimary = Color(0xFF444447)

internal val DarkTextPrimary = Color(0xFFFFFFFF)
internal val DarkTextSecondary = Color(0xFFD3D3D3)
internal val DarkTextPlaceholder = Color(0xFF9E9EA5)
internal val DarkTextContrast = Color(0xffffffff)

class FundamentalColors {
    val black25: Color = Black25
    val black50: Color = Black50
    val black100: Color = Black100
    val black200: Color = Black200
    val black300: Color = Black300
    val black400: Color = Black400
    val black500: Color = Black500
    val black600: Color = Black600
    val black700: Color = Black700
    val black800: Color = Black800
}

val LocalFundamentalColors = staticCompositionLocalOf { FundamentalColors() }