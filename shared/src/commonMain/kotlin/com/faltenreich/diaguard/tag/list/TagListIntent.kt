package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag

sealed interface TagListIntent {

    data object CreateTag : TagListIntent

    data class OpenTag(val tag: Tag.Local) : TagListIntent
}