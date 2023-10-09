package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test

class SeedImportTest {

    private val seedImport = SeedImport(
        fileReader = FileReader(),
        serialization = Serialization(),
    )

    @Test
    fun `imports from YAML`() {
        seedImport()
    }
}