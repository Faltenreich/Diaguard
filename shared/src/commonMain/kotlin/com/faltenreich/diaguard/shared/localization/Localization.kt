package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.StringResource

interface Localization {

    fun getLanguageCode(): String

    fun getString(resource: StringResource): String
}