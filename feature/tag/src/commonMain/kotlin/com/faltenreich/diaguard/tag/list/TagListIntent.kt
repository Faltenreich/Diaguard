package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.data.tag.Tag

sealed interface TagListIntent {

    data object OpenFormDialog : TagListIntent

    data object CloseFormDialog : TagListIntent

    data class OpenTag(val tag: Tag.Local) : TagListIntent

    data class StoreTag(val name: String) : TagListIntent
}