package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import org.jetbrains.compose.resources.StringResource

interface Localization {

    fun getLocale(): Locale

    fun getString(resource: StringResource, vararg args: Any): String

    fun getFile(path: String): String
}