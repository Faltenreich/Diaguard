package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

class FakeLocalization(private val locale: Locale = Locale("en")) : Localization {

    override fun getLocale(): Locale {
        return locale
    }

    override fun getString(resource: StringResource, vararg formatArgs: Any): String {
        return resource.key
    }

    override fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any
    ): String {
        return resource.key
    }

    override fun getFile(path: String): String {
        TODO("Not yet implemented")
    }

    override fun is24HourFormat(): Boolean {
        return true
    }
}