package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource

expect class PlatformLocalization constructor() : Localization {

    override fun getString(resource: StringResource, vararg args: Any): String

    override fun getString(resource: FileResource): String
}