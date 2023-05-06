package com.faltenreich.diaguard.shared.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(
    val P_0: Dp = 0.dp,
    val P_0_5: Dp = 2.dp,
    val P_1: Dp = 4.dp,
    val P_1_5: Dp = 6.dp,
    val P_2: Dp = 8.dp,
    val P_2_5: Dp = 12.dp,
    val P_3: Dp = 16.dp,
    val P_3_5: Dp = 24.dp,
    val P_4: Dp = 32.dp,
    val P_4_5: Dp = 48.dp,
    val P_5: Dp = 64.dp,
    val P_5_5: Dp = 96.dp,
    val P_6: Dp = 128.dp,
)

data class Weight(
    val W_0_5: Float = .5f,
    val W_1: Float = 1f,
    val W_2: Float = 2f,
)

data class Size(
    val ImageSmaller: Dp = 18.dp,
    val ImageSmall: Dp = 24.dp,
    val ImageMedium: Dp = 32.dp,
    val ImageLarge: Dp = 64.dp,
    val MinimumTouchSize: Dp = 48.dp,
)

data class Dimensions(
    val padding: Padding,
    val size: Size,
    val weight: Weight,
) {

    companion object {

        fun forPhone(): Dimensions {
            return Dimensions(
                padding = Padding(),
                size = Size(),
                weight = Weight(),
            )
        }
    }
}