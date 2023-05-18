package com.faltenreich.diaguard.onboarding

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun onboardingModule() = module {
    singleOf(::OnboardingViewModel)
    singleOf(::HasDataUseCase)
}