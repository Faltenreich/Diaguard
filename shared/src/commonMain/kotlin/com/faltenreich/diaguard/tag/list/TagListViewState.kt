package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag

sealed interface TagListViewState {

    data object Loading : TagListViewState

    data class Loaded(
        val tags: List<Tag>,
        val showFormDialog: Boolean,
    ) : TagListViewState
}