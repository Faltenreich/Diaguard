package com.faltenreich.diaguard.preference.list

import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import kotlin.jvm.JvmInline

// TODO: Implement Serializable
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
    ) : Preference(title, null) {

        class Builder {

            lateinit var title: StringResource
            lateinit var icon: ImageResource

            fun build(): Category {
                return Category(title, icon)
            }
        }
    }

    class Plain(
        title: StringResource,
        subtitle: String?,
        // FIXME: Find way to pass Composable context
        val onClick: (Navigator) -> Unit,
    ) : Preference(title, subtitle) {

        class Builder {

            lateinit var title: StringResource
            var subtitle: String? = null
            lateinit var onClick: (Navigator) -> Unit

            fun build(): Plain {
                return Plain(title, subtitle, onClick)
            }
        }
    }

    class Selection(
        title: StringResource,
        subtitle: String?,
        val options: List<SelectablePreferenceOption>,
    ) : Preference(title, subtitle) {

        class Builder {

            lateinit var title: StringResource
            var subtitle: String? = null
            lateinit var options: List<SelectablePreferenceOption>

            fun build(): Selection {
                return Selection(title, subtitle, options)
            }
        }
    }
}

@JvmInline
value class Preferences(val value: List<Preference>) {

    class Builder {

        private val preferences = mutableListOf<Preference>()

        fun plain(init: Preference.Plain.Builder.() -> Unit) {
            preferences += Preference.Plain.Builder().apply(init).build()
        }

        fun selection(init: Preference.Selection.Builder.() -> Unit) {
            preferences += Preference.Selection.Builder().apply(init).build()
        }

        fun category(init: Preference.Category.Builder.() -> Unit) {
            preferences += Preference.Category.Builder().apply(init).build()
        }

        fun build(): Preferences {
            return Preferences(preferences)
        }
    }
}

fun preferences(init: Preferences.Builder.() -> Unit): List<Preference> {
    return Preferences.Builder().apply(init).build().value
}