package com.faltenreich.diaguard.shared.view

import android.app.Activity
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

actual fun Modifier.keyboardPadding(): Modifier {
    return imePadding()
}

@Composable
actual fun SetIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean) {
    val view = LocalView.current
    val activity = view.context as Activity
    val windowInsetsController = WindowCompat.getInsetsController(activity.window, view)
    windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
}