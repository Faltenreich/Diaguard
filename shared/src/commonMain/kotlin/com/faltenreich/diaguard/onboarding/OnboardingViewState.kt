package com.faltenreich.diaguard.onboarding

sealed interface OnboardingViewState {

    data object FirstStart : OnboardingViewState

    data object SubsequentStart : OnboardingViewState
}