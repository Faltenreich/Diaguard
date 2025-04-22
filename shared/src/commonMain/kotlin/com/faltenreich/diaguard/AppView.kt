package com.faltenreich.diaguard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.main.MainView
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.theme.ThemeViewModel
import com.faltenreich.diaguard.shared.view.keyboardPadding

@Composable
fun AppView() {
    val themeViewModel = viewModel<ThemeViewModel>()
    AppTheme(isDarkColorScheme = themeViewModel.isDarkColorScheme()) {
        Surface (
            modifier = Modifier.fillMaxSize().keyboardPadding(),
            color = AppTheme.colors.scheme.surface,
        ) {
            MainView(viewModel = viewModel())
        }
    }
}