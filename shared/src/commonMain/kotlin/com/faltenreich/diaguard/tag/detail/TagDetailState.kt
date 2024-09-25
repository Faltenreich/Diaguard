package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.tag.EntryTag

data class TagDetailState(
    val entryTags: List<EntryTag.Local>,
)