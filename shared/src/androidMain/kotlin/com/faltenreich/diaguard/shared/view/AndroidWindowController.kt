package com.faltenreich.diaguard.shared.view

import android.app.Activity
import androidx.core.view.WindowCompat

class AndroidWindowController(private val activity: Activity) : WindowController {

    override fun setIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean) {
        val windowInsetsController = WindowCompat.getInsetsController(
            activity.window,
            activity.window.decorView,
        )
        windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
    }
}