package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

sealed class Preference<T>(val key: StringResource) {

    data object StartScreen : Preference<Int>(MR.strings.preference_start_screen)

    data object ColorScheme : Preference<Int>(MR.strings.preference_theme)
}