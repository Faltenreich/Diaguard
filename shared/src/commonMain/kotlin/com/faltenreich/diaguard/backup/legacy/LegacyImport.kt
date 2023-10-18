package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import

class LegacyImport(
    private val legacyRepository: LegacyRepository,
) : Import {

    override fun import() {
        val entries = legacyRepository.getEntries()
        val values = legacyRepository.getMeasurementValues()
        val tags = legacyRepository.getTags()
        println("Found ${entries.size} entries, ${values.size} values and ${tags.size} tags")
    }
}