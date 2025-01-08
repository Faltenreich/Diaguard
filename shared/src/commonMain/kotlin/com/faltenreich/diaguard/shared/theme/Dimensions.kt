package com.faltenreich.diaguard.shared.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.faltenreich.diaguard.shared.theme.dimension.Alpha
import com.faltenreich.diaguard.shared.theme.dimension.Padding
import com.faltenreich.diaguard.shared.theme.dimension.Size
import com.faltenreich.diaguard.shared.theme.dimension.Weight

val LocalDimensions = staticCompositionLocalOf { Dimensions.forPhone() }

data class Dimensions(
    val alpha: Alpha,
    val padding: Padding,
    val size: Size,
    val weight: Weight,
) {

    companion object {

        fun forPhone(): Dimensions {
            return Dimensions(
                alpha = Alpha(),
                padding = Padding(),
                size = Size(),
                weight = Weight(),
            )
        }
    }
}