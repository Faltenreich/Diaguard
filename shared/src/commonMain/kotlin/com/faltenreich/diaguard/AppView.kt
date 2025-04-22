package com.faltenreich.diaguard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.main.MainView
import com.faltenreich.diaguard.preference.color.isDark
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.view.keyboardPadding

@Composable
fun AppView(viewModel: AppViewModel = viewModel()) {
    val state = viewModel.collectState()
    AppTheme(isDarkColorScheme = state?.colorScheme.isDark()) {
        Surface(modifier = Modifier.fillMaxSize().keyboardPadding()) {
            MainView(viewModel = viewModel())
        }
    }
}