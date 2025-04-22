package com.faltenreich.diaguard.startup.legacy

class ImportLegacyUseCase(
    private val importPreferences: ImportLegacyPreferencesUseCase,
    private val importDatabase: ImportLegacyDatabaseUseCase,
) {

    suspend operator fun invoke() {
        importPreferences()
        importDatabase()
    }
}