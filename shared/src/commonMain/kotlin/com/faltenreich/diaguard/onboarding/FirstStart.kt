package com.faltenreich.diaguard.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun FirstStart(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = inject(),
) {
    // TODO: Prevent subsequent calls on recomposition by wrapping inside a LaunchedEffect
    viewModel.prepareData()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}