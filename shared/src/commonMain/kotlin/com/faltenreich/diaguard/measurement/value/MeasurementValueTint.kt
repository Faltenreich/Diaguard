package com.faltenreich.diaguard.measurement.value

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.value_range_high
import com.faltenreich.diaguard.resource.value_range_low
import com.faltenreich.diaguard.resource.value_range_target
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

enum class MeasurementValueTint {
    NONE,
    LOW,
    NORMAL,
    HIGH,
    ;

    @Composable
    fun getColor(): Color {
        return when (this) {
            NONE -> AppTheme.colors.scheme.surfaceContainerLow
            LOW -> AppTheme.colors.ValueLow
            NORMAL -> AppTheme.colors.ValueNormal
            HIGH -> AppTheme.colors.ValueHigh
        }
    }

    @Composable
    fun getTitle(): String {
        return when (this) {
            NONE -> ""
            LOW -> stringResource(Res.string.value_range_low)
            NORMAL -> stringResource(Res.string.value_range_target)
            HIGH -> stringResource(Res.string.value_range_high)
        }
    }
}