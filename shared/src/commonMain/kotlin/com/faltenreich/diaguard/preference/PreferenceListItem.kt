package com.faltenreich.diaguard.preference

sealed class PreferenceListItem(
    val title: String,
    val subtitle: String?,
) {

    class Plain(
        title: String,
        subtitle: String?,
    ) : PreferenceListItem(title, subtitle)
}