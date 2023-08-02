package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.preference.list.item.SelectablePreference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

sealed class Preference(
    val title: StringResource,
    val subtitle: String?,
) {

    class Folder(
        title: StringResource,
        val preferences: List<Preference>,
    ) : Preference(title, null)

    class Category(
        title: StringResource,
        val icon: ImageResource,
    ) : Preference(title, null)

    class Plain(
        title: StringResource,
        subtitle: String?,
    ) : Preference(title, subtitle)

    class Selection<T : SelectablePreference>(
        title: StringResource,
        subtitle: String?,
        val options: List<SelectablePreferenceOption<T>>,
    ) : Preference(title, subtitle)
}