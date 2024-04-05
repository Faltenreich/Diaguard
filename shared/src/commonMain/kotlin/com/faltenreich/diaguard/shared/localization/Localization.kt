package com.faltenreich.diaguard.shared.localization

import org.jetbrains.compose.resources.StringResource

expect class Localization constructor() {

    fun getString(resource: StringResource, vararg args: Any): String

    fun getFile(path: String): String
}