package com.faltenreich.diaguard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.main.MainView
import com.faltenreich.diaguard.preference.color.isDark
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.view.keyboardPadding
import com.faltenreich.diaguard.startup.StartupView

@Composable
fun AppView(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel(),
) {
    val state = viewModel.collectState() ?: return

    AppTheme(isDarkColorScheme = state.colorScheme.isDark()) {
        Surface(modifier = modifier.fillMaxSize().keyboardPadding()) {
            when (state) {
                is AppState.FirstStart -> StartupView(viewModel = viewModel())
                is AppState.SubsequentStart -> MainView(viewModel = viewModel())
            }
        }
    }
}