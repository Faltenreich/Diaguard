package com.faltenreich.diaguard.backup.legacy

class ImportLegacyUseCase(
    private val importPreferences: ImportLegacyPreferencesUseCase,
    private val importDatabase: ImportLegacyDatabaseUseCase,
) {

    suspend operator fun invoke() {
        importPreferences()
        // TODO: Re-enable
        return
        importDatabase()
    }
}