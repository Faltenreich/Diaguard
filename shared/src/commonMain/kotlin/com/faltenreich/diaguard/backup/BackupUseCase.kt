package com.faltenreich.diaguard.backup

import com.faltenreich.diaguard.backup.legacy.LegacyImport
import com.faltenreich.diaguard.backup.seed.SeedImport
import com.faltenreich.diaguard.shared.di.inject

class BackupUseCase(
    private val seedImport: SeedImport = inject(),
    private val legacyImport: LegacyImport = inject(),
) {

    operator fun invoke() {
        seedImport.import()
        // Attention: Must be executed at last due to reusing seed data
        legacyImport()
    }
}