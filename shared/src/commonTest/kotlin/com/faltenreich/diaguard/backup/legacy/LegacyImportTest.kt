package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.backup.seed.SeedImport
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.tag.TagRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LegacyImportTest : TestSuite {

    private val dateTimeFactory: DateTimeFactory by inject()
    private val seedImport: SeedImport by inject()

    private lateinit var entryRepository: EntryRepository
    private lateinit var propertyRepository: MeasurementPropertyRepository
    private lateinit var valueRepository: MeasurementValueRepository
    private lateinit var foodRepository: FoodRepository
    private lateinit var foodEatenRepository: FoodEatenRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var entryTagRepository: EntryTagRepository

    private lateinit var import: LegacyImport

    @BeforeTest
    fun setup() {
        entryRepository = get()
        propertyRepository = get()
        valueRepository = get()
        foodRepository = get()
        foodEatenRepository = get()
        tagRepository = get()
        entryTagRepository = get()

        import = LegacyImport(
            legacyRepository = get(),
            entryRepository = entryRepository,
            propertyRepository = propertyRepository,
            valueRepository = valueRepository,
            foodRepository = foodRepository,
            foodEatenRepository = foodEatenRepository,
            tagRepository = tagRepository,
            entryTagRepository = entryTagRepository,
        )

        // We rely on seed properties
        seedImport.import()

        import.import()
    }

    @Test
    fun `imports entries`() = runTest {
        val expected = listOf(
            Entry.Local(
                id = 0,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198200),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198200),
                dateTime = dateTimeFactory.dateTime(millis = 1715234400000),
                note = "Hello, World",
            ),
            Entry.Local(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198204),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198204),
                dateTime = dateTimeFactory.dateTime(millis = 1717221600000),
                note = null,
            ),
            Entry.Local(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198208),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198208),
                dateTime = dateTimeFactory.dateTime(millis = 1717308000000),
                note = null,
            ),
        )
        val actual = entryRepository.getAll().first()
        assertEquals(expected, actual)
    }
}