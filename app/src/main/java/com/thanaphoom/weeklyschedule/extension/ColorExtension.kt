package com.thanaphoom.weeklyschedule.extension

import androidx.compose.ui.graphics.Color

fun String.parseColor(): Color? =
    try {
        Color(android.graphics.Color.parseColor(this))
    } catch (exception: Exception) {
        Color(android.graphics.Color.parseColor("#$this"))
    } catch (exception: Exception) {
        null
    }