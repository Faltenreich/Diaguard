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

    private val error = MutableStateFlow<String?>(null)

    override val state = error.map(::TagFormViewState)

    override fun onIntent(intent: TagFormIntent) {
        when (intent) {
            is TagFormIntent.Close -> closeModal()
            is TagFormIntent.Submit -> createTagIfValid(intent.name)
        }
    }

    private fun createTagIfValid(name: String) {
        val result = validate(name, UniqueTagRule())
        if (result.isSuccess) {
            createTag(name)
            closeModal()
            error.value = null
        } else {
            error.value = when (result.exceptionOrNull()) {
                is RedundantTagException -> getString(MR.strings.tag_already_taken)
                else -> getString(MR.strings.error_unknown)
            }
        }
    }
}