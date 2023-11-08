package com.faltenreich.diaguard.preference.list

import cafe.adriel.voyager.navigator.Navigator
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

fun preferences(init: PreferenceBuilder.Preferences.() -> Unit): List<Preference> {
    return PreferenceBuilder.Preferences().apply(init).build()
}

object PreferenceBuilder {

    class Preferences {

        private val preferences = mutableListOf<Preference>()

        fun category(init: Category.() -> Unit) {
            preferences += Category().apply(init).build()
        }

        fun action(init: Action.() -> Unit) {
            preferences += Action().apply(init).build()
        }

        fun list(init: List.() -> Unit) {
            preferences += List().apply(init).build()
        }

        fun build(): kotlin.collections.List<Preference> {
            return preferences.toList()
        }
    }

    class Category {

        lateinit var title: StringResource
        lateinit var icon: ImageResource

        fun build(): Preference.Category {
            return Preference.Category(title, icon)
        }
    }

    class Action {

        lateinit var title: StringResource
        var subtitle: String? = null
        lateinit var onClick: (Navigator) -> Unit

        fun build(): Preference.Action {
            return Preference.Action(title, subtitle, onClick)
        }
    }

    class List {

        lateinit var title: StringResource
        var subtitle: String? = null
        lateinit var options: kotlin.collections.List<Preference.List.Option>

        fun build(): Preference.List {
            return Preference.List(title, subtitle, options)
        }
    }
}