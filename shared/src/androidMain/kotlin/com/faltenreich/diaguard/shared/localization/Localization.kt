package com.faltenreich.diaguard.shared.localization

import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import java.util.Locale as JvmLocale

actual class Localization {

    actual val currentLocale: Locale
        get() = JvmLocale.getDefault().let { locale ->
            Locale(
                languageCode = locale.language,
                countryCode = locale.country,
            )
        }

    actual fun getString(resource: StringResource, vararg args: Any): String {
        return StringDesc.ResourceFormatted(resource, *args).toString(inject())
    }

    actual fun getString(resource: FileResource): String {
        return resource.readText(inject())
    }
}