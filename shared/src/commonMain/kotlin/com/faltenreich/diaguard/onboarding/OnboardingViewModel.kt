package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class OnboardingViewModel(
    hasData: HasDataUseCase = inject(),
    private val import: ImportUseCase = inject(),
) : ViewModel<OnboardingViewState, OnboardingIntent, Unit>() {

    override val state = hasData().map { hasData ->
        when (hasData) {
            true -> OnboardingViewState.SubsequentStart
            false -> OnboardingViewState.FirstStart
        }
    }

    override fun handleIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.ImportSeedData -> import()
        }
    }
}