package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.tag.Tag

data class EntryFormState(
    val tags: List<Tag>,
    val inputError: String? = null,
)