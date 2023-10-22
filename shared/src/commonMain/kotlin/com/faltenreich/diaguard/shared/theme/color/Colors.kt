package com.faltenreich.diaguard.shared.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColors = staticCompositionLocalOf { Colors }

object Colors {

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

    val material: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    /**
     * Created by Material Theme Builder
     * https://m3.material.io/theme-builder#/custom
     */

    val md_theme_light_primary = Color(0xFF006C48)
    val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    val md_theme_light_primaryContainer = Color(0xFF8DF7C2)
    val md_theme_light_onPrimaryContainer = Color(0xFF002113)
    val md_theme_light_secondary = Color(0xFF006C4A)
    val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    val md_theme_light_secondaryContainer = Color(0xFF8BF8C4)
    val md_theme_light_onSecondaryContainer = Color(0xFF002114)
    val md_theme_light_tertiary = Color(0xFF006C48)
    val md_theme_light_onTertiary = Color(0xFFFFFFFF)
    val md_theme_light_tertiaryContainer = Color(0xFF8DF7C2)
    val md_theme_light_onTertiaryContainer = Color(0xFF002113)
    val md_theme_light_error = Color(0xFFBA1A1A)
    val md_theme_light_errorContainer = Color(0xFFFFDAD6)
    val md_theme_light_onError = Color(0xFFFFFFFF)
    val md_theme_light_onErrorContainer = Color(0xFF410002)
    val md_theme_light_background = Color(0xFFFBFDF8)
    val md_theme_light_onBackground = Color(0xFF191C1A)
    val md_theme_light_surface = Color(0xFFFBFDF8)
    val md_theme_light_onSurface = Color(0xFF191C1A)
    val md_theme_light_surfaceVariant = Color(0xFFDCE5DD)
    val md_theme_light_onSurfaceVariant = Color(0xFF404943)
    val md_theme_light_outline = Color(0xFF707972)
    val md_theme_light_inverseOnSurface = Color(0xFFEFF1ED)
    val md_theme_light_inverseSurface = Color(0xFF2E312F)
    val md_theme_light_inversePrimary = Color(0xFF71DBA7)
    val md_theme_light_shadow = Color(0xFF000000)
    val md_theme_light_surfaceTint = Color(0xFF006C48)
    val md_theme_light_outlineVariant = Color(0xFFC0C9C1)
    val md_theme_light_scrim = Color(0xFF000000)

    val md_theme_dark_primary = Color(0xFF71DBA7)
    val md_theme_dark_onPrimary = Color(0xFF003823)
    val md_theme_dark_primaryContainer = Color(0xFF005235)
    val md_theme_dark_onPrimaryContainer = Color(0xFF8DF7C2)
    val md_theme_dark_secondary = Color(0xFF6EDBAA)
    val md_theme_dark_onSecondary = Color(0xFF003825)
    val md_theme_dark_secondaryContainer = Color(0xFF005237)
    val md_theme_dark_onSecondaryContainer = Color(0xFF8BF8C4)
    val md_theme_dark_tertiary = Color(0xFF71DBA7)
    val md_theme_dark_onTertiary = Color(0xFF003823)
    val md_theme_dark_tertiaryContainer = Color(0xFF005235)
    val md_theme_dark_onTertiaryContainer = Color(0xFF8DF7C2)
    val md_theme_dark_error = Color(0xFFFFB4AB)
    val md_theme_dark_errorContainer = Color(0xFF93000A)
    val md_theme_dark_onError = Color(0xFF690005)
    val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
    val md_theme_dark_background = Color(0xFF191C1A)
    val md_theme_dark_onBackground = Color(0xFFE1E3DF)
    val md_theme_dark_surface = Color(0xFF191C1A)
    val md_theme_dark_onSurface = Color(0xFFE1E3DF)
    val md_theme_dark_surfaceVariant = Color(0xFF404943)
    val md_theme_dark_onSurfaceVariant = Color(0xFFC0C9C1)
    val md_theme_dark_outline = Color(0xFF8A938C)
    val md_theme_dark_inverseOnSurface = Color(0xFF191C1A)
    val md_theme_dark_inverseSurface = Color(0xFFE1E3DF)
    val md_theme_dark_inversePrimary = Color(0xFF006C48)
    val md_theme_dark_shadow = Color(0xFF000000)
    val md_theme_dark_surfaceTint = Color(0xFF71DBA7)
    val md_theme_dark_outlineVariant = Color(0xFF404943)
    val md_theme_dark_scrim = Color(0xFF000000)

    val seed = Color(0xFF478063)
}