package com.faltenreich.diaguard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.NavigationView
import com.faltenreich.diaguard.shared.view.keyboardPadding

@Composable
fun MainView(
    modifier: Modifier = Modifier,
) {
    AppTheme {
        Surface (
            modifier = modifier.fillMaxSize().keyboardPadding(),
            color = AppTheme.colors.material.background,
        ) {
            NavigationView()
        }
    }
}