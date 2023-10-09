package com.faltenreich.diaguard.import

import com.faltenreich.diaguard.import.legacy.LegacyImport
import com.faltenreich.diaguard.import.seed.SeedImport
import com.faltenreich.diaguard.shared.di.inject

class ImportUseCase(
    private val seedImport: SeedImport = inject(),
    private val legacyImport: LegacyImport = inject(),
) {

    operator fun invoke() {
        seedImport()
        // Attention: Must be executed at last due to reusing seed data
        legacyImport()
    }
}