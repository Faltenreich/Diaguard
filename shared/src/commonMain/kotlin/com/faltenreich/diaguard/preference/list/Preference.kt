package com.faltenreich.diaguard.preference.list

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.preference.list.item.SelectableOption
import com.faltenreich.diaguard.preference.list.item.SelectablePreference

sealed class Preference(
    val title: @Composable () -> String,
    val subtitle: @Composable (() -> String)?,
) {

    class Plain(
        title: @Composable () -> String,
        subtitle: @Composable (() -> String)?,
    ) : Preference(title, subtitle)

    class Selection<T : SelectablePreference>(
        title: @Composable () -> String,
        subtitle: @Composable (() -> String)?,
        val options: List<SelectableOption<T>>,
    ) : Preference(title, subtitle)
}