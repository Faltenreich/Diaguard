package com.faltenreich.diaguard.shared.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColors = staticCompositionLocalOf { Colors }

object Colors {

    val Material: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val Green = Color(0xff478063)
    val GreenLight = Color(0xff6d947f)
    val GreenLighter = Color(0xff79a18c)
    val GreenDark = Color(0xff41785a)
    val GreenDarker = Color(0xff396950)
    val GreenDarkest = Color(0xff2E5340)
    val GreenTranslucent = Color(0xCC478063)
    val GreenDarkTranslucent = Color(0xCC41785a)

    val Red = Color(0xffdd6050)
    val RedLight = Color(0xffe96353)
    val RedDark = Color(0xffc95a4a)

    val Yellow = Color(0xffFBC02D)

    val Blue = Color(0xff5771cd)

    val Gray = Color(0xababab)
    val GrayLight = Color(0xffd4d4d4)
    val GrayLighter = Color(0xe5e5e5)
    val GrayDark = Color(0xff8c8c8c)
    val GrayDarker = Color(0x767676)
    val GrayTranslucent = Color(0x77d4d4d4)

    val ScrimCenter = Color(0x3F000000)
    val ScrimEnd = Color(0x58000000)

    val BackgroundLightPrimary = Color(0xf3f3f3)
    val BackgroundLightPrimaryTranslucent = Color(0xCCf3f3f3)
    val BackgroundLightSecondary = Color(0xfff)
    val BackgroundLightTertiary = Color(0xd4d4d4)
    val BackgroundDarkPrimary = Color(0x313131)
    val BackgroundDarkPrimaryTranslucent = Color(0xCC313131)
    val BackgroundDarkSecondary = Color(0x424242)
    val BackgroundDarkTertiary = Color(0x5C5C5C)
}