package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

actual class PlatformLocalization: Localization {

    actual override fun getLanguageCode(): String {
        TODO()
    }

    actual override fun getString(resource: StringResource): String {
        return StringDesc.Resource(resource).localized()
    }
}