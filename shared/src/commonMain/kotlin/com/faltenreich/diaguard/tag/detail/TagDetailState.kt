package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.tag.EntryTag
import com.faltenreich.diaguard.tag.Tag

data class TagDetailState(
    val tag: Tag,
    val entryTags: List<EntryTag>,
)