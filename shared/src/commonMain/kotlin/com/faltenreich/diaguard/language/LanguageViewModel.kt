package com.faltenreich.diaguard.language

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val repository: LanguageRepository,
    dispatcher: CoroutineDispatcher,
): ViewModel() {

    private val state = MutableStateFlow(LanguageState(currentLanguage = Language.default))
    val uiState = state.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            repository.currentLanguage
                .map { language -> state.value = state.value.copy(currentLanguage = language) }
                .collect()
        }
    }

    fun setCurrentLanguage(language: Language) {
        repository.setLanguage(language)
    }
}