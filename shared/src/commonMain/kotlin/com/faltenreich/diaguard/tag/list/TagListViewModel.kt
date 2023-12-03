package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TagListViewModel(
    getTags: GetTagsUseCase = inject(),
) : ViewModel() {

    private val state = getTags().map(TagListViewState::Loaded)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TagListViewState.Loading,
    )
}