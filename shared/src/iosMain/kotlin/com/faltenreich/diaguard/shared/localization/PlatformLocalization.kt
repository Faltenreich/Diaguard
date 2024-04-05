package com.faltenreich.diaguard.shared.localization

import org.jetbrains.compose.resources.StringResource

actual class PlatformLocalization : Localization {

    actual override fun getString(resource: StringResource, vararg args: Any): String {
        TODO("Not yet implemented")
    }

    actual override fun getFile(path: String): String {
        TODO("Not yet implemented")
    }
}