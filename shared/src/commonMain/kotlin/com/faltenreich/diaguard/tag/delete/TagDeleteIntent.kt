package com.faltenreich.diaguard.tag.delete

sealed interface TagDeleteIntent {

    data object Close : TagDeleteIntent

    data object Preview : TagDeleteIntent

    data object Confirm : TagDeleteIntent
}