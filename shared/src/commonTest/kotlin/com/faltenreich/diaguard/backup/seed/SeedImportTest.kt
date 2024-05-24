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
import com.faltenreich.diaguard.food.FoodDaoFake
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodApiFake
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDaoFake
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDaoFake
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDaoFake
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.ResourceLocalization
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.tag.TagDao
import com.faltenreich.diaguard.tag.TagDaoFake
import com.faltenreich.diaguard.tag.TagRepository
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
    private val categoryDao: MeasurementCategoryDao = MeasurementCategoryDaoFake()
    private val propertyDao: MeasurementPropertyDao = MeasurementPropertyDaoFake()
    private val unitDao: MeasurementUnitDao = MeasurementUnitDaoFake()
    private val foodDao: FoodDao = FoodDaoFake()
    private val foodApi: FoodApi = FoodApiFake()
    private val tagDao: TagDao = TagDaoFake()
    private val dateTimeFactory: DateTimeFactory = KotlinxDateTimeFactory()

    private val seedImport = SeedImport(
        localization = localization,
        seedRepository = seedRepository,
        categoryRepository = MeasurementCategoryRepository(dao = categoryDao, dateTimeFactory = dateTimeFactory),
        propertyRepository = MeasurementPropertyRepository(dao = propertyDao, dateTimeFactory = dateTimeFactory),
        unitRepository = MeasurementUnitRepository(dao = unitDao, dateTimeFactory = dateTimeFactory),
        foodRepository = FoodRepository(dao = foodDao, api = foodApi, dateTimeFactory = dateTimeFactory),
        tagRepository = TagRepository(dao = tagDao, dateTimeFactory = dateTimeFactory),
    )

    @Test
    fun `import succeeds`() {
        seedImport.import()
    }
}