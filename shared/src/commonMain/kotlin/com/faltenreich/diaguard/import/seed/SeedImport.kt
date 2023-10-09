package com.faltenreich.diaguard.import.seed

import com.faltenreich.diaguard.import.Import
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(
    private val fileReader: FileReader,
    private val serialization: Serialization,
) : Import<List<SeedMeasurementProperty>> {

    override operator fun invoke(): List<SeedMeasurementProperty> {
        val yaml = fileReader.readFile(IMPORT_FILE_PATH)
        return serialization.decodeYaml(yaml)
    }

    companion object {

        private const val IMPORT_FILE_PATH = "src/commonMain/resources/seed/properties.yml"
    }
}