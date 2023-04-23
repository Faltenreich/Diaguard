package com.faltenreich.diaguard.shared.localization

import com.faltenreich.diaguard.shared.architecture.Context
import dev.icerock.moko.resources.StringResource

expect class PlatformLocalization constructor(context: Context): Localization {

    override fun getLanguageCode(): String

    override fun getString(resource: StringResource): String
}