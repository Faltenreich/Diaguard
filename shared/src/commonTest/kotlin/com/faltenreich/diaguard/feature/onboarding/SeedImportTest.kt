package com.faltenreich.diaguard.feature.onboarding

import com.faltenreich.diaguard.onboarding.SeedImport
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlin.test.Test

class SeedImportTest {

    private val seedImport = SeedImport(Serialization())

    @Test
    fun `imports from YAML`() {
        seedImport.import()
    }
}