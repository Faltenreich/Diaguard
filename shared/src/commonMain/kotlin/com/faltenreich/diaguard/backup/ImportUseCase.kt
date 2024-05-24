package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.backup.legacy.LegacyImport
import com.faltenreich.diaguard.backup.seed.SeedImport

class ImportUseCase(
    private val seedImport: SeedImport,
    private val legacyImport: LegacyImport,
) {

    operator fun invoke() {
        // Attention: Must be executed first as its data will be reused
        seedImport.import()

        legacyImport.import()
    }
}