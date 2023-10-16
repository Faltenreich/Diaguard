package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.shared.test.returns
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.every
import io.mockative.mock
import kotlin.test.Test
import kotlin.test.assertNotNull

class SeedImportTest {

    @Mock private val propertyDao = mock(classOf<MeasurementPropertyDao>())
    @Mock private val typeDao = mock(classOf<MeasurementTypeDao>())
    @Mock private val unitDao = mock(classOf<MeasurementUnitDao>())
    private val dateTimeFactory: DateTimeFactory = KotlinxDateTimeFactory()

    private val seedImport = SeedImport(
        reader = { SystemFileReader("src/commonTest/resources/properties.yml").read() },
        serialization = Serialization(),
        mapper = SeedMapper(),
        propertyRepository = MeasurementPropertyRepository(dao = propertyDao, dateTimeFactory = dateTimeFactory),
        typeRepository = MeasurementTypeRepository(dao = typeDao, dateTimeFactory = dateTimeFactory),
        unitRepository = MeasurementUnitRepository(dao = unitDao, dateTimeFactory = dateTimeFactory),
    )

    init {
        every { propertyDao.getLastId() } returns 0L
        every { typeDao.getLastId() } returns 0L
        every { unitDao.getLastId() } returns 0L
    }

    @Test
    fun `imports from YAML`() {
        val yaml = seedImport()
        assertNotNull(yaml)
    }
}