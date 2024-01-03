package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag

sealed interface TagListIntent {

    data object Create : TagListIntent

    data class Delete(val tag: Tag) : TagListIntent
}