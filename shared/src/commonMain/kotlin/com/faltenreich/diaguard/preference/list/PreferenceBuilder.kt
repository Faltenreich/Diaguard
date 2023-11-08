package com.faltenreich.diaguard.preference.list

import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.preference.list.item.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceCategoryListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceListListItem
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

fun preferences(init: PreferenceBuilder.Preferences.() -> Unit): List<PreferenceListItem> {
    return PreferenceBuilder.Preferences().apply(init).build()
}

object PreferenceBuilder {

    class Preferences {

        private val preferences = mutableListOf<PreferenceListItem>()

        fun category(init: Category.() -> Unit) {
            preferences += Category().apply(init).build()
        }

        fun action(init: Action.() -> Unit) {
            preferences += Action().apply(init).build()
        }

        fun list(init: List.() -> Unit) {
            preferences += List().apply(init).build()
        }

        fun build(): kotlin.collections.List<PreferenceListItem> {
            return preferences.toList()
        }
    }

    class Category {

        lateinit var title: StringResource
        lateinit var icon: ImageResource

        fun build(): PreferenceCategoryListItem {
            return PreferenceCategoryListItem(title, icon)
        }
    }

    class Action {

        lateinit var title: StringResource
        var subtitle: String? = null
        lateinit var onClick: (Navigator) -> Unit

        fun build(): PreferenceActionListItem {
            return PreferenceActionListItem(title, subtitle, onClick)
        }
    }

    class List {

        lateinit var title: StringResource
        var subtitle: String? = null
        lateinit var options: kotlin.collections.List<PreferenceListListItem.Option>

        fun build(): PreferenceListListItem {
            return PreferenceListListItem(title, subtitle, options)
        }
    }
}