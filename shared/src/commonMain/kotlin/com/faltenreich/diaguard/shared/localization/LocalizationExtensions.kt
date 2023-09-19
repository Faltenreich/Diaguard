package com.faltenreich.diaguard.shared.localization

import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.StringResource

private val localization: Localization
    get() = inject()

fun getString(resource: StringResource, vararg args: Any): String {
    return localization.getString(resource, *args)
}