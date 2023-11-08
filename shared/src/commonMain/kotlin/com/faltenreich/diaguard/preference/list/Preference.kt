package com.faltenreich.diaguard.preference.list

import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.preference.list.item.ListPreferenceOption
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

// TODO: Implement Serializable
sealed class Preference(
    val title: StringResource,
    val subtitle: String?,
) {

    class Folder(
        title: StringResource,
        val preferences: kotlin.collections.List<Preference>,
    ) : Preference(title, null)

    class Category(
        title: StringResource,
        val icon: ImageResource,
    ) : Preference(title, null)

    class Action(
        title: StringResource,
        subtitle: String?,
        // FIXME: Find way to pass Composable context
        val action: (Navigator) -> Unit,
    ) : Preference(title, subtitle)

    class List(
        title: StringResource,
        subtitle: String?,
        val options: kotlin.collections.List<ListPreferenceOption>,
    ) : Preference(title, subtitle)
}