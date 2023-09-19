package com.faltenreich.diaguard.shared.localization

import dev.icerock.moko.resources.StringResource

expect class Localization constructor() {

    fun getString(resource: StringResource, vararg args: Any): String
}