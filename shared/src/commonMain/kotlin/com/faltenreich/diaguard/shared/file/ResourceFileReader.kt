package com.faltenreich.diaguard.shared.file

import com.faltenreich.diaguard.shared.localization.Localization
import dev.icerock.moko.resources.FileResource

class ResourceFileReader(private val localization: Localization) {

    fun read(resource: FileResource): String {
        return localization.getString(resource)
    }
}