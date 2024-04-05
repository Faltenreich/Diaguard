package com.faltenreich.diaguard.shared.localization

import diaguard.shared.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource

actual class Localization {

    actual fun getString(resource: StringResource, vararg args: Any): String = runBlocking {
        org.jetbrains.compose.resources.getString(resource, args)
    }

    actual fun getFile(path: String): String = runBlocking {
        Res.readBytes(path).decodeToString()
    }
}