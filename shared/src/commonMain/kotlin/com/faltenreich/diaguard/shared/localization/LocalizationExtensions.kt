package com.faltenreich.diaguard.shared.localization

import com.faltenreich.diaguard.shared.di.inject
import org.jetbrains.compose.resources.StringResource

fun getString(resource: StringResource, vararg args: Any): String {
    return inject<Localization>().getString(resource, args)
}