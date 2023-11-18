package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource

actual class PlatformLocalization : Localization {

    actual override fun getString(resource: StringResource, vararg args: Any): String {
        TODO("Not yet implemented")
    }

    actual override fun getString(resource: FileResource): String {
        TODO("Not yet implemented")
    }
}