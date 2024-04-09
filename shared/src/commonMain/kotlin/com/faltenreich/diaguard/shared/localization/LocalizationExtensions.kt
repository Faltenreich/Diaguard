package com.faltenreich.diaguard.shared.localization

import com.faltenreich.diaguard.shared.di.inject
import org.jetbrains.compose.resources.StringResource

// TODO: Replace with stringResource
fun getString(resource: StringResource, vararg args: Any): String {
    val localization = inject<Localization>()
    return localization.getString(resource, *args)
}