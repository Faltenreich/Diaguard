package com.faltenreich.diaguard.shared.localization

import org.jetbrains.compose.resources.StringResource

expect class PlatformLocalization() : Localization {

    override fun getString(resource: StringResource, vararg args: Any): String

    override fun getFile(path: String): String
}