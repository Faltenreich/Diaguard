package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag

sealed interface TagListViewState {

    data class Loaded(
        val tags: List<Tag>,
        val inputError: String?,
    ) : TagListViewState
}