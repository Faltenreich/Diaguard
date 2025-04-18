package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag

data class TagListState(
    val tags: List<Tag.Local>,
    val form: Form,
) {

    sealed interface Form {

        data object Hidden : Form

        data class Shown(val error: String? = null) : Form
    }
}