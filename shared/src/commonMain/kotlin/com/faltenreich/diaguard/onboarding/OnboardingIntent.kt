package com.faltenreich.diaguard.onboarding

sealed interface OnboardingIntent {

    data object ImportSeedData : OnboardingIntent
}