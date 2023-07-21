package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.item.SelectablePreference

sealed class Preference(
    val title: String,
    val subtitle: String?,
) {

    class Plain(
        title: String,
        subtitle: String?,
    ) : Preference(title, subtitle)

    class Selection<T : SelectablePreference>(
        title: String,
        subtitle: String?,
        val options: List<Pair<String, T>>,
        val onSelection: suspend (T) -> Unit,
    ) : Preference(title, subtitle)
}