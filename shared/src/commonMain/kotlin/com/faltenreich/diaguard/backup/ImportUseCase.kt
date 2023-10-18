package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.backup.legacy.LegacyImport
import com.faltenreich.diaguard.backup.seed.SeedImport
import com.faltenreich.diaguard.shared.di.inject

class ImportUseCase(
    private val seedImport: SeedImport = inject(),
    private val legacyImport: LegacyImport = inject(),
) {

    operator fun invoke() {
        // Attention: Must be executed first as its data will be reused
        seedImport.import()
        legacyImport.import()
    }
}