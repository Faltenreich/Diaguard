package com.faltenreich.diaguard.preference.list.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed interface PreferenceListItem {

    val title: String
    val subtitle: String?
        get() = null

    @Composable
    fun Content(modifier: Modifier)

    class Builder {

        private val preferences = mutableListOf<PreferenceListItem>()

        fun category(init: PreferenceCategoryListItem.Builder.() -> Unit) {
            preferences += PreferenceCategoryListItem.Builder().apply(init).build()
        }

        fun checkbox(init: PreferenceCheckBoxListItem.Builder.() -> Unit) {
            preferences += PreferenceCheckBoxListItem.Builder().apply(init).build()
        }

        fun action(init: PreferenceActionListItem.Builder.() -> Unit) {
            preferences += PreferenceActionListItem.Builder().apply(init).build()
        }

        fun build(): List<PreferenceListItem> {
            return preferences.toList()
        }
    }
}

fun preferences(init: PreferenceListItem.Builder.() -> Unit): List<PreferenceListItem> {
    return PreferenceListItem.Builder().apply(init).build()
}