package com.faltenreich.diaguard.shared.view

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

class AndroidWindowController : WindowController {

    @Composable
    override fun setIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean) {
        val view = LocalView.current
        val activity = view.context as Activity
        val windowInsetsController = WindowCompat.getInsetsController(
            activity.window,
            activity.window.decorView,
        )
        windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
    }
}