package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable

interface WindowController {

    @Composable
    fun setIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean)
}