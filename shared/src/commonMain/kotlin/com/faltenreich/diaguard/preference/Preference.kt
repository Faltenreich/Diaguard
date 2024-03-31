package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.store.ColorScheme
import com.faltenreich.diaguard.preference.store.StartScreen
import dev.icerock.moko.resources.StringResource

sealed class Preference<Store, Domain>(
    val key: StringResource,
    val default: Domain,
    val onRead: (Store) -> Domain?,
    val onWrite: (Domain) -> Store,
)

data object ColorSchemePreference : Preference<Int, ColorScheme>(
    key = MR.strings.preference_theme,
    default = ColorScheme.SYSTEM,
    onRead = ColorScheme::valueOf,
    onWrite = ColorScheme::stableId,
)

data object StartScreenPreference : Preference<Int, StartScreen>(
    key = MR.strings.preference_start_screen,
    default = StartScreen.DASHBOARD,
    onRead = StartScreen::valueOf,
    onWrite = StartScreen::stableId,
)