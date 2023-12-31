package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.TagFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.tag.form.CreateTagUseCase
import com.faltenreich.diaguard.tag.form.HasTagUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class TagListViewModel(
    getTags: GetTagsUseCase = inject(),
    private val hasTag: HasTagUseCase = inject(),
    private val createTag: CreateTagUseCase = inject(),
    private val deleteTag: DeleteTagUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<TagListViewState, TagListIntent>() {

    private val tags = getTags()
    private val inputError = MutableStateFlow<String?>(null)
    override val state = combine(
        tags,
        inputError,
        TagListViewState::Loaded,
    )

    override fun onIntent(intent: TagListIntent) {
        when (intent) {
            is TagListIntent.OpenForm -> openForm()
            is TagListIntent.CloseForm -> closeModal()
            is TagListIntent.Submit -> createTagIfValid(intent.name)
            is TagListIntent.Delete -> deleteTag(intent.tag)
        }
    }

    private fun openForm() {
        openModal(
            TagFormScreen(
                onDismissRequest = { dispatchIntent(TagListIntent.CloseForm) },
                onConfirmRequest = { name -> dispatchIntent(TagListIntent.Submit(name)) },
                error = inputError.value,
            )
        )
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