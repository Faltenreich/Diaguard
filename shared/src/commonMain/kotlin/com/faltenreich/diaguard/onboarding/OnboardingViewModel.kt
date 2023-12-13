package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.backup.ImportUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class OnboardingViewModel(
    dispatcher: CoroutineDispatcher = inject(),
    hasData: HasDataUseCase = inject(),
    private val import: ImportUseCase = inject(),
) : ViewModel<OnboardingViewState>() {

    override val state = hasData().map { hasData ->
        when (hasData) {
            true -> OnboardingViewState.SubsequentStart
            false -> OnboardingViewState.FirstStart
        }
    }.flowOn(dispatcher)

    fun prepareData() {
        import()
    }
}