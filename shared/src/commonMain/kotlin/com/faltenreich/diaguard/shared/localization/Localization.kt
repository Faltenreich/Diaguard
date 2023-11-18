package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource

interface Localization {

    fun getString(resource: StringResource, vararg args: Any): String

    fun getString(resource: FileResource): String
}