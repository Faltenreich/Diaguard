package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TagFormViewModel(
    private val validateTag: ValidateTagUseCase,
    private val createTag: StoreTagUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<TagFormState, TagFormIntent, Unit>() {

    private val error = MutableStateFlow<String?>(null)

    override val state = error.map(::TagFormState)

    override suspend fun handleIntent(intent: TagFormIntent) {
        when (intent) {
            is TagFormIntent.Close -> closeModal()
            is TagFormIntent.Submit -> createTagIfValid(intent.tag)
        }
    }

    private fun createTagIfValid(tag: Tag.User) {
        when (val result = validateTag(tag)) {
            is ValidationResult.Success -> {
                createTag(tag)
                closeModal()
                error.value = null
            }
            is ValidationResult.Failure -> {
                error.value = result.error
            }
        }
    }
}