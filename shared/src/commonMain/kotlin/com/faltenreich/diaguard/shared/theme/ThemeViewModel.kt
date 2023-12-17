package com.faltenreich.diaguard.shared.theme

import com.faltenreich.diaguard.preference.store.color.ColorScheme
import com.faltenreich.diaguard.preference.store.color.GetColorSchemeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.Flow

class ThemeViewModel(
    getColorScheme: GetColorSchemeUseCase,
) : ViewModel<ColorScheme, Unit>() {

    override val state: Flow<ColorScheme> = getColorScheme()

    override fun onIntent(intent: Unit) = Unit
}