package com.faltenreich.diaguard.shared.localization

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

actual class PlatformLocalization(private val context: Context): Localization {

    actual override fun getLanguageCode(): String {
        return Locale.current.language
    }

    actual override fun getString(resource: StringResource): String {
        return StringDesc.Resource(resource).toString(context)
    }
}