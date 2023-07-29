package com.faltenreich.diaguard.preference.list

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.preference.list.item.SelectablePreference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import dev.icerock.moko.resources.ImageResource

sealed class Preference(
    val title: @Composable () -> String,
    val subtitle: @Composable (() -> String)?,
) {

    class Folder(
        title: @Composable () -> String,
        val preferences: List<Preference>,
    ) : Preference(title, null)

    class Category(
        title: @Composable () -> String,
        val icon: ImageResource,
    ) : Preference(title, null)

    class Plain(
        title: @Composable () -> String,
        subtitle: @Composable (() -> String)?,
    ) : Preference(title, subtitle)

    class Selection<T : SelectablePreference>(
        title: @Composable () -> String,
        subtitle: @Composable (() -> String)?,
        val options: List<SelectablePreferenceOption<T>>,
    ) : Preference(title, subtitle)
}