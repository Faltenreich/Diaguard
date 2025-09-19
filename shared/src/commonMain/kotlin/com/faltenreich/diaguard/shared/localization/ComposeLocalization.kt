package com.faltenreich.diaguard.shared.localization

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import diaguard.shared.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class ComposeLocalization : Localization {

    override fun getLocale(): Locale {
        return Locale.current
    }

    override fun getString(resource: StringResource, vararg args: Any): String = runBlocking {
        org.jetbrains.compose.resources.getString(resource, *args)
    }

    override fun getFile(path: String): String = runBlocking {
        Res.readBytes(path).decodeToString()
    }
}

@Composable
fun getString(resource: StringResource, vararg args: Any): String {
    return stringResource(resource, *args)
}