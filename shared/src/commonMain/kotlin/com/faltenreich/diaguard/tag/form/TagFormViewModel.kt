package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TagFormViewModel(
    private val validateTag: ValidateTagUseCase,
    private val createTag: CreateTagUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<TagFormState, TagFormIntent, Unit>() {

    private val error = MutableStateFlow<String?>(null)

    override val state = error.map(::TagFormState)

    override fun handleIntent(intent: TagFormIntent) {
        when (intent) {
            is TagFormIntent.Close -> closeModal()
            is TagFormIntent.Submit -> createTagIfValid(intent.name)
        }
    }

    private fun createTagIfValid(name: String) {
        when (val result = validateTag(name)) {
            is ValidationResult.Success -> {
                createTag(name)
                closeModal()
                error.value = null
            }
            is ValidationResult.Failure -> {
                error.value = result.error
            }
        }
    }
}