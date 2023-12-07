package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TagListViewModel(
    getTags: GetTagsUseCase = inject(),
) : ViewModel() {

    private val tags = getTags()
    private val showFormDialog = MutableStateFlow(false)
    private val state = combine(
        tags,
        showFormDialog,
        TagListViewState::Loaded,
    )
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TagListViewState.Loading,
    )

    fun showFormDialog() {
        showFormDialog.value = true
    }

    fun hideFormDialog() {
        showFormDialog.value = false
    }

    fun createTag(name: String) {

    }
}