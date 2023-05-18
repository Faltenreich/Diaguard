package com.faltenreich.diaguard.onboarding

sealed class OnboardingViewState {

    object Loading

    object FirstStart

    object SubsequentStart
}