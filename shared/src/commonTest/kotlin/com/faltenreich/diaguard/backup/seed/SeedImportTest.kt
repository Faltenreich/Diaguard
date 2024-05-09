package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.data.ActivitySeed
import com.faltenreich.diaguard.backup.seed.data.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.data.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.data.FoodSeed
import com.faltenreich.diaguard.backup.seed.data.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.data.InsulinSeed
import com.faltenreich.diaguard.backup.seed.data.MealSeed
import com.faltenreich.diaguard.backup.seed.data.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.data.PulseSeed
import com.faltenreich.diaguard.backup.seed.data.TagSeed
import com.faltenreich.diaguard.backup.seed.data.WeightSeed
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.ResourceLocalization
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.shared.test.returns
import com.faltenreich.diaguard.tag.TagDao
import com.faltenreich.diaguard.tag.TagRepository
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.every
import io.mockative.mock
import org.jetbrains.compose.resources.StringResource
import kotlin.test.Test

class SeedImportTest {

    private val localization = Localization(
        resourceLocalization = object : ResourceLocalization {
            override fun getString(resource: StringResource, vararg args: Any): String = ""
            override fun getFile(path: String): String = ""
        },
    )

    private val seedRepository = SeedRepository(
        bloodSugarSeed = BloodSugarSeed(),
        insulinSeed = InsulinSeed(),
        mealSeed = MealSeed(),
        activitySeed = ActivitySeed(),
        hbA1cSeed = HbA1cSeed(),
        weightSeed = WeightSeed(),
        pulseSeed = PulseSeed(),
        bloodPressureSeed = BloodPressureSeed(),
        oxygenSaturationSeed = OxygenSaturationSeed(),
        foodSeed = FoodSeed(
            fileReader = SystemFileReader("src/commonTest/resources/food.csv"),
            serialization = Serialization(),
        ),
        tagSeed = TagSeed(
            fileReader = SystemFileReader("src/commonTest/resources/tags.csv"),
            serialization = Serialization(),
        ),
    )
    @Mock private val categoryDao = mock(classOf<MeasurementCategoryDao>())
    @Mock private val propertyDao = mock(classOf<MeasurementPropertyDao>())
    @Mock private val unitDao = mock(classOf<MeasurementUnitDao>())
    @Mock private val foodDao = mock(classOf<FoodDao>())
    @Mock private val foodApi = mock(classOf<FoodApi>())
    @Mock private val tagDao = mock(classOf<TagDao>())
    private val dateTimeFactory: DateTimeFactory = KotlinxDateTimeFactory()

    private val seedImport = SeedImport(
        localization = localization,
        dateTimeFactory = dateTimeFactory,
        seedRepository = seedRepository,
        categoryRepository = MeasurementCategoryRepository(dao = categoryDao),
        propertyRepository = MeasurementPropertyRepository(dao = propertyDao),
        unitRepository = MeasurementUnitRepository(dao = unitDao),
        foodRepository = FoodRepository(dao = foodDao, api = foodApi, dateTimeFactory = dateTimeFactory),
        tagRepository = TagRepository(dao = tagDao),
    )

    init {
        every { categoryDao.getLastId() } returns 0L
        every { propertyDao.getLastId() } returns 0L
        every { unitDao.getLastId() } returns 0L
        every { foodDao.getLastId() } returns 0L
        every { tagDao.getLastId() } returns 0L
    }

    @Test
    fun `import succeeds`() {
        seedImport.import()
    }
}