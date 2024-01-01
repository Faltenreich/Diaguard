package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.localization.getString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TagFormViewModel(
    private val hasTag: HasTagUseCase,
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

    private fun showError() {
        inputError.value = getString(MR.strings.tag_already_taken)
    }

    private fun createTagIfValid(name: String) {
        if (hasTag(name)) {
            showError()
        } else {
            createTag(name)
            closeModal()
        }
    }
}