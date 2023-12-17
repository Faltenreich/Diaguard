package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.MR
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
) : ViewModel<TagListViewState, TagListIntent>() {

    private val tags = getTags()
    private val showFormDialog = MutableStateFlow(false)
    private val inputError = MutableStateFlow<String?>(null)
    override val state = combine(
        tags,
        showFormDialog,
        inputError,
        TagListViewState::Loaded,
    )

    override fun onIntent(intent: TagListIntent) {
        when (intent) {
            is TagListIntent.OpenForm -> showFormDialog()
            is TagListIntent.CloseForm -> hideFormDialog()
            is TagListIntent.Submit -> createTagIfValid(intent.name)
        }
    }

    private fun showFormDialog() {
        showFormDialog.value = true
    }

    private fun hideFormDialog() {
        showFormDialog.value = false
    }

    private fun showError() {
        inputError.value = getString(MR.strings.tag_already_taken)
    }

    private fun createTagIfValid(name: String) {
        if (hasTag(name)) {
            showError()
        } else {
            createTag(name)
            hideFormDialog()
        }
    }
}