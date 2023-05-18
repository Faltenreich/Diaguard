package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OnboardingViewModel(
    hasData: HasDataUseCase = inject(),
) : ViewModel() {

    private val state = hasData().map { hasData ->
        when (hasData) {
            true -> OnboardingViewState.FirstStart
            false -> OnboardingViewState.SubsequentStart
        }
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = OnboardingViewState.Loading,
    )
}