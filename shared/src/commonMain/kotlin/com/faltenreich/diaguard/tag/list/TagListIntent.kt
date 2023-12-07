package com.faltenreich.diaguard.tag.list

sealed interface TagListIntent {

    data object OpenForm : TagListIntent

    data object CloseForm : TagListIntent

    data class Submit(val name: String) : TagListIntent
}