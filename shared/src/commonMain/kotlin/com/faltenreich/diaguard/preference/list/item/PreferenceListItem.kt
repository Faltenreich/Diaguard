package com.faltenreich.diaguard.preference.list.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource

// TODO: Implement Serializable
sealed class PreferenceListItem(
    val title: StringResource,
    val subtitle: String?,
) {

    @Composable
    abstract fun Content(modifier: Modifier)

    class Builder {

        private val preferences = mutableListOf<PreferenceListItem>()

        fun category(init: PreferenceCategoryListItem.Builder.() -> Unit) {
            preferences += PreferenceCategoryListItem.Builder().apply(init).build()
        }

        fun action(init: PreferenceActionListItem.Builder.() -> Unit) {
            preferences += PreferenceActionListItem.Builder().apply(init).build()
        }

        fun list(init: PreferenceListListItem.Builder.() -> Unit) {
            preferences += PreferenceListListItem.Builder().apply(init).build()
        }

        fun build(): List<PreferenceListItem> {
            return preferences.toList()
        }
    }
}

fun preferences(init: PreferenceListItem.Builder.() -> Unit): List<PreferenceListItem> {
    return PreferenceListItem.Builder().apply(init).build()
}