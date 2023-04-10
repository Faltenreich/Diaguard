package com.faltenreich.rhyme.shared.localization

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

import platform.Foundation.NSLocale

actual class PlatformLocalization actual constructor(context: Context): Localization {

    actual override fun getLanguageCode(): String {
        return NSLocale.currentLocale.languageCode
    }

    actual override fun getString(resource: StringResource): String {
        return StringDesc.Resource(resource).localized()
    }
}