package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import org.jetbrains.compose.resources.StringResource

open class FakeLocalization : Localization {

    override fun getLocale(): Locale {
        return Locale("en")
    }

    override fun getString(resource: StringResource, vararg args: Any): String {
        return resource.key
    }

    override fun getFile(path: String): String {
        TODO("Not yet implemented")
    }
}