package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.validation.ValidateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TagFormViewModel(
    private val validate: ValidateUseCase,
    private val createTag: CreateTagUseCase,
    private val closeModal: CloseModalUseCase,
) : ViewModel<TagFormViewState, TagFormIntent>() {

    private val inputError = MutableStateFlow<String?>(null)
    override val state = inputError.map(::TagFormViewState)

    override fun onIntent(intent: TagFormIntent) {
        when (intent) {
            is TagFormIntent.Close -> closeModal()
            is TagFormIntent.Submit -> createTagIfValid(intent.name)
        }
    }

    private fun createTagIfValid(name: String) {
        if (validate(name, UniqueTagRule()).isSuccess) {
            createTag(name)
            closeModal()
        } else {
            // FIXME: Avoid clearing text field
            // TODO: Handle concrete failure
            inputError.value = getString(MR.strings.tag_already_taken)
        }
    }
}