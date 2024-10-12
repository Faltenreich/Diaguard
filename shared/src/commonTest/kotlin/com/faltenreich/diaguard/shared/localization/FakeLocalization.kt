package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import org.jetbrains.compose.resources.StringResource

class FakeLocalization(private val locale: Locale = Locale("en")) : Localization {

    override fun getLocale(): Locale {
        return locale
    }

    override fun getString(resource: StringResource, vararg args: Any): String {
        return resource.key
    }

    override fun getFile(path: String): String {
        TODO("Not yet implemented")
    }
}