package com.faltenreich.diaguard.shared.file

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import dev.icerock.moko.resources.FileResource

class ResourceFileReader(
    private val resource: FileResource,
    private val localization: Localization = inject(),
) : FileReader {

    override fun invoke(): String {
        return localization.getString(resource)
    }
}