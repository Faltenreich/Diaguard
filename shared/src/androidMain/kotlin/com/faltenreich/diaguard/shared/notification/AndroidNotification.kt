package com.faltenreich.diaguard.shared.notification

import androidx.annotation.DrawableRes

data class AndroidNotification(
    val id: Int,
    val title: String,
    val message: String?,
    @get:DrawableRes
    val iconRes: Int,
    val channelId: String,
    val isSoundEnabled: Boolean,
    val isVibrationEnabled: Boolean,
)