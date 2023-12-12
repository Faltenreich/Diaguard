package com.faltenreich.diaguard.shared.theme

import com.faltenreich.diaguard.preference.store.color.ColorScheme
import com.faltenreich.diaguard.preference.store.color.GetColorSchemeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ThemeViewModel(
    getColorScheme: GetColorSchemeUseCase,
) : ViewModel {

    private val colorScheme: Flow<ColorScheme?> = getColorScheme()
    val viewState = colorScheme.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = null,
    )
}