package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) : Import<List<SeedMeasurementProperty>> {

    override operator fun invoke(): List<SeedMeasurementProperty> {
        val yaml = fileReader.readFile(FILE_PATH)
        return serialization.decodeYaml(yaml)
    }

    companion object {

        private const val FILE_PATH = "src/commonMain/resources/seed/properties.yml"
    }
}