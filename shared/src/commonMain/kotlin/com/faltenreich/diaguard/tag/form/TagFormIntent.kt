package com.faltenreich.diaguard.tag.form

sealed interface TagFormIntent {

    data object Close : TagFormIntent

    data class Submit(val name: String) : TagFormIntent
}