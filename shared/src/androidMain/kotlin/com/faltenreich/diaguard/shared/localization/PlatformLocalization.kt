package com.faltenreich.diaguard.shared.localization

import diaguard.shared.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource

actual class PlatformLocalization : Localization {

    actual override fun getString(resource: StringResource, vararg args: Any): String = runBlocking {
        // FIXME: String templates do not work, e.g. in Dashboard's last entry
        org.jetbrains.compose.resources.getString(resource, args)
    }

    actual override fun getFile(path: String): String = runBlocking {
        Res.readBytes(path).decodeToString()
    }
}