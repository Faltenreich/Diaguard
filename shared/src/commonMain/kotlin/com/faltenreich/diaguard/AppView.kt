package com.faltenreich.diaguard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.main.MainView
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.theme.ThemeViewModel
import com.faltenreich.diaguard.shared.view.keyboardPadding

@Composable
fun AppView(
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel = inject(),
) {
    val isDarkColorScheme = when (themeViewModel.collectState()) {
        ColorScheme.SYSTEM, null -> isSystemInDarkTheme()
        ColorScheme.LIGHT -> false
        ColorScheme.DARK -> true
    }
    AppTheme(isDarkColorScheme = isDarkColorScheme) {
        Surface (
            modifier = modifier.fillMaxSize().keyboardPadding(),
            color = AppTheme.colors.scheme.surface,
        ) {
            MainView(modifier = modifier)
        }
    }
}