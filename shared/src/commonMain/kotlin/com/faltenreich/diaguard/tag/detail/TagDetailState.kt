package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.tag.EntryTag

data class TagDetailState(
    val entryTags: List<EntryTag>,
    val inputError: String?,
)