package com.faltenreich.diaguard.measurement.value.tint

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme

enum class MeasurementValueTint {
    NONE,
    LOW,
    NORMAL,
    HIGH,
    ;

    @Composable
    fun getColor(): Color {
        return when (this) {
            NONE -> AppTheme.colors.scheme.onPrimary
            LOW -> AppTheme.colors.ValueLow
            NORMAL -> AppTheme.colors.ValueNormal
            HIGH -> AppTheme.colors.ValueHigh
        }
    }
}