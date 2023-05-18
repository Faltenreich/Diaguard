package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.launch

class OnboardingViewModel(firstStart: FirstStart = inject()) : ViewModel() {

    init {
        viewModelScope.launch { firstStart() }
    }
}