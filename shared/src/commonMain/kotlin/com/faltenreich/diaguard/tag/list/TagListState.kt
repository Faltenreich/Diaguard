package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.data.tag.Tag

data class TagListState(
    val tags: List<Tag.Local>,
    val formDialog: FormDialog?,
) {

    data class FormDialog(val error: String? = null)
}