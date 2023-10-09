package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.shared.database.DatabaseLegacyImport
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun onboardingModule() = module {
    singleOf(::SeedImport)
    singleOf(::DatabaseLegacyImport)
    singleOf(::OnboardingViewModel)
    singleOf(::HasDataUseCase)
}