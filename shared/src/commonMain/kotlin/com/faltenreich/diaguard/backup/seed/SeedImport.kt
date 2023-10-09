package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(
    private val localization: Localization,
    private val serialization: Serialization,
) : Import<List<SeedMeasurementProperty>> {

    override operator fun invoke(): List<SeedMeasurementProperty> {
        val yaml = localization.getString(MR.files.properties)
        return serialization.decodeYaml(yaml)
    }
}