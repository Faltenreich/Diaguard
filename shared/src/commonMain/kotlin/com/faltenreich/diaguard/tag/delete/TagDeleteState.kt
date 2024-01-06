package com.faltenreich.diaguard.tag.delete

import com.faltenreich.diaguard.tag.Tag

data class TagDeleteState(
    val tag: Tag,
    val entryCount: Long,
)