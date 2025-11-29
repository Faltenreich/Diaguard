package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

interface Localization {

    fun getLocale(): Locale

    fun getString(resource: StringResource, vararg formatArgs: Any): String

    fun getPluralString(resource: PluralStringResource, quantity: Int, vararg formatArgs: Any): String

    fun getFile(path: String): String
}