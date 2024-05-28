package com.faltenreich.diaguard.shared.localization

import org.jetbrains.compose.resources.StringResource

class ResourceLocalizationFake : ResourceLocalization {

    override fun getString(resource: StringResource, vararg args: Any): String {
        return resource.key
    }

    override fun getFile(path: String): String {
        TODO("Not yet implemented")
    }
}