package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.tag.Tag

sealed interface TagDetailIntent {

    data class EditTag(val tag: Tag, val name: String) : TagDetailIntent

    data class DeleteTag(val tag: Tag) : TagDetailIntent

    data class OpenEntry(val entry: Entry) : TagDetailIntent
}