package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test

class SeedImportTest {

    private val seedImport = SeedImport(
        // FIXME: KoinApplication has not been started
        //  caused by component relying on dependency injection due to Context on Android
        //  read file via FileReader instead
        localization = Localization(),
        serialization = Serialization(),
    )

    @Test
    fun `imports from YAML`() {
        seedImport()
    }
}