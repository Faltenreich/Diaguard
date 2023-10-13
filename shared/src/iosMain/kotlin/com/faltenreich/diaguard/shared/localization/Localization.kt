package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource

actual class Localization {

    actual fun getString(resource: StringResource, vararg args: Any): String {
        TODO("Not yet implemented")
    }

    actual fun getString(resource: FileResource): String {
        TODO("Not yet implemented")
    }
}