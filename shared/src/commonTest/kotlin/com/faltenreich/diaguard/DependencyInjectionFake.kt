package com.faltenreich.diaguard

import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.entry.EntryDaoFake
import com.faltenreich.diaguard.entry.tag.EntryTagDao
import com.faltenreich.diaguard.entry.tag.EntryTagDaoFake
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.FoodDaoFake
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.food.eaten.FoodEatenDaoFake
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDaoFake
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDaoFake
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDaoFake
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDaoFake
import com.faltenreich.diaguard.tag.TagDao
import com.faltenreich.diaguard.tag.TagDaoFake
import org.koin.dsl.module

fun testModules() = module {
    single<EntryDao> { EntryDaoFake() }
    single<MeasurementCategoryDao> { MeasurementCategoryDaoFake() }
    single<MeasurementPropertyDao> { MeasurementPropertyDaoFake() }
    single<MeasurementUnitDao> { MeasurementUnitDaoFake() }
    single<MeasurementValueDao> { MeasurementValueDaoFake() }
    single<FoodDao> { FoodDaoFake() }
    single<FoodEatenDao> { FoodEatenDaoFake() }
    single<TagDao> { TagDaoFake() }
    single<EntryTagDao> { EntryTagDaoFake() }
}