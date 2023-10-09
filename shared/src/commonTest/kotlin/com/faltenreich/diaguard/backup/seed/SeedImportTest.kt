package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test

class SeedImportTest {

    private val seedImport = SeedImport(
        // FIXME: Injects Context on android which leads to KoinApplication has not been started
        localization = Localization(),
        serialization = Serialization(),
    )

    @Test
    fun `imports from YAML`() {
        seedImport()
    }
}