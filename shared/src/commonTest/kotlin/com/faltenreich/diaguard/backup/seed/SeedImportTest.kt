package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.file.FileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock
import kotlin.test.Test

class SeedImportTest {

    @Mock private val propertyDao = mock(classOf<MeasurementPropertyDao>())
    @Mock private val typeDao = mock(classOf<MeasurementTypeDao>())
    @Mock private val unitDao = mock(classOf<MeasurementUnitDao>())

    private val seedImport = SeedImport(
        reader = object : SeedReader {
            override fun invoke(): String {
                return FileReader().readFile("src/commonTest/resources/properties.yml")
            }
        },
        serialization = Serialization(),
        mapper = SeedMapper(),
        propertyRepository = MeasurementPropertyRepository(dao = propertyDao),
        typeRepository = MeasurementTypeRepository(dao = typeDao),
        unitRepository = MeasurementUnitRepository(dao = unitDao),
    )

    @Test
    fun `imports from YAML`() {
        seedImport()
    }
}