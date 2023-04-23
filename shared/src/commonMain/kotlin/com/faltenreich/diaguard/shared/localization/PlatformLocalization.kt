package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.StringResource

expect class PlatformLocalization: Localization {

    override fun getLanguageCode(): String

    override fun getString(resource: StringResource): String
}