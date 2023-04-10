package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.StringResource

class FakeLocalization: Localization {

    override fun getLanguageCode(): String {
        return "en"
    }

    override fun getString(resource: StringResource): String {
        return resource.toString()
    }
}