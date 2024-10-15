package com.thanaphoom.weeklyschedule.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.noRippleClickable(onClick: () -> Unit) = this.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = onClick
        )