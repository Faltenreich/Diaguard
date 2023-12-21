package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag

sealed interface TagListIntent {

    data object OpenForm : TagListIntent

    data object CloseForm : TagListIntent

    data class Submit(val name: String) : TagListIntent

    data class Delete(val tag: Tag) : TagListIntent
}