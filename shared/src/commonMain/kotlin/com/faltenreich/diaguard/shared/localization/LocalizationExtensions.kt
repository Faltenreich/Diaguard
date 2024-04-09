package com.faltenreich.diaguard.shared.localization

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun getString(resource: StringResource, vararg args: Any): String {
    return stringResource(resource, *args)
}