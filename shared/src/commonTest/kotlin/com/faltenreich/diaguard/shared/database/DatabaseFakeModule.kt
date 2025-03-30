package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.entry.EntryFakeDao
import com.faltenreich.diaguard.entry.tag.EntryTagDao
import com.faltenreich.diaguard.entry.tag.EntryTagFakeDao
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.FoodFakeDao
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.food.eaten.FoodEatenFakeDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryFakeDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyFakeDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitFakeDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueFakeDao
import com.faltenreich.diaguard.tag.TagDao
import com.faltenreich.diaguard.tag.TagFakeDao
import org.koin.dsl.module

fun databaseFakeModule() = module {
    single<EntryDao> { EntryFakeDao() }

    single<MeasurementCategoryDao> { MeasurementCategoryFakeDao() }
    single<MeasurementPropertyDao> { MeasurementPropertyFakeDao() }
    single<MeasurementUnitDao> { MeasurementUnitFakeDao() }
    single<MeasurementValueDao> { MeasurementValueFakeDao() }

    single<FoodDao> { FoodFakeDao() }
    single<FoodEatenDao> { FoodEatenFakeDao() }

    single<TagDao> { TagFakeDao() }
    single<EntryTagDao> { EntryTagFakeDao() }
}