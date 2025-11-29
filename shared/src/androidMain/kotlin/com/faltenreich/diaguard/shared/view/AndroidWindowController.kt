package com.faltenreich.diaguard.shared.view

import android.app.Activity
import androidx.core.view.WindowCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidWindowController(activity: Activity) : WindowController {

    private val windowInsetsController = WindowCompat.getInsetsController(
        activity.window,
        activity.window.decorView,
    )

    override suspend fun setIsAppearanceLightStatusBars(
        isAppearanceLightStatusBars: Boolean,
    ) = withContext(Dispatchers.Main) {
        windowInsetsController.isAppearanceLightStatusBars = isAppearanceLightStatusBars
    }
}