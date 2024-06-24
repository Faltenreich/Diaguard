package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.tag.Tag

sealed interface TagFormIntent {

    data object Close : TagFormIntent

    data class Submit(val tag: Tag.User) : TagFormIntent
}