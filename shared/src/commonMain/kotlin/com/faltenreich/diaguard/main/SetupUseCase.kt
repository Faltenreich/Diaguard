package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.legacy.ImportLegacyUseCase
import com.faltenreich.diaguard.backup.seed.SeedImportUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SetupUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val hasData: HasDataUseCase,
    private val importSeed: SeedImportUseCase,
    private val importLegacy: ImportLegacyUseCase,
) {

    suspend operator fun invoke() = withContext(dispatcher) {
        hasData().collect { hasData ->
            if (!hasData) {
                // importSeed()
                importLegacy()
            }
        }
    }
}