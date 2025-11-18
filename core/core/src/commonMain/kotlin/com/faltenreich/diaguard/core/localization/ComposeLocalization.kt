package com.faltenreich.diaguard.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import diaguard.core.core.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class ComposeLocalization(private val locale: Locale = Locale.current) : Localization {

    override fun getLocale(): Locale {
        return locale
    }

    override fun getString(resource: StringResource, vararg formatArgs: Any): String = runBlocking {
        org.jetbrains.compose.resources.getString(resource, *formatArgs)
    }

    override fun getPluralString(
        resource: PluralStringResource,
        quantity: Int,
        vararg formatArgs: Any,
    ): String = runBlocking {
        org.jetbrains.compose.resources.getPluralString(resource, quantity, *formatArgs)
    }

    override fun getFile(path: String): String = runBlocking {
        Res.readBytes(path).decodeToString()
    }
}

@Composable
fun getString(resource: StringResource, vararg args: Any): String {
    return stringResource(resource, *args)
}