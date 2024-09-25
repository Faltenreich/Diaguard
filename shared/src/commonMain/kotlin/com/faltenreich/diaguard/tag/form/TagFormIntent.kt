package com.faltenreich.diaguard.tag.form

sealed interface TagFormIntent {

    data object Close : TagFormIntent

    data object Submit : TagFormIntent
}