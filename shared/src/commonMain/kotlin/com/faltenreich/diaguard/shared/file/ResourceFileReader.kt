package com.faltenreich.diaguard.shared.file

import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.localization.Localization

class ResourceFileReader(
    private val path: String,
    private val localization: Localization = inject(),
) : FileReader {

    override fun read(): String {
        return localization.getFile(path)
    }
}