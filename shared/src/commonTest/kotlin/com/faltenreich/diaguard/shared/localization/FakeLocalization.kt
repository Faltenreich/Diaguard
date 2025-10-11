package com.faltenreich.diaguard.shared.localization

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.datetime.DayOfWeek
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

class FakeLocalization(
    private val locale: Locale = Locale("en"),
    private val is24HourFormat: Boolean = true,
) : Localization {

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

    override fun getStartOfWeek(): DayOfWeek {
        return DayOfWeek.MONDAY
    }

    override fun is24HourFormat(): Boolean {
        return is24HourFormat
    }
}