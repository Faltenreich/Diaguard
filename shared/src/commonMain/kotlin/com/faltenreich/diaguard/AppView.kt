package com.faltenreich.diaguard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.onboarding.FirstStart
import com.faltenreich.diaguard.onboarding.OnboardingViewModel
import com.faltenreich.diaguard.onboarding.OnboardingViewState
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.keyboardPadding

@Composable
fun AppView(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel = inject(),
) {
    AppTheme {
        Surface (
            modifier = modifier.fillMaxSize().keyboardPadding(),
            color = AppTheme.colors.material.background,
        ) {
            when (onboardingViewModel.viewState.collectAsState().value) {
                is OnboardingViewState.Loading -> Unit
                is OnboardingViewState.FirstStart -> FirstStart(modifier = modifier)
                is OnboardingViewState.SubsequentStart -> MainView(modifier = modifier)
            }
        }
    }
}