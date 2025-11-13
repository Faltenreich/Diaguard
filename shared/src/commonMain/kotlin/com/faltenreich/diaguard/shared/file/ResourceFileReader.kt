package com.faltenreich.diaguard.shared.file

import com.faltenreich.diaguard.core.di.inject
import com.faltenreich.diaguard.core.localization.Localization

class ResourceFileReader(
    private val path: String,
    private val localization: Localization = inject(),
) : FileReader {

    override fun read(): String {
        return localization.getFile(path)
    }
}